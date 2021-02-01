package a4;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/** An instance of FluTree represents the spread of a flu among a Network of people. <br>
 * In this model, each person can "catch" the flu from only a single person. <br>
 * The root of the FluTree is the person who first got the flu. <br>
 * From there, each person in the FluTree is the child of the person who gave <br>
 * them the flu. For example, for the tree:
 *
 * <pre>
 *       A
 *      / \
 *     B   C
 *        / \
 *       D   E
 * </pre>
 *
 * Person A originally got the flu, B and C caught the flu from A, and<br>
 * D and E caught the flu from C.
 *
 * Important note: The name of each person in the flu tree is unique. Check whether two Person's are
 * the same by calling method equals. */
public class FluTree {

    /** The String to be used as a separator in toString() */
    public static final String SEPARATOR= " - ";

    /** The String that marks the start of children in toString() */
    public static final String START_CHILDREN_DELIMITER= "[";

    /** The String that divides children in toString() */
    public static final String DELIMITER= ", ";

    /** The String that marks the end of children in toString() */
    public static final String END_CHILDREN_DELIMITER= "]";

    /** The String that is the space increment in toStringVerbose() */
    public static final String VERBOSE_SPACE_INCREMENT= "\t";

    /** The person at the root of this FluTree. <br>
     * i.e. the person in this node of the FluTree. <br>
     * This field is never null.<br>
     * This is the flu ancestor of everyone in this FluTree: <br>
     * the person who got sick first and indirectly caused everyone in it to get sick. <br>
     * All Person's in a FluTree have different names. There are no duplicates */
    private Person root;

    /** The children of this FluTree node. <br>
     * Each element of this set got the flu from the person at this node. <br>
     * It is the empty set if this node is a leaf. For each child,<br>
     * its root is non-null. */
    private Set<FluTree> children;

    /** Constructor: a new FluTree with root p and no children. <br>
     * Throws an IllegalArgumentException if p is null. */
    public FluTree(Person p) throws IllegalArgumentException {
        if (p == null)
            throw new IllegalArgumentException(
                "Can't construct FluTree with null root");
        root= p;
        children= new HashSet<>();
    }

    /** Constructor: a new FluTree that is a copy of tree p. <br>
     * Tree p and its copy have no node in common (but nodes can share a Person). <br>
     * Throws an IllegalArgumentException if p is null. */
    public FluTree(FluTree p) throws IllegalArgumentException {
        if (p == null)
            throw new IllegalArgumentException(
                "Can't construct copy of null FluTree");
        root= p.root;
        children= new HashSet<>();

        for (FluTree dt : p.children) {
            children.add(new FluTree(dt));
        }
    }

    /** = the person that is at the root of this FluTree. */
    public Person rootPerson() {
        return root;
    }

    /** = the number of direct children of this FluTree. */
    public int childrenSize() {
        return children.size();
    }

    /** = a COPY of the set of children of this FluTree. */
    public Set<FluTree> copyOfChildren() {
        return new HashSet<>(children);
    }

    /** = the FluTree object in this tree whose root is p. <br>
     * (null if p is not in this tree). */
    public FluTree node(Person p) {
        if (root == p) return this; // Base case

        // Recursive case - Return the node with Person p if a child contains it.
        for (FluTree dt : children) {
            FluTree node= dt.node(p);
            if (node != null) return node;
        }

        return null; // p is not in the tree
    }

    /** Insert c in this FluTree as a child of p and return <br>
     * the FluTree object whose root is the new child. <br>
     * Throws an IllegalArgumentException if:<br>
     * -- c or p is null, or<br>
     * -- c is already in this FluTree, or<br>
     * -- p is not in this FluTree. */
    public FluTree insert(Person c, Person p) throws IllegalArgumentException {
        if (p == null || c == null) { throw new IllegalArgumentException(); }
        FluTree pNew= node(p);
        if (pNew == null || node(c) != null) {
            throw new IllegalArgumentException();
        }
        FluTree cNew= new FluTree(c);
        node(p).children.add(cNew);
        return cNew;
    }

    /** = the number of nodes in this FluTree. <br>
     * Note: If this is a leaf, the size is 1 (just the root) */
    public int size() {
        if (childrenSize() == 0) { return 1; }
        int sum= 1;
        for (FluTree root : children) {
            sum= sum + root.size();
        }
        return sum;
    }

    /** = "this flu tree contains a node with person p." */
    public boolean contains(Person p) {
        if (p == null) { return false; }
        if (root.equals(p)) { return true; }
        for (FluTree ch : children) {
            if (ch.contains(p)) { return true; }
        }
        return false;
    }

    /** = the depth at which p occurs in this FluTree, or <br>
     * -1 if p is not in the FluTree. <br>
     * Note: depth(root) is 0. <br>
     * If p is a child of this FluTree node, then depth(p) is 1. etc.
    public int depth(Person p) {
        if (root.equals(p)) { return 0; }
        for (FluTree ch : children) {
            int n= 1 + ch.depth(p);
            if (n != 0) { return n; }
        }
        return -1;
    }

    /** Return the width of this tree at depth d <br>
     * (i.e. the number of nodes that occur at depth d).<br>
     * Throw an IllegalArgumentException if d < 0.<br>
     * Thus, for the following tree:<br>
     *
     * <pre>
      * Depth  level:
     *  0         A
     *           / \
     *  1       B   C
     *         /   / \
     *  2     D   E   F
     *             \
     *  3           G
     * </pre>
     *
     * A.widthAtDepth(0) = 1, A.widthAtDepth(1) = 2,<br>
     * A.widthAtDepth(2) = 3, A.widthAtDepth(3) = 1, A.widthAtDepth(4) = 0.<br>
     * C.widthAtDepth(0) = 1, C.widthAtDepth(1) = 2, C.widthAtDepth(1) = 2 */
    public int widthAtDepth(int d) throws IllegalArgumentException {
        if (d < 0) { throw new IllegalArgumentException(); }
        if (d == 0) { return 1; }
        int deep= 0;
        for (FluTree root : children) {
            deep= deep + root.widthAtDepth(d - 1);
        }
        return deep;
    }

    /** Return the route the flu took to get from "here" (the root of <br>
     * this FluTree) to descendent c. If there is no such route, return null.<br>
     * For example, for this tree:<br>
     *
     * <pre>
     * Depth:
     *    0           A
     *               / \
     *    1         B   C
     *             /   / \
     *    2       D   E   F
     *             \
     *    3         G
     * </pre>
     *
     * A.fluRouteTo(E) returns the list [A,C,E].<br>
     * A.fluRouteTo(A) returns the list [A]. <br>
     * A.fluRouteTo(X) returns null.<br>
     * B.fluRouteTo(C) returns null. <br>
     * B.fluRouteTo(D) returns the list [B,D] */
    public List<Person> fluRouteTo(Person c) {
        if (root.equals(c)) {
            LinkedList<Person> l= new LinkedList<>();
            l.add(root);
            return l;
        }
        for (FluTree ch : children) {
            List<Person> n= ch.fluRouteTo(c);
            if (n != null) {
                n.add(0, root);
                return n;
            }
        }
        return null;
    }

    /** If either child1 or child2 is null or is not in this FluTree, return null.<br>
     * Otherwise, return the person at the root of the smallest subtree of this<br>
     * FluTree that contains child1 and child2.<br>
     *
     * Examples. For the following tree (which does not contain H):
     *
     * <pre>
     * Depth:
     *    0      A
     *          / \
     *    1    B   C
     *        /   / \
     *    2  D   E   F
     *        \
     *    3    G
     * </pre>
     *
     * A.commonAncestor(B, A) is A<br>
     * A.commonAncestor(B, B) is B<br>
     * A.commonAncestor(B, C) is A<br>
     * A.commonAncestor(A, C) is A<br>
     * A.commonAncestor(E, F) is C<br>
     * A.commonAncestor(G, F) is A<br>
     * B.commonAncestor(B, E) is null<br>
     * B.commonAncestor(B, A) is null<br>
     * B.commonAncestor(D, F) is null<br>
     * B.commonAncestor(D, H) is null<br>
     * A.commonAncestor(null, C) is null */
    public Person commonAncestor(Person child1, Person child2) {
        if (child1 == null || child2 == null) { return null; }
        List<Person> l1= fluRouteTo(child1);
        List<Person> l2= fluRouteTo(child2);
        if (l1 == null || l2 == null) { return null; }
        Object[] first= l1.toArray();
        Object[] second= l2.toArray();
        int length1= first.length;
        int length2= second.length;
        int minLength= 0;
        if (length1 < length2) {
            minLength= length1;
        } else {
            minLength= length2;
        }
        if (minLength == 1 && first[0] == second[0]) {
            return (Person) first[0];
        }
        if (minLength == 1) { return null; }
        if (child1.equals(child2)) { return child1; }
        for (int i= minLength - 1; i > -1; i= i - 1) {
            if (first[i] == second[i]) { return (Person) first[i]; }
        }
        return null;
    }

    /** Return true iff this is equal to ob.<br>
     * 1. If this and ob are not of the same class, they are not equal, so return false.<br>
     * 2. Two FluTrees are equal if<br>
     * -- (1) they have the same root Person object (use ==) AND<br>
     * -- (2) their children sets are the same size AND<br>
     * -- (3) their children sets are equal.<br>
     * ------ Let the two children sets be s1 and s2. ------ Since their sizes are equal, this
     * requires:<br>
     * -------- for every FluTree t1 in s1 there is a FluTree<br>
     * -------- t2 in s2 for which t1.equals(t2) is true.<br>
     *
     * -- Otherwise the two FluTrees are not equal.<br>*/
    @Override
    public boolean equals(Object ob) {
        if (this == null || ob == null) { return false; }
        if (this.getClass() != ob.getClass()) { return false; }
        FluTree obj= (FluTree) ob;
        if (root != obj.root) { return false; }
        if (childrenSize() != obj.childrenSize()) { return false; }
        for (FluTree ch : children) {
            if (help(ch, obj.children) == false) { return false; }
        }
        return true;
    }

    private boolean help(FluTree t1, Set<FluTree> s2) {
        for (FluTree ch : t1.children) {
            if (s2.equals(ch) == false) { return false; }
        }
        return true;
    }

    /** Return the maximum depth of this FluTree, <br>
     * i.e. the longest path from the root to a leaf.<br>
     * Example. If this FluTree is a leaf, return 0. */
    public int maxDepth() {
        int maxDepth= 0;
        for (FluTree dt : children) {
            maxDepth= Math.max(maxDepth, dt.maxDepth() + 1);
        }
        return maxDepth;
    }

    /** Return the immediate parent of c (null if c is not in this FluTree).<br>
     * Thus, for the following tree:
     *
     * <pre>
     * Depth:
     *    0      A
     *          / \
     *    1    B   C
     *        /   / \
     *    2  D   E   F
     *        \
     *    3    G
     * </pre>
     *
     * A.getParent(E) returns C.<br>
     * C.getParent(E) returns C.<br>
     * A.getParent(B) returns A.<br>
     * E.getParent(F) returns null. */
    public Person getParent(Person c) {
        // Base case
        for (FluTree dt : children) {
            if (dt.root == c) return root;
        }

        // Recursive case - ask children to look
        for (FluTree dt : children) {
            Person parent= dt.getParent(c);
            if (parent != null) return parent;
        }

        return null; // Not found
    }
