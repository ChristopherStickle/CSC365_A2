package Loader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Clusterer implements Serializable {
    //all of its clusters
    private Cluster cluster0; private Cluster cluster1; private Cluster cluster2; private Cluster cluster3;
    private Cluster cluster4; private Cluster cluster5; private Cluster cluster6; private Cluster cluster7;
    private Cluster cluster8; private Cluster cluster9;

    //ArrayLists of clusters and PageProperties
    public ArrayList<Cluster> clusterArrayList;
    public ArrayList<PageProperties> allPages;
    public ArrayList<String> allUniqueWords;
    public ArrayList<PageProperties> allMedoids = new ArrayList<>();
    Random random = new Random(1);

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
//                System.out.println("Key: " + s + " | tfidf score: " + pageA.local_words_eht.getScore(s) + " | ");
                magnitudeA = magnitudeA + (pageA.local_words_eht.getScore(s) * pageA.local_words_eht.getScore(s));
//                System.out.println("loop magA: " + magnitudeA + " | Key: " + s + " | tdidf in A: " + pageA.getEHT().getScore(s) + " | tdidf in B: " + pageB.getEHT().getScore(s));
            }
            //collect magnitudeB
            if(pageB.local_words_eht.contains(s)){
                magnitudeB = magnitudeB + (pageB.local_words_eht.getScore(s) * pageB.local_words_eht.getScore(s));
            }
            //collect numerator
            if(pageA.local_words_eht.contains(s) && pageB.local_words_eht.contains(s)){
//                System.out.println( "Key that worked: " + s + " | pageA word score: " + pageA.getEHT().getScore(s) + " | pageB word score: " + pageB.getEHT().getScore(s));
                numerator = numerator +  ((pageA.local_words_eht.getScore(s) * pageB.local_words_eht.getScore(s)));

            }
        }
//        System.out.println("MagA: " + magnitudeA);
        magnitudeA = Math.sqrt(magnitudeA); //find magnitude of A
//        System.out.println("MagB: " + magnitudeB);
        magnitudeB = Math.sqrt(magnitudeB); //find magnitude of A

        //calculate and return cosSim
//        System.out.println("Num: " + numerator + " | magA: " + magnitudeA + " | magB: " + magnitudeB);
        return ( numerator / ( magnitudeA * magnitudeB ) ) ;
    }

    public void swapClusters(){
        ArrayList<ArrayList<PageProperties>> listOfTempLists = new ArrayList<>(10); // create a list of lists
        for(int i = 0; i <10; i++){
            ArrayList newList = new ArrayList<PageProperties>(); // fill list with lists
            listOfTempLists.add(newList);
        }
//        System.out.println(listOfTempLists);

        for(PageProperties p : allPages){ // for every page there is
            if(!allMedoids.contains(p)){ // skip the medoids do not swap those
                double bestCosSim = 0; int index = 0; // create something to compare to and keep best index
                for(Cluster c : clusterArrayList){ // for every cluster we have
                    double newCosSim = findCosSim(p, c.medoid); // find distance for every other medoid
                    if(newCosSim > bestCosSim){ // if the new medoid is closer
                        bestCosSim = newCosSim; // update the score
                        index = clusterArrayList.indexOf(c); //remember the index
                    }
                }
                double randomNum = random.nextDouble();
                if(randomNum > 0.15) { // 15 percent of the time we aren't going to swap the thing
                    listOfTempLists.get(index).add(p); // put the page into the correct list in the list
                }
                else if(randomNum < 0.5){ //sometimes we will add it to a random cluster
                    listOfTempLists.get(random.nextInt(10)).add(p); // put the page into the correct list in the list
                } // if it's neither the page just stays where it was
            }
        }
        for(Cluster c : clusterArrayList){ //reassign the lists
            c.clusterList = listOfTempLists.get(clusterArrayList.indexOf(c));
            if (c.clusterList.size() == 0) {
                c.clusterList.add(c.medoid);
            }
//            System.out.println(c);
        }
    }

    public void recenterClusters(){

        for(Cluster c : clusterArrayList){ // for every cluster we have
            double bestClusterSum = 0; int bestIndex = 0; // create things to compare to and keep track of the best
            for(PageProperties p : c.clusterList){ // for every page in the cluster
                double newSum = 0; //make second object to compare to
                for(PageProperties p1 : c.clusterList){ // for every page in the cluster
                    newSum = newSum + findCosSim(p, p1); // let each page act as the medoid
                }
                if(newSum > bestClusterSum){ // if this page was a better medoid
//                    System.out.println("We be swapping");
                    bestIndex = c.clusterList.indexOf(p); // note the index
                    bestClusterSum = newSum; // replace the sum and do it again
                }
            }
            double randomNum = random.nextDouble();
            if (randomNum > .20) {
//                System.out.println("correct recenter");
                c.setMedoid(c.clusterList.get(bestIndex)); // set the best as the new medoid and go to next cluster
            } else {
//                System.out.println("random recenter");
                c.setMedoid(c.clusterList.get(random.nextInt(c.clusterList.size())));
            }
        }
    }

    public void finalRecenterClusters(){

        for(Cluster c : clusterArrayList){ // for every cluster we have
            double bestClusterSum = 0; int bestIndex = 0; // create things to compare to and keep track of the best
            for(PageProperties p : c.clusterList){ // for every page in the cluster
                double newSum = 0; //make second object to compare to
                for(PageProperties p1 : c.clusterList){ // for every page in the cluster
                    newSum = newSum + findCosSim(p, p1); // let each page act as the medoid
                }
                if(newSum > bestClusterSum){ // if this page was a better medoid
//                    System.out.println("We be swapping");
                    bestIndex = c.clusterList.indexOf(p); // note the index
                    bestClusterSum = newSum; // replace the sum and do it again
                }
            }
//            System.out.println("correct recenter");
            c.setMedoid(c.clusterList.get(bestIndex)); // set the best as the new medoid and go to next cluster
        }
    }

    private void instantiateMedoids() {
        ArrayList<PageProperties> tempAllPages = allPages; // create a copy of allPages to work with

        Random random = new Random();
        int nextRand = random.nextInt(90); //create a random. Does not allow to pick last 10,
                                                    // it's a bit of a cop out, but it's 2:16am

        cluster0 = new Cluster(allPages.get(nextRand)); // create a cluster with a random medoid
        cluster0.clusterList.add(allPages.get(nextRand)); // add the medoid to the cluster
        tempAllPages.remove(allPages.get(nextRand)); // remove the used medoid so we do not have duplicates
        allMedoids.add(allPages.get(nextRand)); // add the medoid to an array list for later

        nextRand = random.nextInt(tempAllPages.size()); // pick a new random
        cluster1 = new Cluster(allPages.get(nextRand)); //repeat until finished
        cluster1.clusterList.add(allPages.get(nextRand));
        tempAllPages.remove(allPages.get(nextRand));
        allMedoids.add(allPages.get(nextRand));

        nextRand = random.nextInt(tempAllPages.size());
        cluster2 = new Cluster(allPages.get(nextRand));
        cluster2.clusterList.add(allPages.get(nextRand));
        tempAllPages.remove(allPages.get(nextRand));
        allMedoids.add(allPages.get(nextRand));

        nextRand = random.nextInt(tempAllPages.size());
        cluster3 = new Cluster(allPages.get(nextRand));
        cluster3.clusterList.add(allPages.get(nextRand));
        tempAllPages.remove(allPages.get(nextRand));
        allMedoids.add(allPages.get(nextRand));

        nextRand = random.nextInt(tempAllPages.size());
        cluster4 = new Cluster(allPages.get(nextRand));
        cluster4.clusterList.add(allPages.get(nextRand));
        tempAllPages.remove(allPages.get(nextRand));
        allMedoids.add(allPages.get(nextRand));

        nextRand = random.nextInt(tempAllPages.size());
        cluster5 = new Cluster(allPages.get(nextRand));
        cluster5.clusterList.add(allPages.get(nextRand));
        tempAllPages.remove(allPages.get(nextRand));
        allMedoids.add(allPages.get(nextRand));

        nextRand = random.nextInt(tempAllPages.size());
        cluster6 = new Cluster(allPages.get(nextRand));
        cluster6.clusterList.add(allPages.get(nextRand));
        tempAllPages.remove(allPages.get(nextRand));
        allMedoids.add(allPages.get(nextRand));

        nextRand = random.nextInt(tempAllPages.size());
        cluster7 = new Cluster(allPages.get(nextRand));
        cluster7.clusterList.add(allPages.get(nextRand));
        tempAllPages.remove(allPages.get(nextRand));
        allMedoids.add(allPages.get(nextRand));

        nextRand = random.nextInt(tempAllPages.size());
        cluster8 = new Cluster(allPages.get(nextRand));
        cluster8.clusterList.add(allPages.get(nextRand));
        tempAllPages.remove(allPages.get(nextRand));
        allMedoids.add(allPages.get(nextRand));

        nextRand = random.nextInt(tempAllPages.size());
        cluster9 = new Cluster(allPages.get(nextRand));
        cluster9.clusterList.add(allPages.get(nextRand));
        tempAllPages.remove(allPages.get(nextRand));
        allMedoids.add(allPages.get(nextRand));
    }
    public void instantiateClusters(){
        Random random = new Random();
        //for every page we have, assign it to a random cluster
        for(PageProperties p : allPages){
            //choose a random list to put it in.
            Cluster c = clusterArrayList.get(random.nextInt(10));
            if (c.getMedoid() != p ){ // medoids are already in the cluster list
                c.getClusterList().add(p);
            }
        }
    }

    //Old throwaway code to see my past attempts
    /*
    public void swapClusters(){
        //for every cluster we have
        for(Cluster c : clusterArrayList) {
            if(c.clusterList.size() != 0) {
                ArrayList<PageProperties> removeList = new ArrayList<>();
                // for every page in the list
                for (PageProperties p : allPages) {// for every page in the cluster
                    //if p is not the medoid we CANNOT Swap medoid
                    if (p != c.medoid) {
                        //each item in that cluster needs to see if another medoid fits better.
                        for (Cluster cOther : clusterArrayList) {// for every other cluster we have
                            //calculate distance to current medoid and other medoid
                            double currentDistance = findCosSim(p, c.getMedoid());
                            double newdistance = findCosSim(p, cOther.getMedoid());
                            System.out.println("currentDistance: " + currentDistance + " | newDistance: " + newdistance);

                            if (newdistance > currentDistance) { //when the new medoid is closer
                                //remove the Page from this cluster and add to the new cluster
                                cOther.clusterList.add(p);
                                removeList.add(p);
                                System.out.println("Moved: " + p + " to cluster: " + cOther.medoid.getName());
                            }
                        }
                    }
                }
                for (PageProperties r : removeList) {
                    c.clusterList.remove(r);
                }
            }
        }

    }
     */ // swap cluster
    /*
    public void recenterClusters(){
        for(Cluster c : clusterArrayList){ // for every cluster
            double currentSum = 0; // create a currentSum
            double newSum = 0; // create a newSum
            for(PageProperties p : allPages){ // for every page
                currentSum = currentSum + findCosSim(c.medoid, p); // build up the sum of the cluster
            }
            for(PageProperties p : c.clusterList){ // for every page
                for(PageProperties p1 : c.clusterList){
                    newSum = newSum + findCosSim(p, p1); // build the new sum if the cluster was centered around a different page
                }
                if( newSum > currentSum ){ // if the newSum has a higher sum, i.e. all the cos sims are closer to 1
                    System.out.println("entered the swap zone | From: " + c.getMedoid() + " to: " + p);
                    currentSum = newSum; //set current sum to new sum
                    c.setMedoid(p); //set medoid to new medoid

                }
            }
        }
    }
    */ // recenter cluster
    /*
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
                c.setClusterList(tempClusterList);
                c.setMedoid(tempMedoid);
                System.out.println("butter me up \n");
            }
        }
    }
}
*/ // recenter cluster
    /*
    public void instantiateClusters(){
        //Every page needs to be assigned to the cluster it fits closest with
        for(PageProperties p : allPages){ // for every page in the pageList
            if()
            int matchIndex = 0; double bestCosSim = 0; //instantiate the index to assign later
            for()
        }
    }

     */ // instantiate
    /*
    public void swapClusters(){
        //for every cluster we have
        for(Cluster c : clusterArrayList) {
            if(c.clusterList.size() != 0) {
                ArrayList<PageProperties> removeList = new ArrayList<>();
                // for every page in the list
                for (PageProperties p : allPages) {// for every page in the cluster
                    //if p is not the medoid we CANNOT Swap medoid
                    if (p != c.medoid) {
                        //each item in that cluster needs to see if another medoid fits better.
                        for (Cluster cOther : clusterArrayList) {// for every other cluster we have
                            //calculate distance to current medoid and other medoid
                            double currentDistance = findCosSim(p, c.getMedoid());
                            double newdistance = findCosSim(p, cOther.getMedoid());
                            System.out.println("currentDistance: " + currentDistance + " | newDistance: " + newdistance);

                            if (newdistance > currentDistance) { //when the new medoid is closer
                                //remove the Page from this cluster and add to the new cluster
                                cOther.clusterList.add(p);
                                removeList.add(p);
                                System.out.println("Moved: " + p + " to cluster: " + cOther.medoid.getName());
                            }
                        }
                    }
                }
                for (PageProperties r : removeList) {
                    c.clusterList.remove(r);
                }
            }
        }

    }
     */ // swap cluster
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

    */ // recenter cluster
    /*
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
    */ // instantiate metoids (first 10)
    }