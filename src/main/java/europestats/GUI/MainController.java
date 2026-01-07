package europestats.GUI;

import java.awt.Desktop;
import java.io.File;

import europestats.CSV.ExportadorCSV;
import europestats.CSV.ImportadorCSV;
import europestats.MAIN.App;
import europestats.SEGURETAT.Sessio;
import europestats.SERVEIS.SistemaService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private StackPane contentArea;
    @FXML
    private VBox menuRajoles, sideBar;
    @FXML
    private Label lblEstatSistema;

    // Element per al Logo
    @FXML
    private ImageView imgLogo;

    // Rajoles
    @FXML
    private Button btnAPI, btnImportCSV, btnExportCSV;
    // Barra Lateral
    @FXML
    private Button btnSideAPI, btnSideImport, btnSideExport;

    private final SistemaService sistemaService = new SistemaService();
    private final ExportadorCSV exportadorCSV = new ExportadorCSV();

    @FXML
    public void initialize() {
        // 1. Carreguem el logo visualment
        carregarLogo();

        // 2. Verifiquem la connexió per activar/desactivar botons
        actualitzarEstatConexio();
    }

    /**
     * Carrega la imatge des de la ruta LOGO/logo.JPG
     */
    private void carregarLogo() {
        try {
            File file = new File("LOGO/logo.png");
            if (file.exists()) {
                // PARAMETRES DE LA IMAGE:
                // 1. URL
                // 2. Ample demanat (0 = automàtic)
                // 3. Alçada demanada (160 = el doble del fitHeight per fer "supersampling")
                // 4. Mantenir ratio (true)
                // 5. Suavitzat de qualitat (true) <--- AQUEST ÉS EL SECRET
                Image image = new Image(file.toURI().toString(), 0, 160, true, true);

                if (imgLogo != null) {
                    imgLogo.setImage(image);
                }
            }
        } catch (Exception e) {
            System.err.println("Error en la càrrega: " + e.getMessage());
        }
    }

    private void actualitzarEstatConexio() {
        boolean connectat = sistemaService.isBBDDConnectada();
        lblEstatSistema.setText(connectat ? "● MODE ONLINE" : "● MODE OFFLINE");
        lblEstatSistema.setStyle(connectat ? "-fx-text-fill: #27ae60;" : "-fx-text-fill: #e67e22;");

        // Control de seguretat per botons de rajoles
        if (btnAPI != null)
            btnAPI.setDisable(!connectat);
        if (btnImportCSV != null)
            btnImportCSV.setDisable(!connectat);
        if (btnExportCSV != null)
            btnExportCSV.setDisable(!connectat);

        // Control de seguretat per botons laterals
        if (btnSideAPI != null)
            btnSideAPI.setDisable(!connectat);
        if (btnSideImport != null)
            btnSideImport.setDisable(!connectat);
        if (btnSideExport != null)
            btnSideExport.setDisable(!connectat);
    }

    private void carregarVistaInterior(String fxml) {
        try {
            menuRajoles.setVisible(false);
            menuRajoles.setManaged(false);
            sideBar.setVisible(true);
            sideBar.setManaged(true);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/europestats/GUI/" + fxml));
            Parent view = loader.load();

            contentArea.getChildren().removeIf(node -> node != menuRajoles);
            contentArea.getChildren().add(view);
        } catch (Exception e) {
            mostrarAlerta("Error de Càrrega", "No s'ha pogut carregar la vista: " + fxml);
            e.printStackTrace();
        }
    }

    @FXML
    private void tornarAInici() {
        sideBar.setVisible(false);
        sideBar.setManaged(false);
        contentArea.getChildren().removeIf(node -> node != menuRajoles);
        menuRajoles.setVisible(true);
        menuRajoles.setManaged(true);
    }

    @FXML
    private void mostrarEquips() {
        carregarVistaInterior(sistemaService.isBBDDConnectada() ? "equips_view.fxml" : "equips_view_offline.fxml");
    }

    @FXML
    private void mostrarJugadors() {
        carregarVistaInterior(sistemaService.isBBDDConnectada() ? "jugadors_view.fxml" : "jugadors_view_offline.fxml");
    }

    @FXML
    private void mostrarAPI() {
        carregarVistaInterior("api_view.fxml");
    }

    @FXML
    private void ImportarCSV() {
        if (!sistemaService.isBBDDConnectada())
            return;

        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, "Vols importar de CSV a MySQL?", ButtonType.OK,
                ButtonType.CANCEL);
        if (dialog.showAndWait().orElse(null) == ButtonType.OK) {
            try {
                int[] totals = ImportadorCSV.importarTotDelsCSV();
                mostrarAlerta("Èxit", "Importats: " + totals[0] + " equips i " + totals[1] + " jugadors.");
            } catch (Exception e) {
                mostrarAlerta("Error", e.getMessage());
            }
        }
    }

    @FXML
    private void ExportarCSV() {
        if (!sistemaService.isBBDDConnectada())
            return;

        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, "Vols exportar de MySQL a CSV?", ButtonType.OK,
                ButtonType.CANCEL);
        if (dialog.showAndWait().orElse(null) == ButtonType.OK) {
            try {
                exportadorCSV.exportarTaulaEquips();
                exportadorCSV.exportarTaulaJugadors();
                mostrarAlerta("Èxit", "Exportació finalitzada.");
            } catch (Exception e) {
                mostrarAlerta("Error", e.getMessage());
            }
        }
    }

    @FXML
    private void ObrirCSV() {
        ButtonType bE = new ButtonType("Equips");
        ButtonType bJ = new ButtonType("Jugadors");
        Alert d = new Alert(Alert.AlertType.CONFIRMATION, "Tria fitxer:", bE, bJ, ButtonType.CANCEL);
        d.showAndWait().ifPresent(r -> {
            if (r == bE)
                obrirFitxer("DATA/equips.csv");
            else if (r == bJ)
                obrirFitxer("DATA/jugadors.csv");
        });
    }

    private void obrirFitxer(String path) {
        try {
            Desktop.getDesktop().open(new File(path));
        } catch (Exception e) {
            mostrarAlerta("Error", "No s'ha pogut obrir el fitxer.");
        }
    }

    @FXML
    private void tancarSessio() {
        Sessio.getInstancia().tancar();
        ((Stage) lblEstatSistema.getScene().getWindow()).close();
        try {
            new App().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }
}