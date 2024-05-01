package structures;

//KeyValuePairLinkedList taken from lab 4
public class KeyValueLinkedList<K extends Comparable<K>,V> {

    protected ListElement<KeyValuePair<K,V>> head;
    protected int size;
    
    public KeyValueLinkedList() {
        head = null;
        size = 0;
    }
    
    public boolean add(K key, V value) {
        return this.add(new KeyValuePair<K,V>(key,value));
    }

    public boolean add(KeyValuePair<K,V> kvp) {
        ListElement<KeyValuePair<K,V>> new_element = new ListElement<>(kvp);
        new_element.setNext(head);
        head = new_element;
        size++;
        return true;
    }
    
    public int size() {
        return size;
    }
    
    public ListElement<KeyValuePair<K,V>> getHead() {
        return head;
    }
    
    public KeyValuePair<K,V> get(K key) {
        ListElement<KeyValuePair<K,V>> temp = head;
        
        while(temp != null) {
            if(temp.getValue().getKey().equals(key)) {
                return temp.getValue();
            }
            
            temp = temp.getNext();
        }
        
        return null;
    }

    public boolean remove (K key){
        if (head.getValue().getKey().equals(key)){
            head = head.getNext();
            return true;
        }
        
        ListElement<KeyValuePair<K,V>> nodeBefore = head;
        ListElement<KeyValuePair<K,V>> nodeToRemove = head.getNext();
        
        while(nodeToRemove != null) {
            if(nodeToRemove.getValue().getKey().equals(key)) {
                nodeBefore.setNext(nodeToRemove.getNext());
                return true;
            }
            
            nodeBefore = nodeBefore.getNext();
            nodeToRemove = nodeToRemove.getNext();
        }
        return false;
    }
}
