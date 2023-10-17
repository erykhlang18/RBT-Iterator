// --== CS400 Fall 2023 File Header Information ==--
// Name: <Eric Liang>
// Email: <eliang7@wisc.edu>
// Group: <C17>
// TA: <Matthew Schwennesen>
// Lecturer: <Florian Heimerl>
// Notes to Grader: <optional extra notes>
import java.beans.Transient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class RedBlackTree<T extends Comparable<T>> extends BinarySearchTree<T> {
    // Implement Red-Black Tree specific methods here

    protected static class RBTNode<T> extends Node<T> {
        public int blackHeight = 0;

        public RBTNode(T data) {
            super(data);
        }

        public RBTNode<T> getUp() {
            return (RBTNode<T>) this.up;
        }

        public RBTNode<T> getDownLeft() {
            return (RBTNode<T>) this.down[0];
        }

        public RBTNode<T> getDownRight() {
            return (RBTNode<T>) this.down[1];
        }


    }

    protected void enforceRBTreePropertiesAfterInsert(RBTNode<T> newNode) {
        // clears any issues inside a balanced red black tree when inserting a new node
//blackHeight 0 is red, blackHeight 1 is black
// new nodes are always red

RBTNode<T> parent;
RBTNode<T> grandparent;
RBTNode<T> uncle;  
//parent node
        parent = newNode.getUp();


//case 1: root color is red
        if (parent == null) {

            newNode.blackHeight = 1;
            return;
        }

//case 2: if the new node parent is black
        if (parent.blackHeight == 1) {
            return;
        }

//grandparent
        grandparent = parent.getUp();
        if (grandparent == null) {

            throw new IllegalArgumentException("Not a Red Black tree");
        }

//uncle
        if (parent == grandparent.getDownLeft()) {
            uncle = grandparent.getDownRight();
        } else {
            uncle = grandparent.getDownLeft();
        }

//case 3: if uncle is red
        if (uncle != null && uncle.blackHeight ==0) {

            parent.blackHeight = 1;
            uncle.blackHeight = 1;

            grandparent.blackHeight = 0;
            enforceRBTreePropertiesAfterInsert(grandparent);
            return;

        }

//case 4 if the uncle is black and the newNode, parent, and grandparent form a triangle
        if (newNode == parent.getDownRight() && parent == grandparent.getDownLeft()) {
            rotate(newNode, parent);
        } else if (newNode == parent.getDownLeft() && parent == grandparent.getDownRight()) {
            rotate(newNode, parent);
        }

//case 5 if redNode uncle is black and newNode, parent, and grandparent form a line

        parent.blackHeight = 1;
        grandparent.blackHeight = 0;
        rotate(parent, grandparent);

    }


    /**
     * calls in insert helper to introduce a new node into a red black tree
     *
     * @param data node being inserted
     * @return true or false whether node successfully inserts
     * @throws NullPointerException
     */
    @Override
    public boolean insert(T data) throws NullPointerException {
        RBTNode<T> insertNode = new RBTNode<T>(data);
        insertHelper(insertNode);

        enforceRBTreePropertiesAfterInsert(insertNode);
        ((RBTNode<T>) root).blackHeight = 1;
        return true;
    }

    /**
     * red parent tests if the inserted red node also has a red parent.
     * the tester should should return a failed assertion if the parent
     * is still red. 
     */
    @Test
    public void testRedParent() {
        RBTNode<Integer> root = new RBTNode<>(54);
        root.blackHeight = 1;
        root.down[0] = new RBTNode<>(12);
        root.down[0].up = root;
        root.down[1] = new RBTNode<>(77);
        root.down[1].up = root;
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.root = root;
        tree.size = 3;
        tree.insert(80);


        if (((RBTNode<Integer>) tree.root).getDownRight().getDownRight().blackHeight == 0
                && ((RBTNode<Integer>) tree.root).getDownRight().blackHeight == 0) {
            String message = "asserted node is not correct color";
            Assertions.fail(message);
        }
    }

    /**
     * this tester tests the situation when a new node has a red uncle and \
     * requires the tree to balance itself. the tester then asserts if the 
     * test passes or not given the resulting string of toLevelOrderString()
     */
    @Test
    public void redUncle() {
        RBTNode<Integer> root = new RBTNode<>(44);
        root.blackHeight = 1;
        root.down[0] = new RBTNode<>(13);
        root.down[0].up = root;
        root.down[1] = new RBTNode<>(67);
        root.down[1].up = root;

        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.root = root;
        tree.size = 3;

        tree.insert(27);
        Assertions.assertEquals("[ 44, 13, 67, 27 ]", tree.toLevelOrderString());
    }


    /**
     * testRootNodeColor tests if the root node is always black
     * trees that require rotations will be utilized and the enforce method should change
     * the root to black always.
     */
    @Test
    public void testRootNodeColor() {
        RBTNode<Integer> root = new RBTNode<>(54);
        root.blackHeight = 1;
        root.down[0] = new RBTNode<>(12);
        root.down[0].up = root;
        root.down[1] = new RBTNode<>(77);
        root.down[1].up = root;

        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.root = root;
        tree.size = 3;

        if (((RBTNode<Integer>) tree.root).blackHeight != 1) {
            String message = "Root is not black";
            Assertions.fail(message);
        }

    }

}
