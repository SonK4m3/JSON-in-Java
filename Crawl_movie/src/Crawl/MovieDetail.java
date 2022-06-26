package Crawl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MovieDetail {
	
	final String MOVIE_URL = "https://www.imdb.com/title/";
	final String BASE_URL = "htpps://www.imdb.com";
	final String MOVE_LIST = "https://www.imdb.com/search/title/?title_type=feature&languages=en&view=simple";
	final String BASE_IMAGE = "https://m.media-amazon.com/images/M/";
	final String SAVE_DIR = "./movie_data";
	final String READ_FILE = "res//movies.json";
	final String SAVE_FILE = "res//movie_detail.json";

	Gson gs = new GsonBuilder().setPrettyPrinting().create();
	
	public MovieDetail() {
		
	}
	
	public void movieDetail() {
		JsonArray data = null;
		JsonArray updateData = new JsonArray();
		
		try {
			//1. read file
			FileReader reader = new FileReader(READ_FILE);
			//2. convert json file to object
			data = gs.fromJson(reader, JsonArray.class);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//3. crawl poster
		for(Object obj : data) {
			// get json object 
			JsonObject movie = (JsonObject) obj;
			try {
				String poster = "", bigPoster = "";
				
				// connect to movie url
				Document doc = Jsoup.connect(movie.get("url").getAsString() + "mediaindex").get();
				// get link poster url
				poster = doc.select("div.subpage_title_block").select("img[src]").attr("src");
				// parse poster
				bigPoster = parsePoster(poster);
				
				// add to json object
				movie.addProperty("big_poster", bigPoster);
				movie.addProperty("name", unicodeToChar(movie.get("name").getAsString()));
				
				obj = movie;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//5. write file
		writeJson(SAVE_FILE, data);
	}
	
	String parsePoster(String posterUrl) {
		String bigPosterUrl = "";
		URL posUrl;
		try {
			posUrl = new URL(posterUrl);
			String host = posUrl.getHost();
			if (host != null) {
				int atSign = host.indexOf('@');
				if (atSign != -1)
					host = host.substring(atSign + 1);
			}
			String path = posUrl.getPath().substring(0, posUrl.getPath().indexOf("."));
			bigPosterUrl = "https://" + posUrl.getHost() + path + ".png";
			
			return bigPosterUrl;
		} catch (MalformedURLException e) {
			return "Unknown";
		}
	}
	
	void writeJson(String fileName, JsonArray array) {
		try {
			// open write file
			FileWriter file = new FileWriter(fileName);
			// convert json object to string 
			String stringJson = gs.toJson(array);
			file.write(stringJson);  
			
			//debug
//			System.out.println(stringJson);
			
			file.close();
			
			System.out.println("write successful");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	String unicodeToChar(String text) {
		text.replace("\u00e0", "à");
		text.replace("\u00e4", "ä"); 
		text.replace("\u00e2", "â");
		text.replace("\u00e7", "ç");
		text.replace("\u00e8", "è");
		text.replace("\u00e9", "é");
		text.replace("\u00ea", "ê");
		text.replace("\u00eb", "ë");
		text.replace("\u00ee", "î");
		text.replace("\u00ef", "ï");
		text.replace("\u00f4", "ô");
		text.replace("\u00f6", "ö");
		text.replace("\u00f9", "ù");
		text.replace("\u00fb", "û");
		text.replace("\u00fc", "ü");
		
		return text;
	}
}
