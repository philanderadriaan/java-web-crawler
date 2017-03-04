import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A class that parses an incoming string for URLs.
 * 
 * @author Andy Pelkey
 * @author Phil Adrian
 * @version 1
 * 
 */
public class PageParser implements Runnable
{
  /**
   * Stop counter to stop URLs.
   */
  private static final int STOP_COUNTER = 2000;
  
  /**
   * Token to search for to begin a closer examination.
   */
  private static final String SEARCH_START1 = "<a";

  /**
   * The retriever.
   */
  protected PageRetriever my_page_retriever;

  /**
   * The page analyzer instance.
   */
  protected PageAnalyzer my_page_analyzer;
  
  /**
   * UI instance.
   */
  protected UI my_ui;


  /**
   * Counts total URLs found.
   */
  private int my_url_counter;
  


  /**
   * Counts total pages found.
   */
  private int my_page_counter;
  
  /**
   * The total count.
   */
  private int my_total_count;

  /**
   * URL that is sent in.
   */
  private String my_default_url;
  

  /**
   * The default URL.
   */
  private String my_text;

  /**
   * List of words found while parsing the page.
   */
  private List<String> my_words;


  /**
   * Set of URLs.
   */
  // private Set<URL> my_urls;

  /**
   * The number of threads.
   */
  private ThreadEnum my_thread;

  /**
   * Parser constructor.
   * 
   * @param the_thread the thread.
   */
  public PageParser(final ThreadEnum the_thread)
  {
    my_thread = the_thread;
    my_words = new ArrayList<String>();

  }

  /**
   * The parser method.
   * 
   * @param the_text All of the text of the page.
   * @param the_url The default URL.
   */
  public void parser(final String the_text, final URL the_url)
  {
    my_default_url = the_url.toString();
    my_page_counter++;
    my_text = the_text;
    if (my_thread == ThreadEnum.SINGLE)
    {
      singleParser(my_text, my_default_url);
    }
    else
    {
      singleParser(my_text, my_default_url);
    }
    my_page_analyzer.setMyWords(my_words, my_url_counter, my_page_counter);
  }

  /**
   * Parses string and finds URLs and words.
   * 
   * @param the_string The string to be parsed.
   * @param the_url The URL string.
   */
  private void singleParser(final String the_string, final String the_url)
  {
    String original = the_string;
    final List<String> links = new ArrayList<String>();
    if (original.contains("<body>"))
    {
      original = original.substring(original.indexOf("<body>"));
      String new_string = "";
      while (original.contains("<"))
      {
        new_string += original.substring(0, original.indexOf('<'));
        original = original.substring(original.indexOf('<'));
        final int end = original.indexOf('>');
        final String tag = original.substring(0, end);
        if (tag.contains("href"))
        {
          links.add(tag.substring(tag.indexOf("\"") + 1, tag.lastIndexOf("\"")));
        }
        original = " " + original.substring(end + 1, original.length());
      }
      String[] words = new_string.split(" ");
      for (int i = 0; i < words.length; i++)
      {
        if (!"".equals(words[i]))
        {
          my_words.add(words[i]);
        }
      }
    }
    parseURL(links, the_url);
  }

  /**
   * Parses string and finds URLs and words.
   * 
   * @param the_string The string to be parsed.
   * @param the_url The URL string.
   */
  public void doubleParser(final String the_string, final String the_url)
  {
    String original = the_string;
    final List<String> links = new ArrayList<String>();
    if (original.contains("<body>"))
    {
      original = original.substring(original.indexOf("<body>"));
      String new_string = "";
      while (original.contains("<"))
      {
        new_string += original.substring(0, original.indexOf('<'));
        original = original.substring(original.indexOf('<'));
        int end = original.indexOf('>');
        String tag = original.substring(0, end);
        if (tag.contains("href"))
        {
          links.add(tag.substring(tag.indexOf("\"") + 1, tag.lastIndexOf("\"")));
        }
        original = " " + original.substring(end + 1, original.length());
      }
      String[] words = new_string.split(" ");
      for (int i = 0; i < words.length; i++)
      {
        if (!"".equals(words[i]))
        {
          my_words.add(words[i]);
        }
      }
    }
    /*
     * for (int i = 0; i < links.size(); i++) {
     * System.out.println(links.get(i)); }
     */
    parseURL(links, the_url);
  }
  /**
   * Helper method to parse the URL.
   * @param the_url The url.
   */
  private void parseURL(final List<String> the_links, final String the_url)
  {
    for (String check : the_links)
    {
      my_url_counter++;
      String url = the_url;
      if (check.contains("://"))
      {
        url = check;
      }
      else
      {
        if (url.contains(".html") && check.contains(".html"))
        {
          url = url.substring(0, url.lastIndexOf("/") + 1);
        }
        if (check.contains("../"))
        {
          check = check.substring(3);
          if (!url.contains(".html"))
          {
            url = url.substring(0, url.lastIndexOf("/") + 1);
          }
          url = url.substring(0, url.length() - 2);
          url = url.substring(0, url.lastIndexOf("/") + 1);
        }
        url += check;
      }
      try
      {
        if (my_total_count < STOP_COUNTER)
        {
          my_page_retriever.addURL(new URL(url));
          my_total_count++;
        }
      }
      catch (final MalformedURLException e)
      {
        System.out.println("Invalid URL");
      }
    }
  }

  /**
   * Adds a page retriever.
   * 
   * @param the_page_retriever instance.
   */
  public void addPageRetriever(final PageRetriever the_page_retriever)
  {
    my_page_retriever = the_page_retriever;
  }

  /**
   * Adds a page analyzer.
   * 
   * @param the_page_analyzer instance.
   */
  public void addPageAnalyzer(final PageAnalyzer the_page_analyzer)
  {
    my_page_analyzer = the_page_analyzer;
  }
  
  /**
   * Gets a UI.
   * @param the_ui UI.
   */
  public void getUI(final UI the_ui)
  {
    my_ui = the_ui;
  }

  @Override
  public void run()
  {

    my_page_analyzer = new PageAnalyzer(ThreadEnum.MULTI);
    new Thread(my_page_analyzer).start();
    my_page_analyzer.setUi(my_ui);

  }
}
