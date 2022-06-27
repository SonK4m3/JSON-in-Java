package CrawlQuote;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import IOfile.MyFile;

public class CrawlQuote extends Crawl{
	
	public CrawlQuote(){	
		BASE_URL = "https://www.bartleby.com";
		NAME_FILE = "bartleby";
		
	}
	
	public void crawl() {
		
		String url = BASE_URL + "/quotations/";
		String save_dir = "res/" + NAME_FILE;
		
		myFile.make_dir(save_dir);
		
		String save_file = save_dir + "/" + NAME_FILE + ".json";
		
		try {
			Document doc = Jsoup.connect(url).get();
			Elements links = doc.select("dl").select("dt").select("a[href]");
			for(Element link : links) {
				String quote_url = BASE_URL + link.attr("href");
				
				JsonObject data = crawl_quote(quote_url);
								
				datas.add(data);
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		myFile.writeFile(save_file, datas);
	}
	
	public JsonObject crawl_quote(String url) {
		Document doc_s;
		JsonObject jsObj = new JsonObject();
		String author = "Unknown", quote = "Unknown";
		try {
			doc_s = Jsoup.connect(url).get();
			quote = doc_s.select("font[size]").get(1).text();
			Elements name = doc_s.select("i");
			if(name.size() == 1 &&name.get(0).text() != "") {
				author = name.get(0).text();
			}		
			else if (name.size() > 1 && name.get(1).text() != "")
				author = name.get(1).text();
			
			author = myFile.unicodeToChar(author);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
		jsObj.addProperty("quote", quote);
		jsObj.addProperty("author", author);
		
		return jsObj;
	}
}
