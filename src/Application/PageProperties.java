package Application;//This abstraction of a webpage will hold all relevant url contents separately

import Loader.ExtendableHashTable;

import java.util.ArrayList;

public class PageProperties {

    private String name;
    private HashTable local_words = new HashTable();
    private ExtendableHashTable local_words_eht = new ExtendableHashTable();
    static TFIDFCalculator tfidfCalc = new TFIDFCalculator();

    public PageProperties(String name, ExtendableHashTable local_words_eht) {

        this.name=name;
        this.local_words_eht=local_words_eht;

    }
    public PageProperties(String name, HashTable local_words) {

        this.name=name;
        this.local_words=local_words;
    }

    public String getName() {
        return name;
    }
    public HashTable getLocal_words() {
        return local_words;
    }
    public void setLocal_words_eht(ExtendableHashTable local_words_eht) {
        this.local_words_eht = local_words_eht;
    }
    public ExtendableHashTable getLocal_words_eht() {
        return local_words_eht;
    }

    static void setScores(ArrayList<PageProperties> pageList, ArrayList<String> global_dictionary){
        ArrayList<HashTable> docs = new ArrayList<>();
        for( PageProperties page : pageList){
            docs.add(page.getLocal_words());
        }
        for( PageProperties page : pageList) {
            HashTable local_words = page.getLocal_words();

            for (String word : global_dictionary) {
                if (local_words.contains(word)) {
                    double score = tfidfCalc.tfidf(docs, local_words, word);
                    local_words.setScore(word, score);

                    //System.out.println(local_words.getScore(word)); //EchoCheck
                }
            }
        }
    }
}
