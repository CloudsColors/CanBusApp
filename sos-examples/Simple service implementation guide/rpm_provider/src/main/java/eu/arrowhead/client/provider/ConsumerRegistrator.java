package eu.arrowhead.client.provider;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import eu.arrowhead.client.common.model.ArrowheadService;
import eu.arrowhead.client.common.model.ArrowheadSystem;
import eu.arrowhead.client.common.model.IntraCloudAuthEntry;
import eu.arrowhead.client.common.model.ServiceRegistryEntry;

import java.util.ArrayList;


public class ConsumerRegistrator {

    private ArrayList<IntraCloudAuthEntry> authEntries;
    private String authUri;
    private ServiceRegistrator sr;

    public ConsumerRegistrator(String authUri, ServiceRegistrator sr){
        authEntries = new ArrayList<IntraCloudAuthEntry>();
        this.authUri = authUri;
        this.sr = sr;
    }

    public void createAuthEntriesFromFile(String filename){
        JsonArray auths = JsonFileParser.readFileToJsonArray(filename);
        for(int i = 0; i < auths.size(); i++){
            JsonObject auth = auths.get(i).getAsJsonObject();
            ArrowheadSystem consumer = new ArrowheadSystem(
              auth.get("consumerSystemName").getAsString(),
              auth.get("address").getAsString(),
              auth.get("port").getAsInt(),
              auth.get("authenticationInfo").getAsString()
            );
            String[] consumerSystems = auth.get("providerSystemNames").getAsString().split(",");
            ArrayList<ArrowheadSystem> providerSystemList = new ArrayList<ArrowheadSystem>();
            ArrayList<ArrowheadService> providerServiceList = new ArrayList<ArrowheadService>();
            ArrayList<ServiceRegistryEntry> srEntries = sr.getSrEntries();
            for(int z = 0; z < srEntries.size(); z++){
                //TODO: HITTA RELEVANTA SYSTEM OCH LÄGG TILL
            }
        }
    }

}
