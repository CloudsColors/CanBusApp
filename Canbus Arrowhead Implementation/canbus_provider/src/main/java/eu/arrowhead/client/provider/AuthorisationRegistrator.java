package eu.arrowhead.client.provider;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.exception.ArrowheadException;
import eu.arrowhead.client.common.model.ArrowheadService;
import eu.arrowhead.client.common.model.ArrowheadSystem;
import eu.arrowhead.client.common.model.IntraCloudAuthEntry;

import java.util.ArrayList;

/**
 * AuthorisationRegistrator class handles the registration of services to the Authorisation in arrowhead.
 */
public class AuthorisationRegistrator {

  private ArrayList<IntraCloudAuthEntry> authEntries;
  private String authUri;

  /**
   * Construct to log the authUri and to create the authEntries arraylist for storing the authEntries.
   * @param authUri
   */
  public AuthorisationRegistrator(String authUri){
    authEntries = new ArrayList<IntraCloudAuthEntry>();
    this.authUri = authUri;
  }

  /**
   * This function will fill the authEntries arraylist with authEntry, by reading consumerList.json and putting together a authEntry.
   * Making it possible to register all services in the Authorisation at once.
   * @param path
   * @param sr
   */
  public void createAuthorisationEntriesFromFile(String path, ServiceRegistrator sr){
    JsonArray consumers = JsonFileParser.readFileToJsonArray(path);
    int nrOfEntries = 0;
    for (int i = 0; i < consumers.size(); i++){
      ArrayList<ArrowheadSystem> providerSystems = new ArrayList<ArrowheadSystem>();
      ArrayList<ArrowheadService> providerServices = new ArrayList<ArrowheadService>();
      JsonObject consumer = consumers.get(i).getAsJsonObject();
      ArrowheadSystem consumerSystem = new ArrowheadSystem(
              consumer.get("consumerSystemName").getAsString(),
              consumer.get("address").getAsString(),
              consumer.get("port").getAsInt(),
              consumer.get("authenticationInfo").getAsString()
              );
      String[] providerSystemNames = consumer.get("providerSystemNames").getAsString().split(",");
      String[] providerServiceNames = consumer.get("providedServiceName").getAsString().split(",");
      for(int j = 0; j < providerSystemNames.length; j++){
        ArrowheadSystem ahSystem = sr.getSystem(providerSystemNames[j]);
        if(ahSystem != null){
          providerSystems.add(ahSystem);
        }else{
          System.out.println("Something went wrong when fetching ahSystem: "+providerSystemNames[j]);
        }
      }
      for(int k = 0; k < providerServiceNames.length; k++){
        ArrowheadService ahService = sr.getService(providerServiceNames[k]);
        if(ahService != null){
          providerServices.add(sr.getService(providerServiceNames[k]));
        }else{
          System.out.println("Something went wrong when fetching ahService: "+providerServiceNames[k]);
        }
      }
      authEntries.add(new IntraCloudAuthEntry(consumerSystem, providerSystems, providerServices));
      nrOfEntries++;
    }
    System.out.println("IntraCloudEntries created: "+nrOfEntries);
  }

  /**
   * This function will simply register all services that was made ready with createAuthorisationEntriesFromFile to the Authorisation module in Arrowhead.
   */
  public void registerAuthorisationEntries(){
    for (int i = 0; i < authEntries.size(); i++){
      try {
        Utility.sendRequest(authUri, "POST", authEntries.get(i));
      } catch (ArrowheadException e) {
        e.printStackTrace();
        System.out.println("Authorization registration failed!");
      }
    }
    System.out.println("Authorization registration was a success!");
  }
}
