package agh.ics.oop;

import agh.ics.oop.model.maps.AbstractWorldMap;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Map;

public class SimulationView extends HBox {
    private final Simulation simulation;
    private final Canvas canvas;

    // Etykiety statystyk
    private final Label titleLabel = new Label("Statystyki");
    private final Label dayLabel = new Label("Day: 0");
    private final Label animalCountLabel = new Label("Animals: 0");
    private final Label grassCountLabel = new Label("Grass: 0");
    private final Label freeFieldsLabel = new Label("Free fields: 0");
    private final Label avgEnergyLabel = new Label("Avg energy: 0");
    private final Label avgLifespanLabel = new Label("Avg lifespan: 0");
    private final Label avgChildrenLabel = new Label("Avg children: 0");
    private final VBox genotypesBox = new VBox(); // Osobny VBox na genotypy

    private volatile boolean running = true; // Czy symulacja działa
    private volatile boolean paused = false; // Czy symulacja jest wstrzymana

    public SimulationView(Simulation simulation) {
        this.simulation = simulation;
        this.canvas = new Canvas(620, 620);

        // Stylizacja statystyk
        titleLabel.setFont(new Font("Arial", 20));
        titleLabel.setStyle("-fx-font-weight: bold;");
        VBox statsPanel = new VBox(10); // 10px odstępu między elementami
        statsPanel.setPadding(new Insets(10));
        statsPanel.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-background-color: #f4f4f4;");
        statsPanel.getChildren().addAll(
                titleLabel,
                dayLabel,
                animalCountLabel,
                grassCountLabel,
                freeFieldsLabel,
                avgEnergyLabel,
                avgLifespanLabel,
                avgChildrenLabel,
                new Label("Top genotypes:"),
                genotypesBox // Dodanie VBoxa na genotypy
        );

        // Panel sterowania (pauza i stop)
        HBox controls = new HBox(10); // Odstęp między przyciskami
        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(event -> {
            paused = !paused; // Przełączanie między pauzą a wznowieniem
            pauseButton.setText(paused ? "Resume" : "Pause");
        });

        Button stopButton = new Button("Stop");
        stopButton.setOnAction(event -> stopSimulation());

        controls.getChildren().addAll(pauseButton, stopButton);

        // Główne okno
        VBox mapArea = new VBox(10);
        mapArea.setPadding(new Insets(10));
        mapArea.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
        mapArea.getChildren().addAll(canvas, controls);

        this.getChildren().addAll(mapArea, statsPanel); // Dodanie mapy i panelu statystyk
        draw();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        AbstractWorldMap map = simulation.getWorldMap();

        double cellWidth = canvas.getWidth() / map.getMapEdges().getWidth();
        double cellHeight = canvas.getHeight() / map.getMapEdges().getHeight();

        // Rysowanie trawy
        map.getGrasses().forEach((position, grass) -> {
            gc.setFill(Color.GREEN);
            gc.fillRect(position.getX() * cellWidth, position.getY() * cellHeight, cellWidth, cellHeight);
        });

        // Rysowanie zwierząt
        map.getAnimals().forEach((position, animals) -> {
            gc.setFill(Color.RED);
            gc.fillOval(position.getX() * cellWidth, position.getY() * cellHeight, cellWidth, cellHeight);
        });

        // Aktualizacja statystyk
        updateStats();
    }

    private void updateStats() {
        AbstractWorldMap map = simulation.getWorldMap();
        int day = simulation.getCurrentDay();
        int animalCount = map.countAnimals();
        int grassCount = map.countGrass();
        int freeFields = map.freeFields();
        List<Map.Entry<String, Integer>> topGenotypes = map.printTop3Genotypes();
        float avgEnergy = (float) animalCount == 0 ? 0 : map.calculateAverageEnergy();
        float avgLifespan = map.averageLifespan();
        float avgChildren = calculateAverageChildren(map);

        // Aktualizacja etykiet
        Platform.runLater(() -> {
            dayLabel.setText("Day: " + day);
            animalCountLabel.setText("Animals: " + animalCount);
            grassCountLabel.setText("Grass: " + grassCount);
            freeFieldsLabel.setText("Free fields: " + freeFields);
            avgEnergyLabel.setText(String.format("Avg energy: %.2f", avgEnergy));
            avgLifespanLabel.setText(String.format("Avg lifespan: %.2f", avgLifespan));
            avgChildrenLabel.setText(String.format("Avg children: %.2f", avgChildren));

            // Wyświetlenie genotypów w osobnych wierszach
            genotypesBox.getChildren().clear(); // Usunięcie starych genotypów
            for (Map.Entry<String, Integer> entry : topGenotypes) {
                Label genotypeLabel = new Label(entry.getKey() + " (" + entry.getValue() + ")");
                genotypeLabel.setFont(new Font("Arial", 12));
                genotypesBox.getChildren().add(genotypeLabel);
            }
        });
    }

    private float calculateAverageChildren(AbstractWorldMap map) {
        int totalChildren = 0;
        int animalCount = map.countAnimals();

        for (var entry : map.getAnimals().values()) {
            for (var animal : entry) {
                totalChildren += animal.getChildrenCount();
            }
        }
        return animalCount == 0 ? 0 : (float) totalChildren / animalCount;
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
