package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {

    @Test
    void simulateBasicMoves (){
        //given
        List<MoveDirection> directions = OptionsParser.parse(new String[] {"f","r","b","l"});
        Vector2d startingVector = new Vector2d(2,2);
        List<Vector2d> positions = List.of(startingVector, startingVector, startingVector, startingVector);
        Simulation simulation = new Simulation(positions, directions);

        //when
        simulation.run();

        //then
        assertEquals("[Północ, (2,3), Wschód, (2,2), Północ, (2,1), Zachód, (2,2)]",
            simulation.getAnimals().toString()
        );
    }

    @Test
    void simulationPenetrationTestOfNorthBorder(){
        //given
        List<MoveDirection> directions = OptionsParser.parse(new String[] {"f","f","f","f","f","f","f","f","f","f"});
        List<Vector2d> positions = List.of(new Vector2d(0,0));
        Simulation simulation = new Simulation(positions, directions);

        //when
        simulation.run();

        //then
        assertEquals("[Północ, (0,4)]", simulation.getAnimals().toString());
    }

    @Test
    void simulationPenetrationTestOfEastBorder(){
        //given
        List<MoveDirection> directions = OptionsParser.parse(new String[] {"r","f","f","f","f","f","f","f","f","f"});
        List<Vector2d> positions = List.of(new Vector2d(0,0));
        Simulation simulation = new Simulation(positions, directions);

        //when
        simulation.run();

        //then
        assertEquals("[Wschód, (4,0)]", simulation.getAnimals().toString());
    }

    @Test
    void simulationPenetrationTestOfSouthBorder(){
        //given
        List<MoveDirection> directions = OptionsParser.parse(new String[] {"r","r","f","f","f","f","f","f","f","f"});
        List<Vector2d> positions = List.of(new Vector2d(0,4));
        Simulation simulation = new Simulation(positions, directions);

        //when
        simulation.run();

        //then
        assertEquals("[Południe, (0,0)]", simulation.getAnimals().toString());
    }

    @Test
    void simulationPenetrationTestOfWestBorder(){
        //given
        List<MoveDirection> directions = OptionsParser.parse(new String[] {"l","f","f","f","f","f","f","f","f","f"});
        List<Vector2d> positions = List.of(new Vector2d(4,0));
        Simulation simulation = new Simulation(positions, directions);

        //when
        simulation.run();

        //then
        assertEquals("[Zachód, (0,0)]", simulation.getAnimals().toString());
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
        Simulation simulation = new Simulation(positions, directions);

        //when
        simulation.run();

        //then
        assertEquals("[Wschód, (0,0)]", simulation.getAnimals().toString());
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
        Simulation simulation = new Simulation(positions, directions);

        //when
        simulation.run();

        //then
        assertEquals(
            "[Północ, (2,4), Wschód, (4,2), Południe, (2,1), Zachód, (0,2)]",
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
            new Vector2d(2,5),
            new Vector2d(5, 2)
        );
        Simulation simulation = new Simulation(positions, directions);

        //when
        simulation.run();

        //then - expect invaders to correctly move into map
        assertEquals(
                "[Północ, (2,2), Wschód, (1,2), Południe, (2,4), Zachód, (3,2)]",
                simulation.getAnimals().toString()
        );
    }
}