package agh.ics.oop.model.map_elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal implements WorldElement {
    private static final Random RANDOM = new Random();

    private MapDirection direction;
    private Vector2d position;
    private int energy;

    private int plantsEatenCount;
    private int age;

    private int childrenCount;
    private int descendantCount;

    private Animal parent1;
    private Animal parent2;

    private Genome genome;

    public Animal(Vector2d position, int startingEnergy) {
        this.direction = MapDirection.NORTH;
        this.position = position;
        this.energy = startingEnergy;
        this.childrenCount = 0;
        this.age = 0;
        this.plantsEatenCount = 0;
        this.parent1 = null;
        this.parent2 = null;
    }

    // --- Gettery ---
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

    public int getCurrentGenomeMove() {
        return genome.nextMove();
    }

    public Genome getGenome() {
        return genome;
    }

    public int getDescendantCount() {
        return descendantCount;
    }

    // --- Settery ---
    public void setGenome(Genome genome) {
        this.genome = genome;
    }

    public void setParents(Animal parent1, Animal parent2) {
        this.parent1 = parent1;
        this.parent2 = parent2;
    }

    // --- Główne zachowanie ---
    public void eat(int plantEnergyValue) {
        energy += plantEnergyValue;
        plantsEatenCount++;
    }

    public void energyLoss(int loss) {
        energy -= loss;
    }

    public void incrementAge() {
        age++;
    }

    public Animal copulate(Animal partner, int energyLoss, int mutationCount) {
        // Redukcja energii rodziców
        int childEnergy = energyLoss * 2;
        this.energy -= energyLoss;
        partner.energy -= energyLoss;

        // Tworzenie potomka z energią rodziców i genotypem
        Animal child = new Animal(this.position, childEnergy);
        child.setParents(this, partner);
        child.genome = calculateNewGenome(this, partner, mutationCount);

        // Aktualizacja liczników potomków
        this.childrenCount++;
        partner.childrenCount++;
        updateDescendantCount(this);
        updateDescendantCount(partner);

        return child;
    }

    public void move(int rotationSteps, Boundary boundary) {
        rotate(rotationSteps);

        Vector2d potentialPosition = position.add(direction.toUnitVector());
        if (isOutOfBounds(potentialPosition, boundary)) {
            position = adjustPositionForBoundary(potentialPosition, boundary);
        } else {
            position = potentialPosition;
        }
    }

    public void rotate(int steps) {
        if (steps == 0) return;
        int newDirectionIndex = (direction.ordinal() + steps) % 8;
        direction = MapDirection.values()[newDirectionIndex];
    }

    @Override
    public String toString() {
        return direction.toShortString();
    }

    // --- Prywatne metody pomocnicze ---
    private Genome calculateNewGenome(Animal parent1, Animal parent2, int mutationCount) {
        List<Integer> parent1Genome = parent1.getGenome().getList();
        List<Integer> parent2Genome = parent2.getGenome().getList();
        int genomeLength = parent2Genome.size();

        List<Integer> childGenome = new ArrayList<>();
        double splitRatio = (double) parent1.energy / (parent1.energy + parent2.energy);
        int splitPoint = (int) (genomeLength * splitRatio);

        boolean takeLeftFromParent1 = RANDOM.nextBoolean();
        if (takeLeftFromParent1) {
            childGenome.addAll(parent1Genome.subList(0, splitPoint));
            childGenome.addAll(parent2Genome.subList(splitPoint, genomeLength));
        } else {
            childGenome.addAll(parent2Genome.subList(0, splitPoint));
            childGenome.addAll(parent1Genome.subList(splitPoint, genomeLength));
        }

        // Wprowadzenie mutacji
        for (int i = 0; i < mutationCount; i++) {
            int mutationIndex = RANDOM.nextInt(genomeLength);
            childGenome.set(mutationIndex, RANDOM.nextInt(8));
        }

        Genome childGenomeObject = new Genome(genomeLength);
        childGenomeObject.setGenome(childGenome);
        return childGenomeObject;
    }

    private void updateDescendantCount(Animal ancestor) {
        if (ancestor == null) return;
        ancestor.descendantCount++;
        updateDescendantCount(ancestor.parent1);
        updateDescendantCount(ancestor.parent2);
    }

    private boolean isOutOfBounds(Vector2d position, Boundary boundary) {
        return position.getX() < boundary.bottomLeft().getX() ||
            position.getX() > boundary.topRight().getX() ||
            position.getY() < boundary.bottomLeft().getY() ||
            position.getY() > boundary.topRight().getY();
    }

    private Vector2d adjustPositionForBoundary(Vector2d position, Boundary boundary) {
        int x = position.getX();
        int y = position.getY();

        // Jeśli wychodzi poza mapę z lewej, pojawia się z prawej
        if (x < boundary.bottomLeft().getX()) {
            x = boundary.topRight().getX();
        } else if (x > boundary.topRight().getX()) {
            x = boundary.bottomLeft().getX();
        }

        // Jeśli wychodzi poza mapę z dołu lub z góry, obraca się
        if (y < boundary.bottomLeft().getY() || y > boundary.topRight().getY()) {
            direction = direction.opposite();
        }

        return new Vector2d(x, y);
    }
}
