package Loader;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class ExtendableHashTable implements Serializable {
    static final long serialVersionUID = 811396553750565872L;

    private static class Entry implements Serializable {
        //static final long serialVersionUID = 1L;

        String key;
        int count;
        double tfidfScore;

        Entry(String key, int count, double tfidfScore) {
            this.key = key;
            this.count = count;
            this.tfidfScore = tfidfScore;
        }
    }

    private static class Bucket implements Serializable {
        //static final long serialVersionUID = 1L;

        ArrayList<Entry> entries;
        final static int DEFAULT_SIZE = 8;
        int local_depth = 1;

        Bucket() {
            entries = new ArrayList<>(DEFAULT_SIZE);
        }

        boolean isFull() {
            return entries.size() == DEFAULT_SIZE;
        }

        void incLocal_depth() {
            ++local_depth;
        }

        void setLocal_depth(int depth) {
            local_depth = depth;
        }
    }

    private int global_depth;
    private Bucket[] dir;
    //private int size = 2; // number of buckets in the table
    public ExtendableHashTable() {
        dir = new Bucket[(int) Math.pow(2, 1)];
        this.global_depth = 1;
            dir[0] = new Bucket();
            dir[1] = new Bucket();
    }

    public int getCount(String key) {
        int h = ( key.hashCode() & 0xfffffff );
        int i = (int) (h % (Math.pow(2, global_depth)));
        for (Entry e : dir[i].entries) {
            if (key.equals(e.key)) {
                return e.count;
            }
        }
        return 0;
    }

    public double getScore(String key) {
        int h = ( key.hashCode() & 0xfffffff );
        int i = (int) (h % (Math.pow(2, global_depth)));
        for (Entry e : dir[i].entries) {
            if (key.equals(e.key)) {
                return e.tfidfScore;
            }
        }
        return 0;
    }

    public void setScore(String key, double score) {
        int h = ( key.hashCode() & 0xfffffff );
        int i = (int) (h % (Math.pow(2, global_depth)));
        for (Entry e : dir[i].entries) {
            if (key.equals(e.key)) {
                e.tfidfScore = score;
            }
        }
    }

    boolean contains(String key) {
        int h = ( key.hashCode() & 0xfffffff );
        int i = (int) (h % (Math.pow(2, global_depth)));
        for (Entry e : dir[i].entries) {
            if (key.equals(e.key))
                return true;
        }
        return false;
    }

    public void add(String key) {
        int h = ( key.hashCode() & 0xfffffff );
        int i = (int) (h % (Math.pow(2, global_depth)));
        // Entry found, increment count
        for (Entry e : dir[i].entries) {
            if (key.equals(e.key)) {
                e.count++;
                return;
            }
        }
        // Entry not found, add to bucket
        // if the bucket is not full, add the entry
        if (!dir[i].isFull()) {
            dir[i].entries.add(new Entry(key, 1, 0));
        }
        // Otherwise:
        // 1. if the bucket is full, and the local depth is less than the global depth
        //      increase the local depth of the bucket
        //      and split the bucket into two buckets
        // 2. add the new bucket to the directory
        //      and update the relevant pointers across the directory
        // 3. rehash bucket entries with the new local depth
        // 4. call add(key) again
        else if (dir[i].isFull() && dir[i].local_depth < global_depth) {
            dir[i].incLocal_depth();
            Bucket newBucket = new Bucket();
            //++size;
            newBucket.setLocal_depth(dir[i].local_depth);
            // Make a copy of the entries of the original bucket
            //   We'll use this for rehashing later
            ArrayList<Entry> tempBucket = new ArrayList<>(dir[i].entries);
            dir[i].entries.clear(); // clear the original bucket
            // the index of the first occurrence of the original bucket in the directory
            int index = java.util.Arrays.asList(dir).indexOf(dir[i]);
            // the Local Depth of the original bucket
            int depth = dir[i].local_depth;
            // keep only the (depth)th bit of the index
            int bitMatchB1 = (index & (int) (Math.pow(2, depth) - 1));
            int bitMatchB2 = ( 1 << (depth - 1) ) | bitMatchB1;
            // for every index of the directory
            //  starting at the index of the directory where the original bucket was found
            //  if:
            //      the index of the directory has the same (depth)th bit as the original index of the original bucket
            //      assign the 1st bucket to the index of the new directory
            //  else if:
            //      the index of the directory has the same (depth)th bit as the index of the old directory
            //      assign the new bucket to the index of the new directory
            for (int j = index; j < dir.length; j++) {
                if ( (j & (int) (Math.pow(2, depth) - 1)) == bitMatchB1) {
                    dir[j] = dir[i];
                } else if ( (j & (int) (Math.pow(2, depth) - 1)) == bitMatchB2) {
                    dir[j] = newBucket;
                }
            }
            // rehash the entries of the original bucket
            for (Entry e : tempBucket) {
                add(e.key);
            }
            add(key);
        }

        // Otherwise:
        // 1. if the bucket is full, and the local depth is less than the global depth
        //      double the size of the directory
        // 2. map each bucket in the old directory to the appropriate index(s) in the new directory
        // 3. reassign the new directory as the only directory
        // 4. call add(key) again
        //      let previous method handle bucket split
        else if (dir[i].isFull() && dir[i].local_depth == global_depth) {
            ++global_depth;
            Bucket[] oldDir = dir;
            Bucket[] newDir = new Bucket[(int) Math.pow(2, global_depth)];
            for (Bucket b : oldDir) {
                // the index of the first occurrence of the bucket in the old directory
                int index = java.util.Arrays.asList(oldDir).indexOf(b);
                // the Local Depth of the bucket
                int depth = b.local_depth;
                // keep only the (depth)th bit of the index
                int bitMatch = (index & (int) (Math.pow(2, depth) - 1));
                // for every index of the new directory
                //  starting at the same index where the bucket was found in the old directory
                //  if:
                //      the index of the new directory has the same (depth)th bit as the index of the old directory
                //      assign the bucket to the index of the new directory
                for (int j = index; j < newDir.length; j++) {
                    if ( (j & (int) (Math.pow(2, depth) - 1)) == bitMatch) {
                        newDir[j] = b;
                    }
                }
            }
            dir = newDir;
            add(key);
        }
    }

}