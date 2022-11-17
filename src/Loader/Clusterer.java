package Loader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Clusterer {
    //all of its clusters
    private Cluster cluster0; private Cluster cluster1; private Cluster cluster2; private Cluster cluster3;
    private Cluster cluster4; private Cluster cluster5; private Cluster cluster6; private Cluster cluster7;
    private Cluster cluster8; private Cluster cluster9;

    //ArrayLists of clusters and PageProperties
    ArrayList<Cluster> clusterArrayList;
    private ArrayList<PageProperties> allPages;
    private ArrayList<String> allUniqueWords;

    //constructor
    public Clusterer(ArrayList<PageProperties> allPages, ArrayList<String> allUniqueWords){
        //assign allPages and allUniqueWords
        this.allPages = allPages;
        this.allUniqueWords = allUniqueWords;
        this.clusterArrayList = new ArrayList<>();

        //assign the clusters
        instantiateMedoids();

        //add all clusters to an ArrayList to operate on later
        clusterArrayList.add(cluster0); clusterArrayList.add(cluster1); clusterArrayList.add(cluster2); clusterArrayList.add(cluster3);
        clusterArrayList.add(cluster4); clusterArrayList.add(cluster5); clusterArrayList.add(cluster6); clusterArrayList.add(cluster7);
        clusterArrayList.add(cluster8); clusterArrayList.add(cluster9);
    }

    public double findCosSim(PageProperties pageA, PageProperties pageB){
        //variables to play with
        double numerator = 1; double magnitudeA = 1; double magnitudeB = 1;
//        pageA.local_words_eht;
//        pageB.local_words_eht;
        //get entire numerator, and magnitudes
        for(String s: allUniqueWords){
            //collect magnitudeA
            if(pageA.local_words_eht.contains(s)){
                System.out.println("Key: " + s + " | tfidf score: " + pageA.local_words_eht.getScore(s) + " | ");
                magnitudeA = magnitudeA + (pageA.local_words_eht.getScore(s) * pageA.local_words_eht.getScore(s));

            }
            //collect magnitudeB
            if(pageB.local_words_eht.contains(s)){
                magnitudeB = magnitudeB + (pageB.local_words_eht.getScore(s) * pageB.local_words_eht.getScore(s));
            }
            //collect numerator
            if(pageA.local_words_eht.contains(s) && pageB.local_words_eht.contains(s)){
//                System.out.println( "Key that worked: " + s + " | pageA word score: " + pageAHT.getScore(s) + " | pageB word score: " + pageBHT.getScore(s));
                numerator = numerator +  ((pageA.local_words_eht.getScore(s) * pageB.local_words_eht.getScore(s)));
//                System.out.println(numerator);
            }
        }
        //find magnitude of A
        magnitudeA = Math.sqrt(magnitudeA);
        //find magnitude of B
        magnitudeB = Math.sqrt(magnitudeB);

        //calculate and return cosSim
        return ( numerator / magnitudeA * magnitudeB );
    }
    public void swapClusters(){
        //for every cluster we have
        for(Cluster c : clusterArrayList) {
            // for every page in the list
            for (PageProperties p : c.getClusterList()) {
                //each item in that cluster needs to see if another medoid fits better.
                for (Cluster cOther : clusterArrayList) {
                    //calculate distance to current medoid and other medoid
                    double currentDistance = findCosSim(p, c.getMedoid());
                    double newdistance = findCosSim(p, cOther.getMedoid());

                    //when the current distance is greater than new distance
                    if(currentDistance > newdistance){
                        //remove the Page from this cluster and add to the new cluster
                        c.getClusterList().remove(p);
                        cOther.getClusterList().add(p);
                    }
                }
            }
        }

    }


    public void recenterClusters() {
        // for every cluster in cluster
        for (Cluster c : clusterArrayList) {
            if(c.clusterList.size() != 0) { //if the cluster is more than a medoid
                //calculate current medoid score
                double currentSum = 0;
                for (PageProperties p : c.clusterList) {
                    currentSum = currentSum + findCosSim(p, c.medoid);
                }
                //randomly reassign medoid
                Random random = new Random(); // create new random number generator
                int randomNumber = random.nextInt(c.getClusterList().size()); //get a random number between 0 and #of pages in cluster
                PageProperties tempMedoid = c.clusterList.get(randomNumber); //the new medoid

                //create temp cluster list and remove current Medoid
                ArrayList<PageProperties> tempClusterList = c.getClusterList(); // create temp arraylist
                tempClusterList.remove(tempMedoid); //remove temp medoid from the group
                tempClusterList.add(c.medoid); //add current medoid to calculate

                //recalc w/ new medoid
                double newSum = 0;
                for (PageProperties p : tempClusterList) {
                    newSum = newSum + findCosSim(p, tempMedoid);
                }
                System.out.println("NewSum: " + newSum + " OldSum: " + currentSum + "\n");
                //if new medoid score is better swap
                if (newSum < currentSum) {
                    c.reassignClusterList(tempClusterList);
                    c.reassignMedoid(tempMedoid);
                    System.out.println("butter me up \n");
                }
            }
        }
    }


    /*
    public void recenterClusters(){
        //for every cluster
        for(Cluster c : clusterArrayList){
            //if the cluster is just a medoid skip it
            if(c.clusterList.size() != 0) {

                PageProperties tempMedoid = null; //make a variable to remember later
                //for every page in a cluster
                for (PageProperties p : c.clusterList) {
                    //decide if Page p is a better medoid than the current medoid
                    //do math need to make sure cosSim is actually working go test it dummy




                }
            }
        }
    }

    */

    private void instantiateMedoids(){
        cluster0 = new Cluster(allPages.get(0));
        cluster1 = new Cluster(allPages.get(1));
        cluster2 = new Cluster(allPages.get(2));
        cluster3 = new Cluster(allPages.get(3));
        cluster4 = new Cluster(allPages.get(4));
        cluster5 = new Cluster(allPages.get(5));
        cluster6 = new Cluster(allPages.get(6));
        cluster7 = new Cluster(allPages.get(7));
        cluster8 = new Cluster(allPages.get(8));
        cluster9 = new Cluster(allPages.get(9));
    }
    /*
    private void instantiateMedoids() {
        ArrayList<PageProperties> tempAllPages = allPages;

        Random random = new Random();
        int nextRand = random.nextInt(tempAllPages.size());

        cluster0 = new Cluster(allPages.get(nextRand));
        tempAllPages.remove(allPages.get(nextRand));

        nextRand = random.nextInt(tempAllPages.size());
        cluster1 = new Cluster(allPages.get(nextRand));
        tempAllPages.remove(allPages.get(nextRand));

        nextRand = random.nextInt(tempAllPages.size());
        cluster2 = new Cluster(allPages.get(nextRand));
        tempAllPages.remove(allPages.get(nextRand));

        nextRand = random.nextInt(tempAllPages.size());
        cluster3 = new Cluster(allPages.get(nextRand));
        tempAllPages.remove(allPages.get(nextRand));

        nextRand = random.nextInt(tempAllPages.size());
        cluster4 = new Cluster(allPages.get(nextRand));
        tempAllPages.remove(allPages.get(nextRand));

        nextRand = random.nextInt(tempAllPages.size());
        cluster5 = new Cluster(allPages.get(nextRand));
        tempAllPages.remove(allPages.get(nextRand));

        nextRand = random.nextInt(tempAllPages.size());
        cluster6 = new Cluster(allPages.get(nextRand));
        tempAllPages.remove(allPages.get(nextRand));

        nextRand = random.nextInt(tempAllPages.size());
        cluster7 = new Cluster(allPages.get(nextRand));
        tempAllPages.remove(allPages.get(nextRand));

        nextRand = random.nextInt(tempAllPages.size());
        cluster8 = new Cluster(allPages.get(nextRand));
        tempAllPages.remove(allPages.get(nextRand));

        nextRand = random.nextInt(tempAllPages.size());
        cluster9 = new Cluster(allPages.get(nextRand));
        tempAllPages.remove(allPages.get(nextRand));
    }
     */


    public void instantiateClusters(){
        Random random = new Random();
        //for every page we have, assign it to a random cluster
        for(PageProperties p : allPages){
            //choose a random list to put it in.
            Cluster c = clusterArrayList.get(random.nextInt(10));
            if (c.getMedoid() != p ){
                c.getClusterList().add(p);
            }
        }
    }

    }