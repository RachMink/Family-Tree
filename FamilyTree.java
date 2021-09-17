/**
 * @name:Rachel Minkowitz
 * @since: 07/01/2021
 * @version: 2.0
 * @Description: CISC 3130 Homework 4
 * This program reads in a family tree and answers various questions regarding the family
 */
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;

public class FamilyTree {
    ArrayList<Node> input;
    private Node root;

    public static class Node {
        private String name;
        private int numChildren;
        private Node leftChild;
        private Node rightChild;

        public Node(String name, int numChildren) {
            this.name = name;
            this.numChildren = numChildren;
            leftChild = null;
            rightChild = null;
        }

        public String getName() {
            return name;
        }

        public int getNumChildren() {
            return numChildren;
        }

        public Node getLeftChild() {
            return leftChild;
        }

        public Node getRightChild() {
            return rightChild;
        }

        public void setLeftChild(Node leftChild) {
            this.leftChild = leftChild;
        }

        public void setRightChild(Node rightChild) {
            this.rightChild = rightChild;
        }
    }

    public boolean isEmpty() {
        return root == null;
    }

    public Node getRoot() {
        return root;
    }

    // creates the family tree using the ArrayList of Nodes read in from the scanner
    // in the main
    // this constructor sets the ArrayList of nodes passed in -as the arrayList data
    // feild needed for this class
    // this constructor then calls the "convertToTree" method which actually builds
    // the tree

    public FamilyTree(ArrayList<Node> input) {
        this.input = input;
        convertToTree();
    }

    // this method actually constructs the binary tree of family member nodes
    // I set up two queues - one that we will hold Nodes with more than 0 children,
    // and one which just holds all the data from the
    // arrayList, this queue allows us to access each node by just popping off the
    // first node from the front queue, rather than
    // removing from the arrayList accourding to index or item

    public void convertToTree() {
        Queue<Node> workQueue = new LinkedList<>();
        Queue<Node> inputQueue = new LinkedList<>();

        if (input.isEmpty()) { // if the input ArrayList is empty - the tree is null
            this.root = null;
            return;
        }

        if (isEmpty()) { // uses the isEmpty() method of the FamilyTree class - to check if there is a
                         // root
                         // if the root is null- we initialize the root to be the first Node in the
                         // ArrayList
            root = input.get(0);
        }

        inputQueue.addAll(input); // add all the nodes from the arrayList to the inputQueue to make it easier to
                                  // access each Node

        if (input.get(0).numChildren != 0) { // if a node has more than 0 children we add the node onto the workQueue -
                                             // to keep track of who needs to have children added to them
            workQueue.add(input.get(0));
            inputQueue.remove(); // once add a node to the workQueue we can remove it from the inputQueue
        }
        while (!workQueue.isEmpty()) { // as long as there's another node that has more than 0 children on the workQueue 

            Node cur = workQueue.remove(); // take the front node off the workQueue
            ArrayList<Node> curChildren = new ArrayList<>(); // create an ArrayList that'll hold this Node's childrens'
                                                             // names

            for (int i = 0; i < cur.numChildren; i++) {// go through the inputQueue to get the Node's
                                                       // children
                Node curChild = inputQueue.remove();
                if (curChild.numChildren > 0) {
                    workQueue.add(curChild); // if the child we just removed has more than 0 children - we add this
                                             // child to the work queue as well
                }
                curChildren.add(curChild); // either way, add this child to the ArrayList of children for this Node
            }

            addAllChildren(cur, curChildren); // add all curChildren into tree as children of cur
        }
    }

    private void addAllChildren(Node cur, ArrayList<Node> curChildren) {
        if (curChildren.isEmpty()) {// uses isEmpty of the ArrayList<> collection to check if the ArrayList is empty
            cur.leftChild = null; // this node has no children
            return;
        }

        Node curChild = curChildren.get(0); // the first child is the first one in the ArrayList
        curChildren.remove(curChild); // remove this child from the arrayList of allChildren
        cur.leftChild = curChild; // set this child as the eldest child of the parent - as the left child

        for (Node n : curChildren) { // for the rest of the nodes in the ArrayList we set them as the right child of
                                     // the previous node - starting from the eldest
            curChild.rightChild = n;
            curChild = n;
        }
    }

    // uses the preorder traversal to print the binary Family tree

    public void printTreePreTrav(Node tree, PrintStream ps) throws Exception {

        if (tree != null) {
            ps.println(tree.getName() + " " + tree.getNumChildren());

            printTreePreTrav(tree.getLeftChild(), ps);
            printTreePreTrav(tree.getRightChild(), ps);
    
        }
    }

    // this method recursively searches for a Node using the current node we are
    // pointing at
    // once we find the Node with the proper name we are searching for we return
    // this Node

    private Node findPersonHelper(Node cur, String name) {
        if (cur == null) {
            return null;
        }
        if (cur.name.equals(name)) {
            return cur;
        }
        Node left = findPersonHelper(cur.leftChild, name);
        if (left == null) {
            return findPersonHelper(cur.rightChild, name);
        }
        return left;
    }

    // this method calls the recursive method findPersonHelper
    // in order to find someone within the binary tree using their name starting
    // from the root

    public Node findPerson(String name) {
        return findPersonHelper(root, name);
    }

    // this method creates an arrayList of children for a person which we will use in aswering the
    // questions about the family

    public ArrayList<String> listChildren(String name) {
        ArrayList<String> list = new ArrayList<>();
        Node node = findPerson(name);// we find the person within the tree

        if (node == null) { // this person doesnt exist in the family tree
            return list;
        }

        node = node.leftChild; // we go the left child which we know is the eldest child

        if (node == null) { // this person does not have any children
            return list;
        }
        while (node != null) {
            list.add(node.name); // add the child to the list - starting from the eldest
            node = node.rightChild; // go right from there - and add the siblings to the list

        }
        return list;
    }

    public String findFatherOfPerson(String person) {
        for (Node n : input) {
            ArrayList<String> children = listChildren(n.name); // lists the children of a specified person
            if (children.contains(person)) { // if the child's name is within the arrayList of children - we can
                                             // return the name of the parent
                return "\nFather of " + person + " is : " + n.name;
            }
        }
        return null;
    }

    public String findOldestChild(String person) {
        ArrayList<String> children = listChildren(person);//
        if (children.isEmpty()) { // the person doesnt have any kids
            return null;
        }

        return "\nEldest Child of " + person + " is : " + children.get(0);// the first one on the arrayList will be the
                                                                          // oldest
    }

    public String findYoungestChild(String person) {
        ArrayList<String> children = listChildren(person);
        if (children.isEmpty()) {
            return null;
        }

        return "\nYoungest Child of " + person + " is : " + children.get(children.size() - 1); // the last one on the
                                                                                               // ArrayList will be the
                                                                                               // youngest
    }

    public void findAllChildren(String person, PrintStream ps) {
        ArrayList<String> children = listChildren(person);

        ps.print("\nAll Children of " + person + " : ");
        for (String n : children) {
            ps.print(n + " ");// prints all the children of a person by just printing the ArrayList
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("input.txt"));
        PrintStream ps = new PrintStream(new File("output.txt"));

        ArrayList<Node> inputArrayList = new ArrayList<>();// how to access the Node idea from the main method
        ArrayList<Node> inputList = new ArrayList<>();
        String name;
        int numChildren;
        while (sc.hasNext()) {
            name = sc.next();
            numChildren = sc.nextInt();

            if (name.equals("x")) { // this is the delimiter that it's a new piece of data

                while (sc.hasNext()) {
                    name = sc.next();
                    numChildren = sc.nextInt();

                    inputList.add(new Node(name, numChildren));
                }
            }
            inputArrayList.add(new Node(name, numChildren));
        }

        FamilyTree tree = new FamilyTree(inputArrayList);
        ps.println("Family Tree in preorder Traversal: ");
        tree.printTreePreTrav(tree.getRoot(), ps);
        ps.println(tree.findFatherOfPerson("Deville"));
        ps.println(tree.findOldestChild("Jones"));
        ps.println(tree.findYoungestChild("Brian"));
        tree.findAllChildren("Jones", ps);

        FamilyTree newTree = new FamilyTree(inputList);
        ps.println("\n\nNEW DATA\n");
        ps.println("Family Tree in preorder Traversal: ");
        tree.printTreePreTrav(newTree.getRoot(), ps);
        ps.println(newTree.findFatherOfPerson("Jerry"));
        ps.println(newTree.findOldestChild("Jerry"));
        ps.println(newTree.findYoungestChild("Bobby"));
        newTree.findAllChildren("Stanley", ps);
    }

}

