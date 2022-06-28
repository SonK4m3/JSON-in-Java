package CrawlQuote;

import java.io.IOException;

import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonObject;

public class CrawlQuoteInGoodreads extends Crawl{
	
	public CrawlQuoteInGoodreads() {
		BASE_URL = "https://www.goodreads.com/";
		NAME_FILE = "goodreads";
		SAVE_DIR = "res/" + NAME_FILE;
	}
	
	public void crawl() {
		URLS = BASE_URL + "quotes";
		
		myFile.make_dir(SAVE_DIR);
		
		String save_file = SAVE_DIR + "/" + NAME_FILE + ".json";
		
		try {
			Document doc = Jsoup.connect(URLS).get();
		
			Elements quotes = doc.select("div.quoteText");
			for(Element quote : quotes) {
				String author = "", my_quote = "";
				String quote_s = quote.select("span.authorOrTitle").text();
				author = myFile.unicodeToChar(parseAuthor(quote_s));
				my_quote = myFile.unicodeToChar(parseQuote(quote.text())).replace("\u0027", "’")
								.replace("\"", ":");
								
				JsonObject data = new JsonObject();
				data.addProperty("quote", my_quote);
				data.addProperty("author", author);
				
				this.datas.add(data);
			}
			
			myFile.writeFile(save_file, datas);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	String parseQuote(String quote) {
		int ind = quote.indexOf("―");
		String qu = quote.substring(1, ind - 2);
		return qu;
	}
	
	String parseAuthor(String author) {
		int ind = author.indexOf(",");
		if(ind != -1) {
			String au = author.substring(0, ind);
			return au;			
		}
		return author;
	}
	
	public void debug_crawl() {
		
	}
	
}
