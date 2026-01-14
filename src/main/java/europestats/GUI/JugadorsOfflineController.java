package europestats.GUI;

import java.util.ArrayList;
import java.util.List;

import europestats.CLASES.Equip;
import europestats.CLASES.Jugador;
import europestats.CSV.EscriptorCSV;
import europestats.CSV.LectorCSV;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class JugadorsOfflineController {

    @FXML
    private TableView<Jugador> taulaJugadors;
    @FXML
    private TableColumn<Jugador, Integer> colId, colIdEquip, colIdLliga, colGols, colAssists, colMinuts, colGrogues,
            colVermelles;
    @FXML
    private TableColumn<Jugador, String> colNom, colPosicio;
    @FXML
    private TableColumn<Jugador, Double> colGols90, colAssists90;

    @FXML
    private TextField txtId, txtNom, txtIdLliga, txtGols, txtAssists, txtMinuts, txtGrogues, txtVermelles, txtGols90,
            txtAssists90, filtreField;

    @FXML
    private ComboBox<String> comboPosicio;

    @FXML
    private ComboBox<Equip> comboEquip; // ComboBox d'equips per a mode Offline

    private final ObservableList<Jugador> masterData = FXCollections.observableArrayList();
    private final ObservableList<Equip> equipsOffline = FXCollections.observableArrayList();
    private FilteredList<Jugador> dadesFiltrades;

    @FXML
    public void initialize() {
        configurarTaula();
        configurarFiltre();
        configurarComboEquip();

        comboPosicio.getItems().addAll("Porter", "Defensa", "Migcampista", "Davanter");

        taulaJugadors.getSelectionModel().selectedItemProperty().addListener((obs, vell, nou) -> {
            if (nou != null)
                omplirFormulari(nou);
        });

        carregarDadesCSV();
        carregarEquipsCSV(); // Carrega equips des de DATA/equips.csv
    }

    private void configurarComboEquip() {
        comboEquip.setCellFactory(lv -> new ListCell<Equip>() {
            @Override
            protected void updateItem(Equip e, boolean empty) {
                super.updateItem(e, empty);
                setText(empty || e == null ? "" : e.getNom_Equip());
            }
        });
        comboEquip.setButtonCell(new ListCell<Equip>() {
            @Override
            protected void updateItem(Equip e, boolean empty) {
                super.updateItem(e, empty);
                setText(empty || e == null ? "" : e.getNom_Equip());
            }
        });

        // En triar un equip, actualitzem l'ID de la lliga automàticament al formulari
        comboEquip.getSelectionModel().selectedItemProperty().addListener((obs, vell, nou) -> {
            if (nou != null) {
                txtIdLliga.setText(String.valueOf(nou.getIdLliga()));
            }
        });
    }

    @FXML
    public void carregarDadesCSV() {
        masterData.clear();
        List<Jugador> dades = LectorCSV.carregarJugadorsDesDeFitxer("DATA/jugadors.csv");
        if (dades != null)
            masterData.addAll(dades);
    }

    private void carregarEquipsCSV() {
        equipsOffline.clear();
        // Llegeix el CSV d'equips local
        List<Equip> llista = LectorCSV.carregarEquipsDesDeFitxer("DATA/equips.csv");
        if (llista != null) {
            equipsOffline.addAll(llista);
        }
        comboEquip.setItems(equipsOffline);
    }

    private void configurarTaula() {
        colId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("Nom"));
        colPosicio.setCellValueFactory(new PropertyValueFactory<>("posicio"));
        colIdEquip.setCellValueFactory(new PropertyValueFactory<>("idEquipObjecte"));
        colIdLliga.setCellValueFactory(new PropertyValueFactory<>("idLliga"));
        colGols.setCellValueFactory(new PropertyValueFactory<>("Gols_marcats"));
        colAssists.setCellValueFactory(new PropertyValueFactory<>("Assistencies"));
        colMinuts.setCellValueFactory(new PropertyValueFactory<>("Minuts"));
        colGrogues.setCellValueFactory(new PropertyValueFactory<>("Targetes_Grogues"));
        colVermelles.setCellValueFactory(new PropertyValueFactory<>("Targetes_Vermelles"));
        colGols90.setCellValueFactory(new PropertyValueFactory<>("Gols_per_90"));
        colAssists90.setCellValueFactory(new PropertyValueFactory<>("Assist_per_90"));

        dadesFiltrades = new FilteredList<>(masterData, p -> true);
        taulaJugadors.setItems(dadesFiltrades);
    }

    private void configurarFiltre() {
        filtreField.textProperty().addListener((observable, oldValue, newValue) -> {
            dadesFiltrades.setPredicate(j -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                return j.getNom().toLowerCase().contains(newValue.toLowerCase());
            });
        });
    }

    private void omplirFormulari(Jugador j) {
        if (j == null)
            return;

        txtId.setText(String.valueOf(j.getID()));
        txtNom.setText(j.getNom());
        comboPosicio.setValue(j.getPosicio());
        txtIdLliga.setText(String.valueOf(j.getIdLliga()));

        if (j.getEquip() != null) {
            equipsOffline.stream()
                    .filter(e -> e.getID() == j.getEquip().getID())
                    .findFirst()
                    .ifPresent(e -> comboEquip.setValue(e));
        }

        txtGols.setText(String.valueOf(j.getGols_marcats()));
        txtAssists.setText(String.valueOf(j.getAssistencies()));
        txtMinuts.setText(String.valueOf(j.getMinuts()));
        txtGrogues.setText(String.valueOf(j.getTargetes_Grogues()));
        txtVermelles.setText(String.valueOf(j.getTargetes_Vermelles()));
        txtGols90.setText(String.format("%.2f", j.getGols_per_90()));
        txtAssists90.setText(String.format("%.2f", j.getAssist_per_90()));

        gestionarEdicioCamps(false);
    }

    private void gestionarEdicioCamps(boolean editable) {
        TextField[] campsText = { txtId, txtNom, txtIdLliga };
        String colorFons = editable ? "white" : "#d7d9dbff";

        for (TextField tf : campsText) {
            tf.setEditable(editable);
            tf.setStyle("-fx-background-color: " + colorFons + ";");
        }
        comboPosicio.setDisable(!editable);
        comboEquip.setDisable(!editable);
        comboPosicio.setStyle("-fx-opacity: 1; -fx-background-color: " + colorFons + ";");
        comboEquip.setStyle("-fx-opacity: 1; -fx-background-color: " + colorFons + ";");
    }

    @FXML
    private void guardarCanvi() {
        Equip equipSel = comboEquip.getValue();

        if (txtId.getText().isEmpty() || equipSel == null || txtNom.getText().isEmpty()
                || comboPosicio.getValue() == null) {
            mostrarAlerta("Camps buits", "L'ID, Nom, Equip i Posició són obligatoris.");
            return;
        }

        try {
            Jugador seleccionat = taulaJugadors.getSelectionModel().getSelectedItem();
            boolean esNou = (seleccionat == null);
            int idJugador = Integer.parseInt(txtId.getText());

            if (esNou) {
                if (masterData.stream().anyMatch(j -> j.getID() == idJugador)) {
                    mostrarAlerta("ID repetit", "Aquest ID ja existeix al CSV.");
                    return;
                }
                seleccionat = new Jugador(idJugador, "", "", equipSel, equipSel.getIdLliga(), 0, 0, 0, 0, 0, 0.0, 0.0);
            }

            seleccionat.setNom(txtNom.getText());
            seleccionat.setPosicio(comboPosicio.getValue());
            seleccionat.setEquip(equipSel);
            seleccionat.setIdLliga(equipSel.getIdLliga());

            // Càlculs d'estatístiques
            int mins = Integer.parseInt(txtMinuts.getText());
            int gols = Integer.parseInt(txtGols.getText());
            int asis = Integer.parseInt(txtAssists.getText());

            seleccionat.setMinuts(mins);
            seleccionat.setGols_marcats(gols);
            seleccionat.setAssistencies(asis);
            seleccionat.setTargetes_Grogues(Integer.parseInt(txtGrogues.getText()));
            seleccionat.setTargetes_Vermelles(Integer.parseInt(txtVermelles.getText()));

            seleccionat.setGols_per_90(mins > 0 ? (double) gols / mins * 90 : 0);
            seleccionat.setAssist_per_90(mins > 0 ? (double) asis / mins * 90 : 0);

            if (esNou)
                masterData.add(seleccionat);

            // Guardar al fitxer CSV local
            EscriptorCSV.guardarJugadors(new ArrayList<>(masterData), "DATA/jugadors.csv");

            taulaJugadors.refresh();
            gestionarEdicioCamps(false);
            mostrarAlerta("Èxit", "Guardat correctament al CSV.");

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al processar les dades: " + e.getMessage());
        }
    }

    @FXML
    private void handleAfegirJugador() {
        taulaJugadors.getSelectionModel().clearSelection();
        txtId.clear();
        txtNom.clear();
        comboEquip.getSelectionModel().clearSelection();
        txtIdLliga.clear();
        txtGols.setText("0");
        txtAssists.setText("0");
        txtMinuts.setText("0");
        txtGrogues.setText("0");
        txtVermelles.setText("0");
        gestionarEdicioCamps(true);
    }

    @FXML
    private void eliminarJugador() {
        Jugador seleccionat = taulaJugadors.getSelectionModel().getSelectedItem();
        if (seleccionat == null)
            return;

        masterData.remove(seleccionat);
        EscriptorCSV.guardarJugadors(new ArrayList<>(masterData), "DATA/jugadors.csv");
        mostrarAlerta("Eliminat", "Jugador eliminat del fitxer local.");
    }

    private void mostrarAlerta(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }
}