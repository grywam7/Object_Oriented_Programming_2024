package agh.ics.oop.model.maps;

import java.util.*;
import agh.ics.oop.model.map_elements.*;

public class CrawlingJungleMap extends AbstractWorldMap {

    public CrawlingJungleMap(Boundary mapEdges) {
        super(mapEdges);
    }

    // Własna metoda rozstawiania trawy
    private Vector2d getRandomNearbyPosition() {
        Random random = new Random();
        List<Vector2d> nearbyPositions = new ArrayList<>();

        for (Vector2d grassPosition : grasses.keySet()) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    Vector2d nearbyPosition = grassPosition.add(new Vector2d(dx, dy));
                    if (getMapEdges().contains(nearbyPosition)) {
                        nearbyPositions.add(nearbyPosition);
                    }
                }
            }
        }

        if (!nearbyPositions.isEmpty()) {
            return nearbyPositions.get(random.nextInt(nearbyPositions.size()));
        }

        // Fallback to a random position on the map
        int randomX = random.nextInt(getMapEdges().getWidth()) + getMapEdges().bottomLeft().getX();
        int randomY = random.nextInt(getMapEdges().getHeight()) + getMapEdges().bottomLeft().getY();
        return new Vector2d(randomX, randomY);
    }

    @Override
    public void growGrass(int numberOfGrass) {
        Random random = new Random();

        for (int i = 0; i < numberOfGrass; i++) {
            Vector2d newGrassPosition;

            if (random.nextInt(100) < 80) {
                newGrassPosition = getRandomNearbyPosition();
            } else {
                int randomX = random.nextInt(getMapEdges().getWidth()) + getMapEdges().bottomLeft().getX();
                int randomY = random.nextInt(getMapEdges().getHeight()) + getMapEdges().bottomLeft().getY();
                newGrassPosition = new Vector2d(randomX, randomY);
            }

            // Check for null and avoid placing grass in an occupied position
            if (newGrassPosition != null && !grasses.containsKey(newGrassPosition)) {
                placeGrass(newGrassPosition);
            }
        }
    }


    private void placeGrass(Vector2d position) {
        grasses.put(position, new Grass(position));
    }


}
