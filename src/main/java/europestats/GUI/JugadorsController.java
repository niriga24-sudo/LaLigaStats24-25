package europestats.GUI;

import java.util.List;

import europestats.CLASES.Equip;
import europestats.CLASES.Jugador;
import europestats.CSV.LectorCSV;
import europestats.DAO.EquipDAO;
import europestats.DAO.JugadorDAO;
import europestats.SERVEIS.SistemaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class JugadorsController {

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
    private ComboBox<Equip> comboEquip; // Nou ComboBox d'objectes Equip

    private final ObservableList<Jugador> masterData = FXCollections.observableArrayList();
    private final ObservableList<Equip> equipsDisponibles = FXCollections.observableArrayList();
    private FilteredList<Jugador> dadesFiltrades;

    private final SistemaService sistemaService = new SistemaService();
    private final JugadorDAO jugadorDAO = new JugadorDAO();
    private final EquipDAO equipDAO = new EquipDAO();

    @FXML
    public void initialize() {
        configurarTaula();
        configurarFiltre();
        configurarComboEquip(); // Personalitza la visualització del combo

        // Inicialitzar posicions
        comboPosicio.getItems().addAll("Porter", "Defensa", "Migcampista", "Davanter");

        taulaJugadors.getSelectionModel().selectedItemProperty().addListener((obs, vell, nou) -> {
            if (nou != null)
                omplirFormulari(nou);
        });

        carregarDades();
        carregarEquips(); // Omple el combo d'equips
    }

    private void configurarComboEquip() {
        // Fa que el ComboBox mostri el nom de l'equip a la llista i al botó
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
    }

    @FXML
    public void carregarDades() {
        masterData.clear();
        List<Jugador> dades = sistemaService.isBBDDConnectada()
                ? jugadorDAO.obtenirTotsElsJugadors()
                : LectorCSV.carregarJugadorsDesDeFitxer("DATA/jugadors.csv");
        if (dades != null)
            masterData.addAll(dades);
    }

    public void carregarEquips() {
        equipsDisponibles.clear();
        if (sistemaService.isBBDDConnectada()) {
            List<Equip> llista = equipDAO.obtenirTotsElsEquips();
            if (llista != null)
                equipsDisponibles.addAll(llista);
        }
        comboEquip.setItems(equipsDisponibles);
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
        txtNom.setText(j.getNom() != null ? j.getNom() : "");
        comboPosicio.setValue(j.getPosicio());
        txtIdLliga.setText(String.valueOf(j.getIdLliga()));

        // Buscar l'equip a la llista del combo i seleccionar-lo
        if (j.getEquip() != null) {
            equipsDisponibles.stream()
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
        comboEquip.setDisable(!editable); // El combo també es bloqueja
        comboPosicio.setStyle("-fx-opacity: 1; -fx-background-color: " + colorFons + ";");
        comboEquip.setStyle("-fx-opacity: 1; -fx-background-color: " + colorFons + ";");
    }

    @FXML
    private void guardarCanvi() {
        Equip equipSeleccionat = comboEquip.getValue();

        if (txtId.getText().isEmpty() || equipSeleccionat == null || txtNom.getText().isEmpty()
                || comboPosicio.getValue() == null) {
            mostrarAlerta("Camps buits", "L'ID, el Nom, l'Equip i la Posició són obligatoris.");
            return;
        }

        if (!sistemaService.isBBDDConnectada()) {
            mostrarAlerta("Error de Connexió", "No es pot desar a la Base de Dades.");
            return;
        }

        try {
            Jugador seleccionat = taulaJugadors.getSelectionModel().getSelectedItem();
            boolean esNou = (seleccionat == null);
            int idJugador = Integer.parseInt(txtId.getText());

            if (esNou) {
                if (masterData.stream().anyMatch(j -> j.getID() == idJugador)) {
                    mostrarAlerta("ID ja registrat", "Aquest ID de jugador ja existeix.");
                    return;
                }
                seleccionat = new Jugador(idJugador, "", "", equipSeleccionat, equipSeleccionat.getIdLliga(), 0, 0, 0,
                        0, 0, 0.0, 0.0);
            }

            seleccionat.setNom(txtNom.getText());
            seleccionat.setPosicio(comboPosicio.getValue());
            seleccionat.setEquip(equipSeleccionat);
            seleccionat.setIdLliga(equipSeleccionat.getIdLliga()); // Sincronització automàtica de la lliga

            int gols = txtGols.getText().isEmpty() ? 0 : Integer.parseInt(txtGols.getText());
            int asis = txtAssists.getText().isEmpty() ? 0 : Integer.parseInt(txtAssists.getText());
            int mins = txtMinuts.getText().isEmpty() ? 0 : Integer.parseInt(txtMinuts.getText());

            seleccionat.setGols_marcats(gols);
            seleccionat.setAssistencies(asis);
            seleccionat.setMinuts(mins);
            seleccionat
                    .setTargetes_Grogues(txtGrogues.getText().isEmpty() ? 0 : Integer.parseInt(txtGrogues.getText()));
            seleccionat.setTargetes_Vermelles(
                    txtVermelles.getText().isEmpty() ? 0 : Integer.parseInt(txtVermelles.getText()));

            double g90 = (mins > 0) ? (double) gols / mins * 90 : 0;
            double a90 = (mins > 0) ? (double) asis / mins * 90 : 0;
            seleccionat.setGols_per_90(g90);
            seleccionat.setAssist_per_90(a90);

            jugadorDAO.insertarOActualitzarJugador(seleccionat);

            if (esNou)
                masterData.add(seleccionat);
            europestats.CSV.EscriptorCSV.guardarJugadors(new java.util.ArrayList<>(masterData), "DATA/jugadors.csv");

            taulaJugadors.refresh();
            omplirFormulari(seleccionat);
            gestionarEdicioCamps(false);
            mostrarAlerta("Èxit", "Dades guardades correctament.");

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de format", "Revisa els camps numèrics.");
        } catch (Exception e) {
            mostrarAlerta("Error", "No s'ha pogut guardar: " + e.getMessage());
        }
    }

    @FXML
    private void handleAfegirJugador() {
        taulaJugadors.getSelectionModel().clearSelection();
        txtId.clear();
        txtNom.clear();
        comboEquip.getSelectionModel().clearSelection();
        txtIdLliga.clear();
        comboPosicio.getSelectionModel().clearSelection();
        txtGols.setText("0");
        txtAssists.setText("0");
        txtMinuts.setText("0");
        txtGrogues.setText("0");
        txtVermelles.setText("0");
        txtGols90.setText("0.00");
        txtAssists90.setText("0.00");
        gestionarEdicioCamps(true);
        txtId.requestFocus();
    }

    @FXML
    private void eliminarJugador() {
        Jugador seleccionat = taulaJugadors.getSelectionModel().getSelectedItem();
        if (seleccionat == null || !sistemaService.isBBDDConnectada())
            return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Eliminar a " + seleccionat.getNom() + "?", ButtonType.OK,
                ButtonType.CANCEL);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                jugadorDAO.eliminarJugador(seleccionat.getID());
                masterData.remove(seleccionat);
                europestats.CSV.EscriptorCSV.guardarJugadors(new java.util.ArrayList<>(masterData),
                        "DATA/jugadors.csv");
            }
        });
    }

    private void mostrarAlerta(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }
}