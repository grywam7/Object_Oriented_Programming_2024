package agh.ics.oop.model;

import java.util.*;

import org.apache.commons.collections4.list.TreeList;
import static java.lang.Math.random;

public class GrassField extends AbstractWorldMap{
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Vector2d vector0 = new Vector2d(0,0);

    public GrassField(int grassFields){
        placeGrass(grassFields);
    }

    Map<Vector2d, Grass> getGrasses() {
        return Collections.unmodifiableMap(grasses);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement animal = super.objectAt(position);
        return animal != null ? animal : grasses.get(position);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return grasses.containsKey(position) || super.isOccupied(position);
    }

    public void placeGrass(int grassCount) {
        int maxSize = (int) Math.sqrt(grassCount * 10);
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(maxSize, maxSize, grassCount);

        for (Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    public String toString(){
        Vector2d[] corners = mapCorners();
        return visualizer.draw(corners[0], corners[1]);
    }

    @Override
    public Map<Vector2d, WorldElement> getElements() {
        Map<Vector2d, WorldElement> combinedElements = new HashMap<>(super.getElements());
        combinedElements.putAll(grasses);
        return Collections.unmodifiableMap(combinedElements);
    }

     Vector2d[] mapCorners() {
        final Vector2d[] corners = new Vector2d[] { null, null };
        if( grasses.isEmpty() && animals.isEmpty()){
            corners[0] = vector0;
            corners[1] = vector0;
        } else {
            updateCorners(corners, grasses.keySet());
            updateCorners(corners, animals.keySet());
        }
        return corners;
    }

    private void updateCorners(Vector2d[] corners, Set<Vector2d> vectors) {
        for (Vector2d vector : vectors) {
            corners[0] = corners[0] == null ? vector : corners[0].lowerLeft(vector);
            corners[1] = corners[1] == null ? vector : corners[1].upperRight(vector);
        }
    }
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
