package agh.ics.oop.model.maps;

import agh.ics.oop.model.map_elements.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CrawlingJungleMapTest {

    private CrawlingJungleMap crawlingMap;

    @BeforeEach
    void setUp() {
        Boundary mapBoundary = new Boundary(new Vector2d(0, 0), new Vector2d(5, 5));
        crawlingMap = new CrawlingJungleMap(mapBoundary);
    }

    @Test
    void testGetPriorityPlacesEmptyMap() {
        // No grass at all => No priority places
        List<Vector2d> priorityPlaces = crawlingMap.getPriorityPlaces();
        assertTrue(priorityPlaces.isEmpty(),
                "When there's no grass, we expect no priority positions."
        );
    }

    @Test
    void testGetPriorityPlacesSingleGrassInMiddle() {
        // Place a single grass tile in the middle, say (3,3)
        Vector2d grassPos = new Vector2d(3, 3);
        crawlingMap.grasses.put(grassPos, new Grass(grassPos));

        // For the tile (3,3), the possible neighbors are:
        // (2,2), (2,3), (2,4), (3,2), (3,4), (4,2), (4,3), (4,4),
        // ignoring out-of-bounds & existing grass
        List<Vector2d> priorityPlaces = crawlingMap.getPriorityPlaces();

        // Let's define the expected neighbors
        Set<Vector2d> expectedNeighbors = Set.of(
                new Vector2d(2, 2), new Vector2d(2, 3), new Vector2d(2, 4),
                new Vector2d(3, 2), new Vector2d(3, 4),
                new Vector2d(4, 2), new Vector2d(4, 3), new Vector2d(4, 4)
        );

        assertEquals(expectedNeighbors.size(), priorityPlaces.size(),
                "There should be exactly 8 neighbors around the grass in the middle."
        );
        assertTrue(priorityPlaces.containsAll(expectedNeighbors),
                "All 8 surrounding tiles should be returned as priority places."
        );
    }

    @Test
    void testGetPriorityPlacesGrassNearEdge() {
        // Place grass tile at (0,0) - bottom-left corner
        Vector2d grassEdge = new Vector2d(0, 0);
        crawlingMap.grasses.put(grassEdge, new Grass(grassEdge));

        // The valid neighbors for (0,0) within (0..5, 0..5) are:
        // (0,1), (1,0), (1,1)
        // Because negative indices are out of map boundaries
        List<Vector2d> priorityPlaces = crawlingMap.getPriorityPlaces();

        Set<Vector2d> expected = Set.of(
                new Vector2d(0, 1),
                new Vector2d(1, 0),
                new Vector2d(1, 1)
        );

        assertEquals(expected.size(), priorityPlaces.size(),
                "Grass in the corner yields only 3 valid neighboring cells."
        );
        assertTrue(priorityPlaces.containsAll(expected),
                "Should include exactly (0,1), (1,0), (1,1)."
        );
    }

    @Test
    void testGetPriorityPlacesMultipleGrassWithOverlap() {
        // Place two grass tiles that share some neighbors
        Vector2d g1 = new Vector2d(2, 2);
        Vector2d g2 = new Vector2d(3, 2);
        crawlingMap.grasses.put(g1, new Grass(g1));
        crawlingMap.grasses.put(g2, new Grass(g2));

        // Combine all unique valid neighbors (excluding actual grass positions):

        // (1,1),(1,2),(1,3),
        // (2,1),(2,3),
        // (3,1),(3,3),
        // (4,1),(4,2),(4,3)

        List<Vector2d> priorityPlaces = crawlingMap.getPriorityPlaces();

        Set<Vector2d> expected = Set.of(
                new Vector2d(1, 1), new Vector2d(1, 2), new Vector2d(1, 3),
                new Vector2d(2, 1), new Vector2d(2, 3),
                new Vector2d(3, 1), new Vector2d(3, 3),
                new Vector2d(4, 1), new Vector2d(4, 2), new Vector2d(4, 3)
        );

        // Priority places is a List, but let's convert to a Set for easier comparison
        Set<Vector2d> actualSet = new HashSet<>(priorityPlaces);

        assertEquals(expected.size(), actualSet.size(),
                "We expect exactly 10 unique neighboring cells around both grass tiles."
        );
        assertEquals(expected, actualSet,
                "The result should match the combined unique neighbors (excluding grass)."
        );
    }

    @Test
    void testGetPriorityPlacesGrassFillsItsNeighbors() {
        // If another grass is placed on a potential neighbor,
        // that position is excluded from the priority list.
        Vector2d g1 = new Vector2d(2, 2);
        crawlingMap.grasses.put(g1, new Grass(g1));

        // Place grass at one of its would-be neighbors
        Vector2d g2 = new Vector2d(3, 2);
        crawlingMap.grasses.put(g2, new Grass(g2));

        // So (3,2) won't appear in the final list, but the others will
        List<Vector2d> priorityPlaces = crawlingMap.getPriorityPlaces();
        assertFalse(priorityPlaces.contains(g2),
                "Grass at (3,2) is not a valid priority place as it's already occupied."
        );
    }
}
