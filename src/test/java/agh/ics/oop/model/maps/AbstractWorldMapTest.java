package agh.ics.oop.model.maps;

import agh.ics.oop.model.map_elements.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AbstractWorldMapTest {

    /**
     * A minimal concrete subclass of AbstractWorldMap used solely for testing its
     * non-abstract behavior. The getPriorityPlaces() method returns an empty list
     * for simplicity.
     */
    static class TestWorldMap extends AbstractWorldMap {
        public TestWorldMap(Boundary mapEdges) {
            super(mapEdges);
        }

        @Override
        public List<Vector2d> getPriorityPlaces() {
            // For testing, this stub returns no priority places
            // (all new grass should be placed in non-priority fields, if available).
            return Collections.emptyList();
        }
    }

    private TestWorldMap testMap;

    @BeforeEach
    void setUp() {
        Boundary mapBoundary = new Boundary(new Vector2d(0, 0), new Vector2d(5, 5));
        testMap = new TestWorldMap(mapBoundary);
    }

    @Test
    void testInitializeAnimals() {
        int amountOfAnimals = 5;
        int animalsEnergy = 50;
        int genomeLength = 8;
        boolean animalModification = false; // normal Genome

        testMap.initializeAnimals(amountOfAnimals, animalsEnergy, genomeLength, animalModification);

        assertEquals(amountOfAnimals, testMap.countAnimals(),
                "The count of animals should match the amount specified during initialization."
        );

        // Spot-check an arbitrary animal for expected properties
        // Because the map placement is random, we only verify the range of positions and energy
        for (Set<Animal> animalSet : testMap.getAnimals().values()) {
            for (Animal animal : animalSet) {
                assertTrue(animal.getEnergy() == animalsEnergy,
                        "Each animal should have the specified starting energy."
                );
                assertNotNull(animal.getGenome(),
                        "Each animal should receive a valid Genome."
                );
            }
        }
    }

    @Test
    void testPerformMoves() {
        // Place a single animal with a fixed genome that always moves East (2)
        Animal animal = new Animal(new Vector2d(2, 2), 100);
        Genome fixedGenome = new Genome(1);
        fixedGenome.setGenome(Collections.singletonList(2)); // 2 = EAST rotation steps
        animal.setGenome(fixedGenome);
        testMap.placeAnimal(animal);

        // Perform movement
        testMap.performMoves();

        // Since the initial direction is NORTH, rotating 2 steps => EAST, so the new position is (3,2).
        List<Animal> animalsAtNewPos = testMap.getAnimalsAt(new Vector2d(3, 2));
        assertEquals(1, animalsAtNewPos.size(),
                "Exactly one animal should reside at (3,2) after performing moves."
        );
        assertTrue(animalsAtNewPos.contains(animal),
                "The animal originally at (2,2) should have moved to (3,2)."
        );
    }

    @Test
    void testRemoveDeadAnimals() {
        // Place two animals: one with positive energy, one with zero
        Animal aliveAnimal = new Animal(new Vector2d(2, 2), 10);
        Animal deadAnimal = new Animal(new Vector2d(3, 3), 0);

        testMap.placeAnimal(aliveAnimal);
        testMap.placeAnimal(deadAnimal);

        // Remove animals that have energy <= 0
        testMap.removeDeadAnimals();

        // Only the aliveAnimal should remain
        assertEquals(1, testMap.countAnimals(),
                "Exactly one animal should remain after removing the dead one."
        );
        assertTrue(testMap.getAnimalsAt(new Vector2d(2, 2)).contains(aliveAnimal),
                "The alive animal remains in the same position."
        );
        assertTrue(testMap.getAnimalsAt(new Vector2d(3, 3)).isEmpty(),
                "The dead animal should have been removed from (3,3)."
        );
    }

    @Test
    void testGrowGrass() {
        int grassCount = 3;
        testMap.growGrass(grassCount);

        // The map is 6x6 => 36 positions from (0,0) to (5,5).
        // With no animals to start, we expect exactly grassCount pieces of grass.
        assertEquals(grassCount, testMap.countGrass(),
                "The map should have exactly the specified number of grass after growGrass()."
        );
    }

    @Test
    void testIncrementAgeForAllAnimals() {
        Animal a1 = new Animal(new Vector2d(1, 1), 50);
        Animal a2 = new Animal(new Vector2d(2, 2), 50);
        testMap.placeAnimal(a1);
        testMap.placeAnimal(a2);

        // Age before increment
        assertEquals(0, a1.getAge());
        assertEquals(0, a2.getAge());

        testMap.incrementAgeForAllAnimals();

        // Age after increment
        assertEquals(1, a1.getAge());
        assertEquals(1, a2.getAge());
    }

    @Test
    void testFeedAnimals() {
        // Place grass and an animal in the same position
        Vector2d samePos = new Vector2d(2, 2);
        testMap.placeGrass(samePos);

        Animal hungryAnimal = new Animal(samePos, 10);
        testMap.placeAnimal(hungryAnimal);

        testMap.feedAnimals(5);

        // The animal should gain 5 energy, and grass should disappear from the map
        assertEquals(15, hungryAnimal.getEnergy(),
                "The animal should gain energy from the eaten grass."
        );
        assertEquals(0, testMap.countGrass(),
                "The grass is removed after being eaten."
        );
    }

    @Test
    void testApplyEnergyLossToAllAnimals() {
        Animal a1 = new Animal(new Vector2d(1, 1), 20);
        Animal a2 = new Animal(new Vector2d(2, 2), 30);
        testMap.placeAnimal(a1);
        testMap.placeAnimal(a2);

        int energyLoss = 5;
        testMap.applyEnergyLossToAllAnimals(energyLoss);

        assertEquals(15, a1.getEnergy(),
                "The first animal should lose the specified energy amount."
        );
        assertEquals(25, a2.getEnergy(),
                "The second animal should lose the specified energy amount."
        );
    }

    @Test
    void testPlaceGrass() {
        Vector2d pos = new Vector2d(4, 4);
        testMap.placeGrass(pos);

        assertEquals(1, testMap.countGrass(),
                "The map should contain exactly one piece of grass."
        );
        assertTrue(testMap.getGrasses().containsKey(pos),
                "Grass should be placed at the specified position."
        );
    }

    @Test
    void testCountAnimalsAndCountGrass() {
        // Two animals
        testMap.placeAnimal(new Animal(new Vector2d(1, 1), 10));
        testMap.placeAnimal(new Animal(new Vector2d(2, 2), 20));

        // Three pieces of grass
        testMap.placeGrass(new Vector2d(2, 2));
        testMap.placeGrass(new Vector2d(3, 3));
        testMap.placeGrass(new Vector2d(4, 4));

        assertEquals(2, testMap.countAnimals(),
                "The map should reflect the correct number of animals."
        );
        assertEquals(3, testMap.countGrass(),
                "The map should reflect the correct number of grass elements."
        );
    }

    @Test
    void testFreeFields() {
        // The boundary is (0,0) to (5,5) => 6x6 => 36 total positions
        assertEquals(36, testMap.freeFields(),
                "Initially, all 36 positions are free."
        );

        // Place one animal and one grass
        testMap.placeAnimal(new Animal(new Vector2d(1, 1), 10));
        testMap.placeGrass(new Vector2d(2, 2));

        // Now 2 positions are occupied
        assertEquals(34, testMap.freeFields(),
                "After placing one animal and one grass, 34 positions remain free."
        );
    }

    @Test
    void testAverageLifespan() {
        Animal a1 = new Animal(new Vector2d(2, 2), 1);
        Animal a2 = new Animal(new Vector2d(3, 3), 1);

        testMap.placeAnimal(a1);
        testMap.placeAnimal(a2);

        // Increment each animal's age to 5
        for (int i = 0; i < 5; i++) {
            testMap.incrementAgeForAllAnimals();
        }
        // Now each has age = 5 and energy = 1, let's kill them
        testMap.applyEnergyLossToAllAnimals(2);
        testMap.removeDeadAnimals();

        // Both had age 5 upon death => average should be 5.0
        assertEquals(5.0, testMap.averageLifespan(), 0.001,
                "The average lifespan should be the mean age of the dead animals."
        );
    }

    @Test
    void testCalculateAverageEnergy() {
        // No animals => average is 0
        assertEquals(0, testMap.calculateAverageEnergy(),
                "If there are no animals, the average energy should be 0."
        );

        // Place animals with known energies
        Animal a1 = new Animal(new Vector2d(1, 1), 10);
        Animal a2 = new Animal(new Vector2d(2, 2), 20);
        testMap.placeAnimal(a1);
        testMap.placeAnimal(a2);

        // Average energy = (10 + 20) / 2 = 15.0
        assertEquals(15.0, testMap.calculateAverageEnergy(), 0.0001,
                "The average energy should be computed correctly."
        );
    }

    @Test
    void testGetAnimalsAt() {
        Vector2d position = new Vector2d(2, 2);
        assertTrue(testMap.getAnimalsAt(position).isEmpty(),
                "No animals at this position initially."
        );

        Animal a1 = new Animal(position, 50);
        Animal a2 = new Animal(position, 70);
        testMap.placeAnimal(a1);
        testMap.placeAnimal(a2);

        List<Animal> animalsHere = testMap.getAnimalsAt(position);
        assertEquals(2, animalsHere.size(),
                "Exactly two animals should be present at this position."
        );
        assertTrue(animalsHere.contains(a1) && animalsHere.contains(a2),
                "The list should include both animals placed at this position."
        );
    }

    @Test
    void testPrintTopGenotypes() {
        // Create animals with identical genotypes
        Genome g1 = new Genome(3);
        g1.setGenome(Arrays.asList(0, 0, 0));

        Animal a1 = new Animal(new Vector2d(1, 1), 10);
        a1.setGenome(g1);
        Animal a2 = new Animal(new Vector2d(1, 2), 10);
        a2.setGenome(g1);

        // Create an animal with a different genotype
        Genome g2 = new Genome(3);
        g2.setGenome(Arrays.asList(1, 1, 1));
        Animal a3 = new Animal(new Vector2d(2, 2), 10);
        a3.setGenome(g2);

        testMap.placeAnimal(a1);
        testMap.placeAnimal(a2);
        testMap.placeAnimal(a3);

        List<Map.Entry<String, Integer>> topGenotypes = testMap.printTopGenotypes();

        // Expect something like:
        // ["Genome: [0, 0, 0]"=2, "Genome: [1, 1, 1]"=1]
        assertEquals(2, topGenotypes.size(),
                "There should be two distinct genotypes in the list."
        );
        // The first entry should have frequency 2 (the [0,0,0] genotype)
        assertEquals(2, topGenotypes.getFirst().getValue(),
                "The most frequent genotype should appear first."
        );
    }
}
