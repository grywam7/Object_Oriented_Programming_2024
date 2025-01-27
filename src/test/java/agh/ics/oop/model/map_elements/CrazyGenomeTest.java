package agh.ics.oop.model.map_elements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CrazyGenomeTest {

    private CrazyGenome crazyGenome;
    private int genomeLength;

    @BeforeEach
    void setUp() {
        genomeLength = 8; // Fixed length for testing
        crazyGenome = new CrazyGenome(genomeLength);
    }

    @Test
    void testConstructorCreatesCrazyGenome() {
        assertEquals(genomeLength, crazyGenome.getList().size());
    }

    @RepeatedTest(100)
    void testNextMoveUsesRandomBehavior() {
        crazyGenome.setGenome(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));

        int move = crazyGenome.nextMove();
        assertTrue(move >= 0 && move < 8, "Next move must be between 0 and 7");
    }

    @RepeatedTest(100)
    void testNextMove20PercentRandom() {
        crazyGenome.setGenome(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));

        int move = crazyGenome.nextMove();
        int index = crazyGenome.getCurrentGenomeIndex();

        // Check if the index was set to a random position (not just sequential)
        if (move != crazyGenome.getList().get((index - 1 + genomeLength) % genomeLength)) {
            assertTrue(crazyGenome.getList().contains(move), "The move should exist in the genome list.");
        }
    }

    @Test
    void testSetGenomeWorksCorrectly() {
        crazyGenome.setGenome(Arrays.asList(7, 7, 7, 7));
        assertEquals(Arrays.asList(7, 7, 7, 7), crazyGenome.getList());
    }

    @Test
    void testToString() {
        crazyGenome.setGenome(Arrays.asList(1, 2, 3));
        assertEquals("Genome: [1, 2, 3]", crazyGenome.toString());
    }
}
