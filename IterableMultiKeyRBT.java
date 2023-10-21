// --== CS400 Fall 2023 File Header Information ==--
// Name: <Eric Liang>
// Email: <eliang7@wisc.edu>
// Group: <C17>
// TA: <Matthew Schwennesen>
// Lecturer: <Florian>
// Notes to Grader: <optional extra notes>
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class IterableMultiKeyRBT<T extends Comparable<T>> extends RedBlackTree<KeyListInterface<T>> 
implements IterableMultiKeySortedCollectionInterface<T>, Iterable<T>{

    private Comparable<T> iterationStartPoint; // Field to store the iteration start point
    private int numberOfKeys; // Field to store the number of keys in the tree

    /**
     * Inserts value into tree that can store multiple objects per key by keeping
     * lists of objects in each node of the tree.
     * @param key object to insert
     * @return true if a new node was inserted, false if the key was added into an existing node
     */
    @Override
    public boolean insertSingleKey(T key) {
        // TODO Auto-generated method stub
        KeyListInterface<T> newKeyList = new KeyList<>(key);
        Node<KeyListInterface<T>> existingNode = findNode(newKeyList);

        if(existingNode != null){
            existingNode.data.addKey(key);
            numberOfKeys++;
            return false; 

        } else {
            super.insert(newKeyList);
            numberOfKeys++;
            return true;
        }
                
    }

    /**
     * @return the number of values in the tree.
     */
    @Override
    public int numKeys() {
        // TODO Auto-generated method stub
        return numberOfKeys;
    }

    /**
     * Returns an iterator that does an in-order iteration over the tree.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Stack<RBTNode<KeyListInterface<T>>> start = getStartStack();
            Iterator<T> iterator = null;
            T next = null;
    
            @Override
            public boolean hasNext() {
                if (next != null) {
                    return true;
                }
    
                while ((iterator == null || !iterator.hasNext()) && !start.isEmpty()) {
                    RBTNode<KeyListInterface<T>> node = start.pop();
                    iterator = node.data.iterator();
    
                    // Move to the right-most child of the left subtree (if exists)
                    if (node.down[1] != null) {
                        RBTNode<KeyListInterface<T>> current = (RBTNode<KeyListInterface<T>>) node.down[1];
                        while (current != null) {
                            start.push(current);
                            current = (RBTNode<KeyListInterface<T>>) current.down[0];
                        }
                    }
                }
    
                if (iterator != null && iterator.hasNext()) {
                    next = iterator.next();
                    return true;
                }
    

                return false;
            }
    
            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
    

                T keyToReturn = next;
                next = null;
                return keyToReturn;


            }
        };
    }

    /**
     * Sets the starting point for iterations. Future iterations will start at the
     * starting point or the key closest to it in the tree. This setting is remembered
     * until it is reset. Passing in null disables the starting point.
     * @param startPoint the start point to set for iterations
     */
    @Override
    public void setIterationStartPoint(Comparable<T> startPoint) {
        this.iterationStartPoint = startPoint;
    }

    /**
     * Removes all keys from the tree.
     */
    @Override
     public void clear() {
        super.clear();
        
        numberOfKeys = 0;

    }

    protected Stack<RBTNode<KeyListInterface<T>>> getStartStack() {
       
        Stack<RBTNode<KeyListInterface<T>>> stack = new Stack<>();
       RBTNode<KeyListInterface<T>> currentKey = (RBTNode<KeyListInterface<T>>) this.root;

       while (currentKey != null) {
        Iterator<T> iterator = currentKey.data.iterator();
        T firstKey = iterator.hasNext() ? iterator.next() : null;

        if (firstKey != null && (iterationStartPoint == null || iterationStartPoint.compareTo(firstKey) <= 0)) {
            stack.push(currentKey);
        }

        if (iterationStartPoint == null || (firstKey != null && iterationStartPoint.compareTo(firstKey) <= 0)) {
            currentKey = (RBTNode<KeyListInterface<T>>) currentKey.down[0];
        } else {
            currentKey = (RBTNode<KeyListInterface<T>>) currentKey.down[1];
        }
    }

    return stack;
}








    //testers
    /**
     * Tests the testInsertSingleKey() method.
     * Checks if inserting keys into the tree works as expected, and if attempting
     * to insert the same key again returns false.
     */
    @Test
    public void testInsertSingleKey() {
        IterableMultiKeyRBT<String> tree = new IterableMultiKeyRBT<>();
        Assertions.assertTrue(tree.insertSingleKey("key1"));
        Assertions.assertFalse(tree.insertSingleKey("key1")); // Inserting the same key again should return false
        Assertions.assertTrue(tree.insertSingleKey("key2"));
    }

    /**
     * Tests the NumKeys() method.
     * Checks if the method returns the correct number of keys in the tree,
     * including duplicates.
     */
    @Test
    public void testNumKeys() {
        IterableMultiKeyRBT<Integer> tree = new IterableMultiKeyRBT<>();
        Assertions.assertEquals(0, tree.numKeys());
        tree.insertSingleKey(1);
        tree.insertSingleKey(2);
        tree.insertSingleKey(1); // Duplicate key
        Assertions.assertEquals(3, tree.numKeys());
    }

    /**
     * Tests the SetIterationStartPoint() method.
     * Checks if setting the iteration start point works correctly by inserting keys
     * into the tree and testing the iteration from a point. 
     */
    @Test
    public void testSetIterationStartPoint() {
        IterableMultiKeyRBT<Double> tree = new IterableMultiKeyRBT<>();
    tree.insertSingleKey(1.1);
    tree.insertSingleKey(2.2);
    tree.insertSingleKey(3.3);

    tree.setIterationStartPoint(2.0);

    // Initialize iterator after setting the iteration start point
    Iterator<Double> iterator = tree.iterator();

    Assertions.assertTrue(iterator.hasNext());
    Assertions.assertEquals(2.2, iterator.next());
    Assertions.assertTrue(iterator.hasNext());
    Assertions.assertEquals(3.3, iterator.next());
    Assertions.assertFalse(iterator.hasNext());

    tree.setIterationStartPoint(4.0);

    // Initialize iterator again after changing the start point
    iterator = tree.iterator();

    Assertions.assertFalse(iterator.hasNext());
    }
       
    
}
