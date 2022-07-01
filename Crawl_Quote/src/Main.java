import CrawlQuote.*;

public class Main {

	public static void main(String[] args) {

		// crawl completed
		Crawl crawlQuote = new CrawlQuote();
//		crawlQuote.crawl();

		//is crawling
		Crawl crawlQuoteInGoodreads = new CrawlQuoteInGoodreads();
//		crawlQuoteInGoodreads.crawl();
		
		Crawl crawlQuoteFromParade = new CrawlQuoteFromParade();
		crawlQuoteFromParade.crawl();
	}

}
