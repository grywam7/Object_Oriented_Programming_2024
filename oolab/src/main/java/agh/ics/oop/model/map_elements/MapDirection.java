package agh.ics.oop.model.map_elements;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "Północ";
            case NORTHEAST -> "Północny Wschód";
            case EAST -> "Wschód";
            case SOUTHEAST -> "Południowy Wschód";
            case SOUTH -> "Południe";
            case SOUTHWEST -> "Południowy Zachód";
            case WEST -> "Zachód";
            case NORTHWEST -> "Północny Zachód";
        };
    }

    public String toShortString() {
        return switch (this) {
            case NORTH -> "N";
            case NORTHEAST -> "NE";
            case EAST -> "E";
            case SOUTHEAST -> "SE";
            case SOUTH -> "S";
            case SOUTHWEST -> "SW";
            case WEST -> "W";
            case NORTHWEST -> "NW";
        };
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> NORTHEAST;
            case NORTHEAST -> EAST;
            case EAST -> SOUTHEAST;
            case SOUTHEAST -> SOUTH;
            case SOUTH -> SOUTHWEST;
            case SOUTHWEST -> WEST;
            case WEST -> NORTHWEST;
            case NORTHWEST -> NORTH;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case NORTH -> NORTHWEST;
            case NORTHWEST -> WEST;
            case WEST -> SOUTHWEST;
            case SOUTHWEST -> SOUTH;
            case SOUTH -> SOUTHEAST;
            case SOUTHEAST -> EAST;
            case EAST -> NORTHEAST;
            case NORTHEAST -> NORTH;
        };
    }

    private final static Vector2d northVector = new Vector2d(0, 1);
    private final static Vector2d northeastVector = new Vector2d(1, 1);
    private final static Vector2d eastVector = new Vector2d(1, 0);
    private final static Vector2d southeastVector = new Vector2d(1, -1);
    private final static Vector2d southVector = new Vector2d(0, -1);
    private final static Vector2d southwestVector = new Vector2d(-1, -1);
    private final static Vector2d westVector = new Vector2d(-1, 0);
    private final static Vector2d northwestVector = new Vector2d(-1, 1);

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> northVector;
            case NORTHEAST -> northeastVector;
            case EAST -> eastVector;
            case SOUTHEAST -> southeastVector;
            case SOUTH -> southVector;
            case SOUTHWEST -> southwestVector;
            case WEST -> westVector;
            case NORTHWEST -> northwestVector;
        };
    }

    public MapDirection opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case NORTHEAST -> SOUTHWEST;
            case EAST -> WEST;
            case SOUTHEAST -> NORTHWEST;
            case SOUTH -> NORTH;
            case SOUTHWEST -> NORTHEAST;
            case WEST -> EAST;
            case NORTHWEST -> SOUTHEAST;
        };
    }
}
