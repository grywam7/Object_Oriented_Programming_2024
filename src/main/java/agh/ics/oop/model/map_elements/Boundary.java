package agh.ics.oop.model.map_elements;

public record Boundary(Vector2d bottomLeft, Vector2d topRight) {

    public int getWidth() {
        return topRight.subtract(bottomLeft).getX() + 1;
    }

    public int getHeight() {
        return topRight.subtract(bottomLeft).getY() + 1;
    }

    public boolean contains(Vector2d position) {
        return position.getX() >= bottomLeft.getX() && position.getX() <= topRight.getX()
                && position.getY() >= bottomLeft.getY() && position.getY() <= topRight.getY();
    }

}
