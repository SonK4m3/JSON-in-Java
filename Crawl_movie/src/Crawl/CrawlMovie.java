package Crawl;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CrawlMovie {
	
	final String MOVIE_URL = "https://www.imdb.com/title/";
	final String BASE_URL = "htpps://www.imdb.com";
	final String MOVIE_LIST = "https://www.imdb.com/search/title/?title_type=feature&languages=en&view=simple&start=";
	final String BASE_IMAGE = "https://m.media-amazon.com/images/M/";
	final String SAVE_DIR = "./movie_data";
	final String SAVE_FILE = "res//movies.json";			
	
	
	URL url;
	
	public CrawlMovie() {
		 
	}
	
	public void extractMovie(int start){
		//make link with number of movie started
		String movie_list = MOVIE_LIST + start;
		//create array to store object
		JsonArray data = new JsonArray();
		
		int rank = start;
		
		try {
			//1. connect url 
			url = new URL(movie_list);
			
			//2. jsoup to get html 
			Document doc = Jsoup.connect(url.toString()).get();
			
			//3. get data
			Elements movies = doc.select("div.lister-item-image");
		
			//4. parse movie data
			for(Element movie : movies) {
				String movieName = unicodeToChar(movie.select("img[alt]").attr("alt").toString());
				String movieId = movie.select("img[data-tconst]").attr("data-tconst");
				String movieUrl = MOVIE_URL + movieId + "/";
				String movieRank = new Integer(rank).toString();
				rank ++;
				JsonObject jsonMovie = new JsonObject();
				jsonMovie.addProperty("rank", movieRank);
				jsonMovie.addProperty("name", movieName);
				jsonMovie.addProperty("id", movieId);
				jsonMovie.addProperty("url", movieUrl);

				// add json object to json array
				data.add(jsonMovie);
			}
			
			//write file
			writeJson(SAVE_FILE, data);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	void writeJson(String fileName, JsonArray array) {
		Gson gs = new GsonBuilder().setPrettyPrinting().create();
		try {
			FileWriter file = new FileWriter(fileName);
			String stringJson = gs.toJson(array).toString();
			file.write(stringJson);
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
