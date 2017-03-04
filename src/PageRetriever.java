import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Retrieves web pages and stores them for later analysis by the PageParser
 * class. Only URLs specifying HTML or text documents should be retrieved. Each
 * unique URL should be retrieved only once. This class will need to utilize to
 * a collection of URLs waiting to be retrieved, as well as a repository in
 * which to store the documents' content.
 * 
 * @author Phil Adriaan
 * @author Andy Pelkey
 * @version 1
 */
public class PageRetriever implements Runnable
{
  /**
   * Page parser instance.
   */
  private PageParser my_page_parser;

  /**
   * A set of visited URL's.
   */
  private List<String> my_visited_url = new ArrayList<String>();
  
  /**
   * The thread count.
   */
  private ThreadEnum my_thread;

  /**
   * Constructs the page retriever.
   * 
   * @param the_thread Whether it's single or multi threaded.
   */
  public PageRetriever(final ThreadEnum the_thread)
  {
    my_thread = the_thread;
  }

  /**
   * Adds the page parser instance for later use.
   * 
   * @param the_page_parser Page parser instance.
   */
  public void addPageParser(final PageParser the_page_parser)
  {
    my_page_parser = the_page_parser;
  }
  
  /**
   * Sets a parser.
   * @param the_parser Parser instance.
   */
  public void setParser(final PageParser the_parser)
  {
    my_page_parser = the_parser;
  }

  /**
   * Adds a URL to be retrieved.
   * 
   * @param the_url URL of the website.
   */
  public void addURL(final URL the_url)
  {
    if (!my_visited_url.contains(the_url.toString()))
    {
      try
      {
        final BufferedReader reader =
            new BufferedReader(new InputStreamReader(the_url.openStream()));
        String input;
        final StringBuilder builder = new StringBuilder();
        while ((input = reader.readLine()) != null)
        {
          builder.append(input);
        }
        my_visited_url.add(the_url.toString());
        my_page_parser.parser(builder.toString(), the_url);
      }
      catch (final IOException exception)
      {
        System.out.println("Invalid address");
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Runnable#run()
   */
  @Override
  public void run()
  {

  }
}
