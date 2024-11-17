package agh.ics.oop.model;

import java.util.Iterator;
import org.apache.commons.collections4.list.TreeList;
import static java.lang.Math.random;

public class GrassField implements WorldMap{
}

class RandomPositionGenerator implements Iterable<Vector2d> {

    private int availablePositionsCount;
    private final int maxWidth;
    private final int maxHeight;
    private final int grassCount;
    private final TreeList<Integer> availablePositions = new TreeList<>();

    public RandomPositionGenerator(int maxWidth, int maxHeight, int grassCount) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.grassCount = grassCount;
        availablePositionsCount = maxHeight * maxHeight;
        
        for (int i = 0; i < availablePositionsCount; i++) {
            availablePositions.add(i);
        }
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return new RandomPositionIterator();
    }
    
    private Vector2d positionIndexToVector(Integer positionIndex){
        int x = positionIndex % maxWidth;
        int y = positionIndex / maxHeight;
        return new Vector2d(x,y);
    }

    private class RandomPositionIterator implements Iterator<Vector2d> {

        private int positionsGenerated = 0;

        @Override
        public boolean hasNext() {
            return availablePositionsCount > 0 && grassCount > positionsGenerated;
        }

        @Override
        public Vector2d next() {
            if (!hasNext()) {
                return null;
            }

            positionsGenerated++;
            availablePositionsCount--;
            return positionIndexToVector(
                availablePositions.remove(
                    (int) ((availablePositionsCount - positionsGenerated - 1) * random())
                )
            );
        }
    }
}