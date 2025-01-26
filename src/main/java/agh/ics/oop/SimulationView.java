package agh.ics.oop;

import agh.ics.oop.model.maps.AbstractWorldMap;
import agh.ics.oop.model.map_elements.Animal;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import agh.ics.oop.model.map_elements.Vector2d;

import java.util.List;
import java.util.Map;

public class SimulationView extends HBox {
    private final Simulation simulation;
    private final Canvas canvas;

    //statystyki mapy
    private final Label dayLabel = new Label("Day: 0");
    private final Label animalCountLabel = new Label("Animals: 0");
    private final Label grassCountLabel = new Label("Grass: 0");
    private final Label freeFieldsLabel = new Label("Free fields: 0");
    private final Label avgEnergyLabel = new Label("Avg energy: 0");
    private final Label avgLifespanLabel = new Label("Avg lifespan: 0");
    private final Label avgChildrenLabel = new Label("Avg children: 0");
    private final VBox genotypesBox = new VBox();

    //statystyki zwierząt
    private final Label animalEnergyLabel = new Label("Energy: N/A");
    private final Label animalChildrenLabel = new Label("Children: N/A");
    private final Label animalGenotypeLabel = new Label("Genotype: N/A");
    private final Label animalActiveGeneLabel = new Label("Active Gene: N/A");
    private final Label animalEatenPlantsLabel = new Label("Eaten Plants: N/A");
    private final Label animalDescendantsLabel = new Label("Descendants: N/A");
    private final Label animalAgeLabel = new Label("Age: N/A");

    private volatile boolean running = true;
    private volatile boolean paused = false;

    private Animal selectedAnimal;

    public SimulationView(Simulation simulation) {
        this.simulation = simulation;
        this.canvas = new Canvas(620, 620);

        // Stylizacja statystyk
        // Etykiety statystyk
        Label titleLabel = new Label("Statystyki");
        titleLabel.setFont(new Font("Arial", 20));
        titleLabel.setStyle("-fx-font-weight: bold;");
        VBox statsPanel = new VBox(10);
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
                genotypesBox
        );

        // Panel zwierzęcia
        Label selectedAnimalTitle = new Label("Selected Animal Stats");
        selectedAnimalTitle.setFont(new Font("Arial", 20));
        selectedAnimalTitle.setStyle("-fx-font-weight: bold;");
        VBox selectedAnimalBox = new VBox();
        selectedAnimalBox.setPadding(new Insets(10));
        selectedAnimalBox.setSpacing(5);
        selectedAnimalBox.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-background-color: #f9f9f9;");
        selectedAnimalBox.getChildren().addAll(
                selectedAnimalTitle,
                animalEnergyLabel,
                animalChildrenLabel,
                animalGenotypeLabel,
                animalActiveGeneLabel,
                animalEatenPlantsLabel,
                animalDescendantsLabel,
                animalAgeLabel
        );

        // Panel sterowania
        HBox controls = new HBox(10);
        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(event -> {
            paused = !paused;
            pauseButton.setText(paused ? "Resume" : "Pause");
        });

        controls.getChildren().addAll(pauseButton);

        // Główne okno
        VBox mapArea = new VBox(10);
        mapArea.setPadding(new Insets(10));
        mapArea.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
        mapArea.getChildren().addAll(canvas, controls);

        // Obsługa kliknięć na mapie
        canvas.setOnMouseClicked(this::handleMapClick);

        this.getChildren().addAll(mapArea, statsPanel, selectedAnimalBox);
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

        Platform.runLater(() -> {
            dayLabel.setText("Day: " + day);
            animalCountLabel.setText("Animals: " + animalCount);
            grassCountLabel.setText("Grass: " + grassCount);
            freeFieldsLabel.setText("Free fields: " + freeFields);
            avgEnergyLabel.setText(String.format("Avg energy: %.2f", avgEnergy));
            avgLifespanLabel.setText(String.format("Avg lifespan: %.2f", avgLifespan));
            avgChildrenLabel.setText(String.format("Avg children: %.2f", avgChildren));

            genotypesBox.getChildren().clear();
            for (Map.Entry<String, Integer> entry : topGenotypes) {
                Label genotypeLabel = new Label(entry.getKey() + " (" + entry.getValue() + ")");
                genotypeLabel.setFont(new Font("Arial", 12));
                genotypesBox.getChildren().add(genotypeLabel);
            }

            if (selectedAnimal != null) {
            animalEnergyLabel.setText("Energy: " + selectedAnimal.getEnergy());
            animalChildrenLabel.setText("Children: " + selectedAnimal.getChildrenCount());
            animalGenotypeLabel.setText("Genotype: " + selectedAnimal.getGenome());
            animalActiveGeneLabel.setText("Used genome index: " + selectedAnimal.getGenome().getCurrentGenomeIndex());
            animalEatenPlantsLabel.setText("Plants eaten: " + selectedAnimal.getPlantsEatenCount());
            animalDescendantsLabel.setText("Descendant count: " + selectedAnimal.getDescendantCount());
            animalAgeLabel.setText("Age: " + selectedAnimal.getAge());
            }
        });
    }

    private void handleMapClick(MouseEvent event) {
        AbstractWorldMap map = simulation.getWorldMap();

        double cellWidth = canvas.getWidth() / map.getMapEdges().getWidth();
        double cellHeight = canvas.getHeight() / map.getMapEdges().getHeight();

        int clickedX = (int) (event.getX() / cellWidth);
        int clickedY = (int) (event.getY() / cellHeight);

        var animalsAtPosition = map.getAnimalsAt(new Vector2d(clickedX, clickedY));
        if (animalsAtPosition != null && !animalsAtPosition.isEmpty()) {
            selectedAnimal = animalsAtPosition.getFirst();

            Platform.runLater(() -> {
                animalEnergyLabel.setText("Energy: " + selectedAnimal.getEnergy());
                animalChildrenLabel.setText("Children: " + selectedAnimal.getChildrenCount());
                animalGenotypeLabel.setText("Genotype: " + selectedAnimal.getGenome());
                animalActiveGeneLabel.setText("Used genome index: " + selectedAnimal.getGenome().getCurrentGenomeIndex());
                animalEatenPlantsLabel.setText("Plants eaten: " + selectedAnimal.getPlantsEatenCount());
                animalDescendantsLabel.setText("Descendant count: " + selectedAnimal.getDescendantCount());
                animalAgeLabel.setText("Age: " + selectedAnimal.getAge());
            });
        }
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
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stopSimulation() {
        running = false;
    }
}
