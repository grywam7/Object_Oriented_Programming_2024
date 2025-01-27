package agh.ics.oop.model.maps;

import agh.ics.oop.model.map_elements.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class EquatorMapTest {

    private EquatorMap equatorMap;
    private Boundary mapEdges;

    @BeforeEach
    void setUp() {
        // Suppose our map extends from (0,0) to (5,5)
        mapEdges = new Boundary(new Vector2d(0, 0), new Vector2d(5, 5));

        // The "equator" is a smaller region from (2,1) to (3,3)
        Boundary equatorBoundary = new Boundary(new Vector2d(2, 1), new Vector2d(3, 3));

        // Instantiate EquatorMap
        equatorMap = new EquatorMap(mapEdges, equatorBoundary);
    }

    @Test
    void testGetPriorityPlacesNoGrass() {
        // No grass is placed in the equator region.
        // We expect all positions in that boundary to be returned.

        List<Vector2d> priorityPositions = equatorMap.getPriorityPlaces();

        // Equator boundary covers x=2..3, y=1..3 -> 2 * 3 = 6 positions:
        // (2,1),(2,2),(2,3),(3,1),(3,2),(3,3)
        assertEquals(6, priorityPositions.size(),
                "Should list all unoccupied positions within equator boundary."
        );

        // Let's confirm each position is indeed in the result
        Set<Vector2d> expected = Set.of(
                new Vector2d(2, 1),
                new Vector2d(2, 2),
                new Vector2d(2, 3),
                new Vector2d(3, 1),
                new Vector2d(3, 2),
                new Vector2d(3, 3)
        );
        assertTrue(priorityPositions.containsAll(expected),
                "Priority positions should contain the entire equator region with no grass."
        );
    }

    @Test
    void testGetPriorityPlacesSomeGrassPlaced() {
        // Place grass at (2,2) and (3,3)
        // We assume equatorMap.grasses is accessible, or we have a placeGrass() method
        equatorMap.grasses.put(new Vector2d(2, 2), new Grass(new Vector2d(2, 2)));
        equatorMap.grasses.put(new Vector2d(3, 3), new Grass(new Vector2d(3, 3)));

        // Now (2,2) and (3,3) should NOT appear in the priority list
        List<Vector2d> priorityPositions = equatorMap.getPriorityPlaces();

        // The equator region is x=2..3, y=1..3 -> 6 total spots
        // These two spots have grass, so we expect 4 remaining
        Set<Vector2d> expected = Set.of(
                new Vector2d(2, 1),
                new Vector2d(2, 3),
                new Vector2d(3, 1),
                new Vector2d(3, 2)
        );
        assertEquals(4, priorityPositions.size(),
                "Should list equator positions except the two with grass."
        );
        assertTrue(priorityPositions.containsAll(expected));
        assertFalse(priorityPositions.contains(new Vector2d(2, 2)));
        assertFalse(priorityPositions.contains(new Vector2d(3, 3)));
    }

    @Test
    void testEquatorBoundaryIsOutsideMap() {
        // Let’s define an equator that’s fully outside the main map
        Boundary outOfBoundsEquator = new Boundary(new Vector2d(10, 10), new Vector2d(12, 12));
        EquatorMap weirdMap = new EquatorMap(mapEdges, outOfBoundsEquator);

        List<Vector2d> priorityPositions = weirdMap.getPriorityPlaces();
        assertFalse(priorityPositions.isEmpty(),
                "If the equator doesn't overlap the map edges, we expect positive positions."
        );
    }

    @Test
    void testGetPriorityPlacesDoubleCheckMapEdgesIrrelevant() {
        // Place grass outside the equator boundary to ensure it doesn't affect equator results
        equatorMap.grasses.put(new Vector2d(0, 0), new Grass(new Vector2d(0, 0))); // outside equator
        equatorMap.grasses.put(new Vector2d(4, 4), new Grass(new Vector2d(4, 4))); // outside equator

        List<Vector2d> priorityPositions = equatorMap.getPriorityPlaces();

        // The equator region is x=2..3, y=1..3 -> 6 total spots
        assertEquals(6, priorityPositions.size(),
                "All equator positions remain valid since the grass is outside the equator boundary."
        );

        // Double-check the actual positions returned
        Set<Vector2d> expected = Set.of(
                new Vector2d(2, 1),
                new Vector2d(2, 2),
                new Vector2d(2, 3),
                new Vector2d(3, 1),
                new Vector2d(3, 2),
                new Vector2d(3, 3)
        );
        assertTrue(priorityPositions.containsAll(expected));
    }
}
