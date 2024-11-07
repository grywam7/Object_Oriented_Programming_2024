package agh.ics.oop.model;

import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;

import java.util.Map;

public class Animal {
    private MapDirection direction;
    private Vector2d position;
    private final static Vector2d vector0x0 = new Vector2d(0,0);
    private final static Vector2d vector2x2 = new Vector2d(2,2);
    private final static Vector2d vector4x4 = new Vector2d(4,4);

    public Animal(Vector2d position){
        this.direction = MapDirection.NORTH;
        this.position = position;
    }

    public Animal(){
        this(vector2x2);
    }

    public Vector2d getPosition() {
        return position;
    }

    public MapDirection getDirection(){
        return direction;
    }

    public String toString(){
        return String.format("%s, %s",direction.toString(),position.toString());
    }

    public Boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void move(MoveDirection direction) {
        switch (direction) {
            case LEFT -> this.direction = this.direction.previous();
            case RIGHT -> this.direction = this.direction.next();
            case FORWARD -> moveForward(this.direction);
            case BACKWARD -> moveForward(this.direction.next().next());
        }
    }

    private void moveForward(MapDirection direction){
        Vector2d unverifiedPosition = position.add(direction.toUnitVector());
        if(unverifiedPosition.precedes(vector4x4) && unverifiedPosition.follows(vector0x0)){
            this.position = unverifiedPosition;
        };
    }


//co powinno być finalne a co nie?
// kiedy dodać getter i setter?
//    czy getter jest zawsze bezpieczny
// czy atrybuty klasy i parametry metod lepiej jest deklarować ArrayList czy List
//    typizujemy jako List i mona przypisac arraylist
//    czy jeden konstruktor moe wywołąć drugi? czy powinien? tak przeciąenie
//    czy metody są do czegoś przydatne (wykorzystać pewnie trzeba)
}
