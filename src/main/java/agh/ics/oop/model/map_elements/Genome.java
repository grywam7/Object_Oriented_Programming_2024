package agh.ics.oop.model.map_elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genome {
    List<Integer> list = new ArrayList<>();
    int currentGenomeIndex = 0;
    public static final Random random = new Random();


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

    public void setGenome(List<Integer> genome) {
        this.list = genome;
    }

    public List<Integer> getList() {
        return list;
    }

    public int getLenght(){
        return list.size();
    }

    @Override
    public String toString() {
        return "Genome: " + list.toString();
    }
}
