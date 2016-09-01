import java.util.ArrayList;
import java.util.Scanner;

// Crawler Handler to crawl from seed URLs
public class WebCrawler {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    int nSeedUrls = in.nextInt();

    ArrayList<String> seedUrls = new ArrayList<String>();
    for (int i = 0; i < nSeedUrls; i++) {
      String url = in.next();
      seedUrls.add(url);
    }

    Crawler webCrawler;
    for (int i = 0; i < nSeedUrls; i++) {
      webCrawler = new Crawler(seedUrls.get(i));
      webCrawler.crawl();
    }
    
    in.close();
  }
}
