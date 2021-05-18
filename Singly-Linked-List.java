import java.util.InputMismatchException;

class Node<T> {
  // ------------------- Data Members ------------------- //
  private T x;
  private Node<T> next;

  // ------------------- Constructors ------------------- //
  public Node (T _x, Node<T> _next){
    x = _x; next = _next;
  }
  public Node (T _x){
    this(_x, null);
  }

  // ------------------- Getters and Setters ------------------- //
  public T getX() {return x;}
  public Node<T> getNext() {return next;}

  public void setX(T _x) {x = _x;}
  public void setNext(Node<T> _next) {next = _next;}

  // ------------------- Helper Functions ------------------- //
  public boolean hasNext() {return next != null;}
}

class SinglyLinkedList<T> {
  // ------------------- Data Members ------------------- //
  private Node<T> head = null;
  private int sz = 0;

  public int size() {return sz;}

  // ------------------- Constructors ------------------- //
  public SinglyLinkedList () {}

  // ------------------- Main Functions ------------------- //
  public void addAtIndex(T x, int idx){
    if (idx > sz) throw new InputMismatchException("Invalid Index!");
    if (idx == 0) head = new Node<T>(x, head);
    else {
      Node<T> prev = null, curr = head;
      for ( ; idx > 0; --idx, prev = curr, curr = curr.getNext());
      prev.setNext(new Node<T>(x, curr));
    }
    ++sz;
  }
  public void addFirst(T x) {addAtIndex(x, 0);}
  public void addLast(T x) {addAtIndex(x, sz);}

  public void deleteAtIndex(int idx){
    if (idx >= sz) throw new InputMismatchException("Invalid Index!");
    if (idx == 0) head = head.getNext();
    else {
      Node<T> prev = null, curr = head;
      for ( ; idx > 0; --idx, prev = curr, curr = curr.getNext());
      prev.setNext(curr.getNext());
    }
    --sz;
  }
  public void deleteFirst() {deleteAtIndex(0);}
  public void deletLast() {deleteAtIndex(sz - 1);}

  public void deleteFirstValueOf(T x){
    Node<T> prev = null, curr = head;
    for ( ; curr != null && curr.getX() != x;prev = curr, curr = curr.getNext());
    if (curr == null) return;
    if (prev == null) head = head.getNext();
    else prev.setNext(curr.getNext());
    --sz;
  }
  public void deleteLastValueOf(T x){
    Node<T> curr = head; int idx = -1, i = 0;
    for ( ; curr != null; curr = curr.getNext(), ++i) if (curr.getX() == x) idx = i;
    deleteAtIndex(idx);
  }
  public void deleteAllValuesOf(T x){
    Node<T> prev = null, curr = head;
    for ( ; curr != null; curr = curr.getNext()){
      if (curr.getX() != x) {prev = curr; continue;}
      if (prev == null) head = head.getNext();
      else prev.setNext(curr.getNext());
      --sz;
    }
  }

  public int find(T x) {
    int i = 0;
    for (Node<T> curr = head; curr != null && curr.getX() != x; curr = curr.getNext(), ++i);
    return (i == sz ? -1 : i);
  }

  // ------------------- Extra Functions ------------------- //
  public void print(){
    for (Node<T> curr = head; curr != null; curr = curr.getNext()) System.out.print(curr.getX() + " ");
    System.out.println();
  }
}

public class Main {
  public static void main(String[] args){
    SinglyLinkedList<Integer> lst = new SinglyLinkedList<Integer>();
    lst.print();

    int[] x = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    for (int y : x) lst.addLast(y);
    lst.print();

    lst.addFirst(-2);
    lst.addAtIndex(-3, 7);
    lst.addLast(-8);

    lst.print();

    lst.deleteFirst();
    lst.deleteAtIndex(6);
    lst.deletLast();

    lst.print();

    lst.deleteFirstValueOf(1);
    lst.deleteAllValuesOf(10);
    lst.deleteLastValueOf(9);

    lst.print();

    System.out.println(lst.find(9));
    System.out.println(lst.size());
  }
}
