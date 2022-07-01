import java.io.*;

public class AVLTree <E extends Comparable<E>, K>{
    static final AVLNode NIL = new AVLNode<>(null, null, null, 0);
    public AVLNode<E, K> root;

    public AVLTree() {
        root = NIL;
    }


    //search
    public AVLNode<E, K> search(E item) {
        return searchItem(root, item);
    }

    private AVLNode<E, K> searchItem(AVLNode<E, K> t, E key) {
        if (t == NIL) return NIL; //not found
        else if (key.compareTo(t.getItem())==0) return t;
        else if (key.compareTo(t.getItem())<0) return searchItem(t.left, key);
        else return searchItem(t.right, key);
    }


    //insert
    public void insert(E item, K page) {
        root = insertItem(root, item, page);
    }

    public AVLNode<E, K> insertItem(AVLNode<E, K> t, E item, K page) {
        if (t == NIL) {
            t = new AVLNode<>(item);
            t.addValue(page);
        }
        else if (item.compareTo(t.getItem()) == 0) { //이미 존재할 경우 page만 넘겨줌
            t.addValue(page);
        }
        else if (item.compareTo(t.getItem()) < 0) {
            t.left = insertItem(t.left, item, page);
            t.height = 1 + Math.max(t.right.height, t.left.height);
            int type = needBalance(t);
            if (type != NO_NEED) {
                t = balanceAVL(t, type);
            }
        }
        else if (item.compareTo(t.getItem()) > 0) {
            t.right = insertItem(t.right, item, page);
            t.height = 1 + Math.max(t.right.height, t.left.height);
            int type = needBalance(t);
            if (type != NO_NEED) {
                t = balanceAVL(t, type);
            }
        }
        return t;
    }


    //balance
    private AVLNode<E, K> balanceAVL(AVLNode<E, K> t, int type) { //t를 root로 갖는 서브트리를 밸런싱하고 t를 return
        AVLNode<E, K> returnNode = NIL;
        switch (type) {
            case LL:
                returnNode = rightRotate(t);
                break;
            case LR:
                t.left = leftRotate(t.left);
                returnNode = rightRotate(t);
                break;
            case RR:
                returnNode = leftRotate(t);
                break;
            case RL:
                t.right = rightRotate(t.right);
                returnNode = leftRotate(t);
                break;
            default:
                System.out.println("ILLEGAL TYPE");
                break;
        }
        return returnNode;
    }

    private AVLNode<E, K> leftRotate(AVLNode<E, K> t) {
        AVLNode<E, K> RChild = t.right;
        AVLNode<E, K> RLChild = t.right.left;
        t.right = RLChild;
        RChild.left = t;
        t.height = 1 + Math.max(t.left.height, t.right.height);
        RChild.height = 1 + Math.max(RChild.left.height, RChild.right.height);
        return RChild;
    }

    private AVLNode<E, K> rightRotate(AVLNode<E, K> t) {
        AVLNode<E, K> LChild = t.left;
        AVLNode<E, K> LRChild = t.left.right;
        t.left = LRChild;
        LChild.right = t;
        t.height = 1 + Math.max(t.left.height, t.right.height);
        LChild.height = 1 + Math.max(LChild.left.height,LChild.right.height);
        return LChild;

    }

    private final int LL=1, LR=2, RR=3, RL=4, NO_NEED=0, ILLEGAL = -1;

    private int needBalance(AVLNode<E,K> t) {
        int returnType = ILLEGAL;
        if (t.left.height+2 <= t.right.height) { //R
            if (t.right.left.height <= t.right.right.height) {
                returnType = RR;
            }
            else {
                returnType = RL;
            }
        }
        else if (t.left.height >= t.right.height + 2) { //L
            if (t.left.left.height >= t.left.right.height) {
                returnType = LL;
            } else {
                returnType = LR;
            }
        }
        else {
            returnType = NO_NEED;
        }
        return returnType;
    }

    public void preOrder(AVLNode<E,K> root, StringBuilder sb) {
        if (root!=NIL) {
            sb.append(root.getItem()).append(" ");
            preOrder(root.left, sb);
            preOrder(root.right, sb);
        }
    }
}
