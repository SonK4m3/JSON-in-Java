package craw_celebs;

import java.io.File;

public class Main {

	public static void main(String[] args) {

		CrawlCelebs crawlCelebs = new CrawlCelebs();
//		crawlCelebs.crawl(5);
		
		crawlCelebs.celeb_detail();

		System.out.println("done!");
	}

}
