package agh.ics.oop;

import agh.ics.oop.model.maps.AbstractWorldMap;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SimulationView extends VBox {
    private final Simulation simulation;
    private final Canvas canvas;
    private volatile boolean running = true;  // Czy symulacja działa
    private volatile boolean paused = false; // Czy symulacja jest wstrzymana

    public SimulationView(Simulation simulation) {
        this.simulation = simulation;
        this.canvas = new Canvas(800, 600);
        this.getChildren().add(canvas);

        // Dodanie przycisku pauzy/wznowienia
        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(event -> {
            paused = !paused; // Przełączanie między wstrzymaniem a wznowieniem
            pauseButton.setText(paused ? "Resume" : "Pause");
        });

        this.getChildren().add(pauseButton);
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
        while (running) {
            if (!paused && simulation.step()) {
                Platform.runLater(this::draw);
            }
            try {
                Thread.sleep(500); // 500ms na krok symulacji
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stopSimulation() {
        running = false; // Zatrzymuje symulację
    }
}
