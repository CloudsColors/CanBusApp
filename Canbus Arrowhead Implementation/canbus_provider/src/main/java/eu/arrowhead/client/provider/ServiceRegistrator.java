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

public class ServiceRegistrator {

  private ArrayList<ServiceRegistryEntry> srEntries;
  private String srBaseUri;

  public ServiceRegistrator(String srBaseUri){
    srEntries = new ArrayList<ServiceRegistryEntry>();
    this.srBaseUri = srBaseUri;
  }

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

  public void unregisterFromServiceRegistry(ServiceRegistryEntry srEntry){
    String removeUri = UriBuilder.fromPath(srBaseUri).path("remove").toString();
    Utility.sendRequest(removeUri, "PUT", srEntry);
    System.out.println("Removing service is successful!");
  }

  public void unregisterAllFromServiceRegistry(){
    String removeUri = UriBuilder.fromPath(srBaseUri).path("remove").toString();
    for (int i = 0; i < srEntries.size(); i++){
      Utility.sendRequest(removeUri, "PUT", srEntries.get(i));
    }
    System.out.println("Removing all services was a success!");
  }

  public ArrowheadSystem getSystem(String systemName){
    for(int i = 0; i < srEntries.size(); i++){
      if(srEntries.get(i).getProvider().getSystemName().equals(systemName)){
        return srEntries.get(i).getProvider();
      }
    }
    return null;
  }

  public ArrowheadService getService(String serviceName){
    for(int i = 0; i < srEntries.size(); i++){
      if(srEntries.get(i).getProvidedService().getServiceDefinition().equals(serviceName)){
        return srEntries.get(i).getProvidedService();
      }
    }
    return null;
  }



  public ArrayList<ServiceRegistryEntry> getSrEntries(){
    return srEntries;
  }

}
