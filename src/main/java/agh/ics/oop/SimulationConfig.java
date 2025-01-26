package agh.ics.oop;

import java.io.Serializable;

public class SimulationConfig implements Serializable {
    public int width;
    public int height;
    public int jungleHeight;
    public int initialPlants;
    public int plantEnergy;
    public int dailyPlants;
    public int initialAnimals;
    public int animalEnergy;
    public int sufficientEnergy;
    public int breedingEnergyLoss;
    public int mutationCount;
    public int genomeLength;
    public int targetDay;
    public int dailyEnergyLoss;
    public boolean mapModification;
    public boolean animalModification;

    public SimulationConfig() {
        // Default values
        width = 60;
        height = 60;
        jungleHeight = 20;
        initialPlants = 20;
        plantEnergy = 25;
        dailyPlants = 10;
        initialAnimals = 4;
        animalEnergy = 100;
        sufficientEnergy = 50;
        breedingEnergyLoss = 20;
        mutationCount = 2;
        genomeLength = 8;
        targetDay = 100;
        dailyEnergyLoss = 5;
        mapModification = false;
        animalModification = false;
    }
}
