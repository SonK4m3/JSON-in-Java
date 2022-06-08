package Test;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Object.Student;

public class JSON2Object {
	
	public JSON2Object() {
		
	}
	
	public void convertJsonToObject(String str) {
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = null;
		
		try {
			jsonNode = mapper.readTree(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("name: " + jsonNode.get("name").asText());
		System.out.println("id: " + jsonNode.get("id").asText());
		System.out.println("score: " + jsonNode.get("score").asText());
		

	}
}
