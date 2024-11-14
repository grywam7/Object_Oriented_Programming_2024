package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.HashMap;
import java.util.Map;

public class RectangularMap implements WorldMap {
    private Map<Vector2d, Animal> animals = new HashMap<>();
    private final Vector2d rightTopMapCorner;
    private final Vector2d leftBottomMapCorner = new Vector2d(0,0);


    public RectangularMap(int width, int height){
        rightTopMapCorner = new Vector2d(width-1, height-1);
    }

    @Override
    public boolean isOccupied(Vector2d position){
        return animals.containsKey(position);
    }

    @Override
    public Animal objectAt(Vector2d position) {
        return animals.get(position);
    }

    @Override
    public boolean canMoveTo(Vector2d vectorToCheck) {
        return vectorToCheck.precedes(rightTopMapCorner) && vectorToCheck.follows(leftBottomMapCorner) && !isOccupied(vectorToCheck);
    }

    @Override
    public boolean place(Animal animal){
        Vector2d newAnimalPosition = animal.getPosition();

        if( canMoveTo(newAnimalPosition) ){
            animals.put(animal.getPosition(),animal);
            return true;
        }
        return false;
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        Vector2d oldPosition = animal.getPosition();
        MapDirection oldMapDirection = animal.getDirection();
        animal.move(direction, this);
        if(oldPosition.equals(animal.getPosition()) || oldMapDirection.equals(animal.getDirection())){
            animals.remove(oldPosition);
            animals.put(animal.getPosition(), animal);
        }
    }

    public String toString() {
        MapVisualizer visualizer = new MapVisualizer(this);
        return visualizer.draw(leftBottomMapCorner, rightTopMapCorner);
    }
}
