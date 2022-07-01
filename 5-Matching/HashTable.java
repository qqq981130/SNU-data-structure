import java.util.function.Function;

public class HashTable<E extends Comparable<E>, K> {
    //E: key, K: page. collision resolution by AVL Tree
    private Function<E, Integer> hasher;
    public HashNode<E, K>[] innerTable;

    public HashTable(Function<E,Integer> hashFunc) {
        hasher = hashFunc;
        innerTable = (HashNode<E, K>[]) new HashNode[100];
    }

    public void insert(E key, K page) {
        int slot = hasher.apply(key);
        if (innerTable[slot]==null) {
            innerTable[slot] = new HashNode<>();
            innerTable[slot].insert(key, page);
        }
        else {
            innerTable[slot].insert(key, page);
        }
    }

    public HashNode<E,K> keyToHashNode(E key) {
        int slot = hasher.apply(key);
        return innerTable[slot];
    }

    public HashNode<E,K> indexToHashNode(int index) {
        return innerTable[index];
    }
}

class HashNode<E extends Comparable<E>, K> { //이 클래스의 insert method는 AVL tree에 더하는 것을 의미
    private final AVLTree<E, K> tree;

    public HashNode() {
        tree = new AVLTree<>();
    }

    public void insert(E value, K page) {
        tree.insert(value, page);
    }

    public AVLTree<E, K> getTree() {
        return tree;
    }
}

