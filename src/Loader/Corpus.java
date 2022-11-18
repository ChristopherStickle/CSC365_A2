package Loader;

import java.io.Serializable;
import java.util.ArrayList;

public class Corpus implements Serializable {
    //static final long serialVersionUID = 1L;

    int total_number_of_words = 0;
    ArrayList<String> global_dictionary = new ArrayList<>();
    ExtendableHashTable global_dictionary_eht = new ExtendableHashTable();


    public void setTFIDFToIDF(){
        for( String s : global_dictionary ){ //for every unique word
            double countOfWord = global_dictionary_eht.getCount(s); //get global count
            double idfScore = 100 / countOfWord; // divide that by number of documents
            global_dictionary_eht.setScore(s, idfScore); // set tfidf score
//            System.out.println( "Key: " + s + " | Score: " + global_dictionary_eht.getScore(s));
        }
    }

    public int getWordCount() {
        return total_number_of_words;
    }
    public void addToWordCount(int n) {
        total_number_of_words += n;
    }
    public void setEHT(ExtendableHashTable eht) {
        global_dictionary_eht = eht;
    }
    public ExtendableHashTable getEHT() {
        return global_dictionary_eht;
    }
    public void setGlobal_dictionary(ArrayList<String> global_dictionary) {
        this.global_dictionary = global_dictionary;
    }
    public ArrayList<String> getGlobal_dictionary() {
        return global_dictionary;
    }
}
