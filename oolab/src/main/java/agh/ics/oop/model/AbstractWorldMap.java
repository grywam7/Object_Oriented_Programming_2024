package agh.ics.oop.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import agh.ics.oop.model.util.MapVisualizer;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
    protected final MapVisualizer visualizer = new MapVisualizer(this);

    Map<Vector2d, Animal> getAnimals() {
        return Collections.unmodifiableMap(animals);
    }

    @Override
    public boolean place(Animal animal) {
        Vector2d newAnimalPosition = animal.getPosition();
        if (canMoveTo(newAnimalPosition)) {
            animals.put(newAnimalPosition, animal);
            return true;
        }
        return false;
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        Vector2d oldPosition = animal.getPosition();
        animal.move(direction, this);
        Vector2d newPosition = animal.getPosition();
        if (!oldPosition.equals(newPosition)) {
            animals.remove(oldPosition);
            animals.put(newPosition, animal);
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return animals.get(position);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !isOccupied(position);
    }

    @Override
    public abstract String toString();

    @Override
    public Map<Vector2d, WorldElement> getElements(){
        return Collections.unmodifiableMap(animals);
    }
}
