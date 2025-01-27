package agh.ics.oop;

import agh.ics.oop.model.map_elements.Animal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConflictsTest {

    private Conflicts conflicts;

    private Animal createAnimal(int energy, int age, int childrenCount) {
        Animal a = new Animal(null, energy);
        for (int i = 0; i < age; i++) {
            a.incrementAge();
        }
        return a;
    }

    @BeforeEach
    void setUp() {
        conflicts = new Conflicts();
    }

    @Test
    void testResolveFoodConflictEmpty() {
        List<Animal> animals = new ArrayList<>();
        assertNull(conflicts.resolveFoodConflict(animals),
                "Expected null if no animals are present."
        );
    }

    @Test
    void testResolveReproductionConflictFewerThanTwo() {
        List<Animal> animals0 = new ArrayList<>();
        assertTrue(conflicts.resolveReproductionConflict(animals0).isEmpty());

        List<Animal> animals1 = List.of(createAnimal(10, 5, 2));
        assertTrue(conflicts.resolveReproductionConflict(animals1).isEmpty(),
                "Should return an empty list if only one animal is present."
        );
    }

    @RepeatedTest(10)
    void testResolveReproductionConflictAllTie() {
        Animal a1 = createAnimal(10, 2, 0);
        Animal a2 = createAnimal(10, 1, 5);
        Animal a3 = createAnimal(10, 5, 2);
        List<Animal> animals = new ArrayList<>(List.of(a1, a2, a3));

        List<Animal> result = conflicts.resolveReproductionConflict(animals);

        assertEquals(2, result.size(), "Should choose exactly two animals in a tie.");
        assertTrue(result.contains(a1) || result.contains(a2) || result.contains(a3));
    }

    @RepeatedTest(5)
    void testResolveReproductionConflictPartialTie() {
        Animal a1 = createAnimal(10, 5, 2);
        Animal a2 = createAnimal(10, 2, 1);
        Animal a3 = createAnimal(8, 3, 0);
        Animal a4 = createAnimal(7, 1, 4);
        List<Animal> animals = new ArrayList<>(List.of(a1, a2, a3, a4));

        List<Animal> result = conflicts.resolveReproductionConflict(animals);

        assertEquals(2, result.size());
        assertTrue(result.contains(a1));
        assertTrue(result.contains(a2));
    }

    @Test
    void testResolveReproductionConflictSingleTopCandidateFallback() {
        Animal a1 = createAnimal(10, 2, 0);
        Animal a2 = createAnimal(8, 3, 1);
        Animal a3 = createAnimal(2, 1, 0);
        List<Animal> animals = new ArrayList<>(List.of(a1, a2, a3));

        List<Animal> result = conflicts.resolveReproductionConflict(animals);

        assertEquals(2, result.size(), "Should fall back to the first two after sorting if only one top candidate.");
        assertTrue(result.contains(a1));
        assertTrue(result.contains(a2));
        assertFalse(result.contains(a3));
    }
}
