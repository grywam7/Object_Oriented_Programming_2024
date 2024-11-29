package agh.ics.oop;

import agh.ics.oop.model.map_elements.MoveDirection;

import java.util.List;
import java.util.ArrayList;

public class OptionsParser {

    public static List<MoveDirection> parse(String[] directions) {
        List<MoveDirection> moves = new ArrayList<>();
        for(String maybeMoveDirection : directions) {
            switch (maybeMoveDirection) {
                case "f" -> moves.add(MoveDirection.FORWARD);
                case "b" -> moves.add(MoveDirection.BACKWARD);
                case "r" -> moves.add(MoveDirection.RIGHT);
                case "l" -> moves.add(MoveDirection.LEFT);
                default -> throw new IllegalArgumentException(
                        maybeMoveDirection + " is not a legal move specification"
                );
            }
        }
        return moves;
    }
}
