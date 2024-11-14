package agh.ics.oop;

import agh.ics.oop.model.*; //to pewnie jest źle, ale w sumie to czemu?

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
            if (map.place(animal)) {
                animals.add(animal);
            } else {
                System.out.printf("Zwierzak na pozycji %s, ryczy: \"WON ZAJENTE!\"\n", onePosition);
            }
        }
        this.animals = animals;
        this.directions = directions;
    }

    public void run(){
        int amountOfAnimals = animals.size();
        for(int index = 0; index < directions.size(); index++){ // czemu zmiana na index z i? bo i%amountOfAnimals juz 3 raz mysle ze jest zle
            Animal animal = animals.get(index % amountOfAnimals); // % <-> modulo
            map.move(animal, directions.get(index));
            System.out.printf(map.toString());
        }
    }

    List<Animal> getAnimals() {
        return Collections.unmodifiableList(animals);
    }
}
