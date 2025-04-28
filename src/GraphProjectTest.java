
/**
 * Test class for the GraphProject application.
 * This class contains unit tests that verify the functionality of the
 * GraphProject class,
 * including command processing, insertion, removal, and printing operations.
 * 
 * @author Your Name
 * @version 1.0
 */

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GraphProjectTest {
    private GraphProject project;

    /**
     * Sets up the test fixture before each test method is executed.
     * Initializes a new GraphProject instance with a capacity of 10.
     */
    @Before
    public void setUp() {
        project = new GraphProject(10);
    }


    /**
     * Tests the insertion of a valid artist-song pair into the graph.
     * Verifies that the artist and song are properly added.
     */
    @Test
    public void testProcessInsert() {
        project.processCommand("insert Artist1<SEP>Song1");
        project.processCommand("insert Artist1<SEP>Song2"); // Same artist,
                                                            // different song
        project.processCommand("insert Artist2<SEP>Song1"); // Different artist,
                                                            // same song

        // Verify artist count (would need getter methods)
        // assertEquals(2, project.getArtistCount());
        // assertEquals(2, project.getSongCount());
    }


    /**
     * Tests insertion of duplicate artist-song pairs.
     */
    @Test
    public void testProcessInsertDuplicate() {
        project.processCommand("insert Artist1<SEP>Song1");
        project.processCommand("insert Artist1<SEP>Song1"); // Exact duplicate

        // Verify no duplicate edges created (would need getter methods)
        // assertEquals(1, project.getEdgeCount());
    }


    /**
     * Tests the handling of malformed insert commands.
     */
    @Test
    public void testProcessInsertMalformed() {
        project.processCommand("insert Artist1Song1"); // Missing separator
        project.processCommand("insert Artist1<SEP>"); // Missing song
        project.processCommand("insert <SEP>Song1"); // Missing artist
        project.processCommand("insert "); // Empty command

        // Verify no entries added (would need getter methods)
        // assertEquals(0, project.getArtistCount());
        // assertEquals(0, project.getSongCount());
    }


    /**
     * Tests the removal of an existing artist from the graph.
     */
    @Test
    public void testProcessRemoveArtist() {
        project.processCommand("insert Artist1<SEP>Song1");
        project.processCommand("insert Artist1<SEP>Song2");
        project.processCommand("remove artist Artist1");

        // Verify artist removed (would need getter methods)
        // assertNull(project.getArtistNode("Artist1"));
        // assertEquals(0, project.getEdgeCount());
    }


    /**
     * Tests the removal of a non-existent artist from the graph.
     */
    @Test
    public void testProcessRemoveNonExistent() {
        project.processCommand("remove artist Nonexistent");
        // Verify appropriate "not found" message
    }


    /**
     * Tests complex scenarios with multiple artists and songs.
     */
    @Test
    public void testProcessComplexScenario() {
        project.processCommand("insert Artist1<SEP>Song1");
        project.processCommand("insert Artist1<SEP>Song2");
        project.processCommand("insert Artist2<SEP>Song1");
        project.processCommand("insert Artist2<SEP>Song3");

        // Verify counts (would need getter methods)
        // assertEquals(2, project.getArtistCount());
        // assertEquals(3, project.getSongCount());
        // assertEquals(4, project.getEdgeCount());
    }


    /**
     * Tests the printing of artist information.
     */
    @Test
    public void testProcessPrintArtist() {
        project.processCommand("insert Artist1<SEP>Song1");
        project.processCommand("insert Artist1<SEP>Song2");
        project.processCommand("print artist");
        // Verify output contains artist and songs
    }


    /**
     * Tests the printing of the entire graph structure.
     */
    @Test
    public void testProcessPrintGraph() {
        project.processCommand("insert Artist1<SEP>Song1");
        project.processCommand("insert Artist2<SEP>Song2");
        project.processCommand("print graph");
        // Verify output contains all nodes and edges
    }


    /**
     * Tests the handling of invalid commands.
     */
    @Test
    public void testProcessInvalidCommand() {
        project.processCommand("invalid command");
        project.processCommand(""); // Empty command
        project.processCommand("insert"); // Incomplete command
        // Verify appropriate error messages
    }


    /**
     * Tests edge case with maximum capacity.
     */
    @Test(timeout = 2000)
    public void testProcessAtCapacity() {
        for (int i = 0; i < 10; i++) {
            project.processCommand("insert Artist" + i + "<SEP>Song" + i);
        }
        // Verify all inserted (would need getter methods)
        // assertEquals(10, project.getArtistCount());
    }
}
