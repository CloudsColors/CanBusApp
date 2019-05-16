package Main;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Hashtable;

public class ReadPIDCodes {

    /*-------------Modes-------------*/
    public static final int MODE_SCD = 0x01; // Show current data
    /*-------------PID codes---------*/
    private Hashtable<String, Byte> pidCodes; // This holds all the PID codes
    /*-------------Type of message-------------*/
    public static final int REQUEST_MSG = 0x7DF;
    public static final int[] RESPONSE_MSG = {0x7E8, 0x7E9, 0x7EA, 0x7EB, 0x7EC, 0x7ED, 0x7EE, 0x7EF};

    /**
     * Constructor makes sure that variables are set when creating the class.
     */
    public ReadPIDCodes(){
        //Make sure the variables is read from JSON and assign when class created.
        pidCodes = new Hashtable<String, Byte>();
        readJsonFile();
    }

    public Byte getPIDCode(String pid){
        try{
            return pidCodes.get(pid);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads the JSON file to the pidCodes hashtable for future access.
     */
    public void readJsonFile(){
        // Create a parser to parse the JSON files with the can_codes
        JsonFileParser parser = new JsonFileParser();
        JsonArray pids = parser.readFileToJsonArray("/home/andreas/IdeaProjects/CanBusApp/src/main/java/Main/PIDs.json");
        for(int i = 0; i < pids.size(); i++){
            JsonObject pid = pids.get(i).getAsJsonObject();
            pidCodes.put(pid.get("var-name").getAsString(), pid.get("pid-code").getAsByte());
        }
    }
}
