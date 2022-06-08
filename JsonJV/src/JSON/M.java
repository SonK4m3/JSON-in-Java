package JSON;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Object.Student;
import Test.*;
import java.io.*;
import O2JO.*;

public class M {
	
	static JSONArray array = new JSONArray();
	static Student stu1;
	static Student stu2;
	
	public static void main(String[] args) {		
		//1. khoi tao
		stu1 = new Student("son", "b20dcvt311", 10);
		stu2 = new Student("long", "b20dcqt101", 10);
		addObject("student",stu1);
		addObject("student",stu2);
		
		String address = "res/inform.json";
		JSONArray newArray = null;
		//0.test function
//		testO2JO();
		
		//2. ghi file
		FileWriterJson fileWriter = new FileWriterJson();
//		fileWriter.writeFileJson(array, address);
		//3. doc file
		FileReaderJson reader = new FileReaderJson();
		newArray = (JSONArray) reader.readerJsonToStudent(address);
		//4. in thong tin
		System.out.println(newArray.toJSONString());
		System.out.println();
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(array);
			System.out.println(jsonString);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	static void testO2JO() {
		O2JO o2jo = new O2JO();		
		System.out.println(o2jo.convertO2JSON(array));
	}
	
	static void addObject(String key, Object obj) {
		O2JO o2jo = new O2JO();
		array.add(o2jo.convertO2JO(key, obj));
	}
	
	static void parserStudent(JSONObject stu) {
	}
}
