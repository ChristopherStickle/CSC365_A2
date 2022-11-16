package Loader;

import java.util.ArrayList;

public class Clusterer {
    //all of its clusters
    private Cluster cluster0; private Cluster cluster1; private Cluster cluster2; private Cluster cluster3;
    private Cluster cluster4; private Cluster cluster5; private Cluster cluster6; private Cluster cluster7;
    private Cluster cluster8; private Cluster cluster9;

    //ArrayLists of clusters and PageProperties
    private ArrayList<Cluster> clusterArrayList;
    private ArrayList<PageProperties> allPages;
    private ArrayList<String> allUniqueWords;

    //constructor
    public Clusterer(ArrayList<PageProperties> allPages, ArrayList<String> allUniqueWords){
        //assign allPages and allUniqueWords
        this.allPages = allPages;
        this.allUniqueWords = allUniqueWords;

        //assign the clusters
        cluster0 = new Cluster(allPages.get(0)); cluster1 = new Cluster(allPages.get(1)); cluster2 = new Cluster(allPages.get(2));
        cluster3 = new Cluster(allPages.get(3)); cluster4 = new Cluster(allPages.get(4)); cluster5 = new Cluster(allPages.get(5));
        cluster6 = new Cluster(allPages.get(6)); cluster7 = new Cluster(allPages.get(7)); cluster8 = new Cluster(allPages.get(8));
        cluster9 = new Cluster(allPages.get(9));

        //add all clusters to an ArrayList to operate on later
        clusterArrayList.add(cluster0); clusterArrayList.add(cluster1); clusterArrayList.add(cluster2); clusterArrayList.add(cluster3);
        clusterArrayList.add(cluster4); clusterArrayList.add(cluster5); clusterArrayList.add(cluster6); clusterArrayList.add(cluster7);
        clusterArrayList.add(cluster8); clusterArrayList.add(cluster9);
    }

    public double findCosSim(PageProperties pageA, PageProperties pageB){
        //variables to play with
        double numerator = 0; double magnitudeA = 0; double magnitudeB = 0;
        ExtendableHashTable pageAHT = pageA.getLocal_words_eht();
        ExtendableHashTable pageBHT = pageB.getLocal_words_eht();
        //get entire numerator, and magnitudes
        for(String s: allUniqueWords){
            //collect magnitudeA
            if(pageAHT.contains(s)){
                magnitudeA = magnitudeA + (pageAHT.getScore(s) * pageAHT.getScore(s));
            }
            //collect magnitudeB
            if(pageBHT.contains(s)){
                magnitudeB = magnitudeB + (pageBHT.getScore(s) * pageBHT.getScore(s));
            }
            //collect numerator
            if(pageAHT.contains(s) && pageBHT.contains(s)){
                numerator = numerator + (pageAHT.getScore(s) * pageBHT.getScore(s));
            }
        }
        //find magnitude of A
        magnitudeA = Math.sqrt(magnitudeA);
        //find magnitude of B
        magnitudeB = Math.sqrt(magnitudeB);

        //calculate and return cosSim
        return ( numerator / magnitudeA * magnitudeB );
    }
}