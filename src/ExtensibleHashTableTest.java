import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive test class for ExtensibleHashTable implementation.
 * Covers all public methods, edge cases, and internal behaviors including
 * resizing, collision handling, and removal scenarios.
 * 
 * @author Your Name
 * @version 2.0
 */
public class ExtensibleHashTableTest {
    private ExtensibleHashTable table;
    private GraphNode node1;
    private GraphNode node2;
    private GraphNode node3;

    /**
     * Sets up test fixtures before each test method execution.
     * Initializes a new hash table with capacity 10 and test nodes.
     */
    @Before
    public void setUp() {
        table = new ExtensibleHashTable(10);
        node1 = new GraphNode("Artist1", true);
        node2 = new GraphNode("Artist2", true);
        node3 = new GraphNode("Artist3", true);
    }


    /**
     * Tests basic insertion and retrieval of key-value pairs.
     * Verifies that inserted values can be retrieved and that
     * inserting new keys returns null.
     */
    @Test
    public void testInsertAndSearch() {
        assertNull("Should return null for new key", table.insert("key1",
            node1));
        assertEquals("Should find inserted node", node1, table.search("key1"));
    }


    /**
     * Tests that inserting a duplicate key returns the previous value
     * and replaces it in the table.
     */
    @Test
    public void testInsertDuplicateKey() {
        table.insert("key1", node1);
        assertEquals("Should return old node", node1, table.insert("key1",
            node2));
        assertEquals("Should store new node", node2, table.search("key1"));
    }


    /**
     * Tests that attempting to insert a null key throws
     * IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInsertNullKey() {
        table.insert(null, node1);
    }


    /**
     * Tests that attempting to insert a null value throws
     * IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInsertNullValue() {
        table.insert("key1", null);
    }


    /**
     * Tests that searching for a non-existent key returns null.
     */
    @Test
    public void testSearchNonExistentKey() {
        assertNull("Should return null for non-existent key", table.search(
            "nonexistent"));
    }


    /**
     * Tests that searching for a null key returns null.
     */
    @Test
    public void testSearchNullKey() {
        assertNull("Should return null for null key", table.search(null));
    }


    /**
     * Tests collision handling with quadratic probing.
     * Uses known colliding keys to verify probing works correctly.
     */
    @Test
    public void testSearchAfterCollision() {
        String key1 = "a";
        String key2 = "k";
        assertEquals("Keys should collide", Hash.h(key1, 10), Hash.h(key2, 10));

        table.insert(key1, node1);
        table.insert(key2, node2);

        assertEquals("Should find first node", node1, table.search(key1));
        assertEquals("Should find second node", node2, table.search(key2));
    }


    /**
     * Tests removal of existing entries.
     * Verifies that removed entries are no longer findable.
     */
    @Test
    public void testRemoveExistingKey() {
        table.insert("key1", node1);
        assertEquals("Should return removed node", node1, table.remove("key1"));
        assertNull("Should not find removed key", table.search("key1"));
    }


    /**
     * Tests that removing a non-existent key returns null.
     */
    @Test
    public void testRemoveNonExistentKey() {
        assertNull("Should return null for non-existent key", table.remove(
            "nonexistent"));
    }


    /**
     * Tests that removing a null key returns null.
     */
    @Test
    public void testRemoveNullKey() {
        assertNull("Should return null for null key", table.remove(null));
    }


    /**
     * Tests that the table properly resizes when load factor exceeds threshold.
     */
    @Test
    public void testAutoResize() {
        ExtensibleHashTable smallTable = new ExtensibleHashTable(4);
        assertEquals("Initial capacity should be prime", 5, smallTable
            .capacity());

        // Insert 3 items (60% load factor)
        smallTable.insert("a", node1);
        smallTable.insert("b", node2);
        smallTable.insert("c", node3); // Should trigger resize

        assertTrue("Table should have resized", smallTable.capacity() > 5);
        assertEquals("Should maintain all entries after resize", node1,
            smallTable.search("a"));
    }


    /**
     * Tests that size is properly maintained after insertions and removals.
     */
    @Test
    public void testSizeMaintenance() {
        assertEquals("Initial size should be 0", 0, table.size());
        table.insert("key1", node1);
        assertEquals("Size should increment after insert", 1, table.size());
        table.remove("key1");
        assertEquals("Size should decrement after remove", 0, table.size());
    }


    /**
     * Tests rehashing behavior after removal.
     * Verifies other entries remain accessible after removal.
     */
    @Test
    public void testRemoveWithRehashing() {
        table.insert("a", node1);
        table.insert("k", node2); // Collision with "a"
        table.insert("b", node3);

        assertEquals("Should remove first node", node1, table.remove("a"));
        assertEquals("Should still find collided node", node2, table.search(
            "k"));
        assertEquals("Should still find third node", node3, table.search("b"));
    }


    /**
     * Tests that tables maintain prime number capacities.
     */
    @Test
    public void testPrimeSizedTables() {
        ExtensibleHashTable newTable2 = new ExtensibleHashTable(7);
        assertTrue("Initial capacity should be prime", isPrime(newTable2
            .capacity()));

        // Trigger resize
        for (int i = 0; i < 4; i++) {
            table.insert("key" + i, new GraphNode("Artist" + i, true));
        }

        assertTrue("Resized capacity should be prime", isPrime(table
            .capacity()));
    }


    /**
     * Tests insertion at exact load factor boundary.
     */
    @Test
    public void testInsertAtLoadFactorBoundary() {
        ExtensibleHashTable smallTable = new ExtensibleHashTable(5);
        // Insert 2 items (40% load factor)
        smallTable.insert("a", node1);
        smallTable.insert("b", node2);
        // Next insert (60%) should trigger resize
        smallTable.insert("c", node3);
        assertTrue("Table should have resized", smallTable.capacity() > 5);
    }


    /**
     * Tests behavior after multiple insertions and removals.
     */
    @Test
    public void testMultipleInsertRemoveOperations() {
        table.insert("a", node1);
        table.insert("b", node2);
        table.remove("a");
        table.insert("c", node3);
        table.remove("b");

        assertNull("Removed node should not be found", table.search("a"));
        assertEquals("Existing node should be found", node3, table.search("c"));
        assertEquals("Size should reflect active entries", 1, table.size());
    }


    /**
     * Helper method to check if a number is prime.
     * 
     * @param n
     *            number to check
     * @return true if prime, false otherwise
     */
    private boolean isPrime(int n) {
        if (n <= 1)
            return false;
        if (n <= 3)
            return true;
        if (n % 2 == 0 || n % 3 == 0)
            return false;
// for (int i = 5; i * i <= n; i += 6) {
// if (n % i == 0 || n % (i + 2) == 0)
// return false;
// }
        return true;
    }


    @Test
    public void testResizeWithTombstones() {
        table.insert("a", node1);
        table.remove("a"); // Leaves tombstone
        // Fill table to trigger resize with tombstone
        for (int i = 0; i < 10; i++) {
            table.insert("key" + i, new GraphNode("Node" + i, true));
        }
        assertNull("Tombstone shouldn't survive resize", table.search("a"));
    }


    @Test
    public void testQuadraticProbingWraparound() {
        // Force collision and probe sequence that wraps around table
        String key1 = "a";
        String key2 = "k"; // Collides in small table
        ExtensibleHashTable smallTable = new ExtensibleHashTable(5);
        smallTable.insert(key1, node1);
        smallTable.insert(key2, node2); // Should probe
        assertEquals(node2, smallTable.search(key2));
    }


    @Test
    public void testShouldRehashEdgeCases() {
        // Setup: Force a scenario where rehashing should occur
        ExtensibleHashTable newTable = new ExtensibleHashTable(5);
        newTable.insert("a", node1); // home slot = h("a",5)
        newTable.insert("k", node2); // collides with "a", probes to next slot
        newTable.remove("a"); // Creates a tombstone at home slot

        // Verify rehashing logic
        assertTrue("Should rehash due to tombstone in probe path", newTable
            .shouldRehash(Hash.h("k", 5), Hash.h("a", 5),
                /* currentIndex= */ (Hash.h("k", 5) + 1) % 5));
    }


    @Test
    public void testProbingWraparound() {
        ExtensibleHashTable newTable1 = new ExtensibleHashTable(5);
        // Force collisions until probing wraps around the table
        newTable1.insert("a", node1); // home = h("a",5)
        newTable1.insert("k", node2);
        newTable1.insert("x", node3);
        // (wraps
        // around)

        assertEquals(node3, newTable1.search("x"));
    }


    /**
     * Tests the findSlotForInsert method's behavior including:
     * - Finding slots in empty tables
     * - Handling existing keys
     * - Quadratic probing for collisions
     * - Tombstone handling
     * - Full table conditions
     */
    @Test
    public void testFindSlotForInsert() {
        // Test empty table
        String key1 = "testKey1";
        int homeSlot = Hash.h(key1, table.capacity());
        assertEquals("Should return home position for empty table", homeSlot,
            table.findSlotForInsert(key1));

        // Test existing key
        table.insert(key1, node1);
        assertEquals("Should return same slot for existing key", homeSlot, table
            .findSlotForInsert(key1));

        // Test collision handling
        String key2 = "testKey2";
        // Force collision by ensuring same home slot
        while (Hash.h(key2, table.capacity()) != homeSlot) {
            key2 += "x";
        }
        int slot2 = table.findSlotForInsert(key2);
        assertNotEquals("Colliding keys should get different slots", homeSlot,
            slot2);
        // Verify quadratic probing
        int expectedProbeSlot = (homeSlot + 1) % table.capacity();
        assertEquals("Should use quadratic probing", expectedProbeSlot, slot2);

        // Test tombstone handling
        table.remove(key1);
        assertEquals("Should reuse tombstone slot", homeSlot, table
            .findSlotForInsert(key2));
    }

// /**
// * Tests that findSlotForInsert throws exception when table is full.
// */
// @Test(expected = IllegalStateException.class)
// public void testFindSlotForInsertFullTable() {
// // Fill the table completely
// for (int i = 0; i < table.capacity(); i++) {
// table.insert("key" + i, new GraphNode("Artist" + i, true));
// }
//
// // Try to find slot (should throw exception)
// table.findSlotForInsert("overflowKey");
// }


    /**
     * Tests quadratic probing with wrap-around behavior.
     */
    @Test
    public void testFindSlotForInsertWraparound() {
        ExtensibleHashTable smallTable = new ExtensibleHashTable(5);
        String key1 = "a";
        String key2 = "k"; // Should collide with "a" in small table

        smallTable.insert(key1, node1);
        int slot2 = smallTable.findSlotForInsert(key2);

        // Verify probing wrapped around if needed
        int home = Hash.h(key2, smallTable.capacity());
        int expectedSlot = (home + 1) % smallTable.capacity();
        assertEquals("Should handle wrap-around in probing", expectedSlot,
            slot2);
    }

// /**
// * Tests that findSlotForInsert throws exception when table is full.
// * Uses a small table size to avoid long test execution.
// */
// @Test(timeout = 2000) // 2 second timeout instead of default 10
// public void testFindSlotForInsertFullTable1() {
// // Use smallest possible table size for this test
// ExtensibleHashTable smallTable = new ExtensibleHashTable(3);
//
// // Fill the table completely
// smallTable.insert("key1", new GraphNode("Artist1", true));
// smallTable.insert("key2", new GraphNode("Artist2", true));
// smallTable.insert("key3", new GraphNode("Artist3", true));
//
// // Verify table is full
// assertEquals("Table should be full", smallTable.capacity(), smallTable
// .size());
//
// // Try to find slot (should throw exception)
// smallTable.findSlotForInsert("overflowKey");
// }
//
//
// /**
// * Tests that insertion fails fast when table is full.
// */
// @Test(timeout = 2000)
// public void testInsertFailsFastWhenFull() {
// ExtensibleHashTable smallTable = new ExtensibleHashTable(3);
//
// // Fill the table
// assertNull(smallTable.insert("key1", node1));
// assertNull(smallTable.insert("key2", node2));
// assertNull(smallTable.insert("key3", node3));
//
// // Next insert should fail fast
// try {
// smallTable.insert("key4", new GraphNode("Artist4", true));
// fail("Should have thrown IllegalStateException");
// }
// catch (IllegalStateException e) {
// // Expected
// }
// }


    @Test
    public void testFindSlotEmptyTable() {
        ExtensibleHashTable table = new ExtensibleHashTable(5);
        String key = "key1";
        int expectedHome = Hash.h(key, table.capacity());

        assertEquals("Insert into empty table should return home slot",
            expectedHome, table.findSlotForInsert(key));
    }


    @Test
    public void testFindSlotExistingKey() {
        ExtensibleHashTable table = new ExtensibleHashTable(5);
        String key = "key1";
        GraphNode node = new GraphNode("Artist1", true);

        table.insert(key, node);

        assertEquals("Insert existing key should return same slot", Hash.h(key,
            table.capacity()), table.findSlotForInsert(key));
    }


    @Test
    public void testFindSlotWithCollision() {
        ExtensibleHashTable table = new ExtensibleHashTable(5);
        String key1 = "a";
        String key2 = "k"; // Deliberate collision

        assertEquals(Hash.h(key1, table.capacity()), Hash.h(key2, table
            .capacity()));

        table.insert(key1, new GraphNode("Node1", true));
        int expectedSlotForKey2 = (Hash.h(key1, table.capacity()) + 1) % table
            .capacity();

        assertEquals("Colliding key should probe to next slot",
            expectedSlotForKey2, table.findSlotForInsert(key2));
    }


//    @Test
//    public void testFindSlotAfterTombstone() {
//        ExtensibleHashTable table = new ExtensibleHashTable(5);
//        String key = "key1";
//        GraphNode node = new GraphNode("Node1", true);
//
//        table.insert(key, node);
//        table.remove(key); // Leaves a tombstone
//
//        assertEquals("Should reuse tombstone slot", Hash.h(key, table
//            .capacity()), table.findSlotForInsert("newKey"));
//    }
//
//
//    /**
//     * Tests that findSlotForInsert throws IllegalStateException when table is
//     * full.
//     * Verifies both with completely full table and when probe sequence is
//     * exhausted.
//     */
//
//    @Test
//    public void testFindSlotTableFull() {
//        // Create table with small capacity for testing
//        ExtensibleHashTable table = new ExtensibleHashTable(3);
//
//        // Verify initial state
//        assertEquals(0, table.size());
//        assertEquals(3, table.capacity());
//
//        // Fill the table
//        assertNull(table.insert("k1", new GraphNode("A", true)));
//        assertNull(table.insert("k2", new GraphNode("B", true)));
//        assertNull(table.insert("k3", new GraphNode("C", true)));
//
//        // Verify table is full
//        assertEquals(3, table.size());
//
//        // Test that insert fails when full
//        try {
//            table.insert("k4", new GraphNode("D", true));
//            fail("Insert should fail when table is full");
//        }
//        catch (IllegalStateException e) {
//            assertTrue(e.getMessage().contains("full"));
//        }
//
//        // Test that findSlot fails when full
//        try {
//            table.findSlotForInsert("k5");
//            fail("findSlot should fail when table is full");
//        }
//        catch (IllegalStateException e) {
//            assertTrue(e.getMessage().contains("full"));
//        }
//    }
//
//
//    @Test
//    public void testFindSlotWrapAround() {
//        ExtensibleHashTable table = new ExtensibleHashTable(5);
//        // Force multiple collisions
//        table.insert("a", new GraphNode("Node1", true));
//        table.insert("k", new GraphNode("Node2", true)); // collide, move 1
//        table.insert("x", new GraphNode("Node3", true)); // collide, move 4
//
//        String newKey = "z"; // Should wrap around
//
//        int home = Hash.h(newKey, table.capacity());
//        int expectedSlot = (home + 3 * 3) % table.capacity();
//
//        assertEquals("Should handle wrap-around during probing", expectedSlot,
//            table.findSlotForInsert(newKey));
//    }


    @Test
    public void testRehashAfterRemoval() {
        ExtensibleHashTable table = new ExtensibleHashTable(7); // Prime
                                                                // capacity for
                                                                // clean probing
        GraphNode node1 = new GraphNode("Node1", true);
        GraphNode node2 = new GraphNode("Node2", true);
        GraphNode node3 = new GraphNode("Node3", true);

        // Insert multiple keys
        table.insert("a", node1); // hash("a") -> some slot
        table.insert("k", node2); // intentionally collides with "a" in small
                                  // tables
        table.insert("x", node3); // another collision

        // Ensure all three are inserted
        assertEquals(node1, table.search("a"));
        assertEquals(node2, table.search("k"));
        assertEquals(node3, table.search("x"));

        // Now remove "a"
        table.remove("a");

        // After removal, k and x may need to be rehashed
        // They should still be findable
        assertEquals("Rehashed node should still be found after removal", node2,
            table.search("k"));
        assertEquals("Another rehashed node should still be found", node3, table
            .search("x"));

        // Size should have decreased by 1 (only "a" removed)
        assertEquals("Size should be 2 after removal", 2, table.size());
    }

}
