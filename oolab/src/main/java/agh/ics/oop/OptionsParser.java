package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

import java.util.Arrays;

public class OptionsParser {

    public static MoveDirection[] parse(String[] directions) {
        MoveDirection[] table = new MoveDirection[directions.length];
        int j = 0;
        for(String maybe_move_direction : directions) {
            switch (maybe_move_direction) {
                case "f":
                    table[j++] = MoveDirection.FORWARD;
                    break;
                case "b":
                    table [j++] = MoveDirection.BACKWARD;
                    break;
                case "r":
                    table[j++] = MoveDirection.RIGHT;
                    break;
                case "l":
                    table[j++] = MoveDirection.LEFT;
                    break;
            }
        }
        return Arrays.copyOfRange(table, 0, j);
    }
}
