package europestats.GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import europestats.CLASES.Equip;
import europestats.CSV.EscriptorCSV;
import europestats.CSV.LectorCSV;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class EquipsOfflineController {

    @FXML
    private TableView<Equip> taulaEquips;

    // Sincronitzat amb els fx:id del FXML
    @FXML
    private TableColumn<Equip, Integer> colId, colIdLliga, colPos, colPunts, colPJ, colVictories, colEmpats,
            colDerrotes, colGolsM, colGolsE, colDiff;
    @FXML
    private TableColumn<Equip, String> colLliga, colEquip;

    @FXML
    private TextField txtIdEquip, txtNom, txtIdLliga, txtLligaNom, txtPosicio, txtPunts, txtPartits, txtVictories,
            txtEmpats, txtDerrotes, txtGolsM, txtGolsE;
    @FXML
    private TextField filtreField;

    private final ObservableList<Equip> llistaEquips = FXCollections.observableArrayList();
    private FilteredList<Equip> dadesFiltrades;

    private final String RUTA_CSV = "DATA/equips.csv";

    @FXML
    public void initialize() {
        configurarColumnes();
        configurarFiltre();

        taulaEquips.getSelectionModel().selectedItemProperty().addListener((obs, vell, nou) -> {
            if (nou != null)
                omplirFormulari(nou);
        });

        carregarDadesCSV();
    }

    private void configurarColumnes() {
        // ATENCIÓ: Els noms dins de PropertyValueFactory han de coincidir amb els
        // getters de la teva classe Equip (ex: getID -> "ID", getNom_Equip ->
        // "nom_Equip")
        colId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colIdLliga.setCellValueFactory(new PropertyValueFactory<>("idLliga"));
        colLliga.setCellValueFactory(new PropertyValueFactory<>("Lliga"));
        colPos.setCellValueFactory(new PropertyValueFactory<>("Posicio"));
        colEquip.setCellValueFactory(new PropertyValueFactory<>("nom_Equip"));
        colPunts.setCellValueFactory(new PropertyValueFactory<>("Punts"));
        colPJ.setCellValueFactory(new PropertyValueFactory<>("Partits_Jugats"));
        colVictories.setCellValueFactory(new PropertyValueFactory<>("Victories"));
        colEmpats.setCellValueFactory(new PropertyValueFactory<>("Empats"));
        colDerrotes.setCellValueFactory(new PropertyValueFactory<>("Derrotes"));
        colGolsM.setCellValueFactory(new PropertyValueFactory<>("Gols_Marcats"));
        colGolsE.setCellValueFactory(new PropertyValueFactory<>("Gols_Encaixats"));
        colDiff.setCellValueFactory(new PropertyValueFactory<>("Diferencia_Gols"));

        dadesFiltrades = new FilteredList<>(llistaEquips, p -> true);
        taulaEquips.setItems(dadesFiltrades);
    }

    private void configurarFiltre() {
        filtreField.textProperty().addListener((observable, oldValue, newValue) -> {
            dadesFiltrades.setPredicate(equip -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                // Verificació de seguretat per evitar NullPointer si el nom o lliga són nulls
                // al CSV
                String nom = equip.getNom_Equip() != null ? equip.getNom_Equip().toLowerCase() : "";
                String lliga = equip.getLliga() != null ? equip.getLliga().toLowerCase() : "";

                return nom.contains(lowerCaseFilter) || lliga.contains(lowerCaseFilter);
            });
        });
    }

    @FXML
    public void carregarDadesCSV() {
        llistaEquips.clear();
        // Cridem al teu LectorCSV que ja llegeix directament del fitxer
        List<Equip> dades = LectorCSV.carregarEquipsDesDeFitxer(RUTA_CSV);
        if (dades != null && !dades.isEmpty()) {
            llistaEquips.addAll(dades);
            System.out.println("✅ OK: " + dades.size() + " equips carregats des de CSV.");
        } else {
            System.out.println("⚠️ No s'han trobat dades o el CSV està buit a: " + RUTA_CSV);
        }
    }

    private void omplirFormulari(Equip e) {
        txtIdEquip.setText(String.valueOf(e.getID()));
        txtNom.setText(e.getNom_Equip());
        txtIdLliga.setText(String.valueOf(e.getIdLliga()));
        txtLligaNom.setText(e.getLliga());
        txtPosicio.setText(String.valueOf(e.getPosicio()));
        txtPunts.setText(String.valueOf(e.getPunts()));
        txtPartits.setText(String.valueOf(e.getPartits_Jugats()));
        txtVictories.setText(String.valueOf(e.getVictories()));
        txtEmpats.setText(String.valueOf(e.getEmpats()));
        txtDerrotes.setText(String.valueOf(e.getDerrotes()));
        txtGolsM.setText(String.valueOf(e.getGols_Marcats()));
        txtGolsE.setText(String.valueOf(e.getGols_Encaixats()));

        gestionarEdicioCamps(false);
    }

    private void gestionarEdicioCamps(boolean editable) {
        txtIdEquip.setEditable(editable);
        txtNom.setEditable(editable);
        txtIdLliga.setEditable(editable);
        txtLligaNom.setEditable(editable);

        String colorFons = editable ? "white" : "#ecf0f1";
        String estil = "-fx-background-color: " + colorFons + "; -fx-border-color: #bdc3c7; -fx-background-radius: 5;";

        txtIdEquip.setStyle(estil);
        txtNom.setStyle(estil);
        txtIdLliga.setStyle(estil);
        txtLligaNom.setStyle(estil);
    }

    @FXML
    private void guardarCanvi() {
        Equip seleccionat = taulaEquips.getSelectionModel().getSelectedItem();
        boolean esNou = (seleccionat == null);

        try {
            if (txtIdEquip.getText().isEmpty() || txtIdLliga.getText().isEmpty()) {
                mostrarAlerta("Camps buits", "L'ID de l'Equip i de la Lliga són obligatoris.");
                return;
            }

            int idEquip = Integer.parseInt(txtIdEquip.getText());

            if (esNou) {
                boolean jaExisteix = llistaEquips.stream().anyMatch(e -> e.getID() == idEquip);
                if (jaExisteix) {
                    mostrarAlerta("ID ja registrat", "Aquest ID d'equip ja existeix al fitxer.");
                    return;
                }
                // Creem l'objecte nou
                seleccionat = new Equip(idEquip, 0, "", 0, "", 0, 0, 0, 0, 0, 0, 0, 0);
            }

            // Actualitzem l'objecte amb les dades dels TextFields
            seleccionat.setNom_Equip(txtNom.getText());
            seleccionat.setIdLliga(Integer.parseInt(txtIdLliga.getText()));
            seleccionat.setLliga(txtLligaNom.getText());
            seleccionat.setPosicio(Integer.parseInt(txtPosicio.getText()));
            seleccionat.setPunts(Integer.parseInt(txtPunts.getText()));
            seleccionat.setPartits_Jugats(Integer.parseInt(txtPartits.getText()));
            seleccionat.setVictories(Integer.parseInt(txtVictories.getText()));
            seleccionat.setEmpats(Integer.parseInt(txtEmpats.getText()));
            seleccionat.setDerrotes(Integer.parseInt(txtDerrotes.getText()));
            seleccionat.setGols_Marcats(Integer.parseInt(txtGolsM.getText()));
            seleccionat.setGols_Encaixats(Integer.parseInt(txtGolsE.getText()));
            seleccionat.setDiferencia_Gols(seleccionat.getGols_Marcats() - seleccionat.getGols_Encaixats());

            if (esNou)
                llistaEquips.add(seleccionat);

            // ESCRIBIM AL CSV: Això és el que fa que el mode offline sigui persistent
            EscriptorCSV.guardarEquips(new ArrayList<>(llistaEquips), RUTA_CSV);

            taulaEquips.refresh();
            gestionarEdicioCamps(false);
            mostrarAlerta("Èxit", "Dades desades correctament al fitxer CSV.");

        } catch (NumberFormatException ex) {
            mostrarAlerta("Error de format", "Assegura't que els camps numèrics contenen números vàlids.");
        }
    }

    @FXML
    private void handleAfegirEquip() {

        mostrarAlerta("Informació per a nous equips",
                "Si no saps l'ID de l'equip o de la lliga, pots consultar-ho a la documentació oficial de API-Football.");

        taulaEquips.getSelectionModel().clearSelection();
        txtIdEquip.clear();
        txtNom.clear();
        txtIdLliga.clear();
        txtLligaNom.clear();
        txtPosicio.setText("0");
        txtPunts.setText("0");
        txtPartits.setText("0");
        txtVictories.setText("0");
        txtEmpats.setText("0");
        txtDerrotes.setText("0");
        txtGolsM.setText("0");
        txtGolsE.setText("0");

        gestionarEdicioCamps(true);
        txtIdEquip.requestFocus();
    }

    @FXML
    private void eliminarEquip() {
        Equip seleccionat = taulaEquips.getSelectionModel().getSelectedItem();

        if (seleccionat == null) {
            mostrarAlerta("Atenció", "Selecciona un equip de la taula primer.");
            return;
        }

        // Alerta de confirmació
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar eliminació OFFLINE");
        alerta.setHeaderText(null);
        alerta.setContentText("Segur que vols eliminar l'equip '" + seleccionat.getNom_Equip() + "' del fitxer CSV?");

        Optional<ButtonType> resultat = alerta.showAndWait();
        if (resultat.isPresent() && resultat.get() == ButtonType.OK) {
            llistaEquips.remove(seleccionat);

            // Actualitzem el fitxer CSV
            EscriptorCSV.guardarEquips(new ArrayList<>(llistaEquips), RUTA_CSV);

            taulaEquips.refresh();
            mostrarAlerta("Eliminat", "L'equip s'ha esborrat del fitxer.");
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