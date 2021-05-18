import java.util.*;

class Node<T> {
  // ------------------- Data Members ------------------- //
  private T x;
  private Node<T> parent;
  private Node<T> left;
  private Node<T> right;
  
  // ------------------- Constructors ------------------- //
  public Node (T _x, Node<T> _parent, Node<T> _left, Node<T> _right){
    x = _x; parent = _parent; left = _left; right = _right;
  }
  public Node (T _x){
    this(_x, null, null, null);
  }
  public Node (T _x, Node<T> _parent){
    this(_x, _parent, null, null);
  }

  // ------------------- Getters and Setters ------------------- //
  public T getX() {return x;}
  public Node<T> getParent() {return parent;}
  public Node<T> getLeft() {return left;}
  public Node<T> getRight() {return right;}

  public void setX(T _x) {x = _x;}
  public void setParent(Node<T> _parent) {parent = _parent;}
  public void setLeft(Node<T> _left) {left = _left;}
  public void setRight(Node<T> _right) {right = _right;}

  // ------------------- Main Functions ------------------- //
  public int nbChildren() {
    return (left == null ? 0 : 1) + (right == null ? 0 : 1);
  }

  public boolean isRoot() {return parent == null;}
  public boolean isLeaf() {return nbChildren() == 0;}
}


class LinkedBinaryTree<T> {
  private Node<T> root = null; //pointer to the root of the tree
  private int sz = 0; // nb of nodes in the tree

  // ------------------ Constructors ------------------ //
  public LinkedBinaryTree() {}

  /**
   * Creates a new tree using the values in the list
   * @param lst list of values of the new nodes
   */
  public LinkedBinaryTree(T[] lst){
    if (lst.length == 0) return;
    Queue<Node<T>> q = new LinkedList<>();
    addRoot(lst[0]); q.add(root());
    for (int i = 1; i < lst.length; ++i){
      Node<T> p = q.remove();
      q.add(addLeft(p, lst[i++]));
      if (i < lst.length) q.add(addRight(p, lst[i]));
    }
  }

  // ------------------ Basic Function ------------------ //
  public int size() {return sz;}
  public boolean isEmpty() {return sz == 0;}
  public Node<T> root() {return root;}
  public int height() {return getHeight(root());}

  public void clear() {root = null; sz = 0;}

  // ------------------ Getters and Setters ------------------ //

  /**
   * Initializes the root of the tree
   * @param x the element stored in the new node
   * @return the new root
   * @throws IllegalStateException if the tree already has a root
   */
  public Node<T> addRoot(T x) throws IllegalStateException {
    if (!isEmpty()) throw new IllegalStateException("Tree is not empty.");
    root = new Node<T>(x); sz = 1;
    return root;
  }

  /**
   * Adds a new node to the left of the parent p
   * @param p parent of the new node
   * @param x element stored in the new node
   * @return the new node that we created
   * @throws IllegalArgumentException if the parent is null, or already has a left child
   */
  public Node<T> addLeft(Node<T> p, T x) throws IllegalArgumentException {
    if (p == null) throw new IllegalArgumentException("Node is null.");
    if (p.getLeft() != null) throw new IllegalArgumentException("Node already has a left child.");
    Node<T> child = new Node<T>(x, p);
    p.setLeft(child); ++sz;
    return child;
  }

  /**
   * Adds a new node to the right of the parent p
   * @param p parent of the new node
   * @param x element stored in the new node
   * @return the new node that we created
   * @throws IllegalArgumentException if the parent is null, or already has a right child
   */
  public Node<T> addRight(Node<T> p, T x) throws IllegalArgumentException {
    if (p == null) throw new IllegalArgumentException("Node is null.");
    if (p.getRight() != null) throw new IllegalArgumentException("Node already has a right child.");
    Node<T> child = new Node(x, p);
    p.setRight(child); ++sz;
    return child;
  }

  /**
   * Removes the node n and replaces it with one of it's children
   * @param n the node that needs to be removed
   * @throws IllegalArgumentException if the node is null or if it has 2 children
   */
  public void remove(Node<T> n) throws IllegalArgumentException {
    if (n == null) throw new IllegalArgumentException("Node is null.");
    if (n.nbChildren() == 2) throw new IllegalArgumentException("Node has 2 children.");
    Node<T> c = (n.getLeft() != null ? n.getLeft() : n.getRight());
    if (c != null) c.setParent(n.getParent());
    if (n == root) root = c;
    else {
      Node<T> p = n.getParent();
      if (n == p.getLeft()) p.setLeft(c);
      else p.setRight(c);
    }
    n.setLeft(null); n.setRight(null); n.setParent(null);
    --sz;
  }

  // ------------------ Tree Traversal ------------------ //

  /**
   * prints an indented version of all the nodes in the tree
   */
  public void printIndented() {
    dfs(root(), "", -1);
    System.out.println("----------");
  }

  // ------------------ Helper Functions ------------------ //

  /**
   * returns the height of the sub-tree starting at curr
   * @param curr the starting node
   * @return the height from curr
   */
  private int getHeight(Node<T> curr){
    if (curr == null) return 0;
    return 1 + Math.max(getHeight(curr.getLeft()), getHeight(curr.getRight()));
  }

  /**
   * prints all the nodes in the tree in an indented manner
   * "lt_" means left child
   * "rt_" means right child
   * @param u current node
   * @param indent current indentation level
   * @param l 0 if u is a left child, and 1 if u is a right child
   */
  private void dfs(Node<T> u, String indent, int l) {
    System.out.println(indent + (l == 0 ? "lt_" : (l == 1 ? "rt_" : "")) + u.getX());
    if (u.getLeft() != null) dfs(u.getLeft(), indent + "| ", 0);
    if (u.getRight() != null) dfs(u.getRight(), indent + "| ", 1);
  }
}

public class Main {
  public static void main(String[] args){
    Integer[] arr = {3, 6, 12, 13, 7, 14, 15};
    LinkedBinaryTree<Integer> tree = new LinkedBinaryTree<Integer>(arr);
    tree.printIndented();
    System.out.println(tree.size());
  }
}
