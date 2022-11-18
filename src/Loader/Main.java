package Loader;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        /**
          * This is the main method that is used to run the Loader.
          *
          **/

        Corpus corpus = new Corpus();
        WebScraper ws = new WebScraper();
        Scanner sc;
//        ArrayList<String> global_dictionary = new ArrayList<>();
        ArrayList<PageProperties> pageList = new ArrayList<>();

        /** Loop through all the links in seed_links.txt and load "see also" links into seeAlsoLinks.txt
          *
          * DO NOT RUN THIS AGAIN, Links are trimmed down to 99 good urls.
          * DONE
          *
        WebScraper ws = new WebScraper();
        Scanner sc = new Scanner(new File("C:\\Users\\stick\\IdeaProjects\\CSC365_A2\\src\\Loader\\seed_links.txt"));
        ArrayList<String> seed_links = new ArrayList<>();
        while (sc.hasNextLine()) {
            seed_links.add(sc.nextLine());
        }
        for (String url:seed_links) {
            ws.writeSubLinks(url);
        }
         **/

//------------------------------------------------------------------------------------------------------------------------------------------------------
        /**
          * parse each in url seeAlsoLinks, load into an Extendable HashTable, write the Extendable HashTable to a file, one for each table
          * Make a PageProperties object for each url, load the Extendable HashTable from the file, and add it to the PageProperties object
          *
          * DONE
          **/
        /*sc = new Scanner(new File("src/Loader/seeAlsoLinks.txt"));

        ArrayList<String> linksArray = new ArrayList<>();
        pageList = new ArrayList<>();
        ExtendableHashTable corpus_eht = new ExtendableHashTable();
        ArrayList<String> global_dictionary = new ArrayList<>();

        while (sc.hasNextLine()) {
            linksArray.add(sc.nextLine());
        }

        for (String url:linksArray) {
            PageProperties page = new PageProperties(url.substring(30));

            FileOutputStream fos = new FileOutputStream("src/EHTfiles/"+url.substring(30));
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            String[] words = ws.scrape(url);
            System.out.println("Words parsed from "+url.substring(30));
            page.setParsed_words(words);
            corpus.addToWordCount(words.length);
            ExtendableHashTable eht = new ExtendableHashTable();

            for (String word:words) {
                eht.add(word);
                corpus_eht.add(word);
                if ( !global_dictionary.contains(word) ) { global_dictionary.add(word); }
            }
            System.out.println("EHT complete" + eht.contains("the"));
            oos.writeObject(eht);

            page.setEHT(eht);
            pageList.add(page);

            System.out.println("Done with ["+ linksArray.indexOf(url) + "] " + url);

        }
        corpus.setGlobal_dictionary(global_dictionary);
        corpus.setEHT(corpus_eht);

        for(String s : global_dictionary){
            System.out.println(s);
        }

        // write pages to files
        for(PageProperties page:pageList){
            FileOutputStream fos = new FileOutputStream("src/PageFiles/"+page.getName());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(page);
        }
        //write corpus to files
        FileOutputStream fos = new FileOutputStream("src/corpus");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(corpus);*/
//------------------------------------------------------------------------------------------------------------------------------------------------------
        /*// Make a PageProperties object for each url, load the Extendable HashTable from the file, and add it to the PageProperties object
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
        global_dictionary = (ArrayList<String>) ois.readObject();*/
//------------------------------------------------------------------------------------------------------------------------------------------------------
        // import PageProperties objects from files
        File dir = new File("src/PageFiles");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                //System.out.println(child.getName());
                PageProperties page;
                FileInputStream fis = new FileInputStream("src/PageFiles/"+child.getName());
                ObjectInputStream ois = new ObjectInputStream(fis);
                page = (PageProperties) ois.readObject();
                pageList.add(page);
            }
        } else {
            // throw an exception
            System.out.println("Error: No PageFiles directory found");
        }
        // import Corpus object from file
        FileInputStream fil = new FileInputStream("src/corpus");
        ObjectInputStream ois = new ObjectInputStream(fil);
        corpus = (Corpus) ois.readObject();

        /*
         * Clustering
         */

        TFIDFCalculator tfidfCalculator = new TFIDFCalculator( pageList, corpus.global_dictionary, corpus); //create a new ifidf calc
        corpus.setTFIDFToIDF(); //set all idf scores in the corpus
        tfidfCalculator.setAlltfidfScores(); //set all tfidf scores in all hashtables

        //instantiate a new clusterer
        Clusterer clusterer = new Clusterer(pageList, corpus.global_dictionary);


        clusterer.instantiateClusters(); //have the clusterer initialize its clusters
//        System.out.println("initial clusters");
//        for( Cluster cluster : clusterer.clusterArrayList){
//            System.out.println(cluster.medoid);
//        }
        System.out.println("Initial Clusters:");
        for( Cluster cluster : clusterer.clusterArrayList) {
            System.out.println(cluster); // print out the clusters to look
        }


        //do the cluster algorithm 5 times
        for(int i = 0; i < 3; i++){
            clusterer.swapClusters(); // run a swap on all clusters
            clusterer.recenterClusters(); // recenter all the clusters
            System.out.println("completed one iteration :)");
//            for( Cluster cluster : clusterer.clusterArrayList){
//                System.out.println(cluster); // print out the clusters to look
//            }
        }
        clusterer.finalRecenterClusters();

        System.out.println("Final Clusters:");
        for( Cluster cluster : clusterer.clusterArrayList) {
            System.out.println(cluster); // print out the clusters to look
        }

        FileOutputStream fos = new FileOutputStream("src/clusterer");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(clusterer);

    }
}