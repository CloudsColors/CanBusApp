package eu.arrowhead.client.provider;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.exception.ArrowheadException;
import eu.arrowhead.client.common.exception.ExceptionType;
import eu.arrowhead.client.common.model.ArrowheadService;
import eu.arrowhead.client.common.model.ArrowheadSystem;
import eu.arrowhead.client.common.model.ServiceRegistryEntry;

import javax.ws.rs.core.UriBuilder;
import java.util.*;

/**
 * Class to register all or one service/s to the ServiceRegister in Arrowhead.
 */
public class ServiceRegistrator {

  private ArrayList<ServiceRegistryEntry> srEntries;
  private String srBaseUri;

  /**
   * Constructor to log the service registry base uri and to create the service registry entries
   * that will be needed to register to the ServiceRegistry by reading the json serviceList in config/.
   * @param srBaseUri
   */
  public ServiceRegistrator(String srBaseUri){
    srEntries = new ArrayList<ServiceRegistryEntry>();
    this.srBaseUri = srBaseUri;
  }

  /**
   * Functions to create the service regestries that will later be used to register the services. These are kept in srEntries for future use in AuthRegistrator.
   * @param filename
   */
  public void createServiceEntriesFromFile(String filename){
    JsonArray services = JsonFileParser.readFileToJsonArray(filename);
    for(int i = 0; i < services.size(); i++){
      JsonObject service = services.get(i).getAsJsonObject();
      ArrowheadSystem ahSystem = new ArrowheadSystem(
        service.get("systemname").getAsString(),
        service.get("address").getAsString(),
        service.get("port").getAsInt(),
        null
      );
      String interfaceList = service.get("interfaces").getAsString();
      Set<String> interfaces = new HashSet<>();
      if (interfaceList != null && !interfaceList.isEmpty()) {
        interfaces.addAll(Arrays.asList(interfaceList.replaceAll("\\s+", "").split(",")));
      }
      Map<String, String> metadata = new HashMap<>();
      String metadataString = service.get("serviceMetadata").getAsString();
      if (metadataString != null && !metadataString.isEmpty()) {
        String[] parts = metadataString.split(",");
        for (String part : parts) {
          String[] pair = part.split("-");
          metadata.put(pair[0], pair[1]);
        }
      }
      ArrowheadService ahService = new ArrowheadService(
        service.get("serviceDefinition").getAsString(), interfaces, metadata);

      srEntries.add(new ServiceRegistryEntry(ahService,ahSystem,service.get("serviceUri").getAsString()));
    }
    System.out.println("The number of services created was: " + srEntries.size());
  }

  /**
   * This function registers all services that was created from createServiceEntriesFromFile.
   */
  public void registerAllServices(){
    // create the URI for the request
    String registerUri = UriBuilder.fromPath(srBaseUri).path("register").toString();
    for (int i = 0; i < srEntries.size(); i++){
      try {
        Utility.sendRequest(registerUri, "POST", srEntries.get(i));
      } catch (ArrowheadException e) {
        if (e.getExceptionType() == ExceptionType.DUPLICATE_ENTRY) {
          System.out.println("Received DuplicateEntryException from SR, sending delete request and then registering again.");
          unregisterFromServiceRegistry(srEntries.get(i));
          Utility.sendRequest(registerUri, "POST", srEntries.get(i));
        } else {
          throw e;
        }
      }
    }
    System.out.println("Registering services was a success!");
  }

  /**
   * This function will remove a single entry from the ServiceRegistry.
   * @param srEntry
   */
  public void unregisterFromServiceRegistry(ServiceRegistryEntry srEntry){
    String removeUri = UriBuilder.fromPath(srBaseUri).path("remove").toString();
    Utility.sendRequest(removeUri, "PUT", srEntry);
    System.out.println("Removing service is successful!");
  }

  /**
   * This function will remove all the entries made from registerAllServices from the ServiceRegistry.
   */
  public void unregisterAllFromServiceRegistry(){
    String removeUri = UriBuilder.fromPath(srBaseUri).path("remove").toString();
    for (int i = 0; i < srEntries.size(); i++){
      Utility.sendRequest(removeUri, "PUT", srEntries.get(i));
    }
    System.out.println("Removing all services was a success!");
  }

  /**
   * This function will return the system in srEntries arraylist that corresponds to the argument or null if it doesnt exist.
   * @param systemName
   * @return
   */
  public ArrowheadSystem getSystem(String systemName){
    for(int i = 0; i < srEntries.size(); i++){
      if(srEntries.get(i).getProvider().getSystemName().equals(systemName)){
        return srEntries.get(i).getProvider();
      }
    }
    return null;
  }

  /**
   * This function will return the service in srEntries arraylist that corresponds to the argument or null if it doesnt exist.
   * @param serviceName
   * @return
   */
  public ArrowheadService getService(String serviceName){
    for(int i = 0; i < srEntries.size(); i++){
      if(srEntries.get(i).getProvidedService().getServiceDefinition().equals(serviceName)){
        return srEntries.get(i).getProvidedService();
      }
    }
    return null;
  }

  /**
   * This function will return the srEntries arraylist containing all the service registry entries read in from
   * createServiceEntriesFromFile.
   * @return
   */
  public ArrayList<ServiceRegistryEntry> getSrEntries(){
    return srEntries;
  }

}
