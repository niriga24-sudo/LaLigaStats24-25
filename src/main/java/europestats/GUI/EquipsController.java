package europestats.GUI;

import java.util.List;
import java.util.Optional;

import europestats.CLASES.Equip;
import europestats.CSV.LectorCSV;
import europestats.DAO.EquipDAO;
import europestats.SERVEIS.SistemaService;
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

public class EquipsController {

    @FXML
    private TableView<Equip> taulaEquips;
    @FXML
    private TableColumn<Equip, Integer> colId, colIdLliga, colPos, colPunts, colPJ, colVictories, colEmpats,
            colDerrotes, colGolsM, colGolsE, colDiff;
    @FXML
    private TableColumn<Equip, String> colLliga, colEquip;
    @FXML
    private TextField txtIdEquip, txtNom, txtIdLliga, txtLligaNom, txtPosicio, txtPunts, txtPartits, txtVictories,
            txtEmpats, txtDerrotes, txtGolsM, txtGolsE, filtreField;

    private final ObservableList<Equip> masterData = FXCollections.observableArrayList();
    // Llista que permetrà el filtratge dinàmic
    private FilteredList<Equip> dadesFiltrades;

    private final SistemaService sistemaService = new SistemaService();
    private final EquipDAO equipDAO = new EquipDAO();

    @FXML
    public void initialize() {
        configurarTaula();
        configurarFiltre();

        taulaEquips.getSelectionModel().selectedItemProperty().addListener((obs, vell, nou) -> {
            if (nou != null)
                omplirFormulari(nou);
        });

        carregarDades();
    }

    private void configurarTaula() {
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

        // Inicialitzem la llista filtrada apuntant a masterData
        dadesFiltrades = new FilteredList<>(masterData, p -> true);
        taulaEquips.setItems(dadesFiltrades);
    }

    private void configurarFiltre() {
        // Escoltador per al camp de text del filtre
        filtreField.textProperty().addListener((observable, oldValue, newValue) -> {
            dadesFiltrades.setPredicate(equip -> {
                // Si el filtre està buit, mostrem tot
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Filtrem per nom de l'equip o per nom de la lliga
                if (equip.getNom_Equip().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (equip.getLliga().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
    }

    @FXML
    public void carregarDades() {
        masterData.clear();
        List<Equip> dades = sistemaService.isBBDDConnectada()
                ? equipDAO.obtenirTotsElsEquips()
                : LectorCSV.carregarEquipsDesDeFitxer("DATA/equips.csv");
        if (dades != null)
            masterData.addAll(dades);
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

    private void gestionarEdicioCamps(boolean editable) {
        txtIdEquip.setEditable(editable);
        txtNom.setEditable(editable);
        txtIdLliga.setEditable(editable);
        txtLligaNom.setEditable(editable);

        String colorFons = editable ? "white" : "#d7d9dbff";
        String estil = "-fx-background-color: " + colorFons + ";";

        txtIdEquip.setStyle(estil);
        txtNom.setStyle(estil);
        txtIdLliga.setStyle(estil);
        txtLligaNom.setStyle(estil);
    }

    @FXML
    private void eliminarEquip() {
        Equip seleccionat = taulaEquips.getSelectionModel().getSelectedItem();

        if (seleccionat == null) {
            mostrarAlerta("Atenció", "Selecciona un equip de la taula primer.");
            return;
        }

        if (sistemaService.isBBDDConnectada()) {
            // Alerta de confirmació
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Confirmar eliminació ONLINE");
            alerta.setHeaderText(null);
            alerta.setContentText(
                    "Segur que vols eliminar l'equip '" + seleccionat.getNom_Equip() + "' de la Base de Dades?");

            Optional<ButtonType> resultat = alerta.showAndWait();
            if (resultat.isPresent() && resultat.get() == ButtonType.OK) {

                // 1. CRIDEM AL DAO (Ara sense les barres de comentari)
                // Aquesta línia és la que realment executa el DELETE a MySQL
                equipDAO.eliminarEquip(seleccionat.getID());

                // 2. ELIMINEM DE LA VISTA
                masterData.remove(seleccionat);

                // 3. NETEJEM EL FORMULARI (Opcional, però recomanat)
                taulaEquips.getSelectionModel().clearSelection();

                mostrarAlerta("Èxit", "Equip eliminat correctament de la Base de Dades.");
            }
        } else {
            mostrarAlerta("Error", "No hi ha connexió amb la Base de Dades. No es pot eliminar.");
        }
    }

    @FXML
    private void guardarCanvi() {
        Equip seleccionat = taulaEquips.getSelectionModel().getSelectedItem();
        try {
            // 1. VALIDACIÓ OBLIGATÒRIA
            if (txtIdEquip.getText().isEmpty() ||
                    txtIdLliga.getText().isEmpty() ||
                    txtNom.getText().isEmpty() ||
                    txtLligaNom.getText().isEmpty()) {

                mostrarAlerta("Camps obligatoris",
                        "Has d'introduir: ID Equip, ID Lliga, Nom de l'Equip i Nom de la Lliga.");
                return;
            }

            int idEquipIntroduit = Integer.parseInt(txtIdEquip.getText());
            int idLliga = Integer.parseInt(txtIdLliga.getText());
            String nomLliga = txtLligaNom.getText();

            boolean esNou = (seleccionat == null);

            if (esNou) {
                // Verificació d'ID únic a la llista actual
                boolean jaExisteix = masterData.stream().anyMatch(e -> e.getID() == idEquipIntroduit);
                if (jaExisteix) {
                    mostrarAlerta("ID Duplicat", "L'ID d'equip " + idEquipIntroduit + " ja existeix.");
                    return;
                }
                // Creem l'objecte buit
                seleccionat = new Equip(idEquipIntroduit, idLliga, nomLliga, 0, "", 0, 0, 0, 0, 0, 0, 0, 0);
            }

            // 2. ACTUALITZACIÓ DE DADES I VALORS PER DEFECTE
            seleccionat.setIdLliga(idLliga);
            seleccionat.setLliga(nomLliga);
            seleccionat.setNom_Equip(txtNom.getText());
            seleccionat.setPosicio(parsejarIntSegur(txtPosicio.getText()));
            seleccionat.setPunts(parsejarIntSegur(txtPunts.getText()));
            seleccionat.setPartits_Jugats(parsejarIntSegur(txtPartits.getText()));
            seleccionat.setVictories(parsejarIntSegur(txtVictories.getText()));
            seleccionat.setEmpats(parsejarIntSegur(txtEmpats.getText()));
            seleccionat.setDerrotes(parsejarIntSegur(txtDerrotes.getText()));
            seleccionat.setGols_Marcats(parsejarIntSegur(txtGolsM.getText()));
            seleccionat.setGols_Encaixats(parsejarIntSegur(txtGolsE.getText()));
            seleccionat.setDiferencia_Gols(seleccionat.getGols_Marcats() - seleccionat.getGols_Encaixats());

            // 3. GUARDAT A MYSQL
            if (sistemaService.isBBDDConnectada()) {
                equipDAO.insertarOActualitzarEquipComplet(seleccionat);

                if (esNou) {
                    masterData.add(seleccionat);
                }

                // --- NOVA LÒGICA: SINCRONITZACIÓ AMB CSV ---
                // Guardem sempre al fitxer local per mantenir l'Offline actualitzat
                europestats.CSV.EscriptorCSV.guardarEquips(new java.util.ArrayList<>(masterData), "DATA/equips.csv");

                // 4. ACTUALITZACIÓ DE LA VISTA
                taulaEquips.refresh();
                gestionarEdicioCamps(false);
                mostrarAlerta("Èxit", "Dades guardades a MySQL i sincronitzades al CSV.");
            } else {
                mostrarAlerta("Error de connexió",
                        "No s'ha pogut connectar a MySQL. No es pot guardar en mode Online.");
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de format", "Els IDs han de ser números (sense lletres).");
        }
    }

    /**
     * Mètode auxiliar: si el text està buit o no és un número, retorna 0.
     */
    private int parsejarIntSegur(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void mostrarAlerta(String titol, String missatge) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titol);
        alerta.setHeaderText(null);
        alerta.setContentText(missatge);
        alerta.showAndWait();
    }
}