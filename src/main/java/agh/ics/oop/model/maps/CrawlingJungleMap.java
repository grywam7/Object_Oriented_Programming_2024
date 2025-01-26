package agh.ics.oop.model.maps;

import agh.ics.oop.model.map_elements.*;

import java.util.*;

public class CrawlingJungleMap extends AbstractWorldMap {

    public CrawlingJungleMap(Boundary mapEdges) {
        super(mapEdges);
    }

    @Override
    public List<Vector2d> getPriorityPlaces() {
        Set<Vector2d> priorityPositions = new HashSet<>();

        for (Vector2d grassPosition : grasses.keySet()) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue; // Pomijamy pozycję samej trawy
                    Vector2d nearbyPosition = grassPosition.add(new Vector2d(dx, dy));
                    if (getMapEdges().contains(nearbyPosition) && !grasses.containsKey(nearbyPosition)) {
                        // Dodajemy pozycję, jeśli jest w granicach mapy i nie ma tam trawy
                        priorityPositions.add(nearbyPosition);
                    }
                }
            }
        }

        return new ArrayList<>(priorityPositions);
    }

}
