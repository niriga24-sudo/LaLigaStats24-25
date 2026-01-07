package europestats.MAIN;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        mostrarLogin();
    }

    /**
     * MÈTODE PÚBLIC: Ara el pots cridar des de qualsevol lloc
     */
    public static void configurarIcona(Stage stage) {
        try {
            File file = new File("LOGO/logo.png");
            if (file.exists()) {
                String url = file.toURI().toString();
                stage.getIcons().clear();
                // Afegim mides per a que Windows el vegi gran i nítid
                stage.getIcons().add(new Image(url));
                stage.getIcons().add(new Image(url, 32, 32, true, true));
                stage.getIcons().add(new Image(url, 64, 64, true, true));
                stage.getIcons().add(new Image(url, 128, 128, true, true));
            }
        } catch (Exception e) {
            System.err.println("❌ Error aplicant la icona: " + e.getMessage());
        }
    }

    public static void mostrarLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/europestats/GUI/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            mainStage.setTitle("EUROPE STATS - Login");
            mainStage.setScene(scene);
            configurarIcona(mainStage); // Apliquem al login
            mainStage.setResizable(false);
            mainStage.centerOnScreen();
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void obrirAppPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/europestats/GUI/main_layout.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            mainStage.setTitle("EUROPE STATS - Panell d'Administració");
            mainStage.setScene(scene);
            configurarIcona(mainStage); // Apliquem al main
            mainStage.setResizable(true);
            mainStage.centerOnScreen();
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}