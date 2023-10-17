import java.util.Iterator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class IterableMultiKeyRBT<T extends Comparable<T>> extends RedBlackTree<KeyListInterface<T>> 
implements IterableMultiKeySortedCollectionInterface<T>, Iterable<T>{

    /**
     * Inserts value into tree that can store multiple objects per key by keeping
     * lists of objects in each node of the tree.
     * @param key object to insert
     * @return true if a new node was inserted, false if the key was added into an existing node
     */
    @Override
    public boolean insertSingleKey(T key) {
        // TODO Auto-generated method stub
    
        return false;
        
    }

    /**
     * @return the number of values in the tree.
     */
    @Override
    public int numKeys() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * Returns an iterator that does an in-order iteration over the tree.
     */
    @Override
    public Iterator<T> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Sets the starting point for iterations. Future iterations will start at the
     * starting point or the key closest to it in the tree. This setting is remembered
     * until it is reset. Passing in null disables the starting point.
     * @param startPoint the start point to set for iterations
     */
    @Override
    public void setIterationStartPoint(Comparable<T> startPoint) {
        // TODO Auto-generated method stub
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
        Iterator<Double> iterator = tree.iterator();
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(2.2, iterator.next());
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(3.3, iterator.next());
        Assertions.assertFalse(iterator.hasNext());

        tree.setIterationStartPoint(4.0); // Setting a start point beyond the keys in the tree
        iterator = tree.iterator();
        Assertions.assertFalse(iterator.hasNext());
    }
}
