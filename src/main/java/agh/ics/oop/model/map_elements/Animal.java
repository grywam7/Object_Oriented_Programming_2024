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

    private Genome genome;

    public static final Random random = new Random();

    public Animal(Vector2d position, int startingEnergy,int currentDay) {
        this.direction = MapDirection.NORTH;
        this.position = position;
        this.energy = startingEnergy;
        this.childrenCount = 0;
        this.age = 0;
        this.plantsEatenCount = 0;
        this.birthDay = currentDay;
        parent1 = null;
        parent2 = null;
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
        return genome.nextMove();
    }

    public Genome getGenome() {
        return genome;
    }

    public void setGenome(Genome genome) {
        this.genome = genome;
    }

    public void eat(int plantEnergyValue){
        energy += plantEnergyValue;
        plantsEatenCount += 1;
    }

    public void energyLoss(int loss){
        energy -= loss;
    }

    private Genome calculateNewGenome(Animal animal1, Animal animal2, int mutationCount) {

        List<Integer> animal1genome = animal1.getGenome().getList();
        List<Integer> animal2genome = animal2.getGenome().getList();
        int genomeLength = animal2genome.size();

        List<Integer> childGenome = new ArrayList<>();
        int totalEnergy = animal1.energy + animal2.energy;
        double thisParentRatio = (double) animal1.energy / totalEnergy;
        int splitPoint = (int) (genomeLength * thisParentRatio);


        boolean takeLeftFromStronger = random.nextBoolean();
        if (takeLeftFromStronger) {
            childGenome.addAll(animal1genome.subList(0, splitPoint));
            childGenome.addAll(animal2genome.subList(splitPoint, genomeLength));
        } else {
            childGenome.addAll(animal2genome.subList(0, splitPoint));
            childGenome.addAll(animal1genome.subList(splitPoint, genomeLength));
        }

        // Mutacje genomu potomka
        for (int i = 0; i < mutationCount; i++) {
            int mutationIndex = random.nextInt(genomeLength);
            childGenome.set(mutationIndex, random.nextInt(8));
        }

        Genome output = new Genome(genomeLength);
        output.setGenome(childGenome);
        return output;
    }


    public Animal copulate(Animal secondAnimal, int energyLoss, int simulationDay, int mutationCount) {
        // Sprawdzenie, czy oba zwierzęta mają wystarczającą energię do rozmnażania jest w simulation

        // Obliczanie energii potomka i odejmowanie jej od rodziców
        int childEnergy = energyLoss * 2;
        this.energy -= energyLoss;
        secondAnimal.energy -= energyLoss;

        // Tworzenie nowego zwierzęcia
        Animal child = new Animal(this.position, childEnergy,   simulationDay);
        child.setParents(this, secondAnimal);

        // Aktualizacja liczby dzieci
        this.childrenCount++;
        secondAnimal.childrenCount++;
        updateDescendantCount(this);
        updateDescendantCount(secondAnimal);

        child.genome = calculateNewGenome(this, secondAnimal, mutationCount);
        return child;
    }

    public void rotate(int number) {
        // 0 oznacza brak obrotu
        if (number == 0) {
            return; // Nie zmieniaj kierunku
        }

        // Obliczamy nowy kierunek na podstawie aktualnego kierunku i liczby obrotów
        int newDirectionIndex = (direction.ordinal() + number) % 8;
        direction = MapDirection.values()[newDirectionIndex];
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
