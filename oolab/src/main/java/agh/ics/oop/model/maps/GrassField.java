package agh.ics.oop.model.maps;

import java.util.*;

import agh.ics.oop.model.map_elements.Boundary;
import agh.ics.oop.model.map_elements.Grass;
import agh.ics.oop.model.map_elements.Vector2d;
import agh.ics.oop.model.map_elements.WorldElement;
import org.apache.commons.collections4.list.TreeList;
import static java.lang.Math.random;

public class GrassField extends AbstractWorldMap {
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Vector2d vector0 = new Vector2d(0, 0);
    private final Map<Vector2d, Boolean> grassMap = new HashMap<>();

    private void fillGrassMap() {
        for(MapWidth)
    }


    public GrassField(int grassFields) {
        placeGrass(grassFields);
    }

    // dodac metode add grasses(liczba traw do postawienia?)?


    Map<Vector2d, Grass> getGrasses() {
        return Collections.unmodifiableMap(grasses);
    }


    // potrzebna metoda do wyznaczenia jakie pola moga miec wiecej trawy

    // musimy uwzgledniac pola co maja wieksza zszanse na trawe
    private void placeGrass(int grassCount) {
//        int maxSize = (int) Math.sqrt(grassCount * 10);
//        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(maxSize, maxSize, grassCount);
//
//        for (Vector2d grassPosition : randomPositionGenerator) {
//            if (!grasses.containsKey(grassPosition)) {
//                grasses.put(grassPosition, new Grass(grassPosition));
//            }
//        }
    }


    public void addGrass(Integer amount){



    }

    public List<Vector2d> tilesWithoutGrass(){
        return
    }




}


// dla tych co ma byc ich 80% pozycja 4 razy, a te co 20% tylko raz
class RandomPositionGenerator implements Iterable<Vector2d> {
    private final GrassField
    private final int maxWidth;
    private final int maxHeight;
    private final int grassCount;
    private final TreeList<Integer> availablePositions;

    public RandomPositionGenerator(int maxWidth, int maxHeight, int grassCount, TreeList<Integer> availablePositions) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.grassCount = grassCount;
        this.availablePositions = availablePositions;


        for (int i = 0; i < maxHeight * maxWidth; i++) {
            availablePositions.add(i);
        }
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return new RandomPositionIterator();
    }
    
    private Vector2d positionIndexToVector(Integer positionIndex){
        int x = positionIndex % maxWidth;
        int y = positionIndex / maxWidth;
        return new Vector2d(x,y);
    }

    private class RandomPositionIterator implements Iterator<Vector2d> {

        private int positionsGenerated = 0;

        @Override
        public boolean hasNext() {
            return !availablePositions.isEmpty() && grassCount > positionsGenerated;
        }

        @Override
        public Vector2d next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more positions to generate");
            }

            positionsGenerated++;
            return positionIndexToVector(
                availablePositions.remove(
                    (int) (availablePositions.size() * random())
                )
            );
        }
    }
}
