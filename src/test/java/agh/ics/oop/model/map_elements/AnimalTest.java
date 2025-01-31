package agh.ics.oop.model.map_elements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    private Animal animal;
    private Genome testGenome;
    private int startingEnergy;
    private Boundary boundary;

    @BeforeEach
    void setUp() {
        startingEnergy = 100;
        Vector2d initialPosition = new Vector2d(5, 5);
        animal = new Animal(initialPosition, startingEnergy);

        // Use a fixed-size genome for testing
        testGenome = new Genome(8);
        testGenome.setGenome(java.util.Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));

        // Provide the Animal a genome so getCurrentGenomeMove can work
        animal.setGenome(testGenome);

        // Let’s define a simple boundary from (0,0) to (10,10)
        boundary = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));
    }

    @Test
    void testConstructorInitialValues() {
        assertEquals(5, animal.getPosition().getX());
        assertEquals(5, animal.getPosition().getY());
        assertEquals(100, animal.getEnergy());
        assertEquals(0, animal.getAge());
        assertEquals(0, animal.getChildrenCount());
        assertEquals(0, animal.getPlantsEatenCount());
    }

    @Test
    void testSetGenome() {
        animal.setGenome(testGenome);
        assertNotNull(animal.getGenome());
        assertEquals(8, animal.getGenome().getList().size());
    }

    @Test
    void testEat() {
        int plantEnergyValue = 20;
        animal.eat(plantEnergyValue);

        assertEquals(startingEnergy + plantEnergyValue, animal.getEnergy());
        assertEquals(1, animal.getPlantsEatenCount());
    }

    @Test
    void testEnergyLoss() {
        int loss = 10;
        animal.energyLoss(loss);

        assertEquals(startingEnergy - loss, animal.getEnergy());
    }

    @Test
    void testIncrementAge() {
        assertEquals(0, animal.getAge());
        animal.incrementAge();
        assertEquals(1, animal.getAge());
    }

    @Test
    void testMoveWithinBounds() {
        // direction defaults to NORTH, so we’ll move “up”
        animal.move(0, boundary);
        // No rotation, so direction stays NORTH, position should move from (5,5) to (5,6)
        assertEquals(5, animal.getPosition().getX());
        assertEquals(6, animal.getPosition().getY());
    }

    @Test
    void testMoveOutOfBoundsWrapX() {
        // Force direction to WEST (that’s 6 steps left from NORTH in MapDirection if 0 = NORTH)
        animal.move(6, boundary);
        // Now Animal’s direction is WEST, position is still (5,5).
        // Move forward
        animal.move(0, boundary);
        // Position is now (4,5). Let’s do it enough times to cross boundary
        for (int i = 0; i < 5; i++) {
            animal.move(0, boundary);
        }
        // This last move should wrap x around from 0 -> 10
        animal.move(0, boundary);

        assertEquals(8, animal.getPosition().getX());
        assertEquals(5, animal.getPosition().getY());
    }

    @Test
    void testMoveOutOfBoundsFlipYDirection() {
        // Force direction to NORTH (by default it’s already NORTH, but let’s be explicit)
        animal.rotate(0);

        // Move until we pass the top boundary
        for (int i = 0; i < 6; i++) {
            animal.move(0, boundary);
        }
        // If we started at y=5 and the boundary top is y=10,
        // after 5 moves we should be at y=10. One more move -> out of bounds => flips direction
        assertEquals(MapDirection.SOUTH, getAnimalDirection(animal));
        assertEquals(11, animal.getPosition().getY());
    }

    @Test
    void testRotate() {
        // 0 = NORTH, 2 = EAST, 4 = SOUTH, 6 = WEST, 7= NW, etc.
        assertEquals(MapDirection.NORTH, getAnimalDirection(animal));

        // Rotate by 2 -> EAST
        animal.rotate(2);
        assertEquals(MapDirection.EAST, getAnimalDirection(animal));

        // Rotate by 2 -> SOUTH
        animal.rotate(2);
        assertEquals(MapDirection.SOUTH, getAnimalDirection(animal));

        // Rotate by 2 -> WEST
        animal.rotate(2);
        assertEquals(MapDirection.WEST, getAnimalDirection(animal));

        // Rotate by 2 -> NORTH again
        animal.rotate(2);
        assertEquals(MapDirection.NORTH, getAnimalDirection(animal));
    }

    @Test
    void testCopulate() {
        Animal partner = new Animal(new Vector2d(5, 5), 80);
        partner.setGenome(testGenome); // set the same test genome for simplicity

        int energyLoss = 10;
        int mutationCount = 2;

        Animal child = animal.copulate(partner, energyLoss, mutationCount);

        // Check energy
        assertEquals(startingEnergy - energyLoss, animal.getEnergy());
        assertEquals(80 - energyLoss, partner.getEnergy());
        assertEquals(energyLoss * 2, child.getEnergy(), "Child's energy should be sum of energyLosses from both parents.");

        // Check parent’s children count
        assertEquals(1, animal.getChildrenCount());
        assertEquals(1, partner.getChildrenCount());

        // Check descendant counts
        assertEquals(1, animal.getDescendantCount());
        assertEquals(1, partner.getDescendantCount());

        // Check child’s genome length
        assertNotNull(child.getGenome());
        assertEquals(testGenome.getList().size(), child.getGenome().getList().size(),
                "Child's genome should match parent's genome length.");

        // Check if some mutations occurred
        int differences = 0;
        for (int i = 0; i < testGenome.getList().size(); i++) {
            if (!testGenome.getList().get(i).equals(child.getGenome().getList().get(i))) {
                differences++;
            }
        }

        assertTrue(differences <= 2);
    }

    @Test
    void testGetCurrentGenomeMove() {
        // With the test genome [0,1,2,3,4,5,6,7], the first call is 0, then 1, ...
        int move1 = animal.getCurrentGenomeMove();
        int move2 = animal.getCurrentGenomeMove();
        int move3 = animal.getCurrentGenomeMove();

        assertEquals(0, move1);
        assertEquals(1, move2);
        assertEquals(2, move3);
    }

    @Test
    void testToString() {
        // By default, direction is NORTH, which toShortString might be "N"
        assertEquals("N", animal.toString());
        // After rotating to the East
        animal.rotate(2);
        assertEquals("E", animal.toString());
    }

    // Utility method to get current direction from Animal
    private MapDirection getAnimalDirection(Animal a) {

        String shortDir = a.toString();
        for (MapDirection md : MapDirection.values()) {
            if (md.toShortString().equals(shortDir)) {
                return md;
            }
        }
        return null;
    }
}
