package Main;

import Crawl.CrawlQuote;

public class Main {

	public static void main(String[] args) {
		CrawlQuote crawlQuote = new CrawlQuote();
		//1. crawl topic
//		crawlQuote.crawl_topic();
		
		//2. crawl quote detail follow to topic
		crawlQuote.crawl_quote();
		
		// debug crawl quote detail
//		crawlQuote.debug();
		
	}
}
