package agh.ics.oop;

import java.util.List;

import agh.ics.oop.model.*;

public class World {
    public static void main(String[] args) {
        System.out.println("system wystartował");
        run(OptionsParser.parse(args));
        System.out.println("system zakończył działanie");

        Vector2d position1 = new Vector2d(1,2);
        System.out.println(position1);
        Vector2d position2 = new Vector2d(-2,1);
        System.out.println(position2);
        System.out.println(position1.add(position2));

        Animal animal = new Animal();
        System.out.println(animal);

        List<MoveDirection> directions = OptionsParser.parse(args);
        List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4));
//        RectangularMap map = new RectangularMap(10,10);
        GrassField map = new GrassField(10);
        Simulation simulation = new Simulation(map, positions, directions);
        System.out.println(map.toString());
        simulation.run();

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
