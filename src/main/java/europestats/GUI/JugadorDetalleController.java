package europestats.GUI;

import europestats.CLASES.Jugador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Controlador para la vista de detalle de un jugador
 */
public class JugadorDetalleController {

    @FXML private ImageView imgLogoHeader;
    @FXML private Label lblNombreJugador;
    @FXML private Label lblPosicion;
    @FXML private Button btnEquipo;
    @FXML private Button btnLiga;
    @FXML private Label lblGoles;
    @FXML private Label lblAsistencias;
    @FXML private Label lblMinutos;
    @FXML private Label lblAmarillas;
    @FXML private Label lblRojas;
    @FXML private Label lblGolesPer90;
    @FXML private Label lblAsistPer90;
    @FXML private Label lblPartidos;
    @FXML private Label lblGolesTotal;
    @FXML private Label lblAsistenciasTotal;
    @FXML private Label lblContribucion;
    @FXML private Label lblAmarillasTotal;
    @FXML private Label lblRojasTotal;
    @FXML private Label lblTarjetasTotal;
    @FXML private Label lblEquipoInfo;
    @FXML private Label lblLigaInfo;
    @FXML private Label lblPosicionEquipo;
    @FXML private Label lblPuntosEquipo;
    @FXML private Label lblDifGoles;
    @FXML private Label lblNombreEquipoSeccion;
    @FXML private Label lblVictorias;
    @FXML private Label lblEmpates;
    @FXML private Label lblDerrotas;
    @FXML private Label lblInfo;

    private Jugador jugadorActual;
    private Scene escenaAnterior;
    private String tituloAnterior;
    
    private final DecimalFormat df = new DecimalFormat("0.00");

    @FXML
    public void initialize() {
        cargarLogoHeader();
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

    /**
     * Establece el jugador a mostrar y carga sus datos
     */
    public void setJugador(Jugador jugador) {
        this.jugadorActual = jugador;
        cargarDatosJugador();
    }

    /**
     * Guarda la escena anterior para poder volver
     */
    public void setEscenaAnterior(Scene escena, String titulo) {
        this.escenaAnterior = escena;
        this.tituloAnterior = titulo;
    }

    /**
     * Carga los datos del jugador en la interfaz
     */
    private void cargarDatosJugador() {
        if (jugadorActual == null) return;

        // Nombre y posición
        lblNombreJugador.setText(jugadorActual.getNom() != null ? jugadorActual.getNom() : "Jugador desconegut");
        lblPosicion.setText(jugadorActual.getPosicio() != null ? jugadorActual.getPosicio() : "-");

        // Equipo y Liga
        String nombreEquipo = "-";
        if (jugadorActual.getEquip() != null && jugadorActual.getEquip().getNom_Equip() != null) {
            nombreEquipo = jugadorActual.getEquip().getNom_Equip();
        }
        btnEquipo.setText(nombreEquipo);
        lblEquipoInfo.setText(nombreEquipo);
        
        String nombreLiga = obtenerNombreLiga(jugadorActual.getIdLliga());
        btnLiga.setText(nombreLiga);
        lblLigaInfo.setText(nombreLiga);

        // Estadísticas principales
        lblGoles.setText(String.valueOf(jugadorActual.getGols_marcats()));
        lblAsistencias.setText(String.valueOf(jugadorActual.getAssistencies()));
        lblMinutos.setText(String.valueOf(jugadorActual.getMinuts()));
        lblAmarillas.setText(String.valueOf(jugadorActual.getTargetes_Grogues()));
        lblRojas.setText(String.valueOf(jugadorActual.getTargetes_Vermelles()));
        
        // Estadísticas por 90 minutos
        lblGolesPer90.setText(df.format(jugadorActual.getGols_per_90()));
        lblAsistPer90.setText(df.format(jugadorActual.getAssist_per_90()));
        
        // Calcular partidos aproximados (minutos / 90)
        int partidosAprox = jugadorActual.getMinuts() / 90;
        lblPartidos.setText(String.valueOf(partidosAprox));
        
        // Panel derecho - Contribución ofensiva
        lblGolesTotal.setText(String.valueOf(jugadorActual.getGols_marcats()));
        lblAsistenciasTotal.setText(String.valueOf(jugadorActual.getAssistencies()));
        int contribucion = jugadorActual.getGols_marcats() + jugadorActual.getAssistencies();
        lblContribucion.setText(String.valueOf(contribucion));
        
        // Disciplina
        lblAmarillasTotal.setText(String.valueOf(jugadorActual.getTargetes_Grogues()));
        lblRojasTotal.setText(String.valueOf(jugadorActual.getTargetes_Vermelles()));
        int tarjetasTotal = jugadorActual.getTargetes_Grogues() + jugadorActual.getTargetes_Vermelles();
        lblTarjetasTotal.setText(String.valueOf(tarjetasTotal));

        // Datos del equipo
        if (jugadorActual.getEquip() != null) {
            lblNombreEquipoSeccion.setText("🏟️ " + nombreEquipo.toUpperCase());
            lblPosicionEquipo.setText(String.valueOf(jugadorActual.getEquip().getPosicio()) + "º");
            lblPuntosEquipo.setText(String.valueOf(jugadorActual.getEquip().getPunts()));
            int difGoles = jugadorActual.getEquip().getDiferencia_Gols();
            lblDifGoles.setText((difGoles >= 0 ? "+" : "") + difGoles);
            
            // Balance del equipo
            lblVictorias.setText(String.valueOf(jugadorActual.getEquip().getVictories()));
            lblEmpates.setText(String.valueOf(jugadorActual.getEquip().getEmpats()));
            lblDerrotas.setText(String.valueOf(jugadorActual.getEquip().getDerrotes()));
        } else {
            lblNombreEquipoSeccion.setText("🏟️ EQUIPO");
            lblPosicionEquipo.setText("-");
            lblPuntosEquipo.setText("-");
            lblDifGoles.setText("-");
            lblVictorias.setText("-");
            lblEmpates.setText("-");
            lblDerrotas.setText("-");
        }

        // Info footer
        lblInfo.setText("Temporada 2024-2025 | " + nombreEquipo + " | " + nombreLiga);
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
            Stage stage = (Stage) lblNombreJugador.getScene().getWindow();
            
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
     * Abre la página de detalle del equipo del jugador
     */
    @FXML
    private void handleVerEquipo() {
        if (jugadorActual != null && jugadorActual.getEquip() != null) {
            try {
                Scene escenaActual = btnEquipo.getScene();
                String tituloActual = ((Stage) escenaActual.getWindow()).getTitle();
                EquipoDetalleController.abrirDetalleEquipo(
                    jugadorActual.getEquip(),
                    escenaActual,
                    tituloActual
                );
            } catch (Exception e) {
                System.err.println("Error al abrir detalle del equipo: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Abre la página de la liga del jugador
     */
    @FXML
    private void handleVerLiga() {
        if (jugadorActual != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/europestats/GUI/public_view.fxml"));
                Parent root = loader.load();
                
                PublicController controller = loader.getController();
                // Seleccionar la pestaña de la liga correspondiente
                controller.seleccionarLigaPorId(jugadorActual.getIdLliga());
                
                Stage stage = (Stage) btnLiga.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Europe Stats 23-24 - " + obtenerNombreLiga(jugadorActual.getIdLliga()));
            } catch (Exception e) {
                System.err.println("Error al abrir la liga: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Método estático para abrir la vista de detalle del jugador
     */
    public static void abrirDetalleJugador(Jugador jugador, Scene escenaActual, String tituloActual) {
        try {
            FXMLLoader loader = new FXMLLoader(JugadorDetalleController.class.getResource("/europestats/GUI/jugador_detalle_view.fxml"));
            Parent root = loader.load();
            
            JugadorDetalleController controller = loader.getController();
            controller.setJugador(jugador);
            controller.setEscenaAnterior(escenaActual, tituloActual);
            
            Stage stage = (Stage) escenaActual.getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Detalle - " + jugador.getNom());
        } catch (Exception e) {
            System.err.println("Error al abrir detalle del jugador: " + e.getMessage());
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
            Stage stage = (Stage) lblNombreJugador.getScene().getWindow();
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
            Stage stage = (Stage) lblNombreJugador.getScene().getWindow();
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
            Stage stage = (Stage) lblNombreJugador.getScene().getWindow();
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
