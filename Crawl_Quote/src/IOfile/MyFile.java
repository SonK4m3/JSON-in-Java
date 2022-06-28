package IOfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MyFile {
	
	Gson gs = new GsonBuilder().setPrettyPrinting().create();
	
	public MyFile() {
		
	}
	
	public void make_dir(String path) {
		File file = new File(path);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
	
	public void writeFile(String path, JsonObject data) {
		try {
			FileWriter file = new FileWriter(path);
			String jsonString = gs.toJson(data);
			file.write(jsonString);
			file.close();
			
			System.out.println("Success!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeFile(String path, JsonArray datas) {
		try {
			FileWriter file = new FileWriter(path);
			String jsonString = gs.toJson(datas).toString();
			file.write(jsonString);
			file.close();
			
			System.out.println("Success!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Object readFile(String path, JsonArray arr) {
		Object obj = null;
		try {
			FileReader file = new FileReader(path);
			obj = gs.fromJson(file, JsonArray.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return obj;
	}
	
	public Object readFile(String path, JsonObject arr) {
		Object obj = null;
		try {
			FileReader file = new FileReader(path);
			obj = gs.fromJson(file, JsonObject.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return obj;
	}
	
	public String unicodeToChar(String text) {
		String tmp = text;
		
		tmp.replace("\u00e0", "à");
		tmp.replace("\u00e4", "ä"); 
		tmp.replace("\u00e2", "â");
		tmp.replace("\u00e7", "ç");
		tmp.replace("\u00e8", "è");
		tmp.replace("\u00e9", "é");
		tmp.replace("\u00ea", "ê");
		tmp.replace("\u00eb", "ë");
		tmp.replace("\u00ee", "î");
		tmp.replace("\u00ef", "ï");
		tmp.replace("\u00f4", "ô");
		tmp.replace("\u00f6", "ö");
		tmp.replace("\u00f9", "ù");
		tmp.replace("\u00fb", "û");
		tmp.replace("\u00fc", "ü");
		tmp.replace("\u0027", "'");
		
		return tmp;
	}
}
