import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Scanner;

/**
 * User interface of Web Spider.
 * 
 * @author Phil Adrian
 * @author Andy Pelkey
 * @version 1
 */
public class UI implements Runnable
{
  /**
   * Number of keywords.
   */
  private static final int NUMBER_OF_KEYWORDS = 10;
  /**
   * Pager Retriever Instance.
   */
  private PageRetriever my_page_retriever;
  /**
   * Calendar.
   */
  static Calendar start;
  
  /**
   * Calendar.
   */
  private Calendar finish;
  /**
   * Parser instance.
   */
  private PageParser my_page_parser;
  /**
   * For storing keywords..
   */
  private String[] my_keywords = new String[NUMBER_OF_KEYWORDS];
  
  /**
   * Single or multi threads.
   */
  private ThreadEnum my_thread;

  /**
   * Constructs the UI.
   * 
   * @param the_thread Single or Multi threads.
   */
  public UI(final ThreadEnum the_thread)
  {
    my_thread = the_thread;
  }

  /**
   * Starts the UI.
   */
  public void startUI()
  {
    start = Calendar.getInstance();
    Scanner console = new Scanner(System.in);
    System.out.println("Number of keywords (1 - 10):");
    int number = console.nextInt();
    if (number < 1 || number > NUMBER_OF_KEYWORDS)
    {
      System.out.println("Invalid number");
    }
    else
    {
      try
      {

        for (int i = 0; i < number; i++)
        {
          System.out.println("Keyword " + (i + 1) + ":");
          my_keywords[i] = console.next();
        }
        System.out.println("URL: ");
        String s = console.next();
        System.out.println();
        URL u = new URL(s);
        if (my_thread.equals(ThreadEnum.SINGLE))
        {
          my_page_retriever.addURL(u);
        }
        else
        {
          my_page_parser = new PageParser(ThreadEnum.MULTI);
          new Thread(my_page_parser).start();
          my_page_parser.getUI(this);
          my_page_retriever = new PageRetriever(ThreadEnum.MULTI);
          my_page_retriever.setParser(my_page_parser);
          new Thread(my_page_retriever).start();
          my_page_parser.addPageRetriever(my_page_retriever);
          my_page_retriever.addURL(u);
        }
      }
      catch (final MalformedURLException e)
      {
        System.out.println("Invalid URL");
      }
    }
  }

  /**
   * Adds the page retriever instance.
   * 
   * @param the_page_retriever Page Retriever instance.
   */
  public void addPageRetriever(final PageRetriever the_page_retriever)
  {
    my_page_retriever = the_page_retriever;
  }

  /**
   * @return An array of all keywords.
   */
  public String[] getKeyWords()
  {
    return my_keywords;
  }

  /**
   * Displays the report to the user.
   * 
   * @param the_report The report.
   */
  public void displayReport(final String the_report)
  {
    System.out.println(the_report);
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
  
  /**
   * Stops timer.
   */
  public void stopTimer()
  {
    finish = Calendar.getInstance();
  }
  /**
   * Gets time.
   * @return Time.
   */
  public long getTime()
  {
    final long s = start.getTimeInMillis();
    final long f = finish.getTimeInMillis();
    return f - s;
  }
}