package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

import agh.ics.oop.model.map_elements.MoveDirection;
import agh.ics.oop.model.map_elements.Vector2d;
import agh.ics.oop.model.maps.ConsoleMapDisplay;
import agh.ics.oop.model.maps.GrassField;
import agh.ics.oop.model.maps.RectangularMap;
import javafx.application.Application;

public class World {
    public static void main(String[] args) {
//        System.out.println("system wystartował");

//        try {
//            final List<MoveDirection> directions = OptionsParser.parse(args);
//            final List<Simulation> simulations = new ArrayList<>();
//            List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4), new Vector2d(5,7));
//            ConsoleMapDisplay display = new ConsoleMapDisplay();
//
//            for(int i = 0; i<300; i++) {
//                GrassField grassField = new GrassField(10);;
//                RectangularMap rectangularMap = new RectangularMap(10, 10);
//
//                grassField.addObserver(display);
//                rectangularMap.addObserver(display);
//
//                simulations.add(new Simulation(grassField, positions, directions));
//                simulations.add(new Simulation(rectangularMap, positions, directions));
//            }
//
//            SimulationEngine engine = new SimulationEngine(simulations);
////            engine.awaitSimulationsEnd();
////            engine.runSync();
//            engine.runAsyncInThreadPool();
//
//        } catch (IllegalArgumentException e) {
//            System.err.println("Błąd: " + e.getMessage());
//            System.err.println("System zakończył działanie z błędem.");
//        }



        Application.launch(SimulationApp.class, args);

        System.out.println("system zakończył działanie");
    }

    private static void run(List<MoveDirection> directions) {
        System.out.println("Start");

        for (MoveDirection direction : directions) {
           String direction_output = switch (direction) {
               case FORWARD -> "do przodu,";
               case BACKWARD -> "do tyłu,";
               case RIGHT -> "w prawo,";
               case LEFT -> "w lewo,";
           };
           System.out.println("zwierzak idzie " + direction_output);
        }
        System.out.println("Stop");
    }
}
