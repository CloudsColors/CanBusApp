package Main;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;

/**
 * Class to read in Json files.
 */
public class JsonFileParser {

  /**
   * Function to get an JsonArray from reading a file.
   * @param fileName
   * @return
   */
  public static JsonArray readFileToJsonArray(String fileName){
    try{
      JsonReader reader = new com.google.gson.stream.JsonReader(new FileReader(fileName));
      return new Gson().fromJson(reader, JsonArray.class);
    }
    catch (Exception ex){
      ex.printStackTrace();
    }
    return null;
  }

  /**
   * Function to get a JsonObject from reading a file.
   * @param fileName
   * @return
   */
  public static JsonObject readFileToJsonObject(String fileName){
    try{
      JsonReader reader = new JsonReader(new FileReader(fileName));
      return new Gson().fromJson(reader, JsonObject.class);
    }
    catch (Exception ex){
      ex.printStackTrace();
    }
    return null;
  }


}
