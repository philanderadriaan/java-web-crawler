import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
/**
 * @author Christian Tomyn
 * @version 1.0
 */
public class PageAnalyzer implements Runnable
{
  /**
   * The UI.
   */
  private UI my_ui;
  /**
   * List of words.
   */
  private List<String> my_words;
  /**
   * Array of words.
   */
  private String[] my_find_words;
  
  /**
   * Matched words.
   */
  private List<String> my_matched_words = new LinkedList<String>();
  
  /**
   * Total words.
   */
  private int my_total_words;
  
  /**
   * Total urls.
   */
  private int my_total_urls;
  
  /**
   * Total pages.
   */
  private int my_total_pages;

  /**
   * Constructor.
   * @param the_thread the thread.
   */
  public PageAnalyzer(final ThreadEnum the_thread)
  {

  }
/**
 * Sets UI.
 * @param the_ui The ui.
 */
  public void setUi(final UI the_ui)
  {
    my_ui = the_ui;
  }
  /**
   * Sets the current set of words including the total words to be searched and searched from.
   * @param the_new_words new worlds.
   * @param the_total_urls total urls.
   * @param the_total_pages total pages.
   */
  public void setMyWords(final List<String> the_new_words,
                         final int the_total_urls, final int the_total_pages)
  {
    my_words = the_new_words;
    my_find_words = my_ui.getKeyWords();
    my_total_words = my_words.size();
    my_total_urls = the_total_urls;
    my_total_pages = the_total_pages;
    analyze();
    output();
  }

  /**
   * Adds the UI.
   * @param the_ui the UI.
   */
  public void addUI(final UI the_ui)
  {
    my_ui = the_ui;
  }
  /**
   * Finds matches from keywords.
   */
  public void analyze()
  {
    if (my_words.size() > 0 && my_find_words.length > 0)
    {
      for (int i = 0; i < my_words.size(); i++)
      {
        for (int j = 0; j < my_find_words.length; j++)
        {
          if (my_words.get(i).equalsIgnoreCase(my_find_words[j]))
          {
            my_matched_words.add(my_find_words[j]);
          }
        }
      }
    }
  }

  /**
   * Outputs the matched words to a string using string builder.
   * Uses ui to print to console and returns the string.
   * @return String
   */
  public String output()
  {
    final DecimalFormat df = new DecimalFormat("#.###");
    Collections.sort(my_matched_words);
    final StringBuilder final_string = new StringBuilder();
    final_string.append("\nTotal pages: " + my_total_pages);
    final_string.append("\nTotal words: " + my_total_words);
    final_string.append("\nWords per page: " +
                        df.format((double) my_total_words / (double) my_total_pages));
    final_string.append("\nTotal URLs: " + my_total_urls);
    final_string.append("\nURLs per page: " +
        df.format((double) my_total_urls / (double) my_total_pages));
    final_string.append("\n" + "Words Avg. hits per page Occurrences\n");
    final String[] keywords = my_find_words;
    for (int i = 0; i < my_matched_words.size(); i++)
    {
      int j = 1;
      final_string.append(my_matched_words.get(i));
      
      
      for (int k = 0; k < keywords.length; k++)
      {
        if (my_matched_words.get(i).equalsIgnoreCase(keywords[k]))
        {
          keywords[k] = null;
        }
      }
      boolean has_next;
      do
      {
        if (my_matched_words.size() > i + 1 &&
            my_matched_words.get(i).equalsIgnoreCase(my_matched_words.get(i + 1)))
        {
          j++;
          i++;
          has_next = true;
        }
        else
        {
          has_next = false;
        }
      }
      while (has_next);
      final String space = "   ";
      final_string.append(space + df.format((double) j / (double) my_total_pages));
      final_string.append(space + j + "\n\n");
    }
    for (int k = 0; k < keywords.length; k++)
    {
      if (keywords[k] != null)
      {
        final_string.append(keywords[k] + " 0\n");
      }
    }
    my_ui.displayReport(final_string.toString());
    return final_string.toString();
  }

  @Override
  public void run()
  {
   
  }
}