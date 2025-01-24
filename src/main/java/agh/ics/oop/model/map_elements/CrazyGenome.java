package agh.ics.oop.model.map_elements;


public class CrazyGenome extends Genome {
    public CrazyGenome(int genomeLength) {
        super(genomeLength);
    }

    @Override
    public int nextMove(){
        if (random.nextInt(100) < 80) {
            // 80% szansy na przejście do kolejnego genu w kolejności
            return super.nextMove();
        } else {
            // 20% szansy na losowy indeks
            this.currentGenomeIndex = random.nextInt(this.getLenght());
            return list.get(this.currentGenomeIndex);
        }
    }
}
