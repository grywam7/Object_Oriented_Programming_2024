package agh.ics.oop.model.maps;

import java.util.*;

import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map_elements.*;
import agh.ics.oop.model.util.MapVisualizer;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
    protected final MapVisualizer visualizer = new MapVisualizer(this);
    private final List<MapChangeListener> observers = new ArrayList<>();

    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }
    public void removeObserver(MapChangeListener observer) {
        observers.remove(observer);
    }
    protected void mapChanged(String message) {
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this, message);
        }
    }

    Map<Vector2d, Animal> getAnimals() {
        return Collections.unmodifiableMap(animals);
    }

    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        Vector2d newAnimalPosition = animal.getPosition();
        if (canMoveTo(newAnimalPosition)) {
            animals.put(newAnimalPosition, animal);
            mapChanged("Animal placed at position: " + newAnimalPosition);
        } else {
            throw new IncorrectPositionException(newAnimalPosition);
        }
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        Vector2d oldPosition = animal.getPosition();
        animal.move(direction, this);
        Vector2d newPosition = animal.getPosition();
        if (!oldPosition.equals(newPosition)) {
            animals.remove(oldPosition);
            animals.put(newPosition, animal);
            mapChanged("Animal moved from " + oldPosition + " to " + newPosition);
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

    public String toString() {
        Boundary bounds = getCurrentBounds();
        return visualizer.draw(bounds.bottomLeft(), bounds.topRight());
    }

    @Override
    public Collection<WorldElement> getElements(){
        return Collections.unmodifiableCollection(animals.values());
    }

    @Override
    public abstract Boundary getCurrentBounds();

    @Override
    public String getID(){
        return  super.toString();
    }
}


// kto jest odpowiedzialny za rzucenie wyjątku PositionAlreadyOcuupiedException? dlaczego
//czy throw moze znajdować się bezpośrednio w bloku try?
// kto jest  odpoweiedzialny zza dodawanie ma[y do obserwatorow zwierzecia ldaczego
//czy warto stworzyc w zwoerzeciu metode ktora notufikuje wszystkich obserwatorów
//jeslo tal to z jaka sugnatura ( modyfikator dostepu  nazwa patamety
// cp przechowyje boudnry ? zwierzeia czy tylko ich ozycje
// kto przeohowuje boundry