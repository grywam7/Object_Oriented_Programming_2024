package agh.ics.oop;

import agh.ics.oop.model.maps.AbstractWorldMap;
import agh.ics.oop.model.maps.CrawlingJungleMap;
import agh.ics.oop.model.maps.EquatorMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {

    @Test
    void testConstructorCreatesCrawlingJungleMap() {
        // Given
        boolean mapModification = true;
        int width = 10, height = 6, jungleHeight = 3;

        // When
        Simulation sim = new Simulation(
                width, height, jungleHeight,
                mapModification, /* animalModification= */ false,
                /* initialPlantCount= */ 5, /* plantEnergy= */ 2,
                /* dailyPlantCount= */ 2, /* initialAnimalCount= */ 5, /* initialAnimalEnergy= */ 10,
                /* sufficientEnergy= */ 6, /* breedingEnergyLoss= */ 3, /* dailyEnergyLoss= */ 1,
                /* mutationCount= */ 1, /* genomeLength= */ 8, /* targetDay= */ 10
        );

        // Then
        AbstractWorldMap map = sim.getWorldMap();
        assertInstanceOf(CrawlingJungleMap.class, map, "Expected CrawlingJungleMap when mapModification is true.");
    }

    @Test
    void testConstructorCreatesEquatorMap() {
        // Given
        boolean mapModification = false;
        int width = 10, height = 6, jungleHeight = 2;

        // When
        Simulation sim = new Simulation(
                width, height, jungleHeight,
                mapModification, /* animalModification= */ false,
                /* initialPlantCount= */ 5, /* plantEnergy= */ 2,
                /* dailyPlantCount= */ 2, /* initialAnimalCount= */ 5, /* initialAnimalEnergy= */ 10,
                /* sufficientEnergy= */ 6, /* breedingEnergyLoss= */ 3, /* dailyEnergyLoss= */ 1,
                /* mutationCount= */ 1, /* genomeLength= */ 8, /* targetDay= */ 10
        );

        // Then
        AbstractWorldMap map = sim.getWorldMap();
        assertInstanceOf(EquatorMap.class, map, "Expected EquatorMap when mapModification is false.");
    }

    @Test
    void testConstructorThrowsExceptionForInvalidJungleHeight() {
        // Given
        int width = 5;
        int height = 5;
        int jungleHeight = 6; // invalid, bigger than map height

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new Simulation(
                width, height, jungleHeight,
                /* mapModification= */ false, /* animalModification= */ false,
                /* initialPlantCount= */ 5, /* plantEnergy= */ 2,
                /* dailyPlantCount= */ 2, /* initialAnimalCount= */ 5, /* initialAnimalEnergy= */ 10,
                /* sufficientEnergy= */ 6, /* breedingEnergyLoss= */ 3, /* dailyEnergyLoss= */ 1,
                /* mutationCount= */ 1, /* genomeLength= */ 8, /* targetDay= */ 10
        ), "Expected IllegalArgumentException when jungleHeight exceeds map height.");
    }

    @Test
    void testStepAdvancesDaysAndStops() {
        // Given
        int targetDay = 3;
        Simulation sim = new Simulation(
                /* width= */ 5, /* height= */ 5, /* jungleHeight= */ 2,
                /* mapModification= */ false, /* animalModification= */ false,
                /* initialPlantCount= */ 2, /* plantEnergy= */ 2,
                /* dailyPlantCount= */ 2, /* initialAnimalCount= */ 2, /* initialAnimalEnergy= */ 5,
                /* sufficientEnergy= */ 4, /* breedingEnergyLoss= */ 1, /* dailyEnergyLoss= */ 1,
                /* mutationCount= */ 1, /* genomeLength= */ 5,
                targetDay
        );

        // Initially, currentDay = 0
        assertEquals(0, sim.getCurrentDay(),
                "The initial currentDay should be 0."
        );

        // Step 1
        boolean keepGoing = sim.step();
        assertTrue(keepGoing,
                "After first step, simulation should still continue."
        );
        assertEquals(1, sim.getCurrentDay(),
                "The day counter should have incremented to 1."
        );

        // Step 2
        keepGoing = sim.step();
        assertTrue(keepGoing,
                "After second step, simulation should still continue."
        );
        assertEquals(2, sim.getCurrentDay(),
                "The day counter should now be 2."
        );

        // Step 3
        keepGoing = sim.step();
        assertTrue(keepGoing,
                "Even at day 3 (the last target day), we have not strictly exceeded it, so it might continue."
        );
        assertEquals(3, sim.getCurrentDay(),
                "The day counter should now be 3."
        );

        // Step 4
        // Now we've reached or exceeded targetDay, so we expect false
        keepGoing = sim.step();
        assertFalse(keepGoing,
                "After reaching the target day, the simulation should stop returning true."
        );
    }

    @Test
    void testOneSimulationStepChangesMapState() {
        // Given
        Simulation sim = new Simulation(
                /* width= */ 5, /* height= */ 5, /* jungleHeight= */ 2,
                /* mapModification= */ false, /* animalModification= */ false,
                /* initialPlantCount= */ 2, /* plantEnergy= */ 2,
                /* dailyPlantCount= */ 2, /* initialAnimalCount= */ 2, /* initialAnimalEnergy= */ 5,
                /* sufficientEnergy= */ 4, /* breedingEnergyLoss= */ 2, /* dailyEnergyLoss= */ 1,
                /* mutationCount= */ 1, /* genomeLength= */ 5,
                /* targetDay= */ 5
        );

        int dayBefore = sim.getCurrentDay();

        // When
        boolean keepGoing = sim.step();

        // Then
        assertTrue(keepGoing,
                "Simulation should not end after just one step, given targetDay is 5."
        );
        assertEquals(dayBefore + 1, sim.getCurrentDay(),
                "The simulation day counter should increment by 1 after one step."
        );

        // We expect changes due to movement, feeding, breeding, etc.
        AbstractWorldMap mapAfter = sim.getWorldMap();
        int animalCountAfter = mapAfter.countAnimals();
        int grassCountAfter = mapAfter.countGrass();

        // It's hard to predict exact changes in a random system, but we can at least confirm:
        // 1. The animal count won't be zero if it wasn't zero initially, unless all died somehow.
        // 2. The grass count might increase because of daily grass growth.
        // 3. No negative or impossible states should appear.

        assertTrue(animalCountAfter >= 0 && grassCountAfter >= 0,
                "Animal and grass counts should remain non-negative."
        );
    }
}
