package Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.fasterxml.jackson.databind.ObjectMapper;

import Object.Student;

public class FileReaderJson {

	
	public FileReaderJson() {
		
	}
	
	public Object readerJsonToStudent(String address) {
		Object obj = null;
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			FileReader fileReader = new FileReader(address);
			obj = JSONValue.parse(fileReader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
		return obj;
	}
	
}
