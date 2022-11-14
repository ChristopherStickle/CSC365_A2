package Loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        // Loop through all the links in seed_links.txt and load "see also" links into seed_links.txt
        /*WebScraper ws = new WebScraper();
        Scanner sc = new Scanner(new File("C:\\Users\\stick\\IdeaProjects\\CSC365_A2\\src\\Loader\\seed_links.txt"));
        ArrayList<String> seed_links = new ArrayList<>();
        while (sc.hasNextLine()) {
            seed_links.add(sc.nextLine());
        }
        for (String url:seed_links) {
            ws.writeSubLinks(url);
        }*/
        // parse each in sub_links and load into a frequency table
        WebScraper ws = new WebScraper();
        Scanner sc = new Scanner(new File("C:\\Users\\stick\\IdeaProjects\\CSC365_A2\\sub_links.txt"));
        ArrayList<String> sub_links = new ArrayList<>();
        while (sc.hasNextLine()) {
            sub_links.add(sc.nextLine());
        }
        for (String url:sub_links) {
            String[] words = ws.scrape(url);
            ExtendableHashTable eht = new ExtendableHashTable();
            for (String word:words) {
                eht.add(word);
            }
        }


        // write the Extendable Application.HashTable tables to a file, one for each table



        // test ExtendableHashTable ----------------------------------------------------------------------------
        /*ExtendableHashTable eht = new ExtendableHashTable();
        WebScraper ws = new WebScraper();
        String[] words = ws.scrape("https://en.wikipedia.org/wiki/Amaryllidaceae\n");
        for (String word : words) {
            eht.add(word);
        }
        // assert that each word in words is in the table
        System.out.println(words.length);
        for (String word : words) {
            System.out.println(word + " is in the table " + eht.contains(word));
            System.out.println(word + " has count " + eht.getCount(word));
        }*/
    }
}