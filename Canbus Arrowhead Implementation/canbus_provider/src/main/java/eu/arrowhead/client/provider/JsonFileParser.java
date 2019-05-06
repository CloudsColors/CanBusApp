package eu.arrowhead.client.provider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;

public class JsonFileParser {

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
