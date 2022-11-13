package Loader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WebScraper {

    // scrapes the body of a wiki page and returns it as a String[]
    public static String[] scrape(String url) throws IOException {
        try {
            Document doc = Jsoup.connect(url).get();
            doc = new Cleaner(Safelist.basic()).clean(doc);
            String[] words = doc
                    .body()
                    .text()
                    .replaceAll("[^a-zA-Z0-9]"," ")
                    .toLowerCase()
                    .split("\\s+");
            return words;
        } catch (IOException ex) {
            System.out.println("Connection Error, Please try again...");
            ex.printStackTrace();
        }
        return null;
    }
    // This method will write all the "see also" links on the page to a file
    public static void writeSubLinks(String seed)
    {
        ArrayList<String> sub_links = new ArrayList<>();
        try
        {
            Document doc = Jsoup.connect(seed).get();
            Elements links = doc.select("div.div-col")
                                .select("a");
            for(Element link : links) {
                sub_links.add(link.attr("abs:href"));
                //System.out.println(link.attr("abs:href") + " added to sub_links");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter("sub_links.txt");
            for (String link : sub_links) {
                myWriter.write(link + "\r\n");
                System.out.println(link);
            }
            myWriter.close();
            //System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
