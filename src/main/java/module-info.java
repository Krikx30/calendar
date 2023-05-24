module view.calendar {
    requires javafx.controls;
    requires javafx.fxml;


    opens view.calendar to javafx.fxml;
    exports view.calendar;
}