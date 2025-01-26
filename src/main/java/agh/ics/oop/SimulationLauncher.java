package agh.ics.oop;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.stage.FileChooser;

public class SimulationLauncher extends Application {
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showSettingsWindow();
    }

    private void showSettingsWindow() {

        GridPane settingsPane = new GridPane();
        settingsPane.setVgap(10);
        settingsPane.setHgap(10);

        // Pola wprowadzania parametrów symulacji
        TextField widthField = createLabeledField(settingsPane, "Width:", 1);
        TextField heightField = createLabeledField(settingsPane, "Height:", 2);
        TextField jungleHeightField = createLabeledField(settingsPane, "Jungle Height:", 3);
        TextField initialPlantsField = createLabeledField(settingsPane, "Initial Plants:", 4);
        TextField plantEnergyField = createLabeledField(settingsPane, "Plant Energy:", 5);
        TextField dailyPlantsField = createLabeledField(settingsPane, "Daily Plant Growth:", 6);
        TextField initialAnimalsField = createLabeledField(settingsPane, "Initial Animals:", 7);
        TextField animalEnergyField = createLabeledField(settingsPane, "Animal Energy:", 8);
        TextField sufficientEnergyField = createLabeledField(settingsPane, "Sufficient Energy for Breeding:", 9);
        TextField breedingEnergyLossField = createLabeledField(settingsPane, "Energy Loss on Breeding:", 10);
        TextField mutationCountField = createLabeledField(settingsPane, "Mutation Count:", 11);
        TextField genomeLengthField = createLabeledField(settingsPane, "Genome Length:", 12);
        TextField targetDayField = createLabeledField(settingsPane, "Target Day:", 13);
        TextField dailyEnergyLossField = createLabeledField(settingsPane, "Daily Energy Loss:", 14);

        // Checkboxes for map and animal modifications
        CheckBox mapModificationCheckbox = new CheckBox("Map Modification (Crawling Jungle)");
        CheckBox animalModificationCheckbox = new CheckBox("Animal Modification");
        settingsPane.add(mapModificationCheckbox, 0, 15, 2, 1);
        settingsPane.add(animalModificationCheckbox, 0, 16, 2, 1);

        // Button for starting simulation with user-defined parameters
        Button startButton = new Button("Start Simulation");
        startButton.setOnAction(event -> {
            try {
                // Parse input and start simulation
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                int jungleHeight = Integer.parseInt(jungleHeightField.getText());
                int initialPlants = Integer.parseInt(initialPlantsField.getText());
                int plantEnergy = Integer.parseInt(plantEnergyField.getText());
                int dailyPlants = Integer.parseInt(dailyPlantsField.getText());
                int initialAnimals = Integer.parseInt(initialAnimalsField.getText());
                int animalEnergy = Integer.parseInt(animalEnergyField.getText());
                int sufficientEnergy = Integer.parseInt(sufficientEnergyField.getText());
                int breedingEnergyLoss = Integer.parseInt(breedingEnergyLossField.getText());
                int mutationCount = Integer.parseInt(mutationCountField.getText());
                int genomeLength = Integer.parseInt(genomeLengthField.getText());
                int targetDay = Integer.parseInt(targetDayField.getText());
                int dailyEnergyLoss = Integer.parseInt(dailyEnergyLossField.getText());

                boolean mapModification = mapModificationCheckbox.isSelected();
                boolean animalModification = animalModificationCheckbox.isSelected();

                Simulation simulation = new Simulation(width, height, jungleHeight, mapModification, animalModification,
                        initialPlants, plantEnergy, dailyPlants, initialAnimals, animalEnergy,
                        sufficientEnergy, breedingEnergyLoss, dailyEnergyLoss, mutationCount, genomeLength, targetDay);

                showSimulationWindow(simulation);
            } catch (NumberFormatException e) {
                showError("Invalid input! Please enter valid numbers.");
            }
        });
        settingsPane.add(startButton, 0, 18);

        Button saveConfigButton = new Button("Save Configuration");
        saveConfigButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Configuration");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                SimulationConfig config = new SimulationConfig();
                configFromFields(config, widthField, heightField, jungleHeightField, initialPlantsField,
                        plantEnergyField, dailyPlantsField, initialAnimalsField, animalEnergyField,
                        sufficientEnergyField, breedingEnergyLossField, mutationCountField, genomeLengthField,
                        targetDayField, dailyEnergyLossField, mapModificationCheckbox, animalModificationCheckbox);

                saveConfig(config, file);
            }
        });

        Button loadConfigButton = new Button("Load Configuration");
        loadConfigButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Configuration");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                SimulationConfig config = loadConfig(file);
                if (config != null) {
                    fieldsFromConfig(config, widthField, heightField, jungleHeightField, initialPlantsField,
                            plantEnergyField, dailyPlantsField, initialAnimalsField, animalEnergyField,
                            sufficientEnergyField, breedingEnergyLossField, mutationCountField, genomeLengthField,
                            targetDayField, dailyEnergyLossField, mapModificationCheckbox, animalModificationCheckbox);
                }
            }
        });

        settingsPane.add(saveConfigButton, 0, 17);
        settingsPane.add(loadConfigButton, 1, 17);

        Button defaultButton = getDefaultButton(mapModificationCheckbox, animalModificationCheckbox);
        settingsPane.add(defaultButton, 1, 18);

        Scene settingsScene = new Scene(settingsPane, 500, 700);
        primaryStage.setTitle("Simulation Settings");
        primaryStage.setScene(settingsScene);
        primaryStage.show();
    }

    private Button getDefaultButton(CheckBox mapModificationCheckbox, CheckBox animalModificationCheckbox) {
        Button defaultButton = new Button("Run Default Parameters");
        defaultButton.setOnAction(event -> {
            int width = 60;
            int height = 60;
            int jungleHeight = 20;
            int initialPlants = 20;
            int plantEnergy = 25;
            int dailyPlants = 10;
            int initialAnimals = 4;
            int animalEnergy = 100;
            int sufficientEnergy = 50;
            int breedingEnergyLoss = 20;
            int mutationCount = 2;
            int genomeLength = 8;
            int targetDay = 100;
            int dailyEnergyLoss = 5;

            boolean mapModification = mapModificationCheckbox.isSelected();
            boolean animalModification = animalModificationCheckbox.isSelected();

            Simulation simulation = new Simulation(width, height, jungleHeight, mapModification, animalModification,
                    initialPlants, plantEnergy, dailyPlants, initialAnimals, animalEnergy,
                    sufficientEnergy, breedingEnergyLoss, dailyEnergyLoss, mutationCount, genomeLength, targetDay);

            showSimulationWindow(simulation);
        });
        return defaultButton;
    }

    private void showSimulationWindow(Simulation simulation) {
        SimulationView simulationView = new SimulationView(simulation);
        Scene simulationScene = new Scene(simulationView, 800, 650);

        Stage simulationStage = new Stage();
        simulationStage.setTitle("Simulation");
        simulationStage.setScene(simulationScene);

        simulationStage.setOnCloseRequest(event -> simulationView.stopSimulation());

        new Thread(simulationView::runSimulation).start();

        simulationStage.show();
    }

    private TextField createLabeledField(GridPane pane, String labelText, int row) {
        Label label = new Label(labelText);
        TextField textField = new TextField();
        pane.add(label, 0, row);
        pane.add(textField, 1, row);
        return textField;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveConfig(SimulationConfig config, File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            showError("Failed to save configuration: " + e.getMessage());
        }
    }

    private SimulationConfig loadConfig(File file) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, SimulationConfig.class);
        } catch (IOException e) {
            showError("Failed to load configuration: " + e.getMessage());
            return null;
        }
    }

    private void configFromFields(SimulationConfig config, TextField widthField, TextField heightField,
                                  TextField jungleHeightField, TextField initialPlantsField,
                                  TextField plantEnergyField, TextField dailyPlantsField,
                                  TextField initialAnimalsField, TextField animalEnergyField,
                                  TextField sufficientEnergyField, TextField breedingEnergyLossField,
                                  TextField mutationCountField, TextField genomeLengthField,
                                  TextField targetDayField, TextField dailyEnergyLossField,
                                  CheckBox mapModificationCheckbox, CheckBox animalModificationCheckbox) {
        config.width = Integer.parseInt(widthField.getText());
        config.height = Integer.parseInt(heightField.getText());
        config.jungleHeight = Integer.parseInt(jungleHeightField.getText());
        config.initialPlants = Integer.parseInt(initialPlantsField.getText());
        config.plantEnergy = Integer.parseInt(plantEnergyField.getText());
        config.dailyPlants = Integer.parseInt(dailyPlantsField.getText());
        config.initialAnimals = Integer.parseInt(initialAnimalsField.getText());
        config.animalEnergy = Integer.parseInt(animalEnergyField.getText());
        config.sufficientEnergy = Integer.parseInt(sufficientEnergyField.getText());
        config.breedingEnergyLoss = Integer.parseInt(breedingEnergyLossField.getText());
        config.mutationCount = Integer.parseInt(mutationCountField.getText());
        config.genomeLength = Integer.parseInt(genomeLengthField.getText());
        config.targetDay = Integer.parseInt(targetDayField.getText());
        config.dailyEnergyLoss = Integer.parseInt(dailyEnergyLossField.getText());
        config.mapModification = mapModificationCheckbox.isSelected();
        config.animalModification = animalModificationCheckbox.isSelected();
    }

    private void fieldsFromConfig(SimulationConfig config, TextField widthField, TextField heightField,
                                  TextField jungleHeightField, TextField initialPlantsField,
                                  TextField plantEnergyField, TextField dailyPlantsField,
                                  TextField initialAnimalsField, TextField animalEnergyField,
                                  TextField sufficientEnergyField, TextField breedingEnergyLossField,
                                  TextField mutationCountField, TextField genomeLengthField,
                                  TextField targetDayField, TextField dailyEnergyLossField,
                                  CheckBox mapModificationCheckbox, CheckBox animalModificationCheckbox) {
        widthField.setText(String.valueOf(config.width));
        heightField.setText(String.valueOf(config.height));
        jungleHeightField.setText(String.valueOf(config.jungleHeight));
        initialPlantsField.setText(String.valueOf(config.initialPlants));
        plantEnergyField.setText(String.valueOf(config.plantEnergy));
        dailyPlantsField.setText(String.valueOf(config.dailyPlants));
        initialAnimalsField.setText(String.valueOf(config.initialAnimals));
        animalEnergyField.setText(String.valueOf(config.animalEnergy));
        sufficientEnergyField.setText(String.valueOf(config.sufficientEnergy));
        breedingEnergyLossField.setText(String.valueOf(config.breedingEnergyLoss));
        mutationCountField.setText(String.valueOf(config.mutationCount));
        genomeLengthField.setText(String.valueOf(config.genomeLength));
        targetDayField.setText(String.valueOf(config.targetDay));
        dailyEnergyLossField.setText(String.valueOf(config.dailyEnergyLoss));
        mapModificationCheckbox.setSelected(config.mapModification);
        animalModificationCheckbox.setSelected(config.animalModification);
    }
}
