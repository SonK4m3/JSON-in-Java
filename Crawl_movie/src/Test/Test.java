package Test;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.*;

public class Test {
	
	String stringUrl = "https://www.imdb.com/chart/top/?ref_=nv_mv_250";
	URL url;
	HttpURLConnection connection;
	String jsonPath = "res//move_extracty.json";
	
	public Test() {
		
	}
	
	public void movie_extracty() {
		int responseCode = 0;
		try {
		 	url = new URL(stringUrl);
		 	connection = (HttpURLConnection) url.openConnection();
		 	responseCode = connection.getResponseCode();
		
//		System.out.println("\nSending 'GET' request to URL; " + stringUrl);
//		System.out.println("Response Code: " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			String inputLine;
			StringBuffer response = new StringBuffer();
			while((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	
			Document doc = Jsoup.connect(stringUrl).get();
			String tile = doc.title();
			System.out.println(tile);
			
			Elements buddies = doc.select("img[src]");
			
			JSONArray array = new JSONArray();
			
			for(Element buddy : buddies) {
				JSONObject jobj = new JSONObject();
				String name = buddy.attr("alt");
				String poster = buddy.attr("src");
				
				if(name != "" && poster != "") {
					String bigPoster = getPoster(poster);
					jobj.put("name_movie", name);
					jobj.put("link_movie_poster", poster);
					jobj.put("link_big_poster_movie", bigPoster);
					array.add(jobj);										
				}
			}
			ObjectMapper mapper = new ObjectMapper();
			String jsonString = null;
			jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(array);
			writeFile(jsonString);
			
			System.out.println("end");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void writeFile(String str) {
		try {
			FileWriter write = new FileWriter(jsonPath);
			write.write(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	String getPoster(String posterUrl) throws MalformedURLException {
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
	
	public void start() {
		URL str = null;
		try {
			str = new URL("https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_UY67_CR0,0,45,67_AL_.jpg");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	
		 String host = str.getHost();
		    if (host != null) {
		      int atSign = host.indexOf('@');
		      if (atSign != -1)
		        host = host.substring(atSign + 1);
		    }

		    String path = str.getPath().substring(0, str.getPath().indexOf("."));
		    System.out.println(str.getHost() + path + ".png");
	}
}
