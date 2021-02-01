package a4;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

/** @author david gries */
public class FluTreeTest {

    private static Network n;
    private static Person[] people;
    private static Person personA;
    private static Person personB;
    private static Person personC;
    private static Person personD;
    private static Person personE;
    private static Person personF;
    private static Person personG;
    private static Person personH;
    private static Person personI;
    private static Person personJ;
    private static Person personK;
    private static Person personL;

    /** */
    @BeforeClass
    public static void setup() {
        n= new Network();
        people= new Person[] { new Person("A", 0, n),
                new Person("B", 0, n), new Person("C", 0, n),
                new Person("D", 0, n), new Person("E", 0, n),
                new Person("F", 0, n),
                new Person("G", 0, n), new Person("H", 0, n),
                new Person("I", 0, n),
                new Person("J", 0, n), new Person("K", 0, n),
                new Person("L", 0, n)
        };
        personA= people[0];
        personB= people[1];
        personC= people[2];
        personD= people[3];
        personE= people[4];
        personF= people[5];
        personG= people[6];
        personH= people[7];
        personI= people[8];
        personJ= people[9];
        personK= people[10];
        people[10]= personK;
        personL= people[11];
    }

    /** * */
    @Test
    public void testBuiltInGetters() {
        FluTree st= new FluTree(personB);
        assertEquals("B", toStringBrief(st));
    }

    // A.sh(B, C) = A
    // A.sh(D, F) = B
    // A.sh(D, I) = B
    // A.sh(H, I) = H
    // A.sh(D, C) = A
    // B.sh(B, C) = null
    // B.sh(I, E) = B

    /** Create a FluTree with structure A[B[D E F[G[H[I]]]] C] <br>
     * This is the tree
     *
     * <pre>
     *            A
     *          /   \
     *         B     C
     *       / | \
     *      D  E  F
     *            |
     *            G
     *            |
     *            H
     *            |
     *            I
     * </pre>
     */
    private FluTree makeTree1() {
        FluTree dt= new FluTree(personA); // A
        dt.insert(personB, personA); // A, B
        dt.insert(personC, personA); // A, C
        dt.insert(personD, personB); // B, D
        dt.insert(personE, personB); // B, E
        dt.insert(personF, personB); // B, F
        dt.insert(personG, personF); // F, G
        dt.insert(personH, personG); // G, H
        dt.insert(personI, personH); // H, I
        return new FluTree(dt);
    }

    /** test a call on makeTree1(). */
    @Test
    public void testMakeTree1() {
        FluTree dt= makeTree1();
        assertEquals("A[B[D E F[G[H[I]]]] C]", toStringBrief(dt));
    }

    /** */
    @Test
    public void test1Insert() {
        FluTree st= new FluTree(personB);

        // Test insert in the root
        FluTree dt2= st.insert(personC, personB);
        assertEquals("B[C]", toStringBrief(st)); // test tree
        assertEquals(personC, dt2.rootPerson());  // test return value
    }

    /** */
    @Test
    public void test2size() {
        FluTree st1= new FluTree(personC);
        assertEquals(1, st1.size());

        FluTree st2= new FluTree(personC);
        st2.insert(personB, personC);
        st2.insert(personD, personC);
        assertEquals(3, st2.size());
    }

    /** */
    @Test
    public void test3contains() {
        FluTree st1= new FluTree(personC);
        assertEquals(true, st1.contains(personC));

        FluTree st2= new FluTree(personA);
        st2.insert(personB, personA);
        st2.insert(personD, personA);
        st2.insert(personE, personD);
        assertEquals(true, st2.contains(personB));
        assertEquals(false, st2.contains(personC));
        assertEquals(true, st2.contains(personD));
        assertEquals(true, st2.contains(personE));
    }

    /** */
    @Test
    public void test4depth() {
        FluTree st= new FluTree(personB);
        st.insert(personC, personB);
        st.insert(personD, personC);
        assertEquals(0, st.depth(personB));
        assertEquals(1, st.depth(personC));
        assertEquals(2, st.depth(personD));
        assertEquals(-1, st.depth(personE));
    }

    /** */

    /*
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

    @Test
    public void test5WidthAtDepth() {
        FluTree st= new FluTree(personA);
        st.insert(personB, personA);
        st.insert(personC, personA);
        st.insert(personD, personB);
        st.insert(personE, personC);
        st.insert(personF, personC);
        st.insert(personF, personE);
        assertEquals(1, st.widthAtDepth(0));
        assertEquals(2, st.widthAtDepth(1));
        assertEquals(3, st.widthAtDepth(2));
        assertEquals(1, st.widthAtDepth(3));
        assertEquals(0, st.widthAtDepth(4));

    }
    /*
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

    @SuppressWarnings("javadoc")
    @Test
    public void test6FluRouteTo() {
        FluTree st= new FluTree(personA);
        st.insert(personB, personA);
        st.insert(personC, personA);
        st.insert(personD, personB);
        st.insert(personE, personC);
        st.insert(personF, personC);
        st.insert(personF, personE);
        List<Person> route1= st.fluRouteTo(personA);
        assertEquals("[A]", getNames(route1));
        List<Person> route2= st.fluRouteTo(personE);
        assertEquals("[A, C, E]", getNames(route2));
        List<Person> route3= st.fluRouteTo(personG);
        assertEquals(null, route3);
    }

    /** Return the names of Persons in sp, separated by ", " and delimited by [ ]. Precondition: No
     * name is the empty string. */
    private String getNames(List<Person> sp) {
        String res= "[";
        for (Person p : sp) {
            if (res.length() > 1) res= res + ", ";
            res= res + p.name();
        }
        return res + "]";
    }

    /*  * Depth:
        *    0           A
        *               / \
        *    1         B   C
        *             /   / \
        *    2       D   E   F
        *             \
        *    3         G
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
    @Test
    public void test7commonAncestor() {
        FluTree st= new FluTree(personA);
        st.insert(personB, personA);
        st.insert(personC, personA);
        st.insert(personD, personB);
        st.insert(personE, personC);
        st.insert(personF, personC);
        Person one= st.commonAncestor(personB, personA);
        assertEquals(personA, one);
        Person two= st.commonAncestor(personB, personB);
        assertEquals(personB, two);
        Person three= st.commonAncestor(personB, personC);
        assertEquals(personA, three);
        Person four= st.commonAncestor(personA, personC);
        assertEquals(personA, four);
        Person five= st.commonAncestor(personE, personF);
        assertEquals(personC, five);
        Person six= st.commonAncestor(null, personC);
        assertEquals(null, six);
        FluTree st2= new FluTree(personB);
        st2.insert(personD, personB);
        Person seven= st2.commonAncestor(personB, personE);
        assertEquals(null, seven);
        Person eight= st2.commonAncestor(personB, personA);
        assertEquals(null, eight);
        Person nine= st2.commonAncestor(personD, personH);
        assertEquals(null, nine);
    }

    /** */
    @Test
    public void test8equals() {
        FluTree treeB1= new FluTree(personA);
        FluTree treeB2= new FluTree(personA);
        assertEquals(true, treeB1.equals(treeB2));
        treeB1.insert(personB, personA);
        treeB1.insert(personC, personA);
        treeB2.insert(personB, personA);
        assertEquals(false, treeB1.equals(treeB2));
        treeB2.insert(personC, personA);
        assertEquals(true, treeB1.equals(treeB2));
        treeB1.insert(personD, personB);
        assertEquals(false, treeB1.equals(treeB2));
        treeB1.insert(personE, personB);
        assertEquals(false, treeB1.equals(treeB2));
        treeB2.insert(personD, personB);
        assertEquals(false, treeB1.equals(treeB2));

        FluTree treeB3= new FluTree(personB);
        FluTree treeB4= new FluTree(personA);
        assertEquals(false, treeB3.equals(treeB4));
    }

    // ===================================
    // ==================================

    /** Return a representation of this tree. This representation is: <br>
     * (1) the name of the Person at the root, followed by <br>
     * (2) the representations of the children <br>
     * . (in alphabetical order of the children's names). <br>
     * . There are two cases concerning the children.
     *
     * . No children? Their representation is the empty string. <br>
     * . Children? Their representation is the representation of each child, <br>
     * . with a blank between adjacent ones and delimited by "[" and "]". <br>
     * <br>
     * Examples: One-node tree: "A" <br>
     * root A with children B, C, D: "A[B C D]" <br>
     * root A with children B, C, D and B has a child F: "A[B[F] C D]" */
    public static String toStringBrief(FluTree t) {
        String res= t.rootPerson().name();

        Object[] childs= t.copyOfChildren().toArray();
        if (childs.length == 0) return res;
        res= res + "[";
        selectionSort1(childs);

        for (int k= 0; k < childs.length; k= k + 1) {
            if (k > 0) res= res + " ";
            res= res + toStringBrief((FluTree) childs[k]);
        }
        return res + "]";
    }

    /** Sort b --put its elements in ascending order. <br>
     * Sort on the name of the Person at the root of each FluTree.<br>
     * Throw a cast-class exception if b's elements are not FluTree */
    public static void selectionSort1(Object[] b) {
        int j= 0;
        // {inv P: b[0..j-1] is sorted and b[0..j-1] <= b[j..]}
        // 0---------------j--------------- b.length
        // inv : b | sorted, <= | >= |
        // --------------------------------
        while (j != b.length) {
            // Put into p the index of smallest element in b[j..]
            int p= j;
            for (int i= j + 1; i != b.length; i++ ) {
                String bi= ((FluTree) b[i]).rootPerson().name();
                String bp= ((FluTree) b[p]).rootPerson().name();
                if (bi.compareTo(bp) < 0) {
                    p= i;

                }
            }
            // Swap b[j] and b[p]
            Object t= b[j];
            b[j]= b[p];
            b[p]= t;
            j= j + 1;
        }
    }

}
