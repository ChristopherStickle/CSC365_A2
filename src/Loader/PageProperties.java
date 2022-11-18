package Loader;//This abstraction of a webpage will hold all relevant url contents separately

import Loader.ExtendableHashTable;

import java.io.Serializable;
import java.util.ArrayList;

public class PageProperties implements Serializable {
    //static final long serialVersionUID = 1L;

    String name;
    ExtendableHashTable local_words_eht = new ExtendableHashTable();
    String[] parsed_words;

    public PageProperties(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }
    public void setEHT(ExtendableHashTable local_words_eht) {
        this.local_words_eht = local_words_eht;
    }
    public ExtendableHashTable getEHT() {
        return local_words_eht;
    }
    public String[] getParsed_words() {
        return parsed_words;
    }
    public void setParsed_words(String[] parsed_words) {
        this.parsed_words = parsed_words;
    }

    public String toString(){
        return name;
    }
}
