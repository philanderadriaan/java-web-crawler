import java.util.Scanner;

/**
 * Starts the web crawler.
 * 
 * @author Phil Adriaan
 * @author Andy Pelkey
 * @author Christian Tomyn
 * @version 1
 */
public final class StartSpider
{

  /**
   * Prevent initialization.
   */
  private StartSpider()
  {
  }

  /**
   * Starts the web crawler.
   * 
   * @param the_args Command Line Arguments.
   */
  public static void main(final String[] the_args)
  {

    System.out.println("Press 1 for single thread.");
    System.out.println("Press 2 for multi threads.");
    final Scanner console = new Scanner(System.in);
    final int number = console.nextInt();
    if (number == 1)
    {
      start(ThreadEnum.SINGLE);
    }
    else if (number == 2)
    {
      start(ThreadEnum.MULTI);
    }
    else
    {
      System.out.println("Invalid response.");
    }

  }

  /**
   * Starts spider.
   * @param the_thread
   */
  private static void start(final ThreadEnum the_thread)
  {
    if (the_thread.equals(ThreadEnum.SINGLE))
    {
      final UI ui = new UI(the_thread);
      final PageRetriever page_retriever = new PageRetriever(the_thread);
      final PageParser page_parser = new PageParser(the_thread);
      final PageAnalyzer page_analyzer = new PageAnalyzer(the_thread);
      ui.addPageRetriever(page_retriever);
      page_retriever.addPageParser(page_parser);
      page_parser.addPageRetriever(page_retriever);
      page_parser.addPageAnalyzer(page_analyzer);
      page_analyzer.addUI(ui);

      ui.startUI();
    }
    else
    {
      final UI ui = new UI(the_thread);
      new Thread(ui).start();
      ui.startUI();
    }
  }
}
