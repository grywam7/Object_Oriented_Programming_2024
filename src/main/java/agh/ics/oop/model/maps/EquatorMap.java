package agh.ics.oop.model.maps;

import agh.ics.oop.model.map_elements.*;

import java.util.*;

public class EquatorMap extends AbstractWorldMap {
    private final Boundary equatorBoundary;

    public EquatorMap(Boundary mapEdges, Boundary equatorBoundary) {
        super(mapEdges);
        this.equatorBoundary = equatorBoundary;
    }

    @Override
    public List<Vector2d> getPriorityPlaces() {
        List<Vector2d> priorityPositions = new ArrayList<>();

        for (int x = equatorBoundary.bottomLeft().getX(); x <= equatorBoundary.topRight().getX(); x++) {
            for (int y = equatorBoundary.bottomLeft().getY(); y <= equatorBoundary.topRight().getY(); y++) {
                Vector2d position = new Vector2d(x, y);
                if (!grasses.containsKey(position)) {
                    priorityPositions.add(position);
                }
            }
        }

        return priorityPositions;
    }
}