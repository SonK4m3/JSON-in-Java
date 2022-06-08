package Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;

import Object.Student;

public class FileWriterJson {
	
	Object2JSON o2j = new Object2JSON();
	
	public FileWriterJson() {
		
	}
	
	public void writeFileJson(JSONArray array, String address) {
		try (FileWriter file = new FileWriter(address)){
			file.write(array.toJSONString());
			file.flush();
			System.out.println("Success!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
