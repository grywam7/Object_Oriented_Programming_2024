package agh.ics.oop.model.maps;

import java.util.*;
import agh.ics.oop.model.map_elements.*;

import static agh.ics.oop.model.map_elements.Animal.random;

public abstract class AbstractWorldMap implements WorldMap {
    private final List<MapChangeListener> observers = new ArrayList<>();
    protected final Map<Vector2d, HashSet<Animal>> animals = new HashMap<>();
    protected final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Boundary mapEdges;

    public Map<Vector2d, Set<Animal>> getAnimals() { return Collections.unmodifiableMap(animals); }
    public Map<Vector2d, Grass> getGrasses() { return Collections.unmodifiableMap(grasses); }
    public Boundary getMapEdges() { return mapEdges; }

    public void initializeAnimals(Integer amountOfAnimals, Integer animalsEnergy) {
        Random random = new Random();
        for(int i = 0; i < amountOfAnimals; i++) {
            int randomX = random.nextInt(mapEdges.width()) + mapEdges.bottomLeft().getX();
            int randomY = random.nextInt(mapEdges.height()) + mapEdges.bottomLeft().getY();
            placeAnimal(new Animal(new Vector2d(randomX, randomY), animalsEnergy)) ;
        }
    }

    public void initializeGrasses(Integer numberOfGrasses) {
        this.growGrass(numberOfGrasses);
    }

    public void growGrass(Integer numberOfGrasses) {
        for(int i = 0; i < numberOfGrasses; i++) {


        }
    }
    // trzymamy mape z pozycjami trawy -> jes trzeba wyrzucić
    // a) równik
    // dla wszystkie elementy równika dodajemy razy 4
    // b) pełzajaca dzungla
    // wszyscy sasiedzi traw dodajemy jako 4.
//    drzewo optymalne

    @Override
    public void placeAnimal(Animal animal, Vector2d newAnimalPosition) {
        // jeśli nie ma klucza w HashMapie to trzeba go zrobić
        if (!animals.containsKey(newAnimalPosition)) {
            animals.put(newAnimalPosition, new HashSet<>());
        }
        animals.get(newAnimalPosition).add(animal);
    }

    public void move(Animal animal, MapDirection direction) {
        Vector2d oldPosition = animal.getPosition();
        animal.move(direction.toInteger(), mapEdges);
        Vector2d newPosition = animal.getPosition();
        if (!oldPosition.equals(newPosition)) {
            this.placeAnimal(animal, newPosition);
        }
    }

    public Set<Vector2d> animalsPlaces() {
        return animals.keySet();
    }

    public Set<Vector2d> grassPlaces(){
        return grasses.keySet();
    }

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
