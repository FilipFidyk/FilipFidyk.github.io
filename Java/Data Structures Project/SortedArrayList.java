package structures;

import java.time.LocalDateTime;

public class SortedArrayList<K extends Comparable<K>> extends CustomArrayList<K> {
    
    public SortedArrayList(){
        super();
        super.capacity = 1000;
        super.array = new Object[super.capacity];
    }

    //Sorted array add method taken from lab 4
    public boolean add(K element) {
        super.add(element); 
        
        if (super.size() == 1)
            return true;

        K temp;

        for (int i = (super.size() - 1); i > 0; i--){
            if (super.get(i).compareTo(super.get(i-1)) < 0){
                temp = super.get(i-1);
                super.set(i-1, super.get(i));
                super.set(i, temp);
            }
            else{
                break;
            }
        }
        return true;
    }

    //Binary search since the list is sorted
    public K find(K element) {
        
        // Set first and last pointers and find the pivot 
        double firstIndex = 0;
        double lastIndex = super.size() - 1;
        double pivot;

        if (super.isEmpty())
            return null;

        //Keep looking through the array until the pointers go past each other
        while (firstIndex <= lastIndex){
            pivot = Math.ceil( (firstIndex+lastIndex) / 2); //Round the index up, if after the divison it isn't a whole number

            //Check if the element is at the pivot, if not take the subarray either side of the pivot
            if (element.compareTo(super.get((int)pivot)) == 0)
                return super.get((int)pivot);
            else if (element.compareTo(super.get((int)pivot)) > 0)
                firstIndex = pivot + 1;
            else
                lastIndex = pivot - 1;
            
        }
        //if the element isn't in the array return null
        return null;
    }
}