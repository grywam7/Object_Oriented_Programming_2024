package agh.ics.oop;

public class World {
    public static void main(String[] args) {
        Simulation simulation = new Simulation(30, 30, 3, 3, true, false, 1,
        10, 5, 1, 1000, 50, 50, 5,
        0, 16, 10);
        simulation.step();
    }
}
