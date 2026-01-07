package europestats.GUI;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import europestats.API.GestorAPI;
import europestats.SERVEIS.SistemaService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;

public class APIController {

    @FXML
    private ComboBox<LligaItem> comboLligues;
    @FXML
    private Button btnImportar, btnImportarLliga, btnCancelar;
    @FXML
    private Label lblEstat;
    @FXML
    private TextArea txtLog;
    @FXML
    private ProgressIndicator progressIndicator;

    private final GestorAPI gestorAPI = new GestorAPI();
    private final SistemaService sistemaService = new SistemaService();
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Referència a la tasca actual per poder cancel·lar-la
    private Task<Void> currentTask;

    private static class LligaItem {
        int id;
        String nom;

        LligaItem(int id, String nom) {
            this.id = id;
            this.nom = nom;
        }

        @Override
        public String toString() {
            return nom;
        }
    }

    @FXML
    public void initialize() {
        ObservableList<LligaItem> lligues = FXCollections.observableArrayList(
                new LligaItem(140, "La Liga (Espanya)"),
                new LligaItem(39, "Premier League (Anglaterra)"),
                new LligaItem(78, "Bundesliga (Alemanya)"),
                new LligaItem(135, "Serie A (Itàlia)"),
                new LligaItem(61, "Ligue 1 (França)"),
                new LligaItem(88, "Eredivisie (Països Baixos)"),
                new LligaItem(94, "Primeira Liga (Portugal)"));
        comboLligues.setItems(lligues);

        if (progressIndicator != null)
            progressIndicator.setVisible(false);
        if (btnCancelar != null)
            btnCancelar.setDisable(true);
    }

    private void afegirLog(String missatge) {
        String hora = LocalTime.now().format(timeFormatter);
        txtLog.appendText("[" + hora + "] " + missatge + "\n");
        txtLog.setScrollTop(Double.MAX_VALUE);
    }

    @FXML
    private void handleCancelar() {
        if (currentTask != null && currentTask.isRunning()) {
            afegirLog("⚠️ Sol·licitant cancel·lació...");
            currentTask.cancel(); // Això interromp el fil (Thread.interrupt())
            btnCancelar.setDisable(true);
        }
    }

    private void executarTasca(TascaAPI tasca, String missatgeInicial) {
        // Preparar UI
        btnImportar.setDisable(true);
        btnImportarLliga.setDisable(true);
        btnCancelar.setDisable(false);
        if (progressIndicator != null)
            progressIndicator.setVisible(true);

        txtLog.clear();
        afegirLog("🚀 " + missatgeInicial);

        currentTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Passem el callback al gestor. Si el fil s'interromp,
                // el GestorAPI ho detectarà gràcies als canvis que hem fet.
                tasca.run(missatge -> Platform.runLater(() -> {
                    afegirLog(missatge);
                    lblEstat.setText(missatge);
                }));

                // Només exportem si la tasca no ha estat cancel·lada
                if (!isCancelled()) {
                    afegirLog("💾 Generant fitxers CSV d'exportació...");
                    europestats.CSV.ExportadorCSV exportador = new europestats.CSV.ExportadorCSV();
                    exportador.exportarTaulaEquips();
                    exportador.exportarTaulaJugadors();
                }
                return null;
            }
        };

        // Gestió de finals de tasca
        currentTask.setOnSucceeded(e -> finalitzar("✅ Procés completat amb èxit."));

        currentTask.setOnFailed(e -> {
            finalitzar("❌ Error en el procés.");
            afegirLog("ERROR: " + currentTask.getException().getMessage());
        });

        currentTask.setOnCancelled(e -> {
            finalitzar("🛑 Procés aturat per l'usuari.");
        });

        Thread thread = new Thread(currentTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void finalitzar(String msg) {
        btnImportar.setDisable(false);
        btnImportarLliga.setDisable(false);
        btnCancelar.setDisable(true);
        if (progressIndicator != null)
            progressIndicator.setVisible(false);
        lblEstat.setText(msg);
        afegirLog(msg);
    }

    @FXML
    private void handleImportarLligaIndividual() {
        if (!validarConnexio())
            return;
        LligaItem sel = comboLligues.getValue();
        if (sel == null)
            return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Vols sincronitzar " + sel.nom + "? (~6 minuts)",
                ButtonType.YES, ButtonType.NO);
        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            executarTasca(logger -> gestorAPI.importarLligaIndividual(sel.id, logger), "Sincronitzant " + sel.nom);
        }
    }

    @FXML
    private void iniciarImportacioTotal() {
        if (!validarConnexio())
            return;
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Vols fer la importació massiva? (~45 minuts)",
                ButtonType.YES, ButtonType.NO);
        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            executarTasca(logger -> gestorAPI.executarImportacioEquipsEuropa(logger), "Sincronització Massiva");
        }
    }

    private boolean validarConnexio() {
        if (!sistemaService.isBBDDConnectada()) {
            lblEstat.setText("❌ Sense connexió a MySQL");
            return false;
        }
        return true;
    }

    @FunctionalInterface
    public interface TascaAPI {
        void run(GestorAPI.LogCallback logger);
    }
}