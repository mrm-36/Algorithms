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

class BinarySearchTree<T extends Comparable<? super T>> {
  private Node<T> root = null; // pointer to the node of the root
  private int sz = 0; // nb of nodes in the tree

  public BinarySearchTree() {}

  public int size() {return sz;}
  public Node<T> root() {return root;}

  public boolean isEmpty() {return sz == 0;}
  public int height() {return getHeight(root()) - 1;}

  //---------------- TREE EXTRA OPERATION ------------------//

  /**
   * return the node with the minimum value in the tree
   * i.e. the left-most node
   * @param curr node from where to start the search
   * @return the minimum node
   */
  public Node<T> getMinNode(Node<T> curr){
    while (curr != null && curr.getLeft() != null) curr = curr.getLeft();
    return curr;
  }

  /**
   * recursively computes the height of the tree
   * @param curr node from where to start the search
   * @return height of the tree
   */
  private int getHeight(Node<T> curr){
    if (curr == null) return 0;
    return 1 + Math.max(getHeight(curr.getLeft()), getHeight(curr.getRight()));
  }

  //---------------- TREE OPERATION ------------------//

  /**
   * Adds a new node to the BST with value x
   * @param x value of the new node
   */
  public void add(T x){
    Node<T> nw = new Node(x); ++sz;
    if (root == null) {root = nw; return;}
    Node<T> curr = root;
    while (true){
      if (x.compareTo(curr.getX()) < 0) {
        if (curr.getLeft() == null) {curr.setLeft(nw); return;}
        curr = curr.getLeft();
      }
      else {
        if (curr.getRight() == null) {curr.setRight(nw); return;}
        curr = curr.getRight();
      }
    }
  }

  /**
   * removes the first node that has the value x
   * @param x value of the node to remove
   * @return True if we found the node (removed it), and false otherwise
   */
  public boolean remove(T x){
    Node<T> curr = root();
    while (curr != null && x != curr.getX()){
      if (x.compareTo(curr.getX()) < 0) curr = curr.getLeft();
      else curr = curr.getRight();
    }
    if (curr == null) return false;
    if (curr.isLeaf()){
      Node<T> parent = curr.getParent();
      if (parent == null) root = null;
      else {
        if (parent.getLeft() == curr) parent.setLeft(null);
        else parent.setRight(null);
      }
      --sz; return true;
    }
    if (curr.getRight() == null){
      Node<T> parent = curr.getParent();
      if (parent == null) root = null;
      else {
        if (parent.getLeft() == curr) parent.setLeft(curr.getLeft());
        else parent.setRight(curr.getLeft());
      }
      --sz; return true;
    }
    if (curr.getLeft() == null){
      Node<T> parent = curr.getParent();
      if (parent == null) root = null;
      else {
        if (parent.getLeft() == curr) parent.setLeft(curr.getRight());
        else parent.setRight(curr.getRight());
      }
      --sz; return true;
    }
    Node<T> successor = getMinNode(curr.getRight());
    T k = successor.getX();
    remove(k);
    curr.setX(k);
    return true;
  }

  /**
   * searches for the first node with value x
   * @param x value of the node we are trying to find
   * @return TRUE if we found the node, and FALSE otherwise
   */
  public boolean find(T x){
    Node<T> curr = root();
    while (curr != null && curr.getX() != x){
      if (x.compareTo(curr.getX()) < 0) curr = curr.getLeft();
      else curr = curr.getRight();
    }
    return (curr != null && curr.getX() == x);
  }

  //---------------- TREE TRAVERSAL ------------------//

  /**
   * @return List of all the nodes in tree traversed In-Order
   */
  public List<Node<T>> inOrder() {
    List<Node<T>> v = new ArrayList<Node<T>>();
    if (!isEmpty()) inOrder(root(), v);
    return v;
  }

  /**
   * Fills the list with the nodes of the tree in an In-Order manner
   * @param p current node
   * @param v list that we are filling
   */
  private void inOrder(Node<T> p, List<Node<T>> v){
    if (p.getLeft() != null) inOrder(p.getLeft(), v);
    v.add(p);
    if (p.getRight() != null) inOrder(p.getRight(), v);
  }

  /**
   * @return List of all the nodes in tree traversed Pre-Order
   */
  public List<Node<T>> preOrder() {
    List<Node<T>> v = new ArrayList<Node<T>>();
    if (!isEmpty()) dfs(root(), v);
    return v;
  }

  /**
   * Fills the list with the nodes of the tree in an Pre-Order manner
   * @param p current node
   * @param v list that we are filling
   */
  private void dfs(Node<T> p, List<Node<T>> v){
    v.add(p);
    if (p.getLeft() != null) dfs(p.getLeft(), v);
    if (p.getRight() != null) dfs(p.getRight(), v);
  }

  /**
   * @return List of all the nodes in tree traversed Post-Order
   */
  public List<Node<T>> postOrder() {
    List<Node<T>> v = new ArrayList<Node<T>>();
    if (!isEmpty()) postOrder(root(), v);
    return v;
  }

  /**
   * Fills the list with the nodes of the tree in an Post-Order manner
   * @param p current node
   * @param v list that we are filling
   */
  private void postOrder(Node<T> p, List<Node<T>> v){
    if (p.getLeft() != null) postOrder(p.getLeft(), v);
    if (p.getRight() != null) postOrder(p.getRight(), v);
    v.add(p);
  }

  /**
   * Traverses the tree from top to bottom, from left to right
   * @return List of the all the nodes in the above specified order
   */
  public List<Node<T>> bfs() {
    List<Node<T>> v = new ArrayList<Node<T>>();
    if (isEmpty()) return v;
    Queue<Node<T>> q = new LinkedList<>();
    q.add(root);
    while (!q.isEmpty()){
      Node<T> temp = q.remove();
      if (temp.getLeft() != null) q.add(temp.getLeft());
      if (temp.getRight() != null) q.add(temp.getRight());
      v.add(temp);
    }
    return v;
  }

  /**
   * prints the nodes of the tree in a sideways manner
   */
  public void printSideways() {printSideways(root(), "");}
  /**
   * helper function to the above "printSideways" function
   * @param p current node
   * @param indent current indentation level
   */
  private void printSideways(Node<T> p, String indent){
    if (p == null) return;
    printSideways(p.getRight(), indent + "  ");
    System.out.println(indent + p.getX());
    printSideways(p.getLeft(), indent + "  ");
  }
}

public class Main {
  public static void main(String[] args){
    BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>();
    Random rand = new Random();

    int sz = 20;
    for (int i = 0; i < sz; ++i) tree.add(rand.nextInt(100));

    tree.printSideways();
  }
}
