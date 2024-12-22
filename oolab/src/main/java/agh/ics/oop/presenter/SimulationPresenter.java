package agh.ics.oop.presenter;

import agh.ics.oop.model.map_elements.MoveDirection;
import agh.ics.oop.model.map_elements.Vector2d;
import agh.ics.oop.model.maps.GrassField;
import agh.ics.oop.model.maps.MapChangeListener;
import agh.ics.oop.model.maps.WorldMap;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import agh.ics.oop.*;
import javafx.scene.layout.*;
import java.util.List;

public class SimulationPresenter implements MapChangeListener {

    private static final int CELL_WIDTH = 30;
    private static final int CELL_HEIGHT = 30;

    @FXML
    private Label statusLabel;
    @FXML
    private TextField movesTextField;
    @FXML
    private GridPane mapGrid;

    private WorldMap worldMap;

//    its useless now, i can delete it
    public void setWorldMap(WorldMap map){
        worldMap = map;
        ((agh.ics.oop.model.maps.AbstractWorldMap) this.worldMap).addObserver(this);
    }

    public void drawMap() {
        clearGrid();

        if (worldMap == null) {
            return;
        }

        var bounds = worldMap.getCurrentBounds();
        Vector2d lowerLeft = bounds.bottomLeft();
        Vector2d upperRight = bounds.topRight();

        int width = upperRight.getX() - lowerLeft.getX() + 1;
        int height = upperRight.getY() - lowerLeft.getY() + 1;

        mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        for (int i = 0; i < width; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        }
        mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        for (int i = 0; i < height; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        }

        for (int x = lowerLeft.getX(); x <= upperRight.getX(); x++) {
            Label xLabel = new Label(Integer.toString(x));
            mapGrid.add(xLabel, x - lowerLeft.getX() + 1, 0);
            GridPane.setHalignment(xLabel, HPos.CENTER);
        }

        Label cornerLabel = new Label("y\\x");
        mapGrid.add(cornerLabel, 0, 0);
        GridPane.setHalignment(cornerLabel, HPos.CENTER);

        for (int y = upperRight.getY(); y >= lowerLeft.getY(); y--) {
            Label yLabel = new Label(Integer.toString(y));
            int rowIndex = upperRight.getY() - y + 1;
            mapGrid.add(yLabel, 0, rowIndex);
            GridPane.setHalignment(yLabel, HPos.CENTER);
        }

        for (int y = upperRight.getY(); y >= lowerLeft.getY(); y--) {
            for (int x = lowerLeft.getX(); x <= upperRight.getX(); x++) {
                var element = worldMap.objectAt(new Vector2d(x, y));
                String textToDisplay = (element != null) ? element.toString() : "";
                Label cellLabel = new Label(textToDisplay);

                int colIndex = (x - lowerLeft.getX()) + 1;
                int rowIndex = (upperRight.getY() - y) + 1;

                mapGrid.add(cellLabel, colIndex, rowIndex);
                GridPane.setHalignment(cellLabel, HPos.CENTER);
            }
        }
        BorderPane.setAlignment(mapGrid, Pos.CENTER);
        StackPane.setAlignment(mapGrid, Pos.CENTER);

    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap();
            statusLabel.setText(message);
        });
    }

    @FXML
    public void onSimulationStartClicked() {
        String movesInput = movesTextField.getText();
        if (movesInput == null || movesInput.trim().isEmpty()) {
            statusLabel.setText("No moves provided!");
            return;
        }

        String[] movesArray = movesInput.split("[,\\s]+");

        List<MoveDirection> directions = OptionsParser.parse(movesArray);

        List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4));

        GrassField grassField = new GrassField(10);
        grassField.addObserver(this);

        this.worldMap = grassField;
        Simulation simulation = new Simulation(grassField, positions, directions);

        Thread simulationThread = new Thread(simulation);
        simulationThread.start();

        drawMap();
    }
}
