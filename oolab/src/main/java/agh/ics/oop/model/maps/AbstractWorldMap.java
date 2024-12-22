package agh.ics.oop.model.maps;

import java.util.*;

import agh.ics.oop.model.map_elements.*;
import agh.ics.oop.model.util.MapVisualizer;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
    protected final MapVisualizer visualizer = new MapVisualizer(this);
    private final List<MapChangeListener> observers = new ArrayList<>();
    private final Boundary mapEdges;
    private final Boundary jungleEdges;
    private final Boundary equatorEdges;

// dodac jakies sensowne dane do tych powyzej


    Map<Vector2d, Animal> getAnimals() {
        return Collections.unmodifiableMap(animals);
    }

    @Override
    public void place(Animal animal) {
//do zmiany
        //        Vector2d newAnimalPosition = animal.getPosition();
//        if (canMoveTo(newAnimalPosition)) {
//            animals.put(newAnimalPosition, animal);
//            mapChanged("Animal placed at position: " + newAnimalPosition);
//        } else {
//            throw new IncorrectPositionException(newAnimalPosition);
//        }
    }

    public void move(Animal animal, MoveDirection direction) {
// inaczej walidacja, zwirze na rogach ma sie odwracac

//                Vector2d oldPosition = animal.getPosition();
//        animal.move(direction, this);
//        Vector2d newPosition = animal.getPosition();
//        if (!oldPosition.equals(newPosition)) {
//            animals.remove(oldPosition);
//            animals.put(newPosition, animal);
//            mapChanged("Animal moved from " + oldPosition + " to " + newPosition);
        }
    }
// metoda do wypisania pol ze zwerzetami

// sprawdzamy czy na polu jest trawa, jesli jest to ustalamy kto ja zje
// potem sprawdzamy czy mozna sie rozmanaza, i ustalamy kto z kim
// jesli nie bylo konfliktu w jedzeniu to jest 1 zwierzak czyli nie rozmnozy sie

// czy jest trawa?

// czy sa zwierzeta z energia do rozmanazania

    @Override
    public WorldElement grassesAt(Vector2d position) {
        return grasses.get(position);
    }

    @Override
    public abstract Boundary getMapBounds();

// dodać getery do pozostalych boundry

    @Override
    public String getID(){
        return super.toString();
    }
}
