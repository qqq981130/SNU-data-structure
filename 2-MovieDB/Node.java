public class Node<T> {
    private T item;
    private Node<T> next;

    public Node(T obj) {
        this.next = null;
        this.item = obj;
    }
    
    public Node(T obj, Node<T> next) {
    	this.item = obj;
    	this.next = next;
    }
    
    public final T getItem() {
    	return item;
    }
    
    public final void setItem(T item) {
    	this.item = item;
    }
    
    public final void setNext(Node<T> next) {
    	this.next = next;
    }
    
    public Node<T> getNext() {
    	return this.next;
    }
    
    public final void insertNext(T obj) {
        this.next = new Node<>(obj, this.next);
    }
    
    public final void removeNext() {
        if (this.next == null) {
            return;
        }
        else {
            this.next = this.next.next;
        }
    }
}