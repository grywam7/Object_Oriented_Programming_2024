package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {
    private int updateCount = 0;

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        updateCount++;
        System.out.println("Map Update #" + updateCount + ":");
        System.out.println("Operation: " + message);
        System.out.println("Map State:");
        System.out.println(worldMap.toString());
        System.out.println("------------------------------------");
    }
}
