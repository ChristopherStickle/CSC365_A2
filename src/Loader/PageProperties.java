package Loader;//This abstraction of a webpage will hold all relevant url contents separately

import Loader.ExtendableHashTable;

import java.util.ArrayList;

public class PageProperties {

    private String name;
    private ExtendableHashTable local_words_eht = new ExtendableHashTable();
    static TFIDFCalculator tfidfCalc = new TFIDFCalculator();

    public PageProperties(String name, ExtendableHashTable local_words_eht) {

        this.name=name;
        this.local_words_eht=local_words_eht;

    }

    public String getName() {
        return name;
    }
    public void setLocal_words_eht(ExtendableHashTable local_words_eht) {
        this.local_words_eht = local_words_eht;
    }
    public ExtendableHashTable getLocal_words_eht() {
        return local_words_eht;
    }

}
