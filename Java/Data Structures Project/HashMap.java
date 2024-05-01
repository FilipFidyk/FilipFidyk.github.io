package structures;

import structures.KeyValueLinkedList;
import structures.KeyValuePair;

//HashMap taken from lab4 and added size which isn't the same as the number of KeyValuePairLinkedList in table
public class HashMap<K extends Comparable<K>, V> {
    protected KeyValueLinkedList<K,V>[] table;
    private int linkedListsNumber;

    public HashMap(int inLinkedListsNumber) {
        this.linkedListsNumber = inLinkedListsNumber;
        this.table = new KeyValueLinkedList[linkedListsNumber];
        this.initTable();
    }

    protected void initTable() {
        for(int i = 0; i < table.length; i++) {
            this.table[i] = new KeyValueLinkedList<>();
        }
    }

    protected int hash(K key) {
        int code = key.hashCode();
        return code;    
    }

    public boolean add(K key, V value) {
        //if the load factor reaches 1
        if ((double)this.size() / (double)this.getNoOfLinkedLists() >= 1){
            
            //Double the number of linked lists in the hashmap
            this.linkedListsNumber = this.linkedListsNumber * 2;
            
            //Take a copy of the current hashmap table
            KeyValueLinkedList<K,V>[] temp = table;
            
            //Create the larger table and initialise it using initTable()
            table = new KeyValueLinkedList[linkedListsNumber];
            this.initTable();

            //Cycle through the copy of the smaller table, now called temp
            int x;
            int y;
            for (int i = 0; i < temp.length; i++){
                //Take the head of the i'th linked list
                ListElement<KeyValuePair<K, V>> currentElement = temp[i].getHead();

                //If it isn't empty add it to the new table, recursive call but since we doubled the number of linked lists the load factor will be 0.5
                //and won't cause an infinite loop
                while (currentElement != null){
                    x = this.hash(currentElement.getValue().getKey());
                    y = x % table.length;
                    table[y].add(currentElement.getValue().getKey(), currentElement.getValue().getValue());
                    currentElement = currentElement.getNext();
                }
            }
        }

        int hash_code = hash(key);
        int location = hash_code % table.length;
    
        return table[location].add(key,value);
    }

    public boolean remove(K key) {
        
        int hash_code = hash(key);
        int location = hash_code % table.length;
    
        return table[location].remove(key);
    }

    public V get(K key) {
        
        int hash_code = hash(key);
        int location = hash_code % table.length;
        
        KeyValuePair<K,V> ptr = table[location].get(key);
        
        if (ptr == null) {
            return null;
        }

        return (V) ptr.getValue();
    }

    public KeyValueLinkedList getLinkedList(int row){
        return table[row];
    }

    public int getNoOfLinkedLists(){
        return table.length;
    }

    //Takes the size of every linked list and sums it
    public int size(){
        int totalSize = 0;
        for (int i = 0; i < table.length; i++){
            totalSize +=table[i].size();
        }
        return totalSize;
    }
}
