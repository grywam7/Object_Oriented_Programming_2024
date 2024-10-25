package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

public class World {
    public static void main(String[] args) {
        System.out.println("system wystartował");
        run(OptionsParser.parse(args));
        System.out.println("system zakończył działanie");
    }

    public static void run(MoveDirection[] directions) {
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
