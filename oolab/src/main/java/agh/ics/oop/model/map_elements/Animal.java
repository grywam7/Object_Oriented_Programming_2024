package agh.ics.oop.model.map_elements;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal implements WorldElement {
    private MapDirection direction;
    private Vector2d position;
    private int energy;
    private int childrenCount;
    private int age;
    private int plantsEatenCount;
    private int birthDay;
    private int descendantCount;
    private Animal parent1;
    private Animal parent2;
    private static final Random random = new Random();
    private static final int GENOME_LENGTH = 32; // Przykładowa długość genomu
    private static final int MUTATION_COUNT = 2; // Liczba mutacji dla potomka
    List<Integer> genome = new ArrayList<>();


    public Animal(Vector2d position){
        this.direction = MapDirection.NORTH;
        this.position = position;
    }

    /*
    public Animal(){
        this(newAnimalStartingPosition);
    }*/

    public void eat(int plantEnergyValue){
        energy += plantEnergyValue;
        plantsEatenCount += 1;
    }

    public void setParents(Animal parent1, Animal parent2){
        this.parent1 = parent1;
        this.parent2 = parent2;
    }

    public Animal copulate(Animal secondAnimal, int energyLoss) {
        // Sprawdzenie, czy oba zwierzęta mają wystarczającą energię do rozmnażania będzie w simulation

        // Obliczanie energii potomka i odejmowanie jej od rodziców
        int childEnergy = energyLoss * 2;
        this.energy -= energyLoss;
        secondAnimal.energy -= energyLoss;

        // Tworzenie nowego zwierzęcia
        Animal child = new Animal(this.position);
        child.energy = childEnergy;
        child.age = 0;
        child.plantsEatenCount = 0;
        child.childrenCount = 0;
        child.setParents(this, secondAnimal);

        // Aktualizacja liczby dzieci u rodziców
        this.childrenCount++;
        secondAnimal.childrenCount++;

        // Krzyżowanie genomu
        List<Integer> childGenome = new ArrayList<>();
        int totalEnergy = this.energy + secondAnimal.energy;
        double thisParentRatio = (double) this.energy / totalEnergy;
        int splitPoint = (int) (GENOME_LENGTH * thisParentRatio);

        boolean takeLeftFromStronger = random.nextBoolean();
        if (takeLeftFromStronger) {
            childGenome.addAll(this.genome.subList(0, splitPoint));
            childGenome.addAll(secondAnimal.genome.subList(splitPoint, GENOME_LENGTH));
        } else {
            childGenome.addAll(secondAnimal.genome.subList(0, splitPoint));
            childGenome.addAll(this.genome.subList(splitPoint, GENOME_LENGTH));
        }

        // Mutacje genomu potomka
        for (int i = 0; i < MUTATION_COUNT; i++) {
            int mutationIndex = random.nextInt(GENOME_LENGTH);
            childGenome.set(mutationIndex, random.nextInt(8)); // Gen zmienia się na losową wartość
        }

        child.genome = childGenome;
        return child;
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

    public void rotate(int number) {
        switch (number) {
            case 0 -> direction = MapDirection.NORTH;
            case 1 -> direction = MapDirection.NORTHEAST;
            case 2 -> direction = MapDirection.EAST;
            case 3 -> direction = MapDirection.SOUTHEAST;
            case 4 -> direction = MapDirection.SOUTH;
            case 5 -> direction = MapDirection.SOUTHWEST;
            case 6 -> direction = MapDirection.WEST;
            case 7 -> direction = MapDirection.NORTHWEST;
            default -> throw new IllegalArgumentException("Invalid rotation number: " + number);
        }
    }
    
    private void move(int number) {
        this.position = position.add(this.direction.toUnitVector());
    }
}
