package structures;

//Key value pair class from lab 4
public class KeyValuePair<K extends Comparable<K>,V> implements Comparable<KeyValuePair<K,V>> {

    protected K key;
    protected V value;
    
    public KeyValuePair(K k, V v) {
        key = k;
        value = v;
    }
    
    public K getKey() {
        return key;
    }
    
    public V getValue() {
        return value;
    }

    //I added a setValue method so the value can be adjusted as a couple task needed this in Ratings 
    public void setValue(V inValue){
        this.value = inValue;
    }

    public int compareTo(KeyValuePair<K,V> o) {
        return o.getKey().compareTo(this.getKey());
    }
}