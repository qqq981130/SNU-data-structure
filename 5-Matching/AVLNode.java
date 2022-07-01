import java.util.*;

public class AVLNode<E extends Comparable<E>, K>{ //E: key, K: page
    private E item;
    private ArrayList<K> list = new ArrayList<>();
    public AVLNode<E, K> right;
    public AVLNode<E, K> left;
    public int height;

    public AVLNode(E newItem) {
        item = newItem;
        left = right = AVLTree.NIL;
        height = 1;
    }

    public AVLNode(E newItem, AVLNode<E, K> leftChild, AVLNode<E, K> rightChild, int newHeight) {
        item = newItem;
        left = leftChild;
        right = rightChild;
        height = newHeight;
    }

    public E getItem() {
        return item;
    }
    public ArrayList<K> getList() { return list; }

    public void addValue(K p) { //만약 AVLTree에서 삽입을 하다가 이미 어떤 Node가 해당 key를 갖고있다면 그 page를 더해줌.
        list.add(p);
    }
}

