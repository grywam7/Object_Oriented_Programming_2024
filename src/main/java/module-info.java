module agh.ics.oop {
    requires javafx.controls;
    requires javafx.fxml;

    opens agh.ics.oop to javafx.fxml;
    exports agh.ics.oop;
}
