package Loader;

import java.util.ArrayList;

public class TFIDFCalculator {

//    private static final double DOC_COUNT = 100;
    Corpus corpus;
    ArrayList<PageProperties> allPages;
    ArrayList<String> globalDictionary;



    public TFIDFCalculator(ArrayList<PageProperties> allPages, ArrayList<String> globalDictionary, Corpus corpus){
        this.allPages = allPages;
        this.globalDictionary = globalDictionary;
        this.corpus = corpus;
//        System.out.println("Made the calculator");
//        System.out.println("all pages: " + allPages);
//        System.out.println("global dictionary: " + globalDictionary);
//        System.out.println("----------------------\n");
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


    //Old tfidf Calculator
    /*
    public double tf(ExtendableHashTable doc, String word){
       // tf(t,d) = n/N
       //     n is the number of times term t appears in the document d.
       //     N is the total number of terms in the document d.
        if ( doc.contains(word) )
            return (double)doc.getCount(word)/DOC_COUNT;
        return 0;
    }
    public double idf(ArrayList<ExtendableHashTable> doc_list, String word){
        // idf(t,D) = log (N/( n))
        //     N is the number of documents in the data set.
        //     n is the number of documents that contain the term t among the data set.
        double word_count = 0;
        for (ExtendableHashTable doc : doc_list) {
            if (doc.contains(word)) word_count++;
        }
        if ( word_count > 0)
            return Math.log(doc_list.size() / word_count);
        return 0;
    }
    public double tfidf(ArrayList<ExtendableHashTable> doc_list, ExtendableHashTable doc, String word){
        // TFIDF(t,d,D) = tf(t,d) * idf(t,D)
        return Math.abs( tf( doc, word)*idf( doc_list, word) );
    }
     */

}
