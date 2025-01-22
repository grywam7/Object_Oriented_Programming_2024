package agh.ics.oop.model.maps;

import java.util.*;
import agh.ics.oop.model.map_elements.*;
import agh.ics.oop.Conflicts;

public abstract class AbstractWorldMap implements WorldMap{
    protected final Map<Vector2d, HashSet<Animal>> animals = new HashMap<>();
    protected final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Boundary mapEdges;
    private final Conflicts conflicts = new Conflicts();

    protected AbstractWorldMap(Boundary mapEdges) {
        this.mapEdges = mapEdges;
    }

    public Map<Vector2d, Set<Animal>> getAnimals() { return Collections.unmodifiableMap(animals); }
    public Map<Vector2d, Grass> getGrasses() { return Collections.unmodifiableMap(grasses); }
    public Boundary getMapEdges() { return mapEdges; }

    public void initializeAnimals(Integer amountOfAnimals, Integer animalsEnergy, int genomeLength) {
        Random random = new Random();
        for(int i = 0; i < amountOfAnimals; i++) {
            int randomX = random.nextInt(mapEdges.getWidth()) + mapEdges.bottomLeft().getX();
            int randomY = random.nextInt(mapEdges.getHeight()) + mapEdges.bottomLeft().getY();
            placeAnimal(new Animal(new Vector2d(randomX, randomY), animalsEnergy, genomeLength, 0)) ;
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

    public void move(Animal animal, MapDirection direction) {
        Vector2d oldPosition = animal.getPosition();
        animal.move(direction.toInteger(), mapEdges);
        Vector2d newPosition = animal.getPosition();
        if (!oldPosition.equals(newPosition)) {
            this.placeAnimal(animal);
        }
    }

    public void performMoves() {
        Map<Vector2d, HashSet<Animal>> updatedAnimals = new HashMap<>();

        for (Map.Entry<Vector2d, HashSet<Animal>> entry : animals.entrySet()) {
            for (Animal animal : entry.getValue()) {
                // Pobierz aktualny gen z genomu i wykonaj ruch
                int moveGene = animal.getCurrentGenomeMove();
                animal.move(moveGene, mapEdges);

                animal.incrementGenomeIndex();

                // Dodaj zwierzę do nowej pozycji
                Vector2d newPosition = animal.getPosition();
                updatedAnimals.computeIfAbsent(newPosition, k -> new HashSet<>()).add(animal);
            }
        }

        // Zaktualizuj mapę zwierząt
        animals.clear();
        animals.putAll(updatedAnimals);
    }

    public void performMovesSpecial() {
        Map<Vector2d, HashSet<Animal>> updatedAnimals = new HashMap<>();

        for (Map.Entry<Vector2d, HashSet<Animal>> entry : animals.entrySet()) {
            for (Animal animal : entry.getValue()) {
                // Pobierz aktualny gen z genomu i wykonaj ruch
                int moveGene = animal.getGenomeIndexSpecial();
                animal.move(moveGene, mapEdges);

                animal.incrementGenomeIndex();

                // Dodaj zwierzę do nowej pozycji
                Vector2d newPosition = animal.getPosition();
                updatedAnimals.computeIfAbsent(newPosition, k -> new HashSet<>()).add(animal);
            }
        }

        // Zaktualizuj mapę zwierząt
        animals.clear();
        animals.putAll(updatedAnimals);
    }




    public Set<Vector2d> animalsPlaces() {
        return animals.keySet();
    }

    public Set<Vector2d> grassPlaces(){
        return grasses.keySet();
    }

    public WorldElement grassesAt(Vector2d position) {
        return grasses.get(position);
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
                    iterator.remove(); // Usuń zwierzę z pozycji na mapie
                }
            }
        }

        // Usuwamy puste wpisy w mapie zwierząt
        animals.entrySet().removeIf(entry -> entry.getValue().isEmpty());

        // Logowanie ilości usuniętych zwierząt
        System.out.println("Removed " + deadAnimals.size() + " dead animals.");
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

    public void applyEnergyLossToAllAnimals(int energyLoss) {
        for (Map.Entry<Vector2d, HashSet<Animal>> entry : animals.entrySet()) {
            for (Animal animal : entry.getValue()) {
                animal.energyLoss(energyLoss);
            }
        }
    }

}