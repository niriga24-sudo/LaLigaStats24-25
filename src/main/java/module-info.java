module europestats {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.sql;
    requires com.google.gson;
    requires java.net.http; // Permet usar HttpClient
    requires java.desktop;

    // Permet que JavaFX llegeixi els teus controladors i classes
    opens europestats.GUI to javafx.fxml;
    opens europestats.CLASES to javafx.base, com.google.gson;
    opens europestats.JSON to com.google.gson;

    exports europestats.MAIN;
    exports europestats.GUI;
    exports europestats.API;
}