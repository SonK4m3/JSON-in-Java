package O2JO;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Object.Student;

public class O2JO {
	
	public O2JO() {
		
	}
	
	public JSONObject convertO2JO(String key, Object obj) {
		String jsonString = convertO2JSON(obj);
		JSONParser parser = new JSONParser();
		
		JSONObject jsonObj = null;
		try {
			jsonObj = (JSONObject) parser.parse(jsonString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONObject newJsonObj = new JSONObject();
		newJsonObj.put(key, jsonObj);
		return newJsonObj;
	}
	
	public String convertO2JSON(Object obj) {
		String jsonString = null;
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonString;
	}
}
