package europestats.GUI;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import europestats.CLASES.Equip;
import europestats.CLASES.Jugador;
import europestats.CSV.LectorCSV;
import europestats.DAO.JugadorDAO;
import europestats.SERVEIS.SistemaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Controlador para la vista de detalle de un equipo
 */
public class EquipoDetalleController {

    @FXML private ImageView imgLogoHeader;
    @FXML private ImageView imgEscudoEquipo;
    @FXML private Label lblNombreEquipo;
    @FXML private Label lblLiga;
    @FXML private Label lblPosicion;
    @FXML private Label lblPuntos;
    @FXML private Label lblPJ;
    @FXML private Label lblVictorias;
    @FXML private Label lblEmpates;
    @FXML private Label lblDerrotas;
    @FXML private Label lblGF;
    @FXML private Label lblGC;
    @FXML private Label lblDG;
    @FXML private Label lblInfo;
    @FXML private Label lblTotalJugadores;
    
    // Tabla de jugadores
    @FXML private TableView<Jugador> tablaJugadores;
    @FXML private TableColumn<Jugador, String> colNombre;
    @FXML private TableColumn<Jugador, String> colPosicion;
    @FXML private TableColumn<Jugador, Integer> colGoles;
    @FXML private TableColumn<Jugador, Integer> colAsistencias;
    @FXML private TableColumn<Jugador, Integer> colMinutos;
    @FXML private TableColumn<Jugador, Integer> colAmarillas;
    @FXML private TableColumn<Jugador, Integer> colRojas;
    
    // Top 3 Goleadores
    @FXML private Label lblGoleador1Nombre;
    @FXML private Label lblGoleador1Goles;
    @FXML private Label lblGoleador2Nombre;
    @FXML private Label lblGoleador2Goles;
    @FXML private Label lblGoleador3Nombre;
    @FXML private Label lblGoleador3Goles;
    
    // Top 3 Asistentes
    @FXML private Label lblAsistente1Nombre;
    @FXML private Label lblAsistente1Asist;
    @FXML private Label lblAsistente2Nombre;
    @FXML private Label lblAsistente2Asist;
    @FXML private Label lblAsistente3Nombre;
    @FXML private Label lblAsistente3Asist;

    private Equip equipoActual;
    private Scene escenaAnterior;
    private String tituloAnterior;
    
    private final JugadorDAO jugadorDAO = new JugadorDAO();
    private final SistemaService sistemaService = new SistemaService();

    @FXML
    public void initialize() {
        cargarLogoHeader();
        configurarTablaJugadores();
    }

    private void cargarLogoHeader() {
        try {
            File logoFile = new File("LOGO/logo.png");
            if (logoFile.exists()) {
                Image logo = new Image(logoFile.toURI().toString());
                imgLogoHeader.setImage(logo);
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar el logo: " + e.getMessage());
        }
    }
    
    private void configurarTablaJugadores() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("Nom"));
        colPosicion.setCellValueFactory(new PropertyValueFactory<>("posicio"));
        colGoles.setCellValueFactory(new PropertyValueFactory<>("Gols_marcats"));
        colAsistencias.setCellValueFactory(new PropertyValueFactory<>("Assistencies"));
        colMinutos.setCellValueFactory(new PropertyValueFactory<>("Minuts"));
        colAmarillas.setCellValueFactory(new PropertyValueFactory<>("Targetes_Grogues"));
        colRojas.setCellValueFactory(new PropertyValueFactory<>("Targetes_Vermelles"));
        
        tablaJugadores.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Doble click para abrir detalle del jugador
        tablaJugadores.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                Jugador jugadorSeleccionado = tablaJugadores.getSelectionModel().getSelectedItem();
                if (jugadorSeleccionado != null) {
                    JugadorDetalleController.abrirDetalleJugador(
                        jugadorSeleccionado,
                        tablaJugadores.getScene(),
                        "Detalle - " + equipoActual.getNom_Equip()
                    );
                }
            }
        });
    }

    /**
     * Establece el equipo a mostrar y carga sus datos
     */
    public void setEquipo(Equip equipo) {
        this.equipoActual = equipo;
        cargarDatosEquipo();
        cargarEscudoEquipo();
    }

    private void cargarEscudoEquipo() {
        try {
            if (equipoActual == null) return;
            String escudoPath = buscarEscudoLocal(equipoActual);
            if (escudoPath != null && !escudoPath.isEmpty()) {
                File escFile = new File(escudoPath);
                if (escFile.exists()) {
                    Image esc = new Image(escFile.toURI().toString(), 48, 48, true, true);
                    imgEscudoEquipo.setImage(esc);
                } else {
                    imgEscudoEquipo.setImage(null);
                }
            } else {
                imgEscudoEquipo.setImage(null);
            }
            // Ensure name is readable
            lblNombreEquipo.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 1);");
        } catch (Exception e) {
            System.err.println("❌ Error cargando escudo de equipo: " + e.getMessage());
            imgEscudoEquipo.setImage(null);
        }
    }

    /**
     * Guarda la escena anterior para poder volver
     */
    public void setEscenaAnterior(Scene escena, String titulo) {
        this.escenaAnterior = escena;
        this.tituloAnterior = titulo;
    }

    /**
     * Carga los datos del equipo en la interfaz
     */
    private void cargarDatosEquipo() {
        if (equipoActual == null) return;

        // Nombre y liga
        lblNombreEquipo.setText(equipoActual.getNom_Equip() != null ? equipoActual.getNom_Equip() : "Equip desconegut");
        
        String nombreLiga = equipoActual.getLliga();
        if (nombreLiga == null || nombreLiga.isEmpty()) {
            nombreLiga = obtenerNombreLiga(equipoActual.getIdLliga());
        }
        lblLiga.setText(nombreLiga);

        // Posición
        lblPosicion.setText(String.valueOf(equipoActual.getPosicio()));

        // Estadísticas
        lblPuntos.setText(String.valueOf(equipoActual.getPunts()));
        lblPJ.setText(String.valueOf(equipoActual.getPartits_Jugats()));
        lblVictorias.setText(String.valueOf(equipoActual.getVictories()));
        lblEmpates.setText(String.valueOf(equipoActual.getEmpats()));
        lblDerrotas.setText(String.valueOf(equipoActual.getDerrotes()));
        lblGF.setText(String.valueOf(equipoActual.getGols_Marcats()));
        lblGC.setText(String.valueOf(equipoActual.getGols_Encaixats()));
        
        // Diferencia de goles
        int difGoles = equipoActual.getDiferencia_Gols();
        String difGolesStr = difGoles > 0 ? "+" + difGoles : String.valueOf(difGoles);
        lblDG.setText(difGolesStr);

        // Info footer
        lblInfo.setText("Temporada 2023-2024 | " + nombreLiga);
        
        // Cargar jugadores del equipo
        cargarJugadoresEquipo();
    }
    
    /**
     * Carga los jugadores del equipo actual
     */

    /**
     * Buscar escudo localmente en LOGO/ESCUDOS usando variantes del nombre
     */
    private String buscarEscudoLocal(Equip e) {
        if (e == null || e.getNom_Equip() == null) return "";
        String nomEquip = normalitzarPerFitxer(e.getNom_Equip());
        String nomLliga = normalitzarPerFitxer(e.getLliga());
        String base = "LOGO/ESCUDOS" + File.separator;
        String[] exts = new String[]{".png", ".jpg", ".webp"};
        java.util.LinkedHashSet<String> candidates = new java.util.LinkedHashSet<>();
        candidates.add(nomEquip);
        candidates.add(nomEquip.replaceFirst("^\\d+-", ""));
        String[] parts = nomEquip.split("-");
        if (parts.length > 0) candidates.add(parts[parts.length - 1]);
        candidates.add(nomEquip.replaceAll("^(fsv|vfl|vfb|fc)-", ""));
        candidates.add(stripCommonSuffixes(nomEquip));
        java.util.Map<String,String> overrides = java.util.Map.of(
            "1899-hoffenheim","hoffenheim",
            "vfl-wolfsburg","wolfsburg",
            "fsv-mainz-05","mainz-05",
            "vfl-bochum","bochum",
            "sheffield-utd","sheffield-united",
            "stade-brestois-29","brest",
            "bayern-munich","bayern-munchen",
            "sv-darmstadt-98","darmstadt",
            "psv-eindhoven","psv",
            "celta-vigo","celta"
        );
        String norm = nomEquip.toLowerCase();
        if (overrides.containsKey(norm)) candidates.add(overrides.get(norm));

        // try league folder first
        for (String c : candidates) {
            for (String ext : exts) {
                File possible = new File(base + nomLliga + File.separator + c + ext);
                if (possible.exists()) return possible.getPath();
            }
        }
        // try all subfolders
        File baseDir = new File(base);
        File[] dirs = baseDir.listFiles(File::isDirectory);
        if (dirs != null) {
            for (File d : dirs) {
                for (String c : candidates) {
                    for (String ext : exts) {
                        File f = new File(d, c + ext);
                        if (f.exists()) return f.getPath();
                    }
                }
            }
            // fuzzy
            for (File d : dirs) {
                File[] files = d.listFiles();
                if (files == null) continue;
                for (File f : files) {
                    String name = f.getName().toLowerCase();
                    if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".webp")) {
                        for (String c : candidates) {
                            if (!c.isEmpty() && name.contains(c)) return f.getPath();
                        }
                    }
                }
            }
        }
        return "";
    }

    private String stripCommonSuffixes(String s) {
        if (s == null) return "";
        return s.replaceAll("-(cf|fc|c-f|c-f-)$", "").replaceAll("-(a|b)$", "");
    }

    private String normalitzarPerFitxer(String s) {
        if (s == null) return "";
        String n = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        n = n.toLowerCase().replaceAll("[^a-z0-9]+", "-");
        n = n.replaceAll("(^-+|-+$)", "");
        return n;
    }
    private void cargarJugadoresEquipo() {
        try {
            List<Jugador> todosJugadores;
            
            // Cargar todos los jugadores desde BBDD o CSV
            if (sistemaService.isBBDDConnectada()) {
                todosJugadores = jugadorDAO.obtenirTotsElsJugadors();
            } else {
                todosJugadores = LectorCSV.carregarJugadorsDesDeFitxer("DATA/jugadors.csv");
            }
            
            if (todosJugadores != null) {
                // Filtrar jugadores por ID del equipo
                List<Jugador> jugadoresEquipo = todosJugadores.stream()
                    .filter(j -> j.getEquip() != null && j.getEquip().getID() == equipoActual.getID())
                    .sorted((j1, j2) -> j2.getGols_marcats() - j1.getGols_marcats()) // Ordenar por goles
                    .collect(Collectors.toList());
                
                // Asignar el equipo completo a cada jugador
                for (Jugador j : jugadoresEquipo) {
                    j.setEquip(equipoActual);
                }
                
                ObservableList<Jugador> datos = FXCollections.observableArrayList(jugadoresEquipo);
                tablaJugadores.setItems(datos);
                
                lblTotalJugadores.setText("Total: " + jugadoresEquipo.size() + " jugadors");
                System.out.println("✅ Cargados " + jugadoresEquipo.size() + " jugadores para " + equipoActual.getNom_Equip());
                
                // Cargar Top 3 Goleadores y Asistentes
                cargarTop3Goleadores(jugadoresEquipo);
                cargarTop3Asistentes(jugadoresEquipo);
            }
        } catch (Exception e) {
            System.err.println("❌ Error cargando jugadores: " + e.getMessage());
            lblTotalJugadores.setText("Error al carregar jugadors");
        }
    }
    
    /**
     * Carga el Top 3 de goleadores del equipo
     */
    private void cargarTop3Goleadores(List<Jugador> jugadores) {
        List<Jugador> topGoleadores = jugadores.stream()
            .sorted((j1, j2) -> j2.getGols_marcats() - j1.getGols_marcats())
            .limit(3)
            .collect(Collectors.toList());
        
        // Goleador 1
        if (topGoleadores.size() > 0) {
            Jugador g1 = topGoleadores.get(0);
            lblGoleador1Nombre.setText(g1.getNom());
            lblGoleador1Goles.setText(String.valueOf(g1.getGols_marcats()));
        }
        
        // Goleador 2
        if (topGoleadores.size() > 1) {
            Jugador g2 = topGoleadores.get(1);
            lblGoleador2Nombre.setText(g2.getNom());
            lblGoleador2Goles.setText(String.valueOf(g2.getGols_marcats()));
        }
        
        // Goleador 3
        if (topGoleadores.size() > 2) {
            Jugador g3 = topGoleadores.get(2);
            lblGoleador3Nombre.setText(g3.getNom());
            lblGoleador3Goles.setText(String.valueOf(g3.getGols_marcats()));
        }
    }
    
    /**
     * Carga el Top 3 de asistentes del equipo
     */
    private void cargarTop3Asistentes(List<Jugador> jugadores) {
        List<Jugador> topAsistentes = jugadores.stream()
            .sorted((j1, j2) -> j2.getAssistencies() - j1.getAssistencies())
            .limit(3)
            .collect(Collectors.toList());
        
        // Asistente 1
        if (topAsistentes.size() > 0) {
            Jugador a1 = topAsistentes.get(0);
            lblAsistente1Nombre.setText(a1.getNom());
            lblAsistente1Asist.setText(String.valueOf(a1.getAssistencies()));
        }
        
        // Asistente 2
        if (topAsistentes.size() > 1) {
            Jugador a2 = topAsistentes.get(1);
            lblAsistente2Nombre.setText(a2.getNom());
            lblAsistente2Asist.setText(String.valueOf(a2.getAssistencies()));
        }
        
        // Asistente 3
        if (topAsistentes.size() > 2) {
            Jugador a3 = topAsistentes.get(2);
            lblAsistente3Nombre.setText(a3.getNom());
            lblAsistente3Asist.setText(String.valueOf(a3.getAssistencies()));
        }
    }

    private String obtenerNombreLiga(int ligaId) {
        switch (ligaId) {
            case 140: return "La Liga";
            case 39: return "Premier League";
            case 78: return "Bundesliga";
            case 135: return "Serie A";
            case 61: return "Ligue 1";
            case 88: return "Eredivisie";
            case 94: return "Primeira Liga";
            default: return "Liga desconocida";
        }
    }

    @FXML
    private void handleVolver() {
        try {
            Stage stage = (Stage) lblNombreEquipo.getScene().getWindow();
            
            if (escenaAnterior != null) {
                stage.setScene(escenaAnterior);
                stage.setTitle(tituloAnterior != null ? tituloAnterior : "Europe Stats 23-24");
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/europestats/GUI/public_view.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Europe Stats 23-24 - Vista Pública");
            }
        } catch (Exception e) {
            System.err.println("Error al volver: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Método estático para abrir la vista de detalle del equipo
     */
    public static void abrirDetalleEquipo(Equip equipo, Scene escenaActual, String tituloActual) {
        try {
            FXMLLoader loader = new FXMLLoader(EquipoDetalleController.class.getResource("/europestats/GUI/equipo_detalle_view.fxml"));
            Parent root = loader.load();
            
            EquipoDetalleController controller = loader.getController();
            controller.setEquipo(equipo);
            controller.setEscenaAnterior(escenaActual, tituloActual);
            
            Stage stage = (Stage) escenaActual.getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Detalle - " + equipo.getNom_Equip());
        } catch (Exception e) {
            System.err.println("Error al abrir detalle del equipo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ============================================
    // MÉTODOS DEL MENÚ DESPLEGABLE DEL LOGO
    // ============================================

    @FXML
    private void handleInicio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/europestats/GUI/public_view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblNombreEquipo.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Europe Stats 23-24 - Vista Pública");
        } catch (Exception e) {
            System.err.println("Error al ir al inicio: " + e.getMessage());
        }
    }

    @FXML
    private void handleEquipos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/europestats/GUI/equipos_view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblNombreEquipo.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("EUROPESTATS - Tots els Equips");
        } catch (Exception e) {
            System.err.println("Error al cargar equipos: " + e.getMessage());
        }
    }

    @FXML
    private void handleJugadores() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/europestats/GUI/jugadores_view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblNombreEquipo.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("EUROPESTATS - Tots els Jugadors");
        } catch (Exception e) {
            System.err.println("Error al cargar jugadores: " + e.getMessage());
        }
    }

    @FXML
    private void handleAcercaDe() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("ℹ️ Sobre EUROPESTATS");
        alert.setHeaderText("EUROPESTATS - Estadístiques de Futbol Europeu");
        alert.setContentText("Versió 1.0\n\nAplicació d'estadístiques de les principals lligues europees.\n\nTemporada 2023-2024\n\n© 2024 EUROPESTATS");
        alert.showAndWait();
    }
}
