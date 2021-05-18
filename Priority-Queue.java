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

class PriorityQueue<K, V> {
  // ------------------- Data Members ------------------- //
  private LinkedList<Entry<K, V>> lst = new LinkedList<>();
  private Comparator<K> comp;

  // ------------------- Constructors ------------------- //
  public PriorityQueue() {comp = new DefaultComparator<K>();}
  public PriorityQueue(Comparator<K> _comp) {comp = _comp;}

  // ------------------- Basic Functions ------------------- //
  public int size() {return lst.size();}
  public boolean isEmpty() {return size() == 0;}

  // ------------------- Main Functions ------------------- //
  public Entry<K, V> insert(K key, V value){
    Entry<K, V> nw = new Entry<>(key, value);
    int i = 0;
    for (Entry<K, V> x : lst) {
      if (compare(nw, x) < 0) break;
      ++i;
    }
    lst.add(i, nw);
    return nw;
  }

  public Entry<K, V> getMin() {
    if (lst.isEmpty()) return null;
    return lst.getFirst();
  }

  public Entry<K, V> removeMin() {
    if (lst.isEmpty()) return null;
    return lst.removeFirst();
  }

  // ------------------- Helper Functions ------------------- //
  private int compare(Entry<K, V> a, Entry<K, V> b){
    return comp.compare(a.getKey(), b.getKey());
  }
}

public class Main {
  public static void main(String[] args){
    PriorityQueue<Integer, Integer> pq = new PriorityQueue<>();
    for (int i = 18, j = 1; i > 0; --i, j += 3) pq.insert(i, j);
    while (!pq.isEmpty()) {
      Entry<Integer, Integer> mn = pq.removeMin();
      System.out.println(mn.getKey() + " " + mn.getValue());
    }
  }
}
