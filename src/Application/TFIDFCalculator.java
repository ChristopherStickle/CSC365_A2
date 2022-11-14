package Application;

import java.util.ArrayList;

public class TFIDFCalculator {

    private static final double DOC_COUNT = 10;

    public double tf(HashTable doc, String word){
       // tf(t,d) = n/N
       //     n is the number of times term t appears in the document d.
       //     N is the total number of terms in the document d.
        if ( doc.contains(word) )
            return (double)doc.getCount(word)/DOC_COUNT;
        return 0;
    }
    public double idf(ArrayList<HashTable> doc_list, String word){
        // idf(t,D) = log (N/( n))
        //     N is the number of documents in the data set.
        //     n is the number of documents that contain the term t among the data set.
        double word_count = 0;
        for (HashTable doc : doc_list) {
            if (doc.contains(word)) word_count++;
        }
        if ( word_count > 0)
            return Math.log(doc_list.size() / word_count);
        return 0;
    }
    public double tfidf(ArrayList<HashTable> doc_list, HashTable doc, String word){
        // TFIDF(t,d,D) = tf(t,d) * idf(t,D)
        return Math.abs( tf( doc, word)*idf( doc_list, word) );
    }

}
