package structures;

//listElement taken from lab4
public class ListElement<E> {
    private final E value;
    private ListElement<E> next;
    
    public ListElement(E value) {
        this.value = value;
    }
    
    public E getValue() {
        return this.value;
    }
    
    public ListElement<E> getNext() {
        return this.next;
    }
    
    public void setNext(ListElement<E> e) {
        this.next = e;
    }

}