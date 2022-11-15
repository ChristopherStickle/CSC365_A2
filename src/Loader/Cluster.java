package Loader;

import java.util.ArrayList;

public class Cluster {
    private PageProperties metiod;
    private ArrayList<PageProperties> clusterList;

    public Cluster(Loader.PageProperties metiod){
        this.metiod = metiod;
        this.clusterList = new ArrayList<PageProperties>();
    }



}