package CrawlQuote;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlQuoteFromParade extends Crawl{
	public CrawlQuoteFromParade() {
		BASE_URL = "https://parade.com/";
		NAME_FILE = "parade";
		SAVE_DIR = "res/" + NAME_FILE;
		URLS = BASE_URL + "937586/parade/life-quotes/";
	}
	
	public void crawl(){
//		myFile.make_dir(SAVE_DIR);
		
		try {
			Document doc = Jsoup.connect(BASE_URL).get();
			
			Elements quotes = doc.select("p");

			System.out.println(quotes);
			for(Element quote_s : quotes) {
				System.out.println(quote_s);
			}
			
			System.out.println("done!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
}
