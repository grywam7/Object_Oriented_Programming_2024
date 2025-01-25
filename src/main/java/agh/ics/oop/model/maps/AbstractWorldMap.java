package agh.ics.oop.model.maps;

import java.util.*;
import agh.ics.oop.model.map_elements.*;
import agh.ics.oop.Conflicts;

public abstract class AbstractWorldMap implements WorldMap{
    protected final Map<Vector2d, HashSet<Animal>> animals = new HashMap<>();
    protected final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Boundary mapEdges;
    private final Conflicts conflicts = new Conflicts();
    private final int mapSize;
    private int deadAnimalsLifespan = 0 ;
    private int deadAnimalsCount = 0 ;

    protected AbstractWorldMap(Boundary mapEdges) {
        this.mapEdges = mapEdges;
        this.mapSize = mapEdges.getHeight()*mapEdges.getWidth();
    }

    public Map<Vector2d, Set<Animal>> getAnimals() { return Collections.unmodifiableMap(animals); }
    public Map<Vector2d, Grass> getGrasses() { return Collections.unmodifiableMap(grasses); }
    public Boundary getMapEdges() { return mapEdges; }

    public void initializeAnimals(int amountOfAnimals, int animalsEnergy, int genomeLength, boolean animalModification) {
        Random random = new Random();
        for(int i = 0; i < amountOfAnimals; i++) {
            int randomX = random.nextInt(mapEdges.getWidth()) + mapEdges.bottomLeft().getX();
            int randomY = random.nextInt(mapEdges.getHeight()) + mapEdges.bottomLeft().getY();
            Animal animal= new Animal(new Vector2d(randomX, randomY), animalsEnergy, 0);
            animal.setGenome(new Genome(genomeLength));
            placeAnimal(animal);
        }
    }


    public void placeAnimal(Animal animal) {
        Vector2d newAnimalPosition = animal.getPosition();
        // jeśli nie ma klucza w HashMapie to trzeba go zrobić
        if (!animals.containsKey(newAnimalPosition)) {
            animals.put(newAnimalPosition, new HashSet<>());
        }
        animals.get(newAnimalPosition).add(animal);
    }

    public void performMoves() {
        Map<Vector2d, HashSet<Animal>> updatedAnimals = new HashMap<>();

        for (Map.Entry<Vector2d, HashSet<Animal>> entry : animals.entrySet()) {
            for (Animal animal : entry.getValue()) {
                // Pobierz aktualny gen z genomu i wykonaj ruch
                int moveGene = animal.getCurrentGenomeMove();
                animal.move(moveGene, mapEdges);

                // Dodaj zwierzę do nowej pozycji
                Vector2d newPosition = animal.getPosition();
                updatedAnimals.computeIfAbsent(newPosition, k -> new HashSet<>()).add(animal);
            }
        }

        // Zaktualizuj mapę zwierząt
        animals.clear();
        animals.putAll(updatedAnimals);
    }


// dodać getery do pozostalych boundry

    @Override
    public String getID(){
        return super.toString();
    }

    // Własna metoda rozstawiania trawy
    public abstract void growGrass(int numberOfGrass);

    public void removeDeadAnimals() {
        List<Animal> deadAnimals = new ArrayList<>();

        // Iterujemy po mapie zwierząt
        for (Map.Entry<Vector2d, HashSet<Animal>> entry : animals.entrySet()) {
            HashSet<Animal> animalSet = entry.getValue();
            Iterator<Animal> iterator = animalSet.iterator();

            while (iterator.hasNext()) {
                Animal animal = iterator.next();
                if (animal.getEnergy() <= 0) {
                    deadAnimals.add(animal);
                    iterator.remove();
                    deadAnimalsLifespan += animal.getAge();
                    deadAnimalsCount++;
                }
            }
        }

        // Usuwamy puste wpisy w mapie zwierząt
        animals.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    public void breeding(int reproductionEnergy, int energyLoss, int currentDay, int mutationCount ) {
        for (Vector2d position : animals.keySet()) {
            HashSet<Animal> animalSet = animals.get(position);
            if (animalSet.size() < 2) continue; // Za mało zwierząt na rozmnażanie

            // Rozwiązywanie konfliktów o rozmnażanie
            List<Animal> candidates = new ArrayList<>(animalSet);
            List<Animal> parents = conflicts.resolveReproductionConflict(candidates);

            if (parents.size() == 2 && parents.get(0).getEnergy() >= reproductionEnergy && parents.get(1).getEnergy() >= reproductionEnergy) {
                Animal parent1 = parents.get(0);
                Animal parent2 = parents.get(1);

                // Tworzenie nowego zwierzęcia przez rozmnażanie
                Animal child = parent1.copulate(parent2, energyLoss, currentDay, mutationCount);

                // Dodanie dziecka do mapy
                placeAnimal(child);
            }
        }
    }

    public void incrementAgeForAllAnimals() {
        for (Map.Entry<Vector2d, HashSet<Animal>> entry : animals.entrySet()) {
            for (Animal animal : entry.getValue()) {
                animal.incrementAge();
            }
        }
    }
    public void feedAnimals(int plantEnergyValue) {
        List<Vector2d> grassPositionsToRemove = new ArrayList<>();

        // Iteracja po wszystkich pozycjach, gdzie jest trawa
        for (Map.Entry<Vector2d, Grass> entry : grasses.entrySet()) {
            Vector2d position = entry.getKey();

            // Sprawdzamy, czy na tej pozycji są zwierzęta
            if (animals.containsKey(position) && !animals.get(position).isEmpty()) {
                List<Animal> animalsAtPosition = new ArrayList<>(animals.get(position));

                // Rozwiązujemy konflikt o jedzenie, jeśli jest więcej niż jedno zwierzę
                Conflicts conflicts = new Conflicts();
                Animal eater = conflicts.resolveFoodConflict(animalsAtPosition);

                // Zwierzę zjada trawę i zyskuje energię
                if (eater != null) {
                    eater.eat(plantEnergyValue);
                    grassPositionsToRemove.add(position);
                }
            }
        }

        // Usunięcie trawy z mapy po zjedzeniu
        for (Vector2d position : grassPositionsToRemove) {
            grasses.remove(position);
        }
    }

    public int countAnimals() {
        int totalAnimals = 0;
        for (HashSet<Animal> animalSet : animals.values()) {
            totalAnimals += animalSet.size();
        }
        return totalAnimals;
    }

    public int countGrass() {
        return grasses.size();
    }

    private int countOccupiedFields() {
        HashSet<Vector2d> occupiedFields = new HashSet<>();
        occupiedFields.addAll(animals.keySet());
        occupiedFields.addAll(grasses.keySet());
        return occupiedFields.size();
    }

    public int freeFields() {
        return mapSize - countOccupiedFields();
    }

    public float averageLifespan() {
        return deadAnimalsCount == 0 ? 0 : (float) deadAnimalsLifespan / deadAnimalsCount;
    }

    public List<Map.Entry<String, Integer>> printTop3Genotypes() {
        // Mapa do przechowywania liczby wystąpień każdego genotypu
        Map<String, Integer> genotypeCounts = new HashMap<>();

        // Zliczanie genotypów ze wszystkich zwierząt
        for (HashSet<Animal> animalSet : animals.values()) {
            for (Animal animal : animalSet) {
                String genotype = animal.getGenome().toString();
                genotypeCounts.put(genotype, genotypeCounts.getOrDefault(genotype, 0) + 1);
            }
        }

        // Posortowanie genotypów według liczby wystąpień (malejąco)
        List<Map.Entry<String, Integer>> sortedGenotypes = new ArrayList<>(genotypeCounts.entrySet());
        sortedGenotypes.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        return sortedGenotypes;
    }

    public float calculateAverageEnergy() {
        int totalEnergy = 0;
        int animalCount = this.countAnimals();

        for (var entry : this.getAnimals().values()) {
            for (var animal : entry) {
                totalEnergy += animal.getEnergy();
            }
        }
        return animalCount == 0 ? 0 : (float) totalEnergy / animalCount;
    }

    public void applyEnergyLossToAllAnimals(int energyLoss) {
        for (Map.Entry<Vector2d, HashSet<Animal>> entry : animals.entrySet()) {
            for (Animal animal : entry.getValue()) {
                animal.energyLoss(energyLoss);
            }
        }
    }
    public void placeGrass(Vector2d position) {
        grasses.put(position, new Grass(position));
    }
}