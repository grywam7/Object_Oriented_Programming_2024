package agh.ics.oop.model.map_elements;

public class Animal implements WorldElement {
    private MapDirection direction;
    private Vector2d position;
    private final static Vector2d newAnimalStartingPosition = new Vector2d(2,2);

//    liczba dzieci

    public Animal(Vector2d position){
        this.direction = MapDirection.NORTH;
        this.position = position;
    }

    public Animal(){
        this(newAnimalStartingPosition);
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public MapDirection getDirection(){
        return direction;
    }

    @Override
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
            case FORWARD -> moveByVectorIfValid(this.direction.toUnitVector(), validator);
            case BACKWARD -> moveByVectorIfValid(this.direction.toUnitVector().opposite(), validator);
        }
    }

    private void moveByVectorIfValid(Vector2d vector, MoveValidator validator) {
        Vector2d newPosition = position.add(vector);
        if (validator.canMoveTo(newPosition)) {
            this.position = newPosition;
        }
    }
}
