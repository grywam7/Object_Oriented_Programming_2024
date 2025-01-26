module agh.ics.oop {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens agh.ics.oop to javafx.fxml;
    exports agh.ics.oop;
}
