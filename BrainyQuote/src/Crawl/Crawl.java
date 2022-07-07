package Crawl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Crawl {
	
	Gson gsBuilder = new GsonBuilder().setPrettyPrinting().create();
	
	String BASE_URL = null;
	String SAVE_DIR = null;
	
	
	public Crawl() {
		
	}
	
	public void crawl_topic() {
		
	}
	
	
	public void makeDir(String path) {
		//make directory
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdirs();
			System.out.println("Make dir success");
		}
	}
	
	/* 
	 * write object to json
	*/
	public void writeFile(String address, JsonObject data) {
		try {
			FileWriter file = new FileWriter(address);
			String jsonString = gsBuilder.toJson(data);
			file.write(jsonString);
			file.close();
			
			System.out.println("Write success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeFile(String address, JsonArray datas) {
		try {
			FileWriter file = new FileWriter(address);
			gsBuilder.toJson(datas, file);
			file.close();
			
			System.out.println("Write success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void rewriteFile(String address, JsonObject data) {
		JsonArray datas = null;

		if(new File(address).exists()) {
			datas = readFile(address, datas);
			datas.add(data);			
		}else {
			datas = new JsonArray();
		}
		
		
		try {
			FileWriter file = new FileWriter(address);
			String jsonString = gsBuilder.toJson(datas);
			file.write(jsonString);
			file.close();
			
			System.out.println("Write success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * read json to object
	 */
	public JsonObject readFile(String address, JsonObject jsonObj) {
		JsonObject newJsonObj = null;
		try {
			FileReader file = new FileReader(address);
			newJsonObj = gsBuilder.fromJson(file, JsonObject.class);
		
			System.out.println("Read success");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return newJsonObj;
	}
	
	public JsonArray readFile(String address, JsonArray jsonObj) {
		JsonArray newJsonObj = null;
		try {
			FileReader file = new FileReader(address);
			newJsonObj = gsBuilder.fromJson(file, JsonArray.class);
		
			System.out.println("Read success");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return newJsonObj;
	}
	
	public String unicodeToChar(String text) {		
		text.replace("\u00e0", "�");
		text.replace("\u00e4", "�"); 
		text.replace("\u00e2", "�");
		text.replace("\u00e7", "�");
		text.replace("\u00e8", "�");
		text.replace("\u00e9", "�");
		text.replace("\u00ea", "�");
		text.replace("\u00eb", "�");
		text.replace("\u00ee", "�");
		text.replace("\u00ef", "�");
		text.replace("\u00f4", "�");
		text.replace("\u00f6", "�");
		text.replace("\u00f9", "�");
		text.replace("\u00fb", "�");
		text.replace("\u00fc", "�");
		
		return text;
	}
}
