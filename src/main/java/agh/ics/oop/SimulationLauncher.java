package agh.ics.oop;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

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

        // Input fields for simulation parameters
        TextField widthField = createLabeledField(settingsPane, "Width:", 0);
        TextField heightField = createLabeledField(settingsPane, "Height:", 1);
        TextField jungleWidthField = createLabeledField(settingsPane, "Jungle Width:", 2);
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

        // Start button
        Button startButton = new Button("Start Simulation");
        startButton.setOnAction(event -> {
            try {
                // Parse input and start simulation
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                int jungleWidth = Integer.parseInt(jungleWidthField.getText());
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

                Simulation simulation = new Simulation(width, height, jungleWidth, jungleHeight, mapModification, animalModification,
                        initialPlants, plantEnergy, dailyPlants, initialAnimals, animalEnergy,
                        sufficientEnergy, breedingEnergyLoss, dailyEnergyLoss, mutationCount, genomeLength, targetDay);

                showSimulationWindow(simulation);
            } catch (NumberFormatException e) {
                showError("Invalid input! Please enter valid numbers.");
            }
        });


        Button defaultButton = new Button("Run Default Parameters");
        startButton.setOnAction(event -> {
            try {
                int width = 60;
                int height = 60;
                int jungleWidth = 20;
                int jungleHeight = 20;
                int initialPlants = 20;
                int plantEnergy = 25;
                int dailyPlants = 10;
                int initialAnimals = 4;
                int animalEnergy = 100;
                int sufficientEnergy = Integer.parseInt(sufficientEnergyField.getText());
                int breedingEnergyLoss = Integer.parseInt(breedingEnergyLossField.getText());
                int mutationCount = Integer.parseInt(mutationCountField.getText());
                int genomeLength = Integer.parseInt(genomeLengthField.getText());
                int targetDay = Integer.parseInt(targetDayField.getText());
                int dailyEnergyLoss = Integer.parseInt(dailyEnergyLossField.getText());

                boolean mapModification = mapModificationCheckbox.isSelected();
                boolean animalModification = animalModificationCheckbox.isSelected();

                Simulation simulation = new Simulation(width, height, jungleWidth, jungleHeight, mapModification, animalModification,
                        initialPlants, plantEnergy, dailyPlants, initialAnimals, animalEnergy,
                        sufficientEnergy, breedingEnergyLoss, dailyEnergyLoss, mutationCount, genomeLength, targetDay);

                showSimulationWindow(simulation);
            } catch (NumberFormatException e) {
                showError("Invalid input! Please enter valid numbers.");
            }
        });





        settingsPane.add(defaultButton, 3, 18, 2, 1);

        Scene settingsScene = new Scene(settingsPane, 400, 650);
        primaryStage.setTitle("Simulation Settings");
        primaryStage.setScene(settingsScene);
        primaryStage.show();
    }

    private void showSimulationWindow(Simulation simulation) {
        SimulationView simulationView = new SimulationView(simulation);
        Scene simulationScene = new Scene(simulationView, 800, 600);
        primaryStage.setTitle("Simulation");
        primaryStage.setScene(simulationScene);

        // Start simulation in a new thread
        new Thread(simulationView::runSimulation).start();
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
}
