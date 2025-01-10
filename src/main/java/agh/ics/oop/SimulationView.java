package agh.ics.oop;

import agh.ics.oop.model.maps.AbstractWorldMap;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SimulationView extends VBox {
    private final Simulation simulation;
    private final Canvas canvas;

    public SimulationView(Simulation simulation) {
        this.simulation = simulation;
        this.canvas = new Canvas(800, 600);
        this.getChildren().add(canvas);
        draw();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        AbstractWorldMap map = simulation.getWorldMap();

        double cellWidth = canvas.getWidth() / map.getMapEdges().getWidth();
        double cellHeight = canvas.getHeight() / map.getMapEdges().getHeight();

        map.getGrasses().forEach((position, grass) -> {
            gc.setFill(Color.GREEN);
            gc.fillRect(position.getX() * cellWidth, position.getY() * cellHeight, cellWidth, cellHeight);
        });

        map.getAnimals().forEach((position, animals) -> {
            gc.setFill(Color.RED);
            gc.fillOval(position.getX() * cellWidth, position.getY() * cellHeight, cellWidth, cellHeight);
        });
    }

    public void runSimulation() {
        while (simulation.step()) {
            Platform.runLater(this::draw);
            try {
                Thread.sleep(500); // 1 second per day
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
