package Crawl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MovieDetail {
	
	final String MOVIE_URL = "https://www.imdb.com/title/";
	final String BASE_URL = "htpps://www.imdb.com";
	final String MOVE_LIST = "https://www.imdb.com/search/title/?title_type=feature&languages=en&view=simple";
	final String BASE_IMAGE = "https://m.media-amazon.com/images/M/";
	final String SAVE_DIR = "./movie_data";
	
	String fileName = "res//movies.json";
	JSONArray data;
	
	public MovieDetail() {
		
	}
	
	public void movieDetail() {
		
		JSONArray updateData = new JSONArray();
		
		try {
			//1. read file
			FileReader reader = new FileReader(fileName);
			//2. convert json file to object
			Object ob = JSONValue.parse(reader);
			data = (JSONArray) ob;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//3. crawl poster
		for(Object obj : data) {
			// get json object 
			JSONObject movie = (JSONObject) obj;
			try {
				String poster = "", bigPoster = "";
				
				// connect to movie url
				Document doc = Jsoup.connect(movie.get("url") + "mediaindex").get();
				// get link poster url
				poster = doc.select("div.subpage_title_block").select("img[src]").attr("src");
				// parse poster
				bigPoster = parsePoster(poster);
				
				// add to json object
				movie.put("poster", poster);
				movie.put("big_poster", bigPoster);
				
				obj = movie;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//5. write file
		fileName = "res//movie_detail.json";
		writeJson(fileName, data);
	}
	
	String parsePoster(String posterUrl) throws MalformedURLException {
		String bigPosterUrl = "";
		URL posUrl = null;
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
	}
	
	void writeJson(String fileName, JSONArray array) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			FileWriter file = new FileWriter(fileName);
			String stringJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(array);
			file.write(stringJson);
			System.out.println(stringJson);
			file.close();
			
			System.out.println("write successful");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
