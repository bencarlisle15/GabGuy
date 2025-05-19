import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;

public class InfoFinder {
  // private instance variable
  private String line;

  public String create(String l) {
    // sets the url to be checked as the beginning wiki url plus the String sent
    // without the question or illegal characters
    line = "https://en.wikipedia.org/wiki/" + fix(removeQuestion(l));
    // returns the response from the links page
    String ans = find();
    // if an error of any kind (except for no connection) occurred then continue
    // the searching
    if (!ans.substring(0, Math.min(20, ans.length()))
             .equals("An error has occured"))
      return ans;
    // the beginning ask url + the String without illegal characters
    line = "http://www.ask.com/web?o=0&l=dir&qo=serpSearchTopBox&q=" + fix(l);
    // returns the response from the links page
    String ask = askFind();
    // if an error of any kind (except for no connection) occurred then continue
    // the searching
    if (!ask.substring(0, Math.min(20, ask.length()))
             .equals("An error has occured"))
      return ask;
    // the beginning ask url + the String without the question or illegal
    // characters
    line = "http://www.ask.com/web?o=0&l=dir&qo=serpSearchTopBox&q=" +
           fix(removeQuestion(l));
    // returns the response from the links page
    String noQuestion = askFind();
    // if an error of any kind (except for no connection) occurred then return
    // the wiki response since it is more specific
    if (!noQuestion.substring(0, Math.min(20, noQuestion.length()))
             .equals("An error has occured"))
      return noQuestion;
    return ans;
  }

  private String removeQuestion(String s) {
    // removes the question if it has the word is or 's in it if not then the
    // question shouldn't be removed anyway
    int pos = s.indexOf("'s ") + 3;
    if (pos == 2 && s.indexOf(" is ") < 0)
      return s;
    else if (pos == 2 ||
             s.indexOf(" is ") < s.indexOf("'s ") && s.indexOf(" is ") > 0)
      pos = s.indexOf(" is ") + 4;
    return s.substring(pos);
  }
  private String fix(String whatIs) {
    String ans = "";
    whatIs = whatIs.trim();
    int p = 0;
    // removes articles
    if (whatIs.substring(0, Math.min(1, whatIs.length())).equals("a") &&
        !whatIs.equals("a"))
      whatIs = whatIs.substring(2);
    else if (whatIs.substring(0, Math.min(3, whatIs.length())).equals("the"))
      whatIs = whatIs.substring(4);
    // removes a space of ' and replaces it with the appropriate url substitute
    for (int i = 0; i < whatIs.length(); i++) {
      if (whatIs.substring(i, Math.min(i + 1, whatIs.length())).equals(" ")) {
        ans += whatIs.substring(p, i) + "%20";
        p = ++i;
      } else if (whatIs.substring(i, Math.min(i + 1, whatIs.length()))
                     .equals("'")) {
        ans += whatIs.substring(p, i) + "%27";
        p = ++i;
      }
    }
    ans += whatIs.substring(p);
    return ans;
  }

  private String askFind() {
    try {
      // linereader of the url's source code
      BufferedReader lr = new BufferedReader(new InputStreamReader(
          new URL(line).openConnection().getInputStream()));
      // if it's not ready then the connection must be bad and throws the
      // appropriate error
      if (!lr.ready())
        throw new UnknownHostException();
      String line1 = lr.readLine();
      // line that occurs right before the targeted block of text
      String target = "<span class=\"sa_headline\">";
      String lines = "";
      // while the linereader has more lines
      while (line1 != null) {
        // checks the line for the target
        for (int i = 0; i < line1.length() - target.length(); i++)
          if (line1.substring(i, i + target.length()).equals(target)) {
            // adds lines until one of two ending signals exist
            while (line1 != null &&
                   line1.indexOf("<script type=\"text/javascript\">") < 0 &&
                   line1.indexOf("<br/>") < 0) {
              lines += line1;
              line1 = lr.readLine();
            }
            lr.close();
            // cleans the line
            return addLine(lines);
          }
        line1 = lr.readLine();
      }
      lr.close();
      // could not find the target text therefore the target block does not
      // exist and the given question is too specific
      return "An error has occured please be less specfic.";
    } catch (UnknownHostException e) {
      // no connection
      return "Could not connect.";
    } catch (IOException e) {
      // error somehow
      return "An error has occured.";
    }
  }

  private String find() {
    try {
      // linereader of the url's source code
      BufferedReader lr = new BufferedReader(new InputStreamReader(
          new URL(line).openConnection().getInputStream()));
      // if it's not ready then the connection must be bad and throws the
      // appropriate error
      if (!lr.ready())
        throw new UnknownHostException();
      String line1 = "";
      String line2 = lr.readLine();
      String line3 = lr.readLine();
      String ans;
      // makes sure no lines are null
      while (line1 != null && line2 != null && line3 != null) {
        line1 = line2;
        line2 = line3;
        line3 = lr.readLine();
        // if the three targeted lines appear in the right order
        if (line2.equals("</table>") && line1.equals("</tr>") &&
            line3.substring(0, Math.min(3, line3.length())).equals("<p>")) {
          // clean the line
          ans = addLine(line3);
          for (int i = 0; i < ans.length(); i++)
            if (ans.substring(i, Math.min(i + 3, ans.length()))
                    .equals("Ã¢â‚¬â€œ")) {
              ans = ans.substring(0, i) + "-" + ans.substring(i + 3);
              i -= 2;
            }
          lr.close();
          // if the response is too broad
          if (ans.indexOf("may refer to:") >= 0)
            return "An error has occured, please be more specific.";
          return ans;
        }
      }
      lr.close();
      // could not find the target text therefore the target block does not
      // exist and the given question is too specific
      return "An error has occured, please be less specfic.";
    } catch (UnknownHostException e) {
      // no connection
      return "Could not connect.";
    } catch (IOException e) {
      // error somehow
      return "An error has occured please be less specfic.";
    }
  }

  private static String addLine(String line) {
    // removes any string between each pair of chars
    String[] w = {"<", "(", "["};
    String[] w2 = {">", ")", "]"};
    for (int i = 0; i < w.length; i++)
      line = fix(line, w[i], w2[i]);
    // removes double spaces
    while (line.indexOf("  ") >= 0)
      line = line.replaceFirst("  ", " ");
    return line.trim();
  }

  private static String fix(String a, String q, String q2) {
    int pos = 0;
    String b = "";
    // removes any string between each pair of chars
    for (int i = 0; i < a.length(); i++) {
      // if the first char is found
      if (a.substring(i, Math.min(a.length(), i + q.length())).equals(q)) {
        // adds the string from the last position or 0
        b += a.substring(pos, i);
        // checks for another beginning char
        for (int t = i + 1; t < a.indexOf(q2, pos); t++)
          if (a.substring(t, Math.min(a.length(), t + q.length())).equals(q)) {
            pos = a.indexOf(q2, pos) + 1;
            break;
          }
        // adds the String from the most recent ending char
        pos = a.indexOf(q2, pos) + q.length();
        // changes i to the new pos;
        i = pos - q.length();
      }
    }
    // returns the fixed String
    return b += a.substring(pos);
  }
}