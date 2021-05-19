import java.util.*;

class DefaultComparator<E> implements Comparator<E> {
  @SuppressWarnings({"unchecked"})
   public int compare(E a, E b) throws ClassCastException {
    return ((Comparable<E>) a).compareTo(b);
  }
}

class Entry<K, V> {
  // ------------------- Data Members ------------------- //
  private K k;
  private V v;

  // ------------------- Constructors ------------------- //
  public Entry(K _k, V _v) {k = _k; v = _v;}

  // ------------------- Getters and Setters ------------------- //
  public K getKey() {return k;}
  public V getValue() {return v;}

  public void setKey(K _k) {k = _k;}
  public void setValue(V _v) {v = _v;}
}

class HeapPriorityQueue<K, V> {
  private ArrayList<Entry<K, V>> heap = new ArrayList<>();
  private Comparator<K> comp;

  // ---------------- Constructors ---------------- //

  /**
   * Constructs a new heap with the values in the lists
   * where each entry is (keys[i], values[i])
   * @param keys list of all the keys
   * @param values list of all the values
   */
  public HeapPriorityQueue(K[] keys, V[] values){
    comp = new DefaultComparator<K>();
    for (int i = 0; i < Math.min(keys.length, values.length); ++i)
      heap.add(new Entry<>(keys[i], values[i]));
    heapify();
  }
  public HeapPriorityQueue() {comp = new DefaultComparator<K>();}
  public HeapPriorityQueue(Comparator<K> _comp) {comp = _comp;}

  // ---------------- Basic Heap Operations ---------------- //
  public int size() {return heap.size();}
  public boolean isEmpty() {return size() == 0;}

  // ---------------- KV Operations ---------------- //
  private int parent(int i) {return (i - 1) / 2;}
  private int left(int i) {return 2 * i + 1;}
  private int right(int i) {return 2 * i + 2;}

  private boolean hasLeft(int i) {return left(i) < heap.size();}
  private boolean hasRight(int i) {return right(i) < heap.size();}

  public int compare(Entry<K, V> a, Entry<K, V> b){
    return comp.compare(a.getKey(), b.getKey());
  }

  // ---------------- Heap Operations ----------------//

  /**
   * Swap node at index i and node at index j in the heap
   * @param i index of the first node
   * @param j index of the second node
   */
  private void swap(int i, int j){
    Entry<K, V> temp = heap.get(i);
    heap.set(i, heap.get(j));
    heap.set(j, temp);
  }

  /**
   * WHILE the value of the current node is less (based on the comparator) than it's parent node, we swap them
   * At the end, the node being upheaped is greater than its parent, and its parent is greater than its own parent, all the way up to the root.
   * @param i index of the node being upHeaped
   */
  private void upHeap(int i){
    while (i > 0){
      int p = parent(i);
      if (compare(heap.get(i), heap.get(p)) >= 0) break;
      swap(i, p); i = p;
    }
  }

  /**
   * WHILE the value of the current node is greater (based on the comparator) than at least one of its children,
   * swap it with the child that has the smallest value (ensuring that the parent is the minimum btw all 3 nodes)
   * @param i index of the node being downHeaped
   */
  private void downHeap(int i){
    while (hasLeft(i)){
      int li = left(i);
      int idx = li;
      if (hasRight(i)) {
        int ri = right(i);
        if (compare(heap.get(li), heap.get(ri)) > 0) idx = ri;
      }
      if (compare(heap.get(idx), heap.get(i)) >= 0) break;
      swap(i, idx); i = idx;
    }
  }

  /**
   * Called when initializing a heap using a list of <key, value>
   * downHeaps all nodes
   */
  private void heapify(){
    for (int i = parent(size() - 1); i >= 0; --i) downHeap(i);
  }

  /**
   * Adds a new node to the heap and calls upHeap to maintain the correct order
   * @param key key of the new entry
   * @param val value of the new entry
   * @return the entry of the new created node
   */
  public Entry<K, V> insert(K key, V val){
    Entry<K, V> x = new Entry<>(key, val);
    heap.add(x); upHeap(size() - 1);
    return x;
  }

  // ---------------- Heap Sort Operations ----------------//

  /**
   * @return the root of the heap (min/max value)
   */
  public Entry<K, V> getFirst() {
    return (isEmpty() ? null : heap.get(0));
  }

  /**
   * removes the root of the tree and maintains the correct order
   * @return the root of the tree
   */
  public Entry<K, V> removeFirst(){
    if (isEmpty()) return null;
    Entry<K, V> mn = heap.get(0);
    swap(0, size() - 1); heap.remove(size() - 1); downHeap(0);
    return mn;
  }

  /**
   * Heap Sort
   * turns the heap into a sorted array
   * @return a sorted array of all the nodes in the heap
   */
  public List<V> toArray() {
    List<V> arr = new ArrayList<>();
    while (!isEmpty()) arr.add(removeFirst().getValue());
    return arr;
  }
}

public class Main {
  public static void main(String[] args){
    Integer[] keys = {1, 8, 4, 2, 6, 4, 6, 7, 5, 2, 1};
    Integer[] vals = {7, 5, 2, 4, 6, 4, 6, 2, 4, 5, 6};
    HeapPriorityQueue<Integer, Integer> heap = new HeapPriorityQueue<>(keys, vals);
    
    while (!heap.isEmpty()) {
      Entry<Integer, Integer> mn = heap.removeFirst();
      System.out.println(mn.getKey() + " " + mn.getValue());
    }
  }
}
