package agh.ics.oop.model.maps;

import java.util.*;
import agh.ics.oop.model.map_elements.*;

public class EquatorMap extends AbstractWorldMap {
    private final Boundary equatorBoundary;

    public EquatorMap(Boundary mapEdges, Boundary equatorBoundary) {
        super(mapEdges);
        this.equatorBoundary = equatorBoundary;
    }

    @Override
    public void growGrass(int numberOfGrass) {
        Random random = new Random();
        int grassInEquator = (int) (numberOfGrass * 0.8);
        int grassOutsideEquator = numberOfGrass - grassInEquator;

        placeGrassInBoundary(equatorBoundary, grassInEquator, random);
        placeGrassInBoundary(getMapEdges(), grassOutsideEquator, random, equatorBoundary);
    }

    private void placeGrassInBoundary(Boundary boundary, int count, Random random) {
        placeGrassInBoundary(boundary, count, random, null);
    }

    private void placeGrassInBoundary(Boundary boundary, int count, Random random, Boundary exclusionBoundary) {
        int attempts = 0;
        while (count > 0 && attempts < count * 10) {
            attempts++;
            int randomX = random.nextInt(boundary.getWidth()) + boundary.bottomLeft().getX();
            int randomY = random.nextInt(boundary.getHeight()) + boundary.bottomLeft().getY();
            Vector2d position = new Vector2d(randomX, randomY);

            if (exclusionBoundary != null && exclusionBoundary.contains(position)) continue;

            if (!getGrasses().containsKey(position)) {
                placeGrass(position);
                count--;
            }
        }
    }

    private void placeGrass(Vector2d position) {
        grasses.put(position, new Grass(position));
    }
}
