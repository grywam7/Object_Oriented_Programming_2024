package agh.ics.oop.model;

public enum MapDirection {
    NORTH, EAST, SOUTH, WEST;

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "Północ";
            case SOUTH -> "Południe";
            case WEST -> "Zachód";
            case EAST -> "Wschód";
        };
    }

    public MapDirection next(){
        return switch (this){
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    public MapDirection previous(){
        return switch (this){
            case NORTH -> WEST;
            case EAST -> NORTH;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
        };
    }

    public Vector2d toUnitVector(MapDirection direction){
        return switch (direction) {
            case NORTH -> new Vector2d(1,0);
            case EAST -> new Vector2d(0,1);
            case SOUTH -> new Vector2d(-1,0);
            case WEST -> new Vector2d(0,-1);
        };
    }
}
