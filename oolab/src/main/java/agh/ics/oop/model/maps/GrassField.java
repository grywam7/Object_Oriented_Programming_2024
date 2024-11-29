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

    public GrassField(int grassFields) {
        placeGrass(grassFields);
    }

    // for test use only
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
        return super.isOccupied(position) || grasses.containsKey(position);
    }

    private void placeGrass(int grassCount) {
        int maxSize = (int) Math.sqrt(grassCount * 10);
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(maxSize, maxSize, grassCount);

        for (Vector2d grassPosition : randomPositionGenerator) {
            if (!grasses.containsKey(grassPosition)) {
                grasses.put(grassPosition, new Grass(grassPosition));
            }
        }
    }

    @Override
    public Collection<WorldElement> getElements() {
        Collection<WorldElement> combinedElements = new ArrayList<>(super.getElements());
        combinedElements.addAll(grasses.values());
        return Collections.unmodifiableCollection(combinedElements);
    }

    @Override
    public Boundary getCurrentBounds() {
        final Vector2d[] corners = new Vector2d[]{null, null};
        if (grasses.isEmpty() && animals.isEmpty()) {
            corners[0] = vector0;
            corners[1] = vector0;
        } else {
            updateCorners(corners, grasses.keySet());
            updateCorners(corners, animals.keySet());
        }
        return new Boundary(corners[0], corners[1]);
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
