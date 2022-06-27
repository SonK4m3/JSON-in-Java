package CrawlQuote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import IOfile.MyFile;

public class Crawl {
	
	String BASE_URL = null;
	String NAME_FILE = null;
	Gson gs = new GsonBuilder().setPrettyPrinting().create();
	MyFile myFile = new MyFile();
	JsonArray datas = new JsonArray();
	
	public Crawl() {
		
	}
	
	public void crawl() {
		
	}
	
	public JsonObject crawl_quote(String path) {
		
		return null;
	}
}
