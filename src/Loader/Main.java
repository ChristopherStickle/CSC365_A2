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

        ArrayList<String> global_dictionary = new ArrayList<>(); //A list of all the unique words encountered across all URLs
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

        /*
        sc = new Scanner(new File("C:\\Users\\stick\\IdeaProjects\\CSC365_A2\\src\\Loader\\seeAlsoLinks.txt"));

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
                if ( !global_dictionary.contains(word) ) { global_dictionary.add(word); }
            }
            oos.writeObject(eht);
            System.out.println("Done with ["+ linksArray.indexOf(url) + "] " + url);
        }
        FileOutputStream fos = new FileOutputStream("C:\\Users\\stick\\IdeaProjects\\CSC365_A2\\src\\corpus");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(global_dictionary);

        */

        // Make a PageProperties object for each url, load the Extendable HashTable from the file, and add it to the PageProperties object
        // Add the PageProperties object to an ArrayList
        ArrayList<PageProperties> pageList = new ArrayList<>();
        File dir = new File("src/EHTfiles");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                //System.out.println(child.getName());
                ExtendableHashTable EHT = new ExtendableHashTable();
                PageProperties page = new PageProperties(child.getName(), EHT);
                FileInputStream fis = new FileInputStream("src/EHTfiles/"+child.getName());
                ObjectInputStream ois = new ObjectInputStream(fis);
                page.setLocal_words_eht((ExtendableHashTable) ois.readObject());
                pageList.add(page);
            }
        } else {
            // throw an exception
        }
        FileInputStream fil = new FileInputStream("src/corpus");
        ObjectInputStream ois = new ObjectInputStream(fil);
        global_dictionary = (ArrayList<String>) ois.readObject();

        //assign all the tdidf scores in the hashtables



        //System.out.println(globalDictionary);

//        System.out.println(pageList.size());
//        for( PageProperties p : pageList){
//            System.out.println(p.getName());
//        }

        /*
         * Clustering
         */


        //instantiate a new clusterer
        Clusterer clusterer = new Clusterer(pageList, global_dictionary);

        PageProperties page1 = pageList.get(0); System.out.println("Page1: "+ page1);
        PageProperties page2 = pageList.get(1); System.out.println("Page2: "+ page2);

        System.out.println("tfidf score of the in page1: " + page1.getLocal_words_eht().getScore("the"));
        System.out.println("tfidf score of rousseau in page2: " + page2.getLocal_words_eht().getScore("rousseau"));

        if (page1.getLocal_words_eht().contains("the"))
            System.out.println("Page1 has the. ");

        if (page2.getLocal_words_eht().contains("the"))
            System.out.println("Page2 has the. ");

        if(global_dictionary.contains("the"))
            System.out.println("dictionary has the");



        double tester = clusterer.findCosSim(page1, page2);
        System.out.println(tester);


        /*
        //have the clusterer initialize its clusters
        clusterer.instantiateClusters();

        //do the cluster algorithm 5 times
        for(int i = 0; i < 5; i++){
            clusterer.swapClusters();
            clusterer.recenterClusters();
            System.out.println("completed one iteration :)\n"); //System.out.println();
            for( Cluster cluster : clusterer.clusterArrayList){
                System.out.println(cluster);
            }
        }


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