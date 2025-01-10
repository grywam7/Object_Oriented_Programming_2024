package agh.ics.oop.model.maps;

import agh.ics.oop.model.map_elements.*;

import java.util.*;

public class DiverseMap {
    private final int width;
    private final int height;
    private final Boundary mapBoundary;
    private final Boundary jungleBoundary;
    private final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final Set<Vector2d> grassPositions = new HashSet<>();
    private static final Random random = new Random();

    public DiverseMap(int width, int height, int jungleWidth, int jungleHeight) {
        this.width = width;
        this.height = height;

        // Tworzenie granic mapy
        this.mapBoundary = new Boundary(new Vector2d(0, 0), new Vector2d(width - 1, height - 1));

        // Tworzenie granic dżungli
        int jungleXStart = (width - jungleWidth) / 2;
        int jungleYStart = (height - jungleHeight) / 2;
        this.jungleBoundary = new Boundary(
                new Vector2d(jungleXStart, jungleYStart),
                new Vector2d(jungleXStart + jungleWidth - 1, jungleYStart + jungleHeight - 1)
        );
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public boolean isInJungle(Vector2d position) {
        return jungleBoundary.contains(position);
    }

    public void addAnimal(Animal animal) {
        Vector2d position = animal.getPosition();
        animals.putIfAbsent(position, new ArrayList<>());
        animals.get(position).add(animal);
    }

    public void moveAnimal(Animal animal, int moveNumber) {
        Vector2d currentPosition = animal.getPosition();
        List<Animal> currentAnimals = animals.get(currentPosition);

        // Usuń zwierzę z obecnej pozycji
        if (currentAnimals != null) {
            currentAnimals.remove(animal);
            if (currentAnimals.isEmpty()) {
                animals.remove(currentPosition);
            }
        }

        // Przesuń zwierzę
        animal.move(moveNumber, mapBoundary);

        // Dodaj zwierzę do nowej pozycji
        Vector2d newPosition = animal.getPosition();
        animals.putIfAbsent(newPosition, new ArrayList<>());
        animals.get(newPosition).add(animal);
    }

    public List<Animal> getAnimalsAt(Vector2d position) {
        return animals.getOrDefault(position, new ArrayList<>());
    }

    public void removeDeadAnimals() {
        List<Animal> deadAnimals = new ArrayList<>();

        // Iterujemy po mapie zwierząt
        for (Map.Entry<Vector2d, List<Animal>> entry : animals.entrySet()) {
            List<Animal> animalList = entry.getValue();
            Iterator<Animal> iterator = animalList.iterator();

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

        // Możesz tutaj dodać dodatkową logikę, np. statystyki usuniętych zwierząt
        System.out.println("Removed " + deadAnimals.size() + " dead animals.");
    }


    public void placeGrassPreferJungle() {
        Vector2d grassPosition;
        if (random.nextInt(100) < 80) { // 80% szans na dżunglę
            grassPosition = getRandomPosition(jungleBoundary, grassPositions);
        } else { // 20% szans na resztę mapy
            grassPosition = getRandomPosition(mapBoundary, grassPositions, jungleBoundary);
        }

        if (grassPosition != null) {
            grassPositions.add(grassPosition);
        }
    }

    public void placeGrassNearExisting() {
        if (grassPositions.isEmpty()) {
            // Jeśli nie ma trawy, wybierz losowe miejsce na mapie
            Vector2d newGrass = getRandomPosition(mapBoundary, grassPositions);
            if (newGrass != null) {
                grassPositions.add(newGrass);
            }
            return;
        }

        // Lista możliwych pozycji w sąsiedztwie istniejącej trawy
        List<Vector2d> potentialPositions = new ArrayList<>();
        for (Vector2d grass : grassPositions) {
            for (Vector2d neighbor : grass.getNeighbors()) {
                if (mapBoundary.contains(neighbor) && !grassPositions.contains(neighbor)) {
                    potentialPositions.add(neighbor);
                }
            }
        }

        if (!potentialPositions.isEmpty()) {
            Vector2d grassPosition = potentialPositions.get(random.nextInt(potentialPositions.size()));
            grassPositions.add(grassPosition);
        } else {
            // Jeśli sąsiedztwo jest pełne, wybierz losowe miejsce
            Vector2d fallbackGrass = getRandomPosition(mapBoundary, grassPositions);
            if (fallbackGrass != null) {
                grassPositions.add(fallbackGrass);
            }
        }
    }

    private Vector2d getRandomPosition(Boundary boundary, Set<Vector2d> exclusions) {
        return getRandomPosition(boundary, exclusions, null);
    }

    private Vector2d getRandomPosition(Boundary boundary, Set<Vector2d> exclusions, Boundary exclusionZone) {
        int attempts = 0;
        while (attempts < 100) {
            Vector2d candidate = new Vector2d(
                    random.nextInt(boundary.getWidth()) + boundary.bottomLeft().getX(),
                    random.nextInt(boundary.getHeight()) + boundary.bottomLeft().getY()
            );
            if (!exclusions.contains(candidate) && (exclusionZone == null || !exclusionZone.contains(candidate))) {
                return candidate;
            }
            attempts++;
        }
        return null; // Jeśli nie uda się znaleźć miejsca, zwraca null
    }

    public Set<Vector2d> getGrassPositions() {
        return grassPositions;
    }

    public void removeGrassAt(Vector2d position) {
        grassPositions.remove(position);
    }

    public boolean isGrassAt(Vector2d position) {
        return grassPositions.contains(position);
    }

    //testowo:
    public void printMap() {
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                Vector2d position = new Vector2d(x, y);
                List<Animal> positionAnimals = getAnimalsAt(position);

                if (!positionAnimals.isEmpty()) {
                    System.out.print(positionAnimals.size() + " ");
                } else if (grassPositions.contains(position)) {
                    System.out.print("G ");
                } else if (isInJungle(position)) {
                    System.out.print("J ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
}
