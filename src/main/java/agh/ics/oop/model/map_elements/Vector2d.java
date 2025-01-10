package agh.ics.oop.model.map_elements;

import java.util.*;

public class Vector2d {
    private final int x;
    private final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString() {
        return String.format("(%d,%d)", x, y);
    }

    public Boolean precedes(Vector2d other) {
        return other.x >= x && other.y >= y;
    }

    public Boolean follows(Vector2d other) {
        return other.x <= x && other.y <= y;
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(x + other.x, y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(x - other.x, y - other.y);
    }

    public Vector2d upperRight(Vector2d other) {
        return new Vector2d(Math.max(x, other.x), Math.max(y, other.y));
    }

    public Vector2d lowerLeft(Vector2d other) {
        return new Vector2d(Math.min(x, other.x), Math.min(y, other.y));
    }

    public Vector2d opposite(){
        return new Vector2d(-x,-y);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (!(other instanceof Vector2d that))
            return false;

        return this.x == that.x && this.y == that.y;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }

    public List<Vector2d> getNeighbors() {
        List<Vector2d> neighbors = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (!(dx == 0 && dy == 0)) { // Ignoruj samego siebie
                    neighbors.add(new Vector2d(this.x + dx, this.y + dy));
                }
            }
        }
        return neighbors;
    }

}
