package Loader;

import java.io.Serializable;
import java.util.ArrayList;

public class Cluster implements Serializable {
    static final long serialVersionUID = 5340324687786490679L;

    public PageProperties medoid;
    public ArrayList<PageProperties> clusterList;

    public Cluster(PageProperties medoid){
        this.medoid = medoid;
        this.clusterList = new ArrayList<PageProperties>();
    }

    public PageProperties getMedoid(){
        return this.medoid;
    }
    public ArrayList<PageProperties> getClusterList(){
        return clusterList;
    }

    public boolean contains(PageProperties page){
        if(medoid == page)
            return true;
        else if(clusterList.contains(page))
            return true;
        else{
            return false;
        }
    }

    public String toString(){
        return "Medoid: " + this.medoid.getName() + "\n and the List: " + clusterList.toString();
    }

    public void setClusterList(ArrayList<PageProperties> tempClusterList) {
        this.clusterList = tempClusterList;
    }

    public void setMedoid(PageProperties newMedoid) {
        this.medoid = newMedoid;
    }
}