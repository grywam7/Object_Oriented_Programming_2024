package agh.ics.oop.model.map_elements;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal implements WorldElement {
    private MapDirection direction;
    private Vector2d position;
    private int energy;

    private int plantsEatenCount;

    public int age;
    private final int birthDay;

    private int childrenCount;
    private int descendantCount;

    private Animal parent1;
    private Animal parent2;

    private int genomeLength;
    private int mutationCount;
    private int currentGenomeIndex;
    List<Integer> genome = new ArrayList<>();

    public static final Random random = new Random();

    public Animal(Vector2d position, int startingEnergy, int genomeLength, int currentDay) {
        this.direction = MapDirection.NORTH;
        this.position = position;
        this.energy = startingEnergy;
        this.childrenCount = 0;
        this.age = 0;
        this.plantsEatenCount = 0;
        this.birthDay = currentDay;
        parent1 = null;
        parent2 = null;
        this.genomeLength = genomeLength; // Przypisz długość genomu
        this.currentGenomeIndex = 0;
        for (int i = 0; i < genomeLength; i++) {
            genome.add(random.nextInt(8)); // Wartości od 0 do 7
        }
    }

    public int getPlantsEatenCount() {
        return plantsEatenCount;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }

    public int getAge() {
        return age;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public MapDirection getDirection(){
        return direction;
    }

    public int getCurrentGenomeMove() {
        return genome.get(currentGenomeIndex);
    }

    public int getGenomeIndexSpecial() {
        if (random.nextInt(100) < 80) {
            // 80% szansy na przejście do kolejnego genu w kolejności
            return getCurrentGenomeMove();
        } else {
            // 20% szansy na losowy indeks
            currentGenomeIndex = random.nextInt(genome.size());
            return currentGenomeIndex;
        }
    }


    public void incrementGenomeIndex() {
        currentGenomeIndex = (currentGenomeIndex + 1) % genome.size();
    }

    public Boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void eat(int plantEnergyValue){
        energy += plantEnergyValue;
        plantsEatenCount += 1;
    }

    public void energyLoss(int loss){
        energy -= loss;
    }

    //POROZBIJAĆ NA MNIEJSZE
    public Animal copulate(Animal secondAnimal, int energyLoss, int simulationDay, int mutationCount) {
        // Sprawdzenie, czy oba zwierzęta mają wystarczającą energię do rozmnażania będzie w simulation

        // Obliczanie energii potomka i odejmowanie jej od rodziców
        int childEnergy = energyLoss * 2;
        this.energy -= energyLoss;
        secondAnimal.energy -= energyLoss;

        // Tworzenie nowego zwierzęcia
        Animal child = new Animal(this.position, childEnergy, genomeLength,  simulationDay);
        child.setParents(this, secondAnimal);

        // Aktualizacja liczby dzieci u rodziców DAĆ TO OSOBNO
        this.childrenCount++;
        secondAnimal.childrenCount++;
        updateDescendantCount(this);
        updateDescendantCount(secondAnimal);

        // Krzyżowanie genomu
        List<Integer> childGenome = new ArrayList<>();
        int totalEnergy = this.energy + secondAnimal.energy;
        double thisParentRatio = (double) this.energy / totalEnergy;
        int splitPoint = (int) (genomeLength * thisParentRatio);

        boolean takeLeftFromStronger = random.nextBoolean();
        if (takeLeftFromStronger) {
            childGenome.addAll(this.genome.subList(0, splitPoint));
            childGenome.addAll(secondAnimal.genome.subList(splitPoint, genomeLength));
        } else {
            childGenome.addAll(secondAnimal.genome.subList(0, splitPoint));
            childGenome.addAll(this.genome.subList(splitPoint, genomeLength));
        }

        // Mutacje genomu potomka
        for (int i = 0; i < mutationCount; i++) {
            int mutationIndex = random.nextInt(genomeLength);
            childGenome.set(mutationIndex, random.nextInt(8)); // Gen zmienia się na losową wartość
        }

        child.genome = childGenome;
        return child;
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

    //move przyjmuje liczbe bo tam było ze sie zawsze najpierw obraca a potem idzie do przodu
    public void move(int number, Boundary boundary) {
        // Rotate first
        rotate(number);

        // Calculate potential new position q
        Vector2d potentialPosition = position.add(direction.toUnitVector());

        // Check if moving out of bounds
        if (potentialPosition.getX() < boundary.bottomLeft().getX()) {
            // Wrap around horizontally (left to right)
            potentialPosition = new Vector2d(boundary.topRight().getX(), potentialPosition.getY());
        } else if (potentialPosition.getX() > boundary.topRight().getX()) {
            // Wrap around horizontally (right to left)
            potentialPosition = new Vector2d(boundary.bottomLeft().getX(), potentialPosition.getY());
        }

        if (potentialPosition.getY() < boundary.bottomLeft().getY() || potentialPosition.getY() > boundary.topRight().getY()) {
            // Hit the poles (top or bottom)
            direction = direction.opposite(); // Reverse direction
        } else {
            // Move to the new position if valid
            position = potentialPosition;
        }
    }

    private void updateDescendantCount(Animal animal) {
        if (animal == null) return;
        animal.descendantCount++;
        updateDescendantCount(animal.parent1);
        updateDescendantCount(animal.parent2);
    }

    public void setParents(Animal parent1, Animal parent2){
        this.parent1 = parent1;
        this.parent2 = parent2;
    }

    @Override
    public String toString(){
        return direction.toShortString();
    }

    public String getStats() {
        return String.format(
                "Animal Stats:%n" +
                        "Position: %s%n" +
                        "Direction: %s%n" +
                        "Energy: %d%n" +
                        "Age: %d%n" +
                        "Plants Eaten: %d%n" +
                        "Children Count: %d%n" +
                        "Descendant Count: %d%n" +
                        "Birth Day: %d%n" +
                        "Genome: %s",
                position, direction, energy, age, plantsEatenCount, childrenCount, descendantCount, birthDay, genome.toString()
        );
    }

}
