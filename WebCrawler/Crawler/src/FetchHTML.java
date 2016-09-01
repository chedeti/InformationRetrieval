import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// Fetch Content from a URL
public class FetchHTML {

  private String URL;
  private static int fileCount = 0;
  private HashSet<String> allLinks;
  private final int MIN_CONTENT_LENGTH = 150;
  private String domain;
  private HtmlFile html;

  public FetchHTML(String domain) {
    this.domain = domain;
  }

  public void setURL(String uRL) {
    URL = uRL;
  }

  public HashSet<String> getAllLinks() {
    return allLinks;
  }

  public HtmlFile getContent() {
    return html;
  }

  public boolean fetchHtmlContent() throws IOException, NullPointerException, IllegalArgumentException {

    System.err.println("Fetching content from url " + URL);
    html = new HtmlFile();
    allLinks = new HashSet<String>();

    Document doc = Jsoup.connect(URL).get();

    if (doc == null) {
      System.err.println("Jsoup returned null pointer!!");
      return false;
    }

    getSeedUrlDirectory();
    html.setURL(URL);

    String temp = doc.title();
    if (temp != null) {
      html.setTITLE(doc.title());
    }

    Elements keywords = doc.select("meta[name=keywords]");
    String kwords = "";
    for (Element keyword : keywords) {
      if (keyword.attr("content") != null) {
        kwords += keyword.attr("content");
      }
    }

    if (kwords != "") {
      html.setMETA_KEYWORDS(kwords);
    }

    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    html.setDATE(dateFormat.format(new Date()));

    Elements paragraphs = doc.select("p, h1, h2, h3, h4, h5, h6, textarea");
    String content = "";
    for (Element p : paragraphs) {
      content += p.text();
      content += "\n";
    }
    int len = 0;
    String[] split_content = content.split("\n");
    for (int i = 0; i < split_content.length; i++) {
      String[] a = split_content[i].split(" ");
      len += a.length;
    }
    boolean min_length = false;
    if (len < MIN_CONTENT_LENGTH) {
      System.err
          .println("content has " + split_content.length + " words less than Min words(" + MIN_CONTENT_LENGTH + ")");
      min_length = true;
    } else {

      html.setCONTENT(content);
    }

    // fetch links
    Elements links = doc.select("a[href]");
    String fileDir = getSeedUrlDirectory();

    for (Element link : links) {
      String a = link.attr("href");
      if (a.contains(domain.substring(4, domain.length())) && a.contains("http")) {
        allLinks.add(a);
      } else if (!a.contains("http") && fileDir != null) {
        if (a != "" && a.charAt(0) == '/') {
          allLinks.add(fileDir + a);
        } else {
          allLinks.add(fileDir + "/" + a);
        }
      }
    }
    if (min_length == true) {
      return false;
    }

    if (!isValidHtmlUrl(URL)) {
      return false;
    }
    html.setDOC_ID(++fileCount);
    return true;
  }

  private boolean isValidHtmlUrl(String url) {
    if (url.endsWith(".htm") || url.endsWith(".html") || url.endsWith(".cms")) {
      return true;
    }
    return false;
  }

  private String getSeedUrlDirectory() {
    if (URL == null || URL == "") {
      return null;
    }

    URI myUrl;
    try {
      myUrl = new URI(URL);
      if (URL.contains("https:")) {
        return "https://" + myUrl.getHost();
      } else {
        return "http://" + myUrl.getHost();
      }
    } catch (URISyntaxException e) {
      System.err.println("Invalid URL");
    }

    return null;
  }
}
