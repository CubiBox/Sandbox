module fr.cubibox.backroom2_5d {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens fr.cubibox.backroom2_5d to javafx.fxml;
    exports fr.cubibox.backroom2_5d;
}