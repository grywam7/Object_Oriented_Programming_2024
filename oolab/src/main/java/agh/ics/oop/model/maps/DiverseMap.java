package agh.ics.oop.model.maps;

import agh.ics.oop.model.map_elements.*;
import agh.ics.oop.model.*;

import java.util.*;

public class map  {
    private final int width;
    private final int height;
    private final map<Vector2d, Grass> grasses = new HashMap<>();
    private final Set<Vector2d> jungleRegion = new HashSet<>();
    private final Random random;

    public map(int width, int height, int jungleWidth, int jungleHeight, int initialGrassCount) {
        this(width, height, jungleWidth, jungleHeight, initialGrassCount, new Random());
    }

    public map(int width, int height, int jungleWidth, int jungleHeight, int initialGrassCount, Random random) {
        super();
        this.width = width;
        this.height = height;
        this.random = random;

        defineJungleRegion(jungleWidth, jungleHeight);
        generateGrassFields(initialGrassCount);
    }

    private void defineJungleRegion(int jungleWidth, int jungleHeight) {
        int jungleStartX = (width - jungleWidth) / 2;
        int jungleStartY = (height - jungleHeight) / 2;

        for (int x = jungleStartX; x < jungleStartX + jungleWidth; x++) {
            for (int y = jungleStartY; y < jungleStartY + jungleHeight; y++) {
                jungleRegion.add(new Vector2d(x, y));
            }
        }
    }

    private void generateGrassFields(int initialGrassCount) {
        int jungleGrassCount = (int) (initialGrassCount * 0.7); // 70% in jungle
        int steppeGrassCount = initialGrassCount - jungleGrassCount;

        addGrass(jungleGrassCount, true);
        addGrass(steppeGrassCount, false);
    }

    private void addGrass(int count, boolean inJungle) {
        while (count > 0) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Vector2d position = new Vector2d(x, y);

            if (grasses.containsKey(position)) continue;

            boolean isInJungle = jungleRegion.contains(position);
            if (isInJungle == inJungle) {
                grasses.put(position, new Grass(position));
                count--;
            }
        }
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return isInBounds(position) && super.canMoveTo(position);
    }

    private boolean isInBounds(Vector2d position) {
        return position.follows(new Vector2d(0, 0)) && position.precedes(new Vector2d(width - 1, height - 1));
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return super.isOccupied(position) || grasses.containsKey(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement element = super.objectAt(position);
        if (element != null) {
            return element;
        }
        return grasses.get(position);
    }

    @Override
    public Collection<WorldElement> getElements() {
        List<WorldElement> elements = new ArrayList<>(super.getElements());
        elements.addAll(grasses.values());
        return elements;
    }

    public Boundary getCurrentBounds() {
        return new Boundary(new Vector2d(0, 0), new Vector2d(width - 1, height - 1));
    }
}
