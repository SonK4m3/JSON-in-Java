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
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CrawlCelebs {
	
	final String BASE_URL = "https://www.imdb.com";
	final String CELEB_LIST = "https://www.imdb.com/search/name/?match_all=true";
	final String FILE_SAVED = "res//crawl_celebs.json";
	final String SAVE_DIR = "res//celeb_data";
	final String IMAGE_PATH = "/mediaindex?ref_=nm_phs_md_sm";
	public CrawlCelebs() {
		
	}
	
	void make_dir(String directory) {
		File file = new File(directory);
		if(file.exists() == false) {
			file.mkdirs();
		}
	}
	
	public void celebs_extract() {
		//1. make directory
		make_dir(SAVE_DIR);
		
		// create array to store json data
		JSONArray data = new JSONArray();
		
		try {
			//1. connect url
			URL url = new URL(CELEB_LIST);
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
				JSONObject json_celeb = new JSONObject();
				json_celeb.put("name", celeb_name);
				json_celeb.put("url", celeb_url);
				json_celeb.put("id", celeb_id);
				json_celeb.put("dir", celeb_dir);
				json_celeb.put("poster", celeb_poster);
				
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
	
	public void writeFile(String dir, JSONArray data) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = "";
		
		try {
			FileWriter file = new FileWriter(dir);
			jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
			
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
	
	public void writeFile(String dir, JSONObject data) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = "";
		
		try {
			FileWriter file = new FileWriter(dir);
			jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
			
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
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return newUrlPoster;
	}
	
	JSONArray readFile(String dir) {
		JSONArray array = null;
		try {
			FileReader file = new FileReader(dir);
			// convert to json object
			Object obj = JSONValue.parse(file);
			array = (JSONArray) obj;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return array;
	}
	
	public void celeb_detail() {
		JSONArray data = null;
		data = readFile(FILE_SAVED);
		
		//debug
		//print data to console
//		ObjectMapper mp = new ObjectMapper();
//		try {
//			System.out.println(mp.writerWithDefaultPrettyPrinter().writeValueAsString(data));
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
		
		for(Object obj : data) {
			JSONObject jsonCeleb = (JSONObject) obj;
			 
			JSONObject newJsonCeleb = new JSONObject();
			// celeb info
			String celeb_url = jsonCeleb.get("url").toString();			
			JSONObject info = crawl_celeb_detail(celeb_url);
			//celeb photo
			String celeb_photo_url = jsonCeleb.get("url") + IMAGE_PATH;
			JSONObject photo = crawl_celeb_image(celeb_photo_url);
			
			newJsonCeleb.put("info", info);
			newJsonCeleb.put("photo", photo);
			
			String dir_celeb = jsonCeleb.get("dir").toString();
			
			//make dir 
			make_dir(dir_celeb);
			
			//file json
			String name_file = dir_celeb + "/" + jsonCeleb.get("id").toString() + ".json";
//			System.out.println(name_file);

			//delete file
//			File f = new File(dir_celeb + "/" + jsonCeleb.get("id").toString());
//			if(f.exists()) {
//				f.delete();
//			}
			
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
	
	JSONObject crawl_celeb_detail(String celeb_url){
		JSONObject celeb = new JSONObject();
		try {
			Document doc = Jsoup.connect(celeb_url).get();
			Elements jobs = doc.select("div.infobar").select("a[href]");
			//1.job
			JSONArray celeb_job = new JSONArray();
			for(Element job : jobs) {
				celeb_job.add(job.attr("href").toString().substring(1));
			}
			//2.avatar
			String celeb_avatar = parsePoster(doc.select("div.image").select("img[src]").attr("src").toString()); 
			//3.date
			String celeb_date = isNull(doc.select("div[id]").select("time[datetime]").attr("datetime").toString());
						
			celeb.put("jobs", celeb_job);
			celeb.put("avatar", celeb_avatar);
			celeb.put("date", celeb_date);
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
	JSONObject crawl_celeb_image(String path) {
		JSONObject images = new JSONObject();
		ObjectMapper mp = new ObjectMapper();
		try {
			Document doc = Jsoup.connect(path).get();
			
			Elements image_list = doc.select("div.media_index_thumb_list").select("a[href]");
			for(Element image : image_list) {
				String celeb_image = parsePoster(image.select("img[src]").attr("src").toString());
				images.put("image", celeb_image);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return images;
	}
	
	//debug
	public void debug_crawl_celeb_image() {
		String path = "https://www.imdb.com/name/nm0000115/mediaindex?ref_=nm_phs_md_sm";
		JSONObject images = new JSONObject();
		ObjectMapper mp = new ObjectMapper();
		int index = 0;
		try {
			Document doc = Jsoup.connect(path).get();
			
			Elements image_list = doc.select("div.media_index_thumb_list").select("a[href]");
			for(Element image : image_list) {
				String celeb_image = image.select("img[src]").attr("src").toString();
				index = index + 1;
				images.put(index,celeb_image);
			}
			
			String jsonString = mp.writerWithDefaultPrettyPrinter().writeValueAsString(images);
			System.out.println(jsonString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
