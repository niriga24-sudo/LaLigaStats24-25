package europestats.GUI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import europestats.CLASES.Equip;
import europestats.CLASES.Jugador;
import europestats.CSV.LectorCSV;
import europestats.DAO.EquipDAO;
import europestats.DAO.JugadorDAO;
import europestats.SERVEIS.SistemaService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class JugadoresController {

    @FXML
    private ImageView imgLogoHeader;
    
    @FXML
    private TextField txtBuscar;
    
    @FXML
    private Label lblResultados;
    
    @FXML
    private Label lblTotalJugadores;
    
    @FXML
    private TableView<Jugador> tablaJugadores;
    
    @FXML
    private TableColumn<Jugador, String> colNombre, colPosicion, colEquipo, colLiga;
    
    @FXML
    private TableColumn<Jugador, Integer> colGoles, colAsistencias, colMinutos, colAmarillas, colRojas;
    
    @FXML
    private TableColumn<Jugador, Double> colGoles90, colAsist90;
    
    private final JugadorDAO jugadorDAO = new JugadorDAO();
    private final EquipDAO equipDAO = new EquipDAO();
    private final SistemaService sistemaService = new SistemaService();
    private List<Jugador> todosJugadores;
    private ObservableList<Jugador> jugadoresFiltrados;
    
    // Mapa de equipos por ID para asignar nombres
    private Map<Integer, Equip> equiposMap = new HashMap<>();
    
    // Mapa de IDs de liga a nombres
    private static final Map<Integer, String> LIGAS_NOMBRES = new HashMap<>();
    static {
        LIGAS_NOMBRES.put(140, "La Liga");
        LIGAS_NOMBRES.put(39, "Premier League");
        LIGAS_NOMBRES.put(78, "Bundesliga");
        LIGAS_NOMBRES.put(135, "Serie A");
        LIGAS_NOMBRES.put(61, "Ligue 1");
        LIGAS_NOMBRES.put(88, "Eredivisie");
        LIGAS_NOMBRES.put(94, "Primeira Liga");
    }

    @FXML
    public void initialize() {
        try {
            System.out.println("🔵 JugadoresController - Iniciant...");
            carregarLogo();
            carregarEquipos();  // Cargar equipos primero para tener el mapa
            configurarColumnas();
            carregarJugadores();
            System.out.println("✅ JugadoresController inicialitzat correctament");
        } catch (Exception e) {
            System.err.println("❌ ERROR en initialize() de JugadoresController: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void carregarEquipos() {
        try {
            List<Equip> equipos;
            if (sistemaService.isBBDDConnectada()) {
                equipos = equipDAO.obtenirTotsElsEquips();
            } else {
                equipos = LectorCSV.carregarEquipsDesDeFitxer("DATA/equips.csv");
            }
            
            if (equipos != null) {
                for (Equip e : equipos) {
                    equiposMap.put(e.getID(), e);
                }
                System.out.println("✅ Carregats " + equiposMap.size() + " equips al mapa");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error carregant equipos: " + e.getMessage());
        }
    }
    
    private void carregarLogo() {
        try {
            File logoFile = new File("LOGO/logo.png");
            if (logoFile.exists()) {
                Image logo = new Image(logoFile.toURI().toString());
                imgLogoHeader.setImage(logo);
            }
        } catch (Exception e) {
            System.err.println("⚠️ No s'ha pogut carregar el logo: " + e.getMessage());
        }
    }
    
    private void configurarColumnas() {
        // Columnas de texto
        colNombre.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getNom()));
        
        colPosicion.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getPosicio()));
        
        // Columna Equipo - buscar nombre en el mapa si no está disponible
        colEquipo.setCellValueFactory(cellData -> {
            Jugador j = cellData.getValue();
            // Primero intentar obtener del objeto Equip del jugador
            if (j.getEquip() != null && j.getEquip().getNom_Equip() != null 
                && !j.getEquip().getNom_Equip().isEmpty()) {
                return new SimpleStringProperty(j.getEquip().getNom_Equip());
            }
            // Si no, buscar en el mapa por ID del equipo
            if (j.getEquip() != null && equiposMap.containsKey(j.getEquip().getID())) {
                return new SimpleStringProperty(equiposMap.get(j.getEquip().getID()).getNom_Equip());
            }
            return new SimpleStringProperty("-");
        });
        
        // Columna Liga - usar mapa de nombres de liga
        colLiga.setCellValueFactory(cellData -> {
            Jugador j = cellData.getValue();
            // Primero intentar obtener del objeto Equip del jugador
            if (j.getEquip() != null && j.getEquip().getLliga() != null 
                && !j.getEquip().getLliga().isEmpty()) {
                return new SimpleStringProperty(j.getEquip().getLliga());
            }
            // Si no, buscar en el mapa de ligas por ID
            int idLliga = j.getIdLliga();
            if (LIGAS_NOMBRES.containsKey(idLliga)) {
                return new SimpleStringProperty(LIGAS_NOMBRES.get(idLliga));
            }
            // Último recurso: buscar en equiposMap
            if (j.getEquip() != null && equiposMap.containsKey(j.getEquip().getID())) {
                Equip eq = equiposMap.get(j.getEquip().getID());
                if (eq.getLliga() != null && !eq.getLliga().isEmpty()) {
                    return new SimpleStringProperty(eq.getLliga());
                }
            }
            return new SimpleStringProperty("-");
        });
        
        // Columnas numéricas enteras
        colGoles.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(cellData.getValue().getGols_marcats()).asObject());
        
        colAsistencias.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(cellData.getValue().getAssistencies()).asObject());
        
        colMinutos.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(cellData.getValue().getMinuts()).asObject());
        
        colAmarillas.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(cellData.getValue().getTargetes_Grogues()).asObject());
        
        colRojas.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(cellData.getValue().getTargetes_Vermelles()).asObject());
        
        // Columnas numéricas decimales
        colGoles90.setCellValueFactory(cellData -> 
            new SimpleDoubleProperty(cellData.getValue().getGols_per_90()).asObject());
        
        colAsist90.setCellValueFactory(cellData -> 
            new SimpleDoubleProperty(cellData.getValue().getAssist_per_90()).asObject());
        
        // Política de redimensionamiento
        tablaJugadores.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Doble click para abrir detalle del jugador
        tablaJugadores.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                Jugador jugadorSeleccionado = tablaJugadores.getSelectionModel().getSelectedItem();
                if (jugadorSeleccionado != null) {
                    JugadorDetalleController.abrirDetalleJugador(
                        jugadorSeleccionado,
                        tablaJugadores.getScene(),
                        "Jugadores - Europe Stats"
                    );
                }
            }
        });
    }
    
    private void carregarJugadores() {
        try {
            // Intentar cargar desde BBDD, si falla usar CSV
            if (sistemaService.isBBDDConnectada()) {
                System.out.println("🌐 Carregant jugadors des de BBDD...");
                todosJugadores = jugadorDAO.obtenirTotsElsJugadors();
            } else {
                System.out.println("📁 Carregant jugadors des de CSV...");
                todosJugadores = LectorCSV.carregarJugadorsDesDeFitxer("DATA/jugadors.csv");
            }
            
            if (todosJugadores == null) {
                todosJugadores = new ArrayList<>();
            }
            
            // Relacionar jugadores con equipos completos
            if (!equiposMap.isEmpty()) {
                System.out.println("🔗 Relacionant jugadors amb equips...");
                for (Jugador j : todosJugadores) {
                    if (j.getEquip() != null) {
                        int idEquip = j.getEquip().getID();
                        if (equiposMap.containsKey(idEquip)) {
                            j.setEquip(equiposMap.get(idEquip));
                        }
                    }
                }
                System.out.println("✅ Jugadors relacionats amb equips");
            }
            
            jugadoresFiltrados = FXCollections.observableArrayList(todosJugadores);
            tablaJugadores.setItems(jugadoresFiltrados);
            
            actualizarContadores(todosJugadores.size(), todosJugadores.size());
            System.out.println("✅ Carregats " + todosJugadores.size() + " jugadors");
        } catch (Exception e) {
            System.err.println("❌ Error carregant jugadors des de BBDD, intentant CSV...");
            try {
                todosJugadores = LectorCSV.carregarJugadorsDesDeFitxer("DATA/jugadors.csv");
                if (todosJugadores == null) {
                    todosJugadores = new ArrayList<>();
                }
                
                // Relacionar jugadores con equipos completos también en fallback
                if (!equiposMap.isEmpty()) {
                    for (Jugador j : todosJugadores) {
                        if (j.getEquip() != null) {
                            int idEquip = j.getEquip().getID();
                            if (equiposMap.containsKey(idEquip)) {
                                j.setEquip(equiposMap.get(idEquip));
                            }
                        }
                    }
                }
                
                jugadoresFiltrados = FXCollections.observableArrayList(todosJugadores);
                tablaJugadores.setItems(jugadoresFiltrados);
                actualizarContadores(todosJugadores.size(), todosJugadores.size());
                System.out.println("✅ Carregats " + todosJugadores.size() + " jugadors des de CSV");
            } catch (Exception ex) {
                System.err.println("❌ Error carregant jugadors: " + ex.getMessage());
                todosJugadores = new ArrayList<>();
                jugadoresFiltrados = FXCollections.observableArrayList();
                tablaJugadores.setItems(jugadoresFiltrados);
                actualizarContadores(0, 0);
            }
        }
    }
    
    // Método auxiliar para obtener nombre de equipo
    private String obtenerNombreEquipo(Jugador j) {
        if (j.getEquip() != null) {
            if (j.getEquip().getNom_Equip() != null && !j.getEquip().getNom_Equip().isEmpty()) {
                return j.getEquip().getNom_Equip();
            }
            if (equiposMap.containsKey(j.getEquip().getID())) {
                return equiposMap.get(j.getEquip().getID()).getNom_Equip();
            }
        }
        return "";
    }
    
    // Método auxiliar para obtener nombre de liga
    private String obtenerNombreLiga(Jugador j) {
        if (j.getEquip() != null && j.getEquip().getLliga() != null && !j.getEquip().getLliga().isEmpty()) {
            return j.getEquip().getLliga();
        }
        if (LIGAS_NOMBRES.containsKey(j.getIdLliga())) {
            return LIGAS_NOMBRES.get(j.getIdLliga());
        }
        return "";
    }
    
    @FXML
    private void handleBuscar() {
        String filtro = txtBuscar.getText().toLowerCase().trim();
        
        if (filtro.isEmpty()) {
            jugadoresFiltrados.setAll(todosJugadores);
            lblResultados.setText("Mostrando todos los jugadores");
        } else {
            List<Jugador> resultados = todosJugadores.stream()
                .filter(j -> {
                    boolean coincideNombre = j.getNom().toLowerCase().contains(filtro);
                    boolean coincidePosicion = j.getPosicio() != null && 
                                               j.getPosicio().toLowerCase().contains(filtro);
                    String nombreEquipo = obtenerNombreEquipo(j);
                    boolean coincideEquipo = nombreEquipo.toLowerCase().contains(filtro);
                    String nombreLiga = obtenerNombreLiga(j);
                    boolean coincideLiga = nombreLiga.toLowerCase().contains(filtro);
                    return coincideNombre || coincidePosicion || coincideEquipo || coincideLiga;
                })
                .collect(Collectors.toList());
            
            jugadoresFiltrados.setAll(resultados);
            lblResultados.setText("Resultados para: \"" + txtBuscar.getText() + "\"");
        }
        
        actualizarContadores(jugadoresFiltrados.size(), todosJugadores.size());
    }
    
    @FXML
    private void handleLimpiar() {
        txtBuscar.clear();
        jugadoresFiltrados.setAll(todosJugadores);
        lblResultados.setText("Mostrando todos los jugadores");
        actualizarContadores(todosJugadores.size(), todosJugadores.size());
    }
    
    private void actualizarContadores(int mostrados, int total) {
        if (mostrados == total) {
            lblTotalJugadores.setText("Total: " + total + " jugadores");
        } else {
            lblTotalJugadores.setText("Mostrando " + mostrados + " de " + total + " jugadores");
        }
    }
    
    @FXML
    private void handleVolver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/europestats/GUI/public_view.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) imgLogoHeader.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("EUROPESTATS - Estadístiques del Futbol Europeu");
            stage.centerOnScreen();
            
        } catch (IOException e) {
            System.err.println("❌ Error tornant a la vista principal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ============================================
    // MÉTODOS DEL MENÚ DESPLEGABLE DEL LOGO
    // ============================================

    @FXML
    private void handleInicio() {
        handleVolver();
    }

    @FXML
    private void handleEquipos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/europestats/GUI/equipos_view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) imgLogoHeader.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("EUROPESTATS - Todos los Equipos");
        } catch (Exception e) {
            System.err.println("Error al cargar equipos: " + e.getMessage());
        }
    }

    @FXML
    private void handleJugadores() {
        // Ya estamos aquí, no hacer nada
    }

    @FXML
    private void handleAcercaDe() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("ℹ️ Acerca de EUROPESTATS");
        alert.setHeaderText("EUROPESTATS - Estadísticas de Fútbol Europeo");
        alert.setContentText("Versión 1.0\n\nAplicación de estadísticas de las principales ligas europeas.\n\nTemporada 2023-2024\n\n© 2024 EUROPESTATS");
        alert.showAndWait();
    }
}
