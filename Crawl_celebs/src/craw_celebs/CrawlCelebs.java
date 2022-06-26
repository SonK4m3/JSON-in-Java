package craw_celebs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CrawlCelebs {
	
	final String BASE_URL = "https://www.imdb.com";
	final String CELEB_LIST = "https://www.imdb.com/search/name/?match_all=true&start=";
	final String FILE_SAVED = "res//crawl_celebs.json";
	final String SAVE_DIR = "res//celeb_data";
	final String IMAGE_PATH = "/mediaindex?ref_=nm_phs_md_sm";
	
	final int NUMBER = 50;
	Gson gs = new GsonBuilder().setPrettyPrinting().create();
	public CrawlCelebs() {
		
	}
	
	public void crawl(int pageNumber) {
		for(int i = 0; i < pageNumber; i++) {
			celebs_extract(i * NUMBER + 1);
		}
	}
	
	void make_dir(String directory) {
		File file = new File(directory);
		if(file.exists() == false) {
			file.mkdirs();
		}
	}
	
	public void celebs_extract(int start) {
		//1. make directory
		make_dir(SAVE_DIR);
		
		// create array to store json data
		JsonArray data = new JsonArray();
		
		try {
			//1. connect url
			URL url = new URL(CELEB_LIST + start);
			Document doc = Jsoup.connect(url.toString()).get();
			
//			System.out.println(doc);
			//2. parse celebs
			Elements celebs = doc.select("div.lister-item-image");
			for(Element celeb :celebs) {
				String celeb_id = parseId(celeb.select("a[href]").attr("href"));
				String celeb_dir = "res/celeb_data/" + celeb_id;
				String celeb_name = celeb.select("img[alt]").attr("alt");
				String celeb_url = BASE_URL + "/name/" + celeb_id;
				String celeb_poster = parsePoster(celeb.select("img[src]").attr("src"));
				// create json object to store celeb's information
				JsonObject json_celeb = new JsonObject();
				json_celeb.addProperty("name", celeb_name);
				json_celeb.addProperty("url", celeb_url);
				json_celeb.addProperty("id", celeb_id);
				json_celeb.addProperty("dir", celeb_dir);
				json_celeb.addProperty("poster", celeb_poster);
				
//				System.out.println(parsePoster(celeb_poster));
				
				//wait 0,1s 
				Thread.sleep(100);
				
				data.add(json_celeb);
			}

			writeFile(FILE_SAVED, data);
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void writeFile(String dir, JsonArray data) {
		String jsonString = "";
		
		try {
			FileWriter file = new FileWriter(dir);
			jsonString = gs.toJson(data);
			file.write(jsonString);
			file.close();
			System.out.println("successful !");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		System.out.println(jsonString);
	}
	
	public void writeFile(String dir, JsonObject data) {
		String jsonString = "";
		
		try {
			FileWriter file = new FileWriter(dir);
			jsonString = gs.toJson(data);
			file.write(jsonString);
			file.close();
			System.out.println("successful !");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		System.out.println(jsonString);
	}
	
	String parseId(String id) {
		int index = id.indexOf("/", 2);
		return id.substring(index + 1);
	}
	
	String parsePoster(String urlPoster) {
		String newUrlPoster = "";
		try {
			URL url = new URL(urlPoster);
			String host = url.getHost();
			String path = url.getPath();
			int index = path.indexOf(".");
			
			newUrlPoster = "https://" + host + "/" + path.substring(0,index) + ".jpg"; 
			return newUrlPoster;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			
			return "Unknown";
		}
	}
	
	JsonArray readFile(String dir) {
		JsonArray array = null;
		try {
			FileReader file = new FileReader(dir);
			// convert to json object
			array = gs.fromJson(file, JsonArray.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return array;
	}
	
	public void celeb_detail() {
		JsonArray data = null;
		data = readFile(FILE_SAVED);
		
		for(Object obj : data) {
			JsonObject jsonCeleb = (JsonObject) obj;
			 
			JsonObject newJsonCeleb = new JsonObject();
			// celeb info
			String celeb_url = jsonCeleb.get("url").getAsString();			
			JsonObject info = crawl_celeb_detail(celeb_url);
			//celeb photo
			String celeb_photo_url = jsonCeleb.get("url").getAsString() + IMAGE_PATH;
			JsonObject photo = crawl_celeb_image(celeb_photo_url);
			
			newJsonCeleb.add("info", info);
			newJsonCeleb.add("photo", photo);
			
			String dir_celeb = jsonCeleb.get("dir").getAsString();
			
			//make dir 
			make_dir(dir_celeb);
			
			//file json
			String name_file = dir_celeb + "/" + jsonCeleb.get("id").getAsString() + ".json";
			
			//write movie detail
			writeFile(name_file, newJsonCeleb);
		}
	}
	
	//
	String isNull(String str) {
		if(str.equals("")) {
			return "Unknown";
		}
		
		return str;
	}
	
	JsonObject crawl_celeb_detail(String celeb_url){
		JsonObject celeb = new JsonObject();
		try {
			Document doc = Jsoup.connect(celeb_url).get();
			Elements jobs = doc.select("div.infobar").select("a[href]");
			//1.job
			JsonArray celeb_job = new JsonArray();
			for(Element job : jobs) {
				celeb_job.add(job.attr("href").toString().substring(1));
			}
			//2.avatar
			String celeb_avatar = parsePoster(doc.select("div.image").select("img[src]").attr("src").toString()); 
			//3.date
			String celeb_date = isNull(doc.select("div[id]").select("time[datetime]").attr("datetime").toString());
						
			celeb.add("jobs", celeb_job);
			celeb.addProperty("avatar", celeb_avatar);
			celeb.addProperty("date", celeb_date);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return celeb;
	}
	//debug
	JSONObject debug_crawl_celeb_detail() {
		String url = "https://www.imdb.com/name/nm0000115/";
		JSONObject json_obj = new JSONObject();
		try {
			Document doc = Jsoup.connect(url).get();
			Elements jobs = doc.select("div.infobar").select("a[href]");
			JSONArray celeb_job = new JSONArray();
			for(Element job : jobs) {
				celeb_job.add(job.attr("href").toString().substring(1));
			}
			
			String celeb_avatar = parsePoster(doc.select("div.image").select("img[src]").attr("src").toString()); 
			String celeb_date = doc.select("div[id]").select("time[datetime]").attr("datetime").toString();
			
			json_obj.put("jobs", celeb_job);
			json_obj.put("avatar", celeb_avatar);
			json_obj.put("date", celeb_date);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json_obj;
	}
	/*
	 * crawl celeb's image
	 * 
	 */
	JsonObject crawl_celeb_image(String path) {
		JsonObject images = new JsonObject();
		int index = 0;
		
		try {
			Document doc = Jsoup.connect(path).get();
			
			Elements image_list = doc.select("div.media_index_thumb_list").select("a[href]");
			for(Element image : image_list) {
				String celeb_image = parsePoster(image.select("img[src]").attr("src").toString());
				index ++;        
				images.addProperty("image " + index, celeb_image);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return images;
	}
	
	//debug
	public void debug_crawl_celeb_image() {
		String path = "https://www.imdb.com/name/nm0000115/mediaindex?ref_=nm_phs_md_sm";
		JsonObject images = new JsonObject();
		int index = 0;
		try {
			Document doc = Jsoup.connect(path).get();
			
			Elements image_list = doc.select("div.media_index_thumb_list").select("a[href]");
			for(Element image : image_list) {
				String celeb_image = image.select("img[src]").attr("src").toString();
				index = index + 1;
				images.addProperty("" + index,celeb_image);
			}
			
			String jsonString = gs.toJson(images);
			System.out.println(jsonString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
