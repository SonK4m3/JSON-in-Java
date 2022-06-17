package Crawl;

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CrawlMovie {
	
	final String MOVIE_URL = "https://www.imdb.com/title/";
	final String BASE_URL = "htpps://www.imdb.com";
	final String MOVE_LIST = "https://www.imdb.com/search/title/?title_type=feature&languages=en&view=simple";
	final String BASE_IMAGE = "https://m.media-amazon.com/images/M/";
	final String SAVE_DIR = "./movie_data";
	
	URL url;
	
	public CrawlMovie() {
		 
	}
	
	public void extractMovie(int start){
		
		JSONArray data = new JSONArray();
		
		try {
			//1. connect url 
			url = new URL(MOVE_LIST);
			
			//2. jsoup to get html 
			Document doc = Jsoup.connect(url.toString()).get();
			
			//3. get data
			Elements movies = doc.select("div.lister-item-image img.loadlate");
//			"div.lister-item-image img.loadlate"
			
			//4. 
			for(Element movie : movies) {
				String movieName = unicodeToChar(movie.attr("alt"));
				String movieId = movie.attr("data-tconst");
				String movieUrl = MOVIE_URL + movieId + "/";
//				String posterUrl = doc.select("div.subpage_title_block img.poster").attr("src");
				
				JSONObject jsonMovie = new JSONObject();
				jsonMovie.put("name", movieName);
				jsonMovie.put("url", movieUrl);
				jsonMovie.put("id", movieId);
//				jsonMovie.put("poster_url", posterUrl);
				
//				System.out.println(jsonMovie.toJSONString());
				// add json object to json array
				data.add(jsonMovie);
			}
			
			String fileName = "res//movies.json";
//			System.out.println(data);
			//write file
			writeJson(fileName, data);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	void writeJson(String fileName, JSONArray array) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			FileWriter file = new FileWriter(fileName);
			String stringJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(array);
			file.write(stringJson);
			file.close();
			
//			System.out.println(stringJson);
			
			System.out.println("write successful");
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
