package agh.ics.oop.model.map_elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genome {
    List<Integer> list = new ArrayList<>(); // modyfikator dostępu?
    int currentGenomeIndex = 0; // modyfikator dostępu?
    public static final Random random = new Random(); // public?

    public Genome(int genomeLength) {
        for (int i = 0; i < genomeLength; i++) {
            list.add(random.nextInt(8)); // Wartości od 0 do 7
        }
    }

    public int nextMove() {
        int move = list.get(this.currentGenomeIndex);
        this.currentGenomeIndex = (currentGenomeIndex + 1) % list.size();
        return move;
    }

    public int getCurrentGenomeIndex() {
        return this.currentGenomeIndex;
    }

    public void setGenome(List<Integer> genome) { // Panowie już wiedzą co
        this.list = genome;
    }

    public List<Integer> getList() { // nazwa
        return list; // dehermetyzacja... gdyby nie to, że i tak mamy publiczny setter
    }

    public int getLength() {
        return list.size();
    }

    @Override
    public String toString() {
        return "Genome: " + list.toString();
    }
}
