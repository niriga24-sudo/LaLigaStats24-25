package europestats.GUI;

import java.io.File;
import java.io.IOException;

import europestats.MAIN.App;
import europestats.SEGURETAT.GestorSeguretat;
import europestats.SEGURETAT.Sessio;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private ImageView imgLogo;

    private final GestorSeguretat gestorSeguretat = new GestorSeguretat();

    @FXML
    public void initialize() {
        carregarLogo();
    }

    private void carregarLogo() {
        try {
            File file = new File("LOGO/logo.png");
            if (file.exists()) {
                // Carreguem a 450px per fer 'supersampling' i que es vegi molt nítid a 180px
                Image image = new Image(file.toURI().toString(), 0, 450, true, true);
                imgLogo.setImage(image);
            }
        } catch (Exception e) {
            System.err.println("No s'ha pogut carregar el logo: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        errorLabel.setText("");

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setStyle("-fx-text-fill: #e67e22;");
            errorLabel.setText("❌ Si us plau, omple tots els camps.");
            return;
        }

        if (gestorSeguretat.intentarLogin(email, password)) {
            System.out.println("✅ Login correcte per a: " + Sessio.getInstancia().getEmail());
            errorLabel.setStyle("-fx-text-fill: #2ecc71;");
            errorLabel.setText("✅ Accés concedit. Entrant...");
            obrirAppPrincipal();
        } else {
            errorLabel.setStyle("-fx-text-fill: #e74c3c;");
            errorLabel.setText("❌ Email o contrasenya incorrectes.");
        }
    }

    private void obrirAppPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/europestats/GUI/main_layout.fxml"));
            Parent root = loader.load();

            Stage mainStage = new Stage();
            mainStage.setTitle("EUROPESTATS - Panell d'Administració");

            // Configurar la icona per evitar el pingüí de Java a la finestra principal
            App.configurarIcona(mainStage);

            mainStage.setMinWidth(1100);
            mainStage.setMinHeight(750);

            Scene scene = new Scene(root);
            mainStage.setScene(scene);

            // Tancar la finestra de Login actual
            Stage loginStage = (Stage) emailField.getScene().getWindow();
            loginStage.close();

            mainStage.show();
            mainStage.centerOnScreen();

        } catch (IOException e) {
            System.err.println("❌ Error en carregar el layout principal: " + e.getMessage());
            e.printStackTrace();
            errorLabel.setText("❌ Error crític al carregar l'interfície.");
        }
    }
}