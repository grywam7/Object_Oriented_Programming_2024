package agh.ics.oop.model;

public class Animal {
    private MapDirection direction;
    private Vector2d position;
    private final static Vector2d newAnimalStartingPosition = new Vector2d(2,2);

    public Animal(Vector2d position){
        this.direction = MapDirection.NORTH;
        this.position = position;
    }

    public Animal(){
        this(newAnimalStartingPosition);
    }

    public Vector2d getPosition() {
        return position;
    }

    public MapDirection getDirection(){
        return direction;
    }

    public String toString(){
        return direction.toShortString();
    }

    public Boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void move(MoveDirection moveDirection, MoveValidator validator) {
        switch (moveDirection) {
            case LEFT -> this.direction = this.direction.previous();
            case RIGHT -> this.direction = this.direction.next();
            case FORWARD -> moveForwardIfValid(this.direction, validator);
            case BACKWARD -> moveForwardIfValid(this.direction.next().next(), validator);
        }
    }

    private void moveForwardIfValid(MapDirection direction, MoveValidator validator) {
        Vector2d newPosition = position.add(direction.toUnitVector());
        if (validator.canMoveTo(newPosition)) {
            this.position = newPosition;
        }
    }
}
