package agh.ics.oop.model.map_elements;

public record Boundary(Vector2d bottomLeft, Vector2d topRight, int width, int height) {

    public Boundary(Vector2d bottomLeft, Vector2d topRight) {
        this(
            bottomLeft,
            topRight,
            bottomLeft.subtract(topRight).getX() + 1,
            bottomLeft.subtract(topRight).getY() + 1
        );
    }
}
