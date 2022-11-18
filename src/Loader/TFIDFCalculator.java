package Loader;

import java.util.ArrayList;

public class TFIDFCalculator {

static Corpus corpus;
    ArrayList<PageProperties> allPages;
    static ArrayList<String> globalDictionary;



    public TFIDFCalculator(ArrayList<PageProperties> allPages, ArrayList<String> globalDictionary, Corpus corpus){
        this.allPages = allPages;
        this.globalDictionary = globalDictionary;
        this.corpus = corpus;
//        System.out.println("Made the calculator");
//        System.out.println("all pages: " + allPages);
//        System.out.println("global dictionary: " + globalDictionary);
//        System.out.println("----------------------\n");
    }
    public TFIDFCalculator( ArrayList<String> globalDictionary, Corpus corpus){
        this.globalDictionary = globalDictionary;
        this.corpus = corpus;
    }

    public void setAlltfidfScores(){
        for(PageProperties page : allPages ){ // for every page we have
            for(String key : globalDictionary) { // for every word in the dictionary
                if (page.getEHT().contains(key)) {
                    double docCount = page.getEHT().getCount(key); //count of the word from the page
                    double docLength = page.getParsed_words().length; // total number of words from the page
                    double wordIDF = corpus.getEHT().getScore(key); //
                    double newScore = (docCount / docLength) * Math.log(wordIDF);
                    if(Math.abs(newScore) > 100.0){ //catches odd cases when the tfidf of a word is infinity
                        newScore = 0; //set it to zero, just scrap it we have enough numbers
                    }
                    page.getEHT().setScore(key, newScore); //set score in the HT
//                System.out.println("Key: " + key + " | Length: " + docLength + " | wordIDF: " + wordIDF);
                }
            }
        }
    }

    public static void setPagetfidfScore(PageProperties page){
        for(String key : globalDictionary) { // for every word in the dictionary
            if (page.getEHT().contains(key)) {
                double docCount = page.getEHT().getCount(key); //count of the word from the page
                double docLength = page.getParsed_words().length; // total number of words from the page
                double wordIDF = corpus.getEHT().getScore(key); //
                double newScore = (docCount / docLength) * Math.log(wordIDF);
                if(Math.abs(newScore) > 100.0){ //catches odd cases when the tfidf of a word is infinity
                    newScore = 0; //set it to zero, just scrap it we have enough numbers
                }
                page.getEHT().setScore(key, newScore); //set score in the HT
//                System.out.println("Key: " + key + " | Length: " + docLength + " | wordIDF: " + wordIDF);
            }
        }
    }

}
