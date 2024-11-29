package agh.ics.oop;

import agh.ics.oop.model.*; //to pewnie jest źle, ale w sumie to czemu?
import agh.ics.oop.model.exceptions.*;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private final List<Animal> animals;
    private final List<MoveDirection> directions;
    private final WorldMap map;

    public Simulation(WorldMap map, List<Vector2d> positions,List<MoveDirection> directions){
        this.map = map;
        List<Animal> animals = new ArrayList<>();
        for(Vector2d onePosition : positions){
            Animal animal = new Animal(onePosition);
            try {
                map.place(animal);
                animals.add(animal);
            } catch (IncorrectPositionException e) {
                System.out.printf("Zwierzak na pozycji %s, ryczy: \"WON ZAJENTE!\"\n (%s)\n", onePosition, e.getMessage());
            }
        }
        this.animals = animals;
        this.directions = directions;
    }

    public void run(){
        int amountOfAnimals = animals.size();
        for(int index = 0; index < directions.size(); index++){
            Animal animal = animals.get(index % amountOfAnimals); // % <-> modulo
            map.move(animal, directions.get(index));
        }
    }

    List<Animal> getAnimals() {
        return Collections.unmodifiableList(animals);
    }
}
