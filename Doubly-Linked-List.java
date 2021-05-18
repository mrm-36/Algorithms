import java.util.InputMismatchException;

class Node<T> {
  // ------------------- Data Members ------------------- //
  private T x;
  private Node<T> next;
  private Node<T> prev;

  // ------------------- Constructors ------------------- //
  public Node (T _x, Node<T> _prev, Node<T> _next){
    x = _x; prev = _prev; next = _next; 
  }
  public Node (T _x){
    this(_x, null, null);
  }

  // ------------------- Getters and Setters ------------------- //
  public T getX() {return x;}
  public Node<T> getNext() {return next;}
  public Node<T> getPrev() {return prev;}

  public void setX(T _x) {x = _x;}
  public void setNext(Node<T> _next) {next = _next;}
  public void setPrev(Node<T> _prev) {prev = _prev;}

  // ------------------- Helper Functions ------------------- //
  public boolean hasNext() {return next != null;}
  public boolean hasPrev() {return prev != null;}
}

class DoublyLinkedList<T> {
  // ------------------- Data Members ------------------- //
  private Node<T> head = null;
  private Node<T> tail = null;
  private int sz = 0;

  public int size() {return sz;}

  // ------------------- Constructors ------------------- //
  public DoublyLinkedList () {}

  // ------------------- Main Functions ------------------- //
  public void addAtIndex(T x, int idx){
    if (idx > sz) throw new InputMismatchException("Invalid Index!");
    Node<T> prev = null, curr = head;
    for (; idx > 0; --idx, prev = curr, curr = curr.getNext());
    Node<T> nw = new Node<T>(x, prev, curr);
    addNode(prev, nw, curr);
    ++sz;
  }
  public void addFirst(T x) {addAtIndex(x, 0);}
  public void addLast(T x) {addAtIndex(x, sz);}

  /*public void deleteAtIndex(int idx){
    if (idx >= sz) throw new InputMismatchException("Invalid Index!");
    if (sz == 1) head = tail = null;
    else if (idx == 0) head = head.getNext();
    else {
      Node<T> prev = null, curr = head;
      for ( ; idx > 0; --idx, prev = curr, curr = curr.getNext());
      prev.setNext(curr.getNext());
      if (curr.hasNext()) curr.setPrev(prev);
      else tail = prev;
    }
    --sz;
  }*/
  public void deleteAtIndex(int idx){
    if (idx >= sz) throw new InputMismatchException("Invalid Index!");
    Node<T> curr = head;
    for ( ; idx > 0; --idx, curr = curr.getNext());
    deleteNode(curr.getPrev(), curr, curr.getNext());
    --sz;
  }
  public void deleteFirst() {deleteAtIndex(0);}
  public void deletLast() {deleteAtIndex(sz - 1);}

  public void deleteFirstValueOf(T x){
    Node<T> curr = head;
    for ( ; curr != null && curr.getX() != x; curr = curr.getNext());
    if (curr == null) return;
    deleteNode(curr.getPrev(), curr, curr.getNext());
    --sz;
  }
  public void deleteLastValueOf(T x){
    Node<T> curr = tail;
    for ( ; curr != null && curr.getX() != x; curr = curr.getPrev());
    if (curr == null) return;
    deleteNode(curr.getPrev(), curr, curr.getNext());
    --sz;
  }
  public void deleteAllValuesOf(T x){
    Node<T> curr = head;
    while (curr != null){
      Node<T> temp = curr.getNext();
      if (curr.getX() == x) {deleteNode(curr.getPrev(), curr, curr.getNext()); --sz;}
      curr = temp;
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

  // ------------------- Helper Functions ------------------- //
  private void addNode(Node<T> prev, Node<T> curr, Node<T> next){
    if (prev == null && next == null) head = tail = curr;
    else if (prev == null) {next.setPrev(curr); head = curr;}
    else if (next == null) {prev.setNext(curr); tail = curr;}
    else {prev.setNext(curr); next.setPrev(curr);}
  }
  private void deleteNode(Node<T> prev, Node<T> curr, Node<T> next){
    if (prev == null && next == null) head = tail = null;
    else if (prev == null) {curr.setNext(null); next.setPrev(null); head = next;}
    else if (next == null) {prev.setNext(null); curr.setPrev(null); tail = prev;}
    else {curr.setNext(null); curr.setPrev(null); prev.setNext(next); next.setPrev(prev);}
  }
}

public class Main {
  public static void main(String[] args){
    DoublyLinkedList<Integer> lst = new DoublyLinkedList<Integer>();
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
