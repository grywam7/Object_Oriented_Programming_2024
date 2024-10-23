package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

import java.util.Arrays;

public class OptionsParser {

    public static MoveDirection[] parse(String[] args) {
        MoveDirection[] _table = new MoveDirection[args.length];
        int j = 0;
        for(String arg : args) {
            switch (arg) {
                case "f":
                    _table[j] = MoveDirection.FORWARD;
                    j++;
                    break;
                case "b":
                    _table [j] = MoveDirection.BACKWARD;
                    j++;
                    break;
                case "r":
                    _table[j] = MoveDirection.RIGHT;
                    j++;
                    break;
                case "l":
                    _table[j] = MoveDirection.LEFT;
                    j++;
                    break;
            }
        }
        return Arrays.copyOfRange(_table, 0, j);
    }
}
