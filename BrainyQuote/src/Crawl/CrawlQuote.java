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
		//1. make dir to save data
		makeDir(SAVE_DIR);
		//2. create array to store topic
		JsonArray data_topics = new JsonArray();
		//3. create set to select same topic
		Set<String> set_name = new HashSet<String>();
		
		try {
			//1. get html file
			Document doc = Jsoup.connect(QUOTE_TOPICS).get();
			//2. select tag
			Elements list_topic = doc.select("div.bqLn").select("span.topicContentName");
			Elements url_list_topic = doc.select("div.bqLn").select("a[href]");
			int index_url_topic = 0;
			//3 parse data
			for(Element topics : list_topic) {
				//1. create object to store data
				JsonObject data = new JsonObject();
				
				String name_topic = unicodeToChar(topics.text().replace("\u0027", "’"));
				String url_topic = BASE_URL + url_list_topic.get(index_url_topic).attr("href").toString();
				String save_dir_topic = SAVE_DIR + "/" + name_topic;
				// if has only topic, store in object
				if(!set_name.contains(name_topic)) {
					data.addProperty("topic", name_topic);
					data.addProperty("url", url_topic);			
					data.addProperty("dir", save_dir_topic);
					//print to console
//					System.out.println(gsBuilder.toJson(data));
				
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
	
	@SuppressWarnings("unused")
	public void debug() {
		String path = "res/debug.json";
		crawl_quote_topic("Age Quotes", "https://www.brainyquote.com/topics/age-quotes", 5, path);
//		crawl_quote_detail("https://www.brainyquote.com/quotes/mark_twain_103892");
	}
	
	public void crawl_quote() {
		///1. get topic data
		JsonArray datas = null;
		datas = readFile(quote_topics_info, datas);
		
		//2. loop to crawl quote from each topic
		for(int i = 0; i < datas.size(); i++) {
			JsonObject data = (JsonObject) datas.get(i);
			
			String url_topic = data.get("url").getAsString();
			String save_dir = data.get("dir").getAsString();
			String save_file = save_dir + "/quote_detail.json";
			//1. make dir to save quote detail
			makeDir(save_dir);
			//2. crawl quote 
			crawl_quote_topic(data.get("topic").getAsString(),url_topic, NUMBER, save_file);
			
		}
	
	}
	
	public void crawl_quote_topic(String topic, String url_page, int numberPage, String path) {
		//1. create array to store data
		JsonArray datas = new JsonArray();
		//2. loop to crawl multilpage
		for(int i = 1; i <= numberPage; i++) {			
			try {
				//1. get html file
				Document doc = Jsoup.connect(url_page).get();
				//2. select tag
				Elements tags = doc.select("div.qbcol-c").select("div.qbcol").select("a[href]");
				//3. set to check if has same name
				Set<String> set_name = new HashSet<String>();
			
				for(Element tag : tags) {
					//1. create object to store
					JsonObject data = new JsonObject();
					
					String id = "";
					String quote_url = tag.attr("href");
					//2. get url quotes
					// we get text has format /quotes/....
					if(!set_name.contains(quote_url) && getType(quote_url).equals("quotes")) {
						String quote_id = parseId(quote_url);
						String new_quote_url = BASE_URL + "/quotes/" + quote_id;
						String quote_page = "page " + i;
						String quote_topic = topic;
						
						data.addProperty("id", quote_id);
						data.addProperty("url", new_quote_url);
						data.addProperty("topic", quote_topic);
						data.addProperty("page", quote_page);
						//1. quote detail
						JsonObject info_quote = crawl_quote_detail(new_quote_url);
						
						data.add("detail", info_quote);
						
//						System.out.println(gsBuilder.toJson(data));
						datas.add(data);
					}
					set_name.add(quote_url);
				}
				System.out.println("page" + i + " done");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		writeFile(path, datas);
		
	}
	
	JsonObject crawl_quote_detail(String url_quote) {
		JsonObject data = null;
		try {			
			data = new JsonObject();
			Document doc = Jsoup.connect(url_quote).get();
			
			String quote = unicodeToChar(doc.select("p").first().text()).replace("\u0027", "’");
			String author_name = doc.select("p").last().text().replace("\u0027", "’");
			String author_url = BASE_URL +  doc.select("p").last().select("a[href]").attr("href");
			
			JsonArray related_topic = new JsonArray();
			JsonArray related_author = new JsonArray();
			JsonObject info = new JsonObject();
			
			Elements list = doc.select("div.bq_fl").select("a[href]");
			int cnt = 0; 
			//we get info in /type// to store to object
			for(Element ele : list) {
				String inf = ele.attr("href");
				String text = ele.text();
				if(getType(inf).equals("topics")) {
					related_topic.add(text.replace("\u0027", "’"));
				}
				else if(getType(inf).equals("authors")) {
					related_author.add(text.replace("\u0027", "’"));
				}
				else if(getType(inf).equals("nationality")) {
					info.addProperty("nationality", text.replace("\u0027", "’"));
				}
				else if(getType(inf).equals("profession")) {
					info.addProperty("profession", text.replace("\u0027", "’"));
				}
				else if(getType(inf).equals("birthdays")) {
					if(cnt == 0)
						info.addProperty("date of birth", text);
					else
						info.addProperty("date of death", text);
					cnt ++;
				}
			}			
			
			JsonObject author = new JsonObject();
			author.addProperty("name", author_name);
			author.addProperty("url", author_url);
			author.add("info", info);
			
			JsonObject related = new JsonObject();
			related.add("related_topic", related_topic);
			related.add("related_author", related_author);
			
			data.addProperty("quote", quote);
			data.add("author", author);
			data.add("related", related);
			
//			System.out.println(gsBuilder.toJson(data));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println("done");
		return data;
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
	
	String getType(String str) {
		if(str.length() > 1) {
			int end = str.indexOf('/', 1);
			return str.substring(1, end);			
		}
		return "";
	}
	
}
