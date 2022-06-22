package craw_celebs;

import java.io.File;

public class Main {

	public static void main(String[] args) {

		CrawlCelebs crawlCelebs = new CrawlCelebs();
		crawlCelebs.celebs_extract();
//		crawlCelebs.celeb_detail();

		System.out.println("done!");
	}

}
