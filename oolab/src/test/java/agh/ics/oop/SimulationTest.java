package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.RectangularMap;
import agh.ics.oop.model.Animal;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class SimulationTest {

    private Simulation simulation;
    private RectangularMap map;

    @BeforeEach
    void setUp() {
        map = new RectangularMap(5, 5);
    }

    @Test
    void simulateBasicMoves (){
        //given
        List<MoveDirection> directions = OptionsParser.parse(new String[] {"f","r","b","l"});
        Vector2d startingVector = new Vector2d(2,2);
        List<Vector2d> positions = List.of(startingVector, startingVector, startingVector, startingVector);
        simulation = new Simulation(map, positions, directions);

        //when
        simulation.run();

        //then
        assertEquals("[N]",
            simulation.getAnimals().toString()
        );
    }

    @Test
    void simulationPenetrationTestOfNorthBorder(){
        //given
        List<MoveDirection> directions = OptionsParser.parse(new String[] {"f","f","f","f","f","f","f","f","f","f"});
        List<Vector2d> positions = List.of(new Vector2d(0,0));
        simulation = new Simulation(map, positions, directions);

        //when
        simulation.run();

        //then
        assertEquals("[N]", simulation.getAnimals().toString());
    }

    @Test
    void simulationPenetrationTestOfEastBorder(){
        //given
        List<MoveDirection> directions = OptionsParser.parse(new String[] {"r","f","f","f","f","f","f","f","f","f"});
        List<Vector2d> positions = List.of(new Vector2d(0,0));
        simulation = new Simulation(map, positions, directions);

        //when
        simulation.run();

        //then
        assertEquals("[E]", simulation.getAnimals().toString());
    }

    @Test
    void simulationPenetrationTestOfSouthBorder(){
        //given
        List<MoveDirection> directions = OptionsParser.parse(new String[] {"r","r","f","f","f","f","f","f","f","f"});
        List<Vector2d> positions = List.of(new Vector2d(0,4));
        simulation = new Simulation(map, positions, directions);

        //when
        simulation.run();

        //then
        assertEquals("[S]", simulation.getAnimals().toString());
    }

    @Test
    void simulationPenetrationTestOfWestBorder(){
        //given
        List<MoveDirection> directions = OptionsParser.parse(new String[] {"l","f","f","f","f","f","f","f","f","f"});
        List<Vector2d> positions = List.of(new Vector2d(4,0));
        simulation = new Simulation(map, positions, directions);

        //when
        simulation.run();

        //then
        assertEquals("[W]", simulation.getAnimals().toString());
    }

    @Test
    void simulateWalkingOnSquareOf2(){
        //given
        List<MoveDirection> directions = OptionsParser.parse(new String[] {
            "f","f","r",
            "f","f","l",
            "b","b","r",
            "b","b"});
        List<Vector2d> positions = List.of(new Vector2d(0,0));
        simulation = new Simulation(map, positions, directions);

        //when
        simulation.run();

        //then
        assertEquals("[E]", simulation.getAnimals().toString());
    }

    @Test
    void simulateMultipleAnimalsStepingForwardInDifferentDirections(){
        //given
        List<MoveDirection> directions = OptionsParser.parse(new String[] {
            "f","r","r","l",
            "f","f","r","f",
            "f","f","f","f"});
        Vector2d startingVector = new Vector2d(2,2);
        List<Vector2d> positions = List.of(startingVector, startingVector, startingVector, startingVector);
        simulation = new Simulation(map, positions, directions);

        //when
        simulation.run();

        //then
        assertEquals(
            "[S]",
            simulation.getAnimals().toString()
        );
    }

    //it's not a bug, it's a feature!
    @Test
    void simulateInvasionFromAnimalsOutsideTheMap(){
        //given
        List<MoveDirection> directions = OptionsParser.parse(new String[] {
            "f","r","r","l",
            "f","f","r","f",
            "f","f","f","f"});
        List<Vector2d> positions = List.of(
            new Vector2d(2, -1),
            new Vector2d(-1,2),
            new Vector2d(2,4),
            new Vector2d(4, 2)
        );
        simulation = new Simulation(map, positions, directions);

        //when
        simulation.run();

        //then - expect invaders to correctly move into map
        assertEquals(
                "[S, N]",
                simulation.getAnimals().toString()
        );
    }

    @Test
    void testAnimalPlacement() {
        // given
        List<Vector2d> positions = Arrays.asList(new Vector2d(2, 2), new Vector2d(3, 3));
        List<MoveDirection> directions = List.of(MoveDirection.FORWARD);
        simulation = new Simulation(map, positions, directions);

        //when
        List<Animal> animals = simulation.getAnimals();

        //then
        assertEquals(2, animals.size());
        assertTrue(map.isOccupied(new Vector2d(2, 2)));
        assertTrue(map.isOccupied(new Vector2d(3, 3)));
    }

    @Test
    void testMoveAndBoundaryCheck() {
        // given
        List<Vector2d> positions = List.of(new Vector2d(0, 0));
        List<MoveDirection> directions = OptionsParser.parse(new String[] {"f","f","f","f","f","r","f","f","f","f"});
        simulation = new Simulation(map, positions, directions);

        //when
        simulation.run();

        //then
        Animal animal = simulation.getAnimals().getFirst();
        assertTrue(animal.getPosition().precedes(new Vector2d(4, 4)) && animal.getPosition().follows(new Vector2d(0, 0)));
    }

    @Test
    void testCollisionPrevention() {
        //given
        List<Vector2d> positions = Arrays.asList(new Vector2d(2, 2), new Vector2d(3, 2));
        List<MoveDirection> directions = Arrays.asList(MoveDirection.FORWARD, MoveDirection.FORWARD);
        simulation = new Simulation(map, positions, directions);

        //when
        simulation.run();

        //then
        Animal animal1 = simulation.getAnimals().get(0);
        Animal animal2 = simulation.getAnimals().get(1);
        assertNotEquals(animal1.getPosition(), animal2.getPosition());
    }

    @Test
    void testMapStateAfterMovement() {
        //given
        List<Vector2d> positions = List.of(new Vector2d(1, 1), new Vector2d(4, 4));
        List<MoveDirection> directions = Arrays.asList(
                MoveDirection.FORWARD, MoveDirection.LEFT, MoveDirection.FORWARD, MoveDirection.FORWARD
        );
        simulation = new Simulation(map, positions, directions);

        //when
        simulation.run();

        //then
        Animal animal1 = simulation.getAnimals().get(0);
        Animal animal2 = simulation.getAnimals().get(1);

        assertEquals(new Vector2d(1, 3), animal1.getPosition());
        assertEquals(new Vector2d(3, 4), animal2.getPosition());
    }
}