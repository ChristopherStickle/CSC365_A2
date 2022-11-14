package Application;

public class HashTable {

    static final class Node {
        String key;  //This is the word itself
        int count;   //This is the number of times the word appears
        double tfidfScore;
        Node next;   //The next word node
        Node(String k, int v, double s, Node n) { key = k; count = v; tfidfScore = s; next = n; }
    }
    Node[] table = new Node[8];
    int size = 0;

/*    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(size);
        for (int i = 0; i < table.length; ++i) {
            for (Node e = table[i]; e != null; e = e.next) {
                s.writeObject(e.key);
            }
        }
    }
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        int n = s.readInt();
        for (int i = 0; i < n; ++i)
            add((String) s.readObject());
    }*/
    public int getCount(String key)
    {
        int h = key.hashCode();
        int i = h & (table.length-1);
        for (Node e = table[i]; e != null; e = e.next)
        {
            if (key.equals(e.key))
            {
                return e.count;
            }
        }
        return 0;
    }
    public double getScore(String key)
    {
        int h = key.hashCode();
        int i = h & (table.length-1);
        for (Node e = table[i]; e != null; e = e.next)
        {
            if (key.equals(e.key))
            {
                return e.tfidfScore;
            }
        }
        return 0;
    }
    public void setScore(String key, double score)
    {
        int h = key.hashCode();
        int i = h & (table.length-1);
        for (Node e = table[i]; e != null; e = e.next)
        {
            if (key.equals(e.key))
            {
                e.tfidfScore = score;
            }
        }
    }
    boolean contains(String key) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key))
                return true;
        }
        return false;
    }
    void add(String key) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key)){
                e.count++;
                return;
            }
        }
        table[i] = new Node(key, 1, 0, table[i]);
        ++size;
        if ((float)size/table.length >= 0.75f)  resize();
    }
    void resize() {
        Node[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity * 2;
        Node[] newTable = new Node[newCapacity];
        for (int i = 0; i < oldCapacity; ++i) {
            for (Node e = oldTable[i]; e != null; e = e.next) {
                int h = e.key.hashCode();
                int j = h & (newTable.length - 1);
                newTable[j] = new Node(e.key, e.count, e.tfidfScore, newTable[j]);
            }
        }
        table = newTable;
    }

}
