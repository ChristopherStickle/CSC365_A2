package Loader;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        /**
          * This is the main method that is used to run the Loader.
          *
          **/

        WebScraper ws = new WebScraper();
        Scanner sc;


        // Loop through all the links in seed_links.txt and load "see also" links into seeAlsoLinks.txt
        /**
          * DONE
          **/
        /*WebScraper ws = new WebScraper();
        Scanner sc = new Scanner(new File("C:\\Users\\stick\\IdeaProjects\\CSC365_A2\\src\\Loader\\seed_links.txt"));
        ArrayList<String> seed_links = new ArrayList<>();
        while (sc.hasNextLine()) {
            seed_links.add(sc.nextLine());
        }
        for (String url:seed_links) {
            ws.writeSubLinks(url);
        }*/



        // parse each in url seeAlsoLinks, load into an Extendable HashTable, write the Extendable HashTable to a file, one for each table
        /**
          * DONE
          **/
        /*sc = new Scanner(new File("C:\\Users\\stick\\IdeaProjects\\CSC365_A2\\src\\Loader\\seeAlsoLinks.txt"));

        ArrayList<String> linksArray = new ArrayList<>();

        while (sc.hasNextLine()) {
            linksArray.add(sc.nextLine());
        }

        for (String url:linksArray) {
            FileOutputStream fos = new FileOutputStream("C:\\Users\\stick\\IdeaProjects\\CSC365_A2\\src\\EHTfiles\\"+url.substring(30));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            String[] words = ws.scrape(url);
            ExtendableHashTable eht = new ExtendableHashTable();
            for (String word:words) {
                eht.add(word);
            }
            oos.writeObject(eht);
            System.out.println("Done with ["+ linksArray.indexOf(url) + "] " + url);
        }*/








        // test ExtendableHashTable ----------------------------------------------------------------------------
        /*ExtendableHashTable eht = new ExtendableHashTable();
        WebScraper ws = new WebScraper();
        String[] words = ws.scrape("https://en.wikipedia.org/wiki/Amaryllidaceae\n");
        for (String word : words) {
            eht.add(word);
        }
        // assert that each word in words is in the table
        //System.out.println(words.length);
        *//*for (String word : words) {
            System.out.println(word + " is in the table " + eht.contains(word));
        }*//*
        // write eht to a file
        FileOutputStream fos = new FileOutputStream("C:\Users\stick\IdeaProjects\CSC365_A2\src\EHTfiles\testEHT");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(eht);
        //eht.writeObject(oos);

        // read eht from a file
        ExtendableHashTable ehtREAD = new ExtendableHashTable();
        FileInputStream fis = new FileInputStream("C:\Users\stick\IdeaProjects\CSC365_A2\src\EHTfiles\testEHT");
        ObjectInputStream ois = new ObjectInputStream(fis);
        ehtREAD = (ExtendableHashTable) ois.readObject();

        ois.close();
        System.out.println("-------------------READING THE FILE-----------------------");
        for (String word : words) {
            System.out.println(word + " is in the table " + ehtREAD.contains(word) + " with count " + ehtREAD.getCount(word));
        }*/
    }
}