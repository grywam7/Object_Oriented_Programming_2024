package agh.ics.oop.model;

import java.util.NoSuchElementException;
import java.util.Iterator;
import org.apache.commons.collections4.list.TreeList;
import static java.lang.Math.random;

public class GrassField implements WorldMap{
}

class RandomPositionGenerator implements Iterable<Vector2d> {

    private final int maxWidth;
    private final int maxHeight;
    private final int grassCount;
    private final TreeList<Integer> availablePositions = new TreeList<>();

    public RandomPositionGenerator(int maxWidth, int maxHeight, int grassCount) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.grassCount = grassCount;
        
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
