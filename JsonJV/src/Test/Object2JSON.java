package Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Object.Student;

public class Object2JSON {
	
	public Object2JSON() {
		
	}
	
	public String convertObjectToJson(Student stu) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = null;
		
		try {
//			System.out.println("Chuyen doi doi tuong thanh chuoi JSON:");
			jsonInString = mapper.writeValueAsString(stu);
//			System.out.println(jsonInString);
//			System.out.println();
			
//			System.out.println("Chuyen doi doi tuong thanh chuoi JSON theo format:");
//			jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(stu);
//			System.out.println(jsonInString);

		} catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return jsonInString;
	}
	
	public String convertMap(Map stu) {
		String jsonInString = JSONValue.toJSONString(stu);
		return jsonInString;
	}
	
	public Student createStudent() {
		Student stu = new Student();
		
		stu.setName("Son");
		stu.setId("B20DCVT311");
		stu.setScore(9);
		
		return stu;
	}
}
