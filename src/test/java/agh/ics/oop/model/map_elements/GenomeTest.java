package agh.ics.oop.model.map_elements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenomeTest {

    private Genome genome;
    private int genomeLength;

    @BeforeEach
    void setUp() {
        genomeLength = 8;
        genome = new Genome(genomeLength);
    }

    @Test
    void testConstructorCreatesGenomeOfCorrectLength() {
        assertEquals(genomeLength, genome.getList().size());
    }

    @Test
    void testNextMoveCyclesThroughGenome() {
        genome.setGenome(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
        assertEquals(0, genome.nextMove());
        assertEquals(1, genome.nextMove());
        assertEquals(2, genome.nextMove());
        assertEquals(3, genome.nextMove());
        assertEquals(4, genome.nextMove());
    }

    @Test
    void testNextMoveCyclesBackToStart() {
        genome.setGenome(Arrays.asList(0, 1, 2));
        assertEquals(0, genome.nextMove());
        assertEquals(1, genome.nextMove());
        assertEquals(2, genome.nextMove());
        assertEquals(0, genome.nextMove()); // Back to the start
    }

    @Test
    void testSetGenomeChangesList() {
        List<Integer> newGenome = Arrays.asList(3, 3, 3, 3);
        genome.setGenome(newGenome);
        assertEquals(newGenome, genome.getList());
    }

    @Test
    void testToString() {
        genome.setGenome(Arrays.asList(0, 1, 2, 3));
        assertEquals("Genome: [0, 1, 2, 3]", genome.toString());
    }
}
