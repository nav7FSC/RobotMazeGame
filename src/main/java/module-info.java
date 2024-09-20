module org.example.mazewithrobot {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.mazewithrobot to javafx.fxml;
    exports org.example.mazewithrobot;
}