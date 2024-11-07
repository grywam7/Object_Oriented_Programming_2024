package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private final List<Animal> animals;
    private final List<MoveDirection> directions;

    public Simulation(List<Vector2d> positions,List<MoveDirection> directions){
        List<Animal> animals = new ArrayList<>();
        for(Vector2d onePosition : positions){
            animals.add(new Animal(onePosition));
        }
        this.animals = animals;
        this.directions = directions;
    }

    public void run(){
        int amountOfAnimals = animals.size();
        for(int i = 0; i < directions.size(); i++){
            Animal animal = animals.get(i%amountOfAnimals);
            animal.move(directions.get(i));
            System.out.printf("Zwierzę %d : %s%n",i%amountOfAnimals,animal.getPosition());
        }

    }

    public List<Animal> getAnimals() {
        return animals;
    }
}
