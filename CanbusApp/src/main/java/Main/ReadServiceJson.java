package Main;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;

public class ReadServiceJson {

    /*-------------Modes-------------*/
    public static final int MODE_SCD = 0x01; // Show current data
    /*-------------PID---------------*/
    public static byte ENGINE_COOL_TEMP; // Engine Coolant temperature.
    public static byte ENGINE_RPM; // Engine RPM
    /*-------------Type of message-------------*/
    public static final int REQUEST_MSG = 0x7DF;
    public static final int[] RESPONSE_MSG = {0x7E8, 0x7E9, 0x7EA, 0x7EB, 0x7EC, 0x7ED, 0x7EE, 0x7EF};

    /**
     * Constructor makes sure that variables are set when creating the class.
     */
    public ReadServiceJson(){
        //Make sure the variables is read from JSON and assign when class created.
        readJsonFile();
    }

    /**
     * Reads the JSON file to a variable
     */
    public void readJsonFile(){
        // Create a parser to parse the JSON files with the can_codes
        JSONParser parser = new JSONParser();
        JSONObject pid = null;
        try{
            //Parse the JSON file
            Object obj = parser.parse(new FileReader("/home/andreas/IdeaProjects/CanBusApp/src/main/java/Main/Services.json"));
            JSONObject jsonObj = (JSONObject) obj;
            //Everything within the PID section of the JSON is stored in pid variable
            pid = (JSONObject) jsonObj.get("PID");
            //send to function to assign the class variables.
            assignVariables(pid);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    /**
     * Takes a JSONObject variable to assign static variables which hold PID codes.
     * @param pid
     */
    public void assignVariables(JSONObject pid){
        ENGINE_RPM = Byte.decode(((JSONObject) pid.get("rpm")).get("code").toString());
        ENGINE_COOL_TEMP = Byte.decode(((JSONObject) pid.get("enginecooltemp")).get("code").toString());
    }
}
