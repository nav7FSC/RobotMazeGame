module org.example.mazewithrobot {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.mazewithrobot to javafx.fxml;
    exports org.example.mazewithrobot;
}