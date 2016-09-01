import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

// Crawler which crawls from a seed Url
// Crawls in BFS order
public class Crawler {

  private Queue<String> crawlerQueue;
  private static HashSet<String> visitedUrls;
  private static int numberOfFiles = 0;
  protected String domain = null;
  private final int MAX_FILES_GENERATED = 1000;
  private ArrayList<HtmlFile> contentFiles;
  private FetchHTML fetcher;

  public Crawler(String seedUrl) {
    crawlerQueue = new LinkedList<String>();
    crawlerQueue.add(seedUrl);
    visitedUrls = new HashSet<String>();
    contentFiles = new ArrayList<HtmlFile>();
    setDomain(seedUrl);
    fetcher = new FetchHTML(domain);
  }

  public Crawler(ArrayList<String> seedUrlList) {
    visitedUrls = new HashSet<String>();
    contentFiles = new ArrayList<HtmlFile>();
    crawlerQueue = new LinkedList<String>();
    crawlerQueue.addAll(seedUrlList);
    setDomain(seedUrlList);
    fetcher = new FetchHTML(domain);
  }

  public HashSet<String> getCrawledUrls() {
    return visitedUrls;
  }

  public ArrayList<HtmlFile> getContentFiles() {
    return contentFiles;
  }

  public void setDomain(String url) {
    try {
      URI uri = new URI(url);
      domain = uri.getHost();
    } catch (URISyntaxException e) {
      System.err.println("Seed Url is not reacheable " + e.getMessage());
      System.exit(0);
    }
  }

  public void crawl() {

    // stop when queue is empty or when max files are generated
    while (!crawlerQueue.isEmpty() && numberOfFiles != MAX_FILES_GENERATED) {
      // remove first element in queue
      String url = (String) crawlerQueue.remove();
      // fetch data only from valid html urls within seed domain
      if (isWithinSeedDomain(url) && !isVisited(url)) {
        // Add this url to visited list
        visitedUrls.add(url);

        // fetch html data from this url
        fetcher.setURL(url);
        try {
          if (fetcher.fetchHtmlContent()) {
            HtmlFile file = fetcher.getContent();
            contentFiles.add(file);

            // write generated content to a file
            FileWrite writer = new FileWrite("");
            try {
              writer.write(file);
            } catch (FileNotFoundException e) {
              System.err.println("Failed to write to a file " + e.getMessage());
            }

            numberOfFiles++;
            // fetch hyper links from html
            HashSet<String> childUrls = fetcher.getAllLinks();

            // add all child urls to crawlerQueue
            crawlerQueue.addAll(childUrls);
          } else {
            crawlerQueue.addAll(fetcher.getAllLinks());
          }
        } catch (IOException e) {
          System.err.println("Failed to fetch content from url " + url);
        } catch (NullPointerException e) {
          e.printStackTrace();
          System.err.println("Failed Null pointer Exception " + e.getMessage());
        } catch (IllegalArgumentException e) {
          System.err.println("Invalid url");
        }
      }
    }
    // return if required amount of data is fetched or if crawlerQueue ran
    // out of urls
    if (crawlerQueue.isEmpty()) {
      System.err.println("Crawler Queue is empty");
      System.err.println("Visited Urls : " + visitedUrls.size());
      return;
    } else if (numberOfFiles == MAX_FILES_GENERATED) {
      System.err.println("Crawled URLs hit Max LIMIT of files to be generated " + MAX_FILES_GENERATED);
      System.err.println("Stopped Crawling!!");
      return;
    }
    return;
  }

  private void setDomain(ArrayList<String> urlList) {
    if (urlList == null || urlList.isEmpty()) {
      return;
    }
    try {
      URI uri = new URI(urlList.get(0));
      domain = uri.getHost();
      for (String i : urlList) {
        uri = new URI(i);
        if (domain != uri.getHost()) {
          System.err.println("All seedUrls are not from same domain");
          System.exit(0);
        }
      }
    } catch (URISyntaxException e) {
      System.err.println("Invalid SeedUrl");
      System.exit(0);
    }
    return;
  }

  private boolean isWithinSeedDomain(String uri) {
    try {
      if (domain == null) {
        return false;
      }
      URI url = new URI(uri);
      String host = url.getHost();
      if (host.contains(domain.substring(4, domain.length()))) {
        return true;
      } else {
        return false;
      }

    } catch (URISyntaxException e) {
      System.err.println("Syntax Error in Url " + e.getInput());
    }
    return false;
  }

  private boolean isVisited(String url) {
    if (visitedUrls.contains(url)) {
      return true;
    }
    return false;
  }
}
