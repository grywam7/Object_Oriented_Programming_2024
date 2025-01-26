package agh.ics.oop;

import java.util.*;
import agh.ics.oop.model.map_elements.Animal;

public class Conflicts {

    private static final Random random = new Random();

    // Metoda pomocnicza do sortowania i wybierania zwierząt z najwyższą energią
    private List<Animal> getTopCandidates(List<Animal> animals) {
        if (animals.isEmpty()) return Collections.emptyList();

        // Sortowanie zwierząt według kryteriów: energia, wiek, liczba dzieci
        animals.sort(Comparator.comparingInt(Animal::getEnergy).reversed()
                .thenComparingInt(Animal::getAge).reversed()
                .thenComparingInt(Animal::getChildrenCount).reversed());

        // Pobranie maksymalnej energii
        int maxEnergy = animals.getFirst().getEnergy();

        // Wybranie zwierząt z najwyższą energią
        List<Animal> topCandidates = new ArrayList<>();
        for (Animal animal : animals) {
            if (animal.getEnergy() == maxEnergy) {
                topCandidates.add(animal);
            } else {
                break;
            }
        }

        return topCandidates;
    }

    // Metoda rozwiązywania konfliktu o jedzenie
    public Animal resolveFoodConflict(List<Animal> animals) {
        List<Animal> topCandidates = getTopCandidates(animals);

        if (topCandidates.isEmpty()) return null;

        // Jeżeli jest remis, wybór losowego zwierzaka
        if (topCandidates.size() > 1) {
            return topCandidates.get(random.nextInt(topCandidates.size()));
        }

        return topCandidates.getFirst(); // Zwierzę z najwyższą energią
    }

    // Metoda rozwiązywania konfliktu o rozmnażanie
    public List<Animal> resolveReproductionConflict(List<Animal> animals) {
        if (animals.size() < 2) return Collections.emptyList();

        List<Animal> topCandidates = getTopCandidates(animals);

        if (topCandidates.size() > 2) {
            // Jeżeli więcej niż dwa mają remis, losowy wybór dwóch zwierząt
            Collections.shuffle(topCandidates, random);
            return topCandidates.subList(0, 2);
        }

        return topCandidates.size() >= 2 ? topCandidates.subList(0, 2) : animals.subList(0, 2);
    }
}
