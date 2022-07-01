package Crawl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CrawlQuote extends Crawl{
	
	String QUOTE_TOPICS;
	String quote_topics_info;
	int NUMBER = 5;
	
	public CrawlQuote() {
		BASE_URL = "https://www.brainyquote.com";
		SAVE_DIR = "res/BrainyQuote";
		QUOTE_TOPICS = BASE_URL + "/topics";
		quote_topics_info = "res/quote_topics_info.json";
	}
	
	@Override
	public void crawl_topic() {
		
		makeDir(SAVE_DIR);
		
		JsonArray data_topics = new JsonArray();
		
		Set<String> set_name = new HashSet<String>();
		
		try {
			Document doc = Jsoup.connect(QUOTE_TOPICS).get();

			Elements list_topic = doc.select("div.bqLn").select("span.topicContentName");
			Elements url_list_topic = doc.select("div.bqLn").select("a[href]");
			int index_url_topic = 0;
			
			for(Element topics : list_topic) {
				JsonObject data = new JsonObject();
				
				String name_topic = unicodeToChar(topics.text().replace("\u0027", "’"));
				String url_topic = BASE_URL + url_list_topic.get(index_url_topic).attr("href").toString();
				String save_dir_topic = SAVE_DIR + "/" + name_topic;
			
				if(!set_name.contains(name_topic)) {
					data.addProperty("topic", name_topic);
					data.addProperty("url", url_topic);			
					data.addProperty("dir", save_dir_topic);
					System.out.println(gsBuilder.toJson(data));
				
					data_topics.add(data);					
				}
				set_name.add(name_topic);	
				index_url_topic ++;
			}	
			System.out.println("----\nDone");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		writeFile(quote_topics_info, data_topics);
	}
	
	public void debug() {
		String path = "res/debug.json";
		crawl_quote_topic("https://www.brainyquote.com/topics/age-quotes", 5, path);
	}
	
	public void crawl_quote_detail() {
		JsonArray datas = null;
		datas = readFile(quote_topics_info, datas);
		
		for(Object o_data : datas) {
			JsonObject data = (JsonObject) o_data;
			
			String url_topic = data.get("url").getAsString();
			String save_dir = data.get("dir").getAsString();
			String save_file = save_dir + "/quote_detail.json";
			
			System.out.println(data.get("topic").getAsString());
			makeDir(save_dir);
			
			crawl_quote_topic(url_topic, NUMBER, save_file);
			
		}
	
	}
	
	public void crawl_quote_topic(String url_page, int numberPage, String path) {
		JsonArray datas = new JsonArray();

		for(int i = 1; i <= numberPage; i++) {			
			try {
				Document doc = Jsoup.connect(url_page).get();
				
				Elements tags = doc.select("div.qbcol-c").select("div.qbcol").select("a[href]");
				Set<String> set_name = new HashSet<String>();
			
				for(Element tag : tags) {
					JsonObject data = new JsonObject();
					String id = "";
					
					String quote_url = tag.attr("href");
					
					if(!set_name.contains(quote_url) && isQuotes(quote_url)) {
						String quote_id = parseId(quote_url);
						String new_quote_url = BASE_URL + "/quotes/" + quote_id;
						String quote_page = "page " + i;
						String quote_topic = "Age Quotes";
						
						
						data.addProperty("id", quote_id);
						data.addProperty("url", new_quote_url);
						data.addProperty("topic", quote_topic);
						data.addProperty("page", quote_page);
						
//						System.out.println(gsBuilder.toJson(data));
						datas.add(data);
					}
					set_name.add(quote_url);
				}
				System.out.println("page" + i + " done");
			} catch (IOException e) {
				e.printStackTrace();
//				return;
			}
		}
		writeFile(path, datas);
		
	}
	
	String parseId(String id) {
		String newId = "";
		int start = id.indexOf('/', 1);
		int end = id.indexOf('?');
		if(start != -1 && end != -1)
			newId = id.substring(start + 1, end);
		else if(end == -1) {
			newId = id.substring(start + 1); 
		}

		return newId;
	}
	
	Boolean isQuotes(String url_quote) {
		int end = url_quote.indexOf('/', 1);
		
		return url_quote.substring(1, end).equals("quotes");
	}
	
}
