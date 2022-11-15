package Loader;//This abstraction of a webpage will hold all relevant url contents separately

import java.util.ArrayList;

public class PageProperties {

    private String url;
    private String[] parsed_words;
    private ExtendableHashTable local_words = new ExtendableHashTable();
    private double similarityScore = 0;
    static TFIDFCalculator tfidfCalc = new TFIDFCalculator();

    public PageProperties(String url, String[] parsed_words){

        this.url=url;
        this.parsed_words=parsed_words;

        //foreach entry in the word[] add it to a Application.HashTable
        for (String word : parsed_words) {
            local_words.add(word);
        }


    }

    public String getUrl() {
        return url;
    }
    public String[] getParsed_words() {
        return parsed_words;
    }
    public ExtendableHashTable getLocal_words() {
        return local_words;
    }
    public double getSimilarityScore() {return similarityScore;}

    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }

    static void setScores(ArrayList<PageProperties> pageList, ArrayList<String> global_dictionary){
        ArrayList<ExtendableHashTable> docs = new ArrayList<>();
        for( PageProperties page : pageList){
            docs.add(page.getLocal_words());
        }
        for( PageProperties page : pageList) {
            ExtendableHashTable local_words = page.getLocal_words();

            for (String word : global_dictionary) {
                if (local_words.contains(word)) {
                    double score = tfidfCalc.tfidf(docs, local_words, word);
                    local_words.setScore(word, score);

                    //System.out.println(local_words.getScore(word)); //EchoCheck
                }
            }
        }
    }
    static void setTFIDFScores(ArrayList<PageProperties> pageList, String[] userIn){
        // for each word in userIn, for each page in page list, check if the word exists in the Application.HashTable, if so
        //      get the words tfidf score and add it to that page's simScore
        for(String word : userIn){
            for( PageProperties page : pageList){
                if ( page.getLocal_words().contains(word) )
                    page.similarityScore += page.getLocal_words().getScore(word);
            }
        }
        // Echo-Check
        /*for(Application.PageProperties page : pageList){
            System.out.println(page.similarityScore);
        }*/

    }
    static PageProperties[] getMaxSimScores(ArrayList<PageProperties> pageList){
        PageProperties[] suggestions = new PageProperties[3];
        for( PageProperties page : pageList){
            if ( suggestions[0]==null) suggestions[0]=page;
            else if (page.getSimilarityScore() > suggestions[0].getSimilarityScore() ) suggestions[0]=page;
            else if ( suggestions[1]==null) suggestions[1]=page;
            else if (page.getSimilarityScore() > suggestions[1].getSimilarityScore() ) suggestions[1]=page;
            else if ( suggestions[2]==null) suggestions[2]=page;
            else if (page.getSimilarityScore() > suggestions[2].getSimilarityScore() ) suggestions[2]=page;
        }
        return suggestions;
    }
}
