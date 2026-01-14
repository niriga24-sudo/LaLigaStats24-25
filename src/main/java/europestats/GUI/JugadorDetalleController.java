package europestats.GUI;

import java.io.File;
import java.text.DecimalFormat;

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

public class JugadorDetalleController {

    @FXML
    private ImageView imgLogoHeader;
    @FXML
    private Label lblNombreJugador;
    @FXML
    private Label lblPosicion;
    @FXML
    private Button btnEquipo;
    @FXML
    private Button btnLiga;

    // Estadísticas
    @FXML
    private Label lblGoles;
    @FXML
    private Label lblAsistencias;
    @FXML
    private Label lblMinutos;
    @FXML
    private Label lblGolesPer90;
    @FXML
    private Label lblAsistPer90;
    @FXML
    private Label lblPartidos;

    // Totales
    @FXML
    private Label lblGolesTotal;
    @FXML
    private Label lblAsistenciasTotal;
    @FXML
    private Label lblContribucion;
    @FXML
    private Label lblAmarillasTotal;
    @FXML
    private Label lblRojasTotal;

    // Equipo
    @FXML
    private Label lblEquipoInfo;
    @FXML
    private Label lblPosicionEquipo;
    @FXML
    private Label lblVictorias;
    @FXML
    private Label lblEmpates;
    @FXML
    private Label lblDerrotas;
    @FXML
    private Label lblNombreEquipoSeccion;
    @FXML
    private Label lblInfo;

    @FXML
    private ImageView imgEscudoEquipoJugador;

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
                imgLogoHeader.setImage(new Image(logoFile.toURI().toString()));
            }
        } catch (Exception e) {
            System.out.println("Error logo: " + e.getMessage());
        }
    }

    public void setJugador(Jugador jugador) {
        this.jugadorActual = jugador;
        cargarDatosJugador();
    }

    public void setEscenaAnterior(Scene escena, String titulo) {
        this.escenaAnterior = escena;
        this.tituloAnterior = titulo;
    }

    private void cargarDatosJugador() {
        if (jugadorActual == null)
            return;

        lblNombreJugador.setText(jugadorActual.getNom());
        lblPosicion.setText(jugadorActual.getPosicio());

        String nombreEquipo = (jugadorActual.getEquip() != null) ? jugadorActual.getEquip().getNom_Equip() : "-";
        btnEquipo.setText(nombreEquipo);
        lblEquipoInfo.setText(nombreEquipo);

        String nombreLiga = obtenerNombreLiga(jugadorActual.getIdLliga());
        btnLiga.setText(nombreLiga);

        lblGoles.setText(String.valueOf(jugadorActual.getGols_marcats()));
        lblAsistencias.setText(String.valueOf(jugadorActual.getAssistencies()));
        lblMinutos.setText(String.valueOf(jugadorActual.getMinuts()));

        lblGolesPer90.setText(df.format(jugadorActual.getGols_per_90()));
        lblAsistPer90.setText(df.format(jugadorActual.getAssist_per_90()));
        lblPartidos.setText(String.valueOf(jugadorActual.getMinuts() / 90));

        lblGolesTotal.setText(String.valueOf(jugadorActual.getGols_marcats()));
        lblAsistenciasTotal.setText(String.valueOf(jugadorActual.getAssistencies()));
        lblContribucion.setText(String.valueOf(jugadorActual.getGols_marcats() + jugadorActual.getAssistencies()));

        lblAmarillasTotal.setText(String.valueOf(jugadorActual.getTargetes_Grogues()));
        lblRojasTotal.setText(String.valueOf(jugadorActual.getTargetes_Vermelles()));

        if (jugadorActual.getEquip() != null) {
            // Mostrar título estático en la sección del equipo y dejar el nombre dentro del recuadro
            lblNombreEquipoSeccion.setText("EQUIP");
            lblPosicionEquipo.setText(jugadorActual.getEquip().getPosicio() + "º");
            lblVictorias.setText(String.valueOf(jugadorActual.getEquip().getVictories()));
            lblEmpates.setText(String.valueOf(jugadorActual.getEquip().getEmpats()));
            lblDerrotas.setText(String.valueOf(jugadorActual.getEquip().getDerrotes()));
            // Cargar escudo del equipo y desplazar la info a la derecha (ya hecho en el FXML con HBox)
            cargarEscudoEquipoJugador();
        }

        lblInfo.setText("Temporada 2023-2024 | " + nombreEquipo + " | " + nombreLiga);
    }

    private void cargarEscudoEquipoJugador() {
        try {
            if (jugadorActual == null || jugadorActual.getEquip() == null) {
                imgEscudoEquipoJugador.setImage(null);
                return;
            }
            String path = buscarEscudoLocal(jugadorActual.getEquip());
            if (path != null && !path.isEmpty()) {
                java.io.File f = new java.io.File(path);
                if (f.exists()) {
                    imgEscudoEquipoJugador.setImage(new Image(f.toURI().toString(), 72, 72, true, true));
                    return;
                }
            }
            imgEscudoEquipoJugador.setImage(null);
        } catch (Exception e) {
            System.err.println("Error cargando escudo jugador: " + e.getMessage());
            imgEscudoEquipoJugador.setImage(null);
        }
    }

    // Buscar escudo localmente (reutiliza lógica ya usada en otros controladores)
    private String buscarEscudoLocal(europestats.CLASES.Equip e) {
        if (e == null || e.getNom_Equip() == null) return "";
        String nomEquip = normalitzarPerFitxer(e.getNom_Equip());
        String nomLliga = normalitzarPerFitxer(e.getLliga());
        String base = "LOGO/ESCUDOS" + java.io.File.separator;
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
                java.io.File possible = new java.io.File(base + nomLliga + java.io.File.separator + c + ext);
                if (possible.exists()) return possible.getPath();
            }
        }
        // try all subfolders
        java.io.File baseDir = new java.io.File(base);
        java.io.File[] dirs = baseDir.listFiles(java.io.File::isDirectory);
        if (dirs != null) {
            for (java.io.File d : dirs) {
                for (String c : candidates) {
                    for (String ext : exts) {
                        java.io.File f = new java.io.File(d, c + ext);
                        if (f.exists()) return f.getPath();
                    }
                }
            }
            // fuzzy
            for (java.io.File d : dirs) {
                java.io.File[] files = d.listFiles();
                if (files == null) continue;
                for (java.io.File f : files) {
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

    private String obtenerNombreLiga(int ligaId) {
        return switch (ligaId) {
            case 140 -> "La Liga";
            case 39 -> "Premier League";
            case 78 -> "Bundesliga";
            case 135 -> "Serie A";
            case 61 -> "Ligue 1";
            case 94 -> "Primeira Liga";
            default -> "Otras Ligas";
        };
    }

    @FXML
    private void handleVolver() {
        Stage stage = (Stage) lblNombreJugador.getScene().getWindow();
        if (escenaAnterior != null) {
            stage.setScene(escenaAnterior);
            stage.setTitle(tituloAnterior);
        }
    }

    @FXML
    private void handleVerEquipo() {
        if (jugadorActual != null && jugadorActual.getEquip() != null) {
            EquipoDetalleController.abrirDetalleEquipo(jugadorActual.getEquip(), btnEquipo.getScene(),
                    "Detalle Equipo");
        }
    }

    @FXML
    private void handleVerLiga() {
        try {
            if (jugadorActual == null) return;
            int idLliga = jugadorActual.getIdLliga();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/europestats/GUI/public_view.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y seleccionar la liga correspondiente
            europestats.GUI.PublicController controller = loader.getController();
            controller.seleccionarLigaPorId(idLliga);

            Stage stage = (Stage) lblNombreJugador.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("EUROPESTATS - " + obtenerNombreLiga(idLliga));
            stage.centerOnScreen();
        } catch (Exception e) {
            System.err.println("Error al navegar a la liga: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Métodos de navegación del menú
    @FXML
    private void handleInicio() {
        navegar("/europestats/GUI/public_view.fxml", "Inici");
    }

    @FXML
    private void handleEquipos() {
        navegar("/europestats/GUI/equipos_view.fxml", "Equips");
    }

    @FXML
    private void handleJugadores() {
        navegar("/europestats/GUI/jugadores_view.fxml", "Jugadors");
    }

    private void navegar(String fxml, String titulo) {
        try {
            Stage stage = (Stage) lblNombreJugador.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            stage.setScene(new Scene(root));
            stage.setTitle(titulo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAcercaDe() {
        // Mostrar alerta de información
    }

    public static void abrirDetalleJugador(Jugador jugador, Scene escenaActual, String tituloActual) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    JugadorDetalleController.class.getResource("/europestats/GUI/jugador_detalle_view.fxml"));
            Parent root = loader.load();
            JugadorDetalleController controller = loader.getController();
            controller.setJugador(jugador);
            controller.setEscenaAnterior(escenaActual, tituloActual);
            Stage stage = (Stage) escenaActual.getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Detalle - " + jugador.getNom());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}