package Loader;

import java.util.ArrayList;

public class Corpus {
    int total_number_of_words = 0;
    ArrayList<String> global_dictionary = new ArrayList<>();
    ExtendableHashTable global_dictionary_eht = new ExtendableHashTable();


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
