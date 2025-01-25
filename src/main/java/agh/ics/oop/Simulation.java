package agh.ics.oop;

import agh.ics.oop.model.map_elements.Boundary;
import agh.ics.oop.model.map_elements.Vector2d;
import agh.ics.oop.model.maps.AbstractWorldMap;
import agh.ics.oop.model.maps.CrawlingJungleMap;
import agh.ics.oop.model.maps.EquatorMap;

public class Simulation {
    private final AbstractWorldMap worldMap;
    private int currentDay;
    private final int targetDay;
    private final int dailyPlantCount;
    private final int sufficientEnergy;
    private final int breedingEnergyLoss;
    private final int mutationCount;
    private final int plantEnergy;
    private final boolean animalModification;
    private final int dailyEnergyLoss;

    public Simulation(int width, int height, int jungleHeight, boolean mapModification, boolean animalModification,
                      int initialPlantCount, int plantEnergy, int dailyPlantCount, int initialAnimalCount, int initialAnimalEnergy,
                      int sufficientEnergy, int breedingEnergyLoss, int dailyEnergyLoss, int mutationCount, int genomeLength, int targetDay) {

        this.currentDay = 0;
        this.targetDay = targetDay;
        this.dailyPlantCount = dailyPlantCount;
        this.sufficientEnergy = sufficientEnergy;
        this.breedingEnergyLoss = breedingEnergyLoss;
        this.mutationCount = mutationCount;
        this.plantEnergy = plantEnergy;
        this.animalModification = animalModification;
        this.dailyEnergyLoss = dailyEnergyLoss;

        // Walidacja wysokości dżungli
        if (jungleHeight > height) {
            throw new IllegalArgumentException("Jungle height cannot be greater than the map height!");
        }

        // Wybór mapy
        if (mapModification) {
            this.worldMap = new CrawlingJungleMap(new Boundary(new Vector2d(0, 0), new Vector2d(width - 1, height - 1)));
        } else {
            int yMin = (height - jungleHeight) / 2;
            int yMax = yMin + jungleHeight - 1;
            this.worldMap = new EquatorMap(
                    new Boundary(new Vector2d(0, 0), new Vector2d(width - 1, height - 1)), // Pełna mapa
                    new Boundary(new Vector2d(0, yMin), new Vector2d(width - 1, yMax))    // Równik
            );
        }

        // Inicjalizacja zwierząt i roślin
        this.worldMap.initializeAnimals(initialAnimalCount, initialAnimalEnergy, genomeLength, animalModification);
        this.worldMap.growGrass(initialPlantCount);
    }

    /**
     * Wykonuje jeden dzień symulacji.
     * @return true, jeśli symulacja powinna trwać dalej, false, jeśli osiągnięto docelowy dzień.
     */
    public boolean step() {
        if (currentDay >= targetDay) {
            return false; // Koniec symulacji
        }

        // 1. Usunięcie martwych zwierząt
        worldMap.removeDeadAnimals();

        // 2. Ruch zwierząt
        worldMap.performMoves();

        // 3. Zwierzęta jedzą rośliny
        worldMap.feedAnimals(plantEnergy);

        // 4. Rozmnażanie zwierząt
        worldMap.breeding(sufficientEnergy, breedingEnergyLoss, currentDay, mutationCount);

        // 5. Wzrost roślin
        worldMap.growGrass(dailyPlantCount);

        // 6. Utrata energii przez zwierzęta
        worldMap.applyEnergyLossToAllAnimals(dailyEnergyLoss);

        //7. Zwiększenie wieku dla zwierząt które pozostały na mapie
        worldMap.incrementAgeForAllAnimals();

        currentDay++;

        return true;
    }

    public AbstractWorldMap getWorldMap() {
        return this.worldMap;
    }

    public int getCurrentDay() {
        return currentDay;
    }
}
