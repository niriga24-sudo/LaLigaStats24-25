package europestats.GUI;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import europestats.CLASES.Equip;
import europestats.CLASES.Jugador;
import europestats.CSV.LectorCSV;
import europestats.DAO.EquipDAO;
import europestats.DAO.JugadorDAO;
import europestats.MAIN.App;
import europestats.SERVEIS.SistemaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class PublicController {

    @FXML
    private MenuButton menuLogo;
    
    @FXML
    private ImageView imgLogoHeader;
    
    @FXML
    private javafx.scene.control.TabPane tabPane;

    @FXML
    private ImageView imgLeagueIcon;
    
    @FXML
    private Label lblLeagueName;

    // TablesViews para cada liga
    @FXML
    private TableView<Equip> tablaLaLiga, tablaPremier, tablaBundesliga, tablaSerieA, tablaLigue1, tablaEredivisie, tablaPrimeira;

    // Columnas La Liga
    @FXML
    private TableColumn<Equip, Integer> colPosLaLiga, colPuntsLaLiga, colPJLaLiga, colVictLaLiga, colEmpLaLiga, colDerrLaLiga, colGFLaLiga, colGCLaLiga, colDGLaLiga;
    @FXML
    private TableColumn<Equip, String> colEquipLaLiga;

    // Columnas Premier League
    @FXML
    private TableColumn<Equip, Integer> colPosPremier, colPuntsPremier, colPJPremier, colVictPremier, colEmpPremier, colDerrPremier, colGFPremier, colGCPremier, colDGPremier;
    @FXML
    private TableColumn<Equip, String> colEquipPremier;

    // Columnas Bundesliga
    @FXML
    private TableColumn<Equip, Integer> colPosBundesliga, colPuntsBundesliga, colPJBundesliga, colVictBundesliga, colEmpBundesliga, colDerrBundesliga, colGFBundesliga, colGCBundesliga, colDGBundesliga;
    @FXML
    private TableColumn<Equip, String> colEquipBundesliga;

    // Columnas Serie A
    @FXML
    private TableColumn<Equip, Integer> colPosSerieA, colPuntsSerieA, colPJSerieA, colVictSerieA, colEmpSerieA, colDerrSerieA, colGFSerieA, colGCSerieA, colDGSerieA;
    @FXML
    private TableColumn<Equip, String> colEquipSerieA;

    // Columnas Ligue 1
    @FXML
    private TableColumn<Equip, Integer> colPosLigue1, colPuntsLigue1, colPJLigue1, colVictLigue1, colEmpLigue1, colDerrLigue1, colGFLigue1, colGCLigue1, colDGLigue1;
    @FXML
    private TableColumn<Equip, String> colEquipLigue1;

    // Columnas Eredivisie
    @FXML
    private TableColumn<Equip, Integer> colPosEredivisie, colPuntsEredivisie, colPJEredivisie, colVictEredivisie, colEmpEredivisie, colDerrEredivisie, colGFEredivisie, colGCEredivisie, colDGEredivisie;
    @FXML
    private TableColumn<Equip, String> colEquipEredivisie;

    // Columnas Primeira Liga
    @FXML
    private TableColumn<Equip, Integer> colPosPrimeira, colPuntsPrimeira, colPJPrimeira, colVictPrimeira, colEmpPrimeira, colDerrPrimeira, colGFPrimeira, colGCPrimeira, colDGPrimeira;
    @FXML
    private TableColumn<Equip, String> colEquipPrimeira;

    private final SistemaService sistemaService = new SistemaService();
    private final EquipDAO equipDAO = new EquipDAO();
    private final JugadorDAO jugadorDAO = new JugadorDAO();
    
    // ListView y Label para goleadores
    @FXML
    private ListView<String> listGoleadores;
    @FXML
    private Label lblGoleadoresTitol;
    
    // ListView y Label para asistidores
    @FXML
    private ListView<String> listAsistidores;
    @FXML
    private Label lblAsistidoresTitol;
    
    // ListView y Label para tarjetas amarillas
    @FXML
    private ListView<String> listAmarillas;
    @FXML
    private Label lblAmarillasTitol;
    
    // ListView y Label para tarjetas rojas
    @FXML
    private ListView<String> listRojas;
    @FXML
    private Label lblRojasTitol;
    
    // Datos de jugadores cargados una vez
    private List<Jugador> totsJugadors;
    
    // Datos de equipos cargados una vez
    private List<Equip> totsEquips;
    
    // Listas actuales de jugadores mostrados (para el doble click)
    private List<Jugador> currentGoleadores;
    private List<Jugador> currentAsistidores;
    private List<Jugador> currentAmarillas;
    private List<Jugador> currentRojas;
    
    // IDs de ligas en orden de tabs
    private final int[] lligaIds = {140, 39, 78, 135, 61, 88, 94};
    private final String[] lligaNoms = {"La Liga", "Premier League", "Bundesliga", "Serie A", "Ligue 1", "Eredivisie", "Primeira Liga"};

    @FXML
    public void initialize() {
        try {
            System.out.println("🔵 PublicController - Iniciant...");
            carregarLogo();
            System.out.println("✅ Logo principal carregat");
            
            carregarBanderesLligues();
            System.out.println("✅ Banderes de lligues carregades");
            
            configurarNavegacio();
            System.out.println("✅ Navegació configurada");
            
            // Inicialitzar l'encapçalament de la lliga (logo + nom)
            if (tabPane != null) {
                int initIndex = tabPane.getSelectionModel().getSelectedIndex();
                actualizarEncabezadoLiga(initIndex >= 0 ? initIndex : 0);
            }
            
            configurarTaules();
            System.out.println("✅ Taules configurades");
            
            carregarDadesLligues();
            System.out.println("✅ Dades de lligues carregades");
            
            carregarJugadors();
            System.out.println("✅ Jugadors carregats");
            
            // Mostrar goleadores y asistidores de la primera liga por defecto
            actualitzarGoleadores(0);
            actualitzarAsistidores(0);
            actualitzarAmarillas(0);
            actualitzarRojas(0);
            
            System.out.println("✅ PublicController inicialitzat correctament");
        } catch (Exception e) {
            System.err.println("❌ ERROR en initialize() de PublicController: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void configurarNavegacio() {
        if (tabPane == null) return;
        
        // Array de todas las tablas
        @SuppressWarnings("unchecked")
        TableView<Equip>[] taules = new TableView[] {tablaLaLiga, tablaPremier, tablaBundesliga, tablaSerieA, tablaLigue1, tablaEredivisie, tablaPrimeira};
        
        // Configurar política de redimensionado y doble click para todas las tablas
        for (TableView<Equip> taula : taules) {
            if (taula != null) {
                taula.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                // Añadir doble click para abrir detalle del equipo
                taula.setOnMouseClicked((MouseEvent event) -> {
                    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                        Equip equipSeleccionat = taula.getSelectionModel().getSelectedItem();
                        if (equipSeleccionat != null) {
                            abrirDetalleEquipo(equipSeleccionat);
                        }
                    }
                });
            }
        }
        
        // Listener para cambio de tab
        tabPane.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int index = newVal.intValue();
            for (int i = 0; i < taules.length; i++) {
                if (taules[i] != null) {
                    boolean visible = (i == index);
                    taules[i].setVisible(visible);
                    taules[i].setManaged(visible);
                }
            }
            // Actualizar encabezado (logo + nombre) y estadísticas al cambiar de liga
            actualizarEncabezadoLiga(index);
            actualitzarGoleadores(index);
            actualitzarAsistidores(index);
            actualitzarAmarillas(index);
            actualitzarRojas(index);
        });
    }

    /**
     * Abre la vista de detalle del equipo seleccionado
     */
    private void abrirDetalleEquipo(Equip equipo) {
        try {
            Scene escenaActual = tabPane.getScene();
            String tituloActual = ((Stage) escenaActual.getWindow()).getTitle();
            EquipoDetalleController.abrirDetalleEquipo(equipo, escenaActual, tituloActual);
        } catch (Exception e) {
            System.err.println("❌ Error al abrir detalle del equipo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void carregarLogo() {
        try {
            File file = new File("LOGO/logo.png");
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imgLogoHeader.setImage(image);
            }
        } catch (Exception e) {
            System.err.println("No s'ha pogut carregar el logo: " + e.getMessage());
        }
    }


    private void carregarBanderesLligues() {
        if (tabPane == null) return;
        
        // Banderes per a cada lliga en l'ordre de les pestanyes
        String[] banderes = {
            "LOGO/Bandera_Paises/espana.jpg",      // La Liga
            "LOGO/Bandera_Paises/inglaterra.jpg",  // Premier League
            "LOGO/Bandera_Paises/alemana.jpg",     // Bundesliga
            "LOGO/Bandera_Paises/italia.jpeg",     // Serie A
            "LOGO/Bandera_Paises/francia.jpg",     // Ligue 1
            "LOGO/Bandera_Paises/paisesbajos.jpg",// Eredivisie
            "LOGO/Bandera_Paises/portugal.jpg"     // Primeira Liga
        };
        
        for (int i = 0; i < Math.min(banderes.length, tabPane.getTabs().size()); i++) {
            try {
                File file = new File(banderes[i]);
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString(), 0, 24, true, true);
                    ImageView imageView = new ImageView(image);
                    imageView.setPreserveRatio(true);
                    imageView.setFitHeight(24);
                    tabPane.getTabs().get(i).setGraphic(imageView);
                    tabPane.getTabs().get(i).setText(""); // Mostrar només la bandera
                } else {
                    System.err.println("⚠️ Bandera no trobada: " + banderes[i]);
                }
            } catch (Exception e) {
                System.err.println("⚠️ Error carregant bandera " + i + ": " + e.getMessage());
            }
        }
    }

    private void actualizarEncabezadoLiga(int index) {
        if (lblLeagueName != null && index >= 0 && index < lligaNoms.length) {
            lblLeagueName.setText(lligaNoms[index]);
        }
        if (imgLeagueIcon != null) {
            String[] logos = {
                "LOGO/Logos_Ligas/LaLiga_2023_Vertical_Logo.svg.png",
                "LOGO/Logos_Ligas/premierLeague.png",
                "LOGO/Logos_Ligas/bundesliga.png",
                "LOGO/Logos_Ligas/serieA.png",
                "LOGO/Logos_Ligas/ligue1.png",
                "LOGO/Logos_Ligas/eredivisie.png",
                "LOGO/Logos_Ligas/primeiraLiga.png"
            };
            if (index >= 0 && index < logos.length) {
                try {
                    File file = new File(logos[index]);
                    if (file.exists()) {
                        Image image = new Image(file.toURI().toString(), 0, 36, true, true);
                        imgLeagueIcon.setImage(image);
                    } else {
                        imgLeagueIcon.setImage(null);
                        System.err.println("⚠️ Logo de lliga no trobat per encapçalament: " + logos[index]);
                    }
                } catch (Exception e) {
                    System.err.println("⚠️ Error carregant logo d'encapçalament: " + e.getMessage());
                }
            }
        }
    }

    private void configurarTaules() {
        // Configurar La Liga (ID: 140)
        configurarTaula(colPosLaLiga, colEquipLaLiga, colPuntsLaLiga, colPJLaLiga, colVictLaLiga, colEmpLaLiga, colDerrLaLiga, colGFLaLiga, colGCLaLiga, colDGLaLiga);

        // Configurar Premier League (ID: 39)
        configurarTaula(colPosPremier, colEquipPremier, colPuntsPremier, colPJPremier, colVictPremier, colEmpPremier, colDerrPremier, colGFPremier, colGCPremier, colDGPremier);

        // Configurar Bundesliga (ID: 78)
        configurarTaula(colPosBundesliga, colEquipBundesliga, colPuntsBundesliga, colPJBundesliga, colVictBundesliga, colEmpBundesliga, colDerrBundesliga, colGFBundesliga, colGCBundesliga, colDGBundesliga);

        // Configurar Serie A (ID: 135)
        configurarTaula(colPosSerieA, colEquipSerieA, colPuntsSerieA, colPJSerieA, colVictSerieA, colEmpSerieA, colDerrSerieA, colGFSerieA, colGCSerieA, colDGSerieA);

        // Configurar Ligue 1 (ID: 61)
        configurarTaula(colPosLigue1, colEquipLigue1, colPuntsLigue1, colPJLigue1, colVictLigue1, colEmpLigue1, colDerrLigue1, colGFLigue1, colGCLigue1, colDGLigue1);

        // Configurar Eredivisie (ID: 88)
        configurarTaula(colPosEredivisie, colEquipEredivisie, colPuntsEredivisie, colPJEredivisie, colVictEredivisie, colEmpEredivisie, colDerrEredivisie, colGFEredivisie, colGCEredivisie, colDGEredivisie);

        // Configurar Primeira Liga (ID: 94)
        configurarTaula(colPosPrimeira, colEquipPrimeira, colPuntsPrimeira, colPJPrimeira, colVictPrimeira, colEmpPrimeira, colDerrPrimeira, colGFPrimeira, colGCPrimeira, colDGPrimeira);
    }

    private void configurarTaula(TableColumn<Equip, Integer> colPos, TableColumn<Equip, String> colEquip,
                                   TableColumn<Equip, Integer> colPunts, TableColumn<Equip, Integer> colPJ,
                                   TableColumn<Equip, Integer> colVict, TableColumn<Equip, Integer> colEmp,
                                   TableColumn<Equip, Integer> colDerr, TableColumn<Equip, Integer> colGF,
                                   TableColumn<Equip, Integer> colGC, TableColumn<Equip, Integer> colDG) {
        
        colPos.setCellValueFactory(new PropertyValueFactory<>("Posicio"));
        colEquip.setCellValueFactory(new PropertyValueFactory<>("nom_Equip"));
        // Mostrar escudo a la izquierda del nombre del equip
        colEquip.setCellFactory(column -> new javafx.scene.control.TableCell<Equip, String>() {
            private final HBox box = new HBox(8);
            private final ImageView escut = new ImageView();
            private final Label nom = new Label();
            {
                escut.setFitHeight(28);
                escut.setPreserveRatio(true);
                escut.setSmooth(true);
                box.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                nom.setStyle("-fx-text-fill: #c9d1d9; -fx-font-weight: 600; -fx-font-size: 12px;");
                nom.setWrapText(false);
                nom.setMaxWidth(220);
                nom.setTextOverrun(javafx.scene.control.OverrunStyle.ELLIPSIS);
                box.getChildren().addAll(escut, nom);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Equip e = (Equip) getTableRow().getItem();
                    String ruta = buscarEscut(e);
                    try {
                        if (ruta != null && !ruta.isEmpty()) {
                            escut.setImage(new Image(new File(ruta).toURI().toString(), 28, 28, true, true));
                        } else {
                            escut.setImage(null);
                        }
                    } catch (Exception ex) {
                        escut.setImage(null);
                    }
                    nom.setText(item != null ? item : "");
                    setGraphic(box);
                }
            }
        });
        colPunts.setCellValueFactory(new PropertyValueFactory<>("Punts"));
        colPJ.setCellValueFactory(new PropertyValueFactory<>("Partits_Jugats"));
        colVict.setCellValueFactory(new PropertyValueFactory<>("Victories"));
        colEmp.setCellValueFactory(new PropertyValueFactory<>("Empats"));
        colDerr.setCellValueFactory(new PropertyValueFactory<>("Derrotes"));
        colGF.setCellValueFactory(new PropertyValueFactory<>("Gols_Marcats"));
        colGC.setCellValueFactory(new PropertyValueFactory<>("Gols_Encaixats"));
        colDG.setCellValueFactory(new PropertyValueFactory<>("Diferencia_Gols"));
    }

    private void carregarDadesLligues() {
        try {
            System.out.println("📊 Carregant dades de lligues...");
            // Obtener todos los equipos
            if (sistemaService.isBBDDConnectada()) {
                System.out.println("🌐 Carregant des de BBDD...");
                totsEquips = equipDAO.obtenirTotsElsEquips();
            } else {
                System.out.println("📁 Carregant des de CSV...");
                totsEquips = LectorCSV.carregarEquipsDesDeFitxer("DATA/equips.csv");
            }

            if (totsEquips != null && !totsEquips.isEmpty()) {
                System.out.println("✅ S'han carregat " + totsEquips.size() + " equips en total");
                // Filtrar y cargar cada liga
                carregarLliga(tablaLaLiga, totsEquips, 140);        // La Liga
                carregarLliga(tablaPremier, totsEquips, 39);        // Premier League
                carregarLliga(tablaBundesliga, totsEquips, 78);     // Bundesliga
                carregarLliga(tablaSerieA, totsEquips, 135);        // Serie A
                carregarLliga(tablaLigue1, totsEquips, 61);         // Ligue 1
                carregarLliga(tablaEredivisie, totsEquips, 88);     // Eredivisie
                carregarLliga(tablaPrimeira, totsEquips, 94);       // Primeira Liga
                System.out.println("✅ Totes les lligues carregades");
            } else {
                System.err.println("⚠️ No s'han trobat dades d'equips");
            }
        } catch (Exception e) {
            System.err.println("❌ Error carregant dades de lligues: " + e.getMessage());
            e.printStackTrace();
            // NO re-llancem per no aturar la càrrega
        }
    }

    /**
     * Normalize strings to safe filenames and try to locate the escudo image.
     */
    private String buscarEscut(Equip e) {
        if (e == null || e.getNom_Equip() == null) return "";
        String nomEquipRaw = e.getNom_Equip();
        String nomEquip = normalitzarPerFitxer(nomEquipRaw);
        String nomLliga = normalitzarPerFitxer(e.getLliga());
        String base = "LOGO/ESCUDOS" + File.separator;

        String[] exts = new String[]{".png", ".jpg", ".webp"};
        java.util.LinkedHashSet<String> candidates = new java.util.LinkedHashSet<>();
        candidates.add(nomEquip);
        // Add variants: strip leading digits (e.g., 1899-hoffenheim -> hoffenheim)
        candidates.add(nomEquip.replaceFirst("^\\d+-", ""));
        // Add last token (after '-')
        String[] parts = nomEquip.split("-");
        if (parts.length > 0) candidates.add(parts[parts.length - 1]);
        // Strip common prefixes like fsv-, vfl-, vfb-
        candidates.add(nomEquip.replaceAll("^(fsv|vfl|vfb|fc)-", ""));
        // Add simplified version
        candidates.add(stripCommonSuffixes(nomEquip));
        // Known mappings for tricky names
        java.util.Map<String, String> overrides = java.util.Map.of(
            "1899-hoffenheim", "hoffenheim",
            "vfl-wolfsburg", "wolfsburg",
            "fsv-mainz-05", "mainz-05",
            "vfl-bochum", "bochum",
            "sheffield-utd", "sheffield-united",
            "stade-brestois-29", "brest",
            "bayern-munich", "bayern-munchen",
            "sv-darmstadt-98", "darmstadt",
            "psv-eindhoven", "psv",
            "celta-vigo", "celta"
        );
        String norm = nomEquip.toLowerCase();
        if (overrides.containsKey(norm)) candidates.add(overrides.get(norm));

        // Try league folder first
        for (String c : candidates) {
            for (String ext : exts) {
                File possible = new File(base + nomLliga + File.separator + c + ext);
                if (possible.exists()) return possible.getPath();
            }
        }

        // Try all subfolders with the candidate names
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
            // Fuzzy fallback: any file name containing any candidate token
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

    private void carregarLliga(TableView<Equip> tabla, List<Equip> totsEquips, int idLliga) {
        List<Equip> equipsLliga = totsEquips.stream()
                .filter(e -> e.getIdLliga() == idLliga)
                .sorted((e1, e2) -> Integer.compare(e1.getPosicio(), e2.getPosicio()))
                .collect(Collectors.toList());

        ObservableList<Equip> dades = FXCollections.observableArrayList(equipsLliga);
        tabla.setItems(dades);
    }
    
    private void carregarJugadors() {
        try {
            System.out.println("⚽ Carregant jugadors...");
            if (sistemaService.isBBDDConnectada()) {
                System.out.println("🌐 Carregant jugadors des de BBDD...");
                totsJugadors = jugadorDAO.obtenirTotsElsJugadors();
            } else {
                System.out.println("📁 Carregant jugadors des de CSV...");
                totsJugadors = LectorCSV.carregarJugadorsDesDeFitxer("DATA/jugadors.csv");
            }
            
            if (totsJugadors != null) {
                System.out.println("✅ S'han carregat " + totsJugadors.size() + " jugadors");
                
                // Relacionar jugadores con equipos completos
                if (totsEquips != null && !totsEquips.isEmpty()) {
                    System.out.println("🔗 Relacionant jugadors amb equips...");
                    for (Jugador j : totsJugadors) {
                        if (j.getEquip() != null) {
                            int idEquip = j.getEquip().getID();
                            // Buscar el equipo completo por ID
                            for (Equip e : totsEquips) {
                                if (e.getID() == idEquip) {
                                    j.setEquip(e);
                                    break;
                                }
                            }
                        }
                    }
                    System.out.println("✅ Jugadors relacionats amb equips");
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error carregant jugadors: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void actualitzarGoleadores(int indexLliga) {
        if (listGoleadores == null || totsJugadors == null || totsJugadors.isEmpty()) {
            return;
        }
        
        int idLliga = lligaIds[indexLliga];
        String nomLliga = lligaNoms[indexLliga];
        
        // Actualizar título
        if (lblGoleadoresTitol != null) {
            lblGoleadoresTitol.setText("Top Golejadors");
        }
        
        // Filtrar y ordenar goleadores de esta liga (top 10)
        currentGoleadores = totsJugadors.stream()
                .filter(j -> j.getIdLliga() == idLliga)
                .sorted(Comparator.comparingInt(Jugador::getGols_marcats).reversed())
                .limit(10)
                .collect(Collectors.toList());
        
        List<String> goleadores = currentGoleadores.stream()
                .map(j -> String.format("%d ⚽  %s (%s)", 
                        j.getGols_marcats(), 
                        j.getNom(), 
                        j.getEquip() != null ? j.getEquip().getNom_Equip() : ""))
                .collect(Collectors.toList());
        
        listGoleadores.setItems(FXCollections.observableArrayList(goleadores));
        
        // Añadir doble click para abrir detalle del jugador
        listGoleadores.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                int index = listGoleadores.getSelectionModel().getSelectedIndex();
                if (index >= 0 && index < currentGoleadores.size()) {
                    JugadorDetalleController.abrirDetalleJugador(
                        currentGoleadores.get(index),
                        listGoleadores.getScene(),
                        "Europe Stats - Vista Pública"
                    );
                }
            }
        });
    }
    
    private void actualitzarAsistidores(int indexLliga) {
        if (listAsistidores == null || totsJugadors == null || totsJugadors.isEmpty()) {
            return;
        }
        
        int idLliga = lligaIds[indexLliga];
        String nomLliga = lligaNoms[indexLliga];
        
        // Actualizar título
        if (lblAsistidoresTitol != null) {
            lblAsistidoresTitol.setText("Top Assistents");
        }
        
        // Filtrar y ordenar asistidores de esta liga (top 10)
        currentAsistidores = totsJugadors.stream()
                .filter(j -> j.getIdLliga() == idLliga)
                .sorted(Comparator.comparingInt(Jugador::getAssistencies).reversed())
                .limit(10)
                .collect(Collectors.toList());
        
        List<String> asistidores = currentAsistidores.stream()
                .map(j -> String.format("%d 🎯  %s (%s)", 
                        j.getAssistencies(), 
                        j.getNom(), 
                        j.getEquip() != null ? j.getEquip().getNom_Equip() : ""))
                .collect(Collectors.toList());
        
        listAsistidores.setItems(FXCollections.observableArrayList(asistidores));
        
        // Añadir doble click para abrir detalle del jugador
        listAsistidores.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                int index = listAsistidores.getSelectionModel().getSelectedIndex();
                if (index >= 0 && index < currentAsistidores.size()) {
                    JugadorDetalleController.abrirDetalleJugador(
                        currentAsistidores.get(index),
                        listAsistidores.getScene(),
                        "Europe Stats - Vista Pública"
                    );
                }
            }
        });
    }
    
    private void actualitzarAmarillas(int indexLliga) {
        if (listAmarillas == null || totsJugadors == null || totsJugadors.isEmpty()) {
            return;
        }
        
        int idLliga = lligaIds[indexLliga];
        String nomLliga = lligaNoms[indexLliga];
        
        // Actualizar título
        if (lblAmarillasTitol != null) {
            lblAmarillasTitol.setText("Top Targetes Grogues");
        }
        
        // Filtrar y ordenar por tarjetas amarillas de esta liga (top 10)
        currentAmarillas = totsJugadors.stream()
                .filter(j -> j.getIdLliga() == idLliga)
                .sorted(Comparator.comparingInt(Jugador::getTargetes_Grogues).reversed())
                .limit(10)
                .collect(Collectors.toList());
        
        List<String> amarillas = currentAmarillas.stream()
                .map(j -> String.format("%d 🟨  %s (%s)", 
                        j.getTargetes_Grogues(), 
                        j.getNom(), 
                        j.getEquip() != null ? j.getEquip().getNom_Equip() : ""))
                .collect(Collectors.toList());
        
        listAmarillas.setItems(FXCollections.observableArrayList(amarillas));
        
        // Renderizar caja amarilla en la celda en vez del emoji (mejora compatibilidad en Windows)
        listAmarillas.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                if (empty || s == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String content = s;
                    String count = "";
                    String rest = content;
                    int firstSpace = content.indexOf(' ');
                    if (firstSpace > 0) {
                        count = content.substring(0, firstSpace);
                        rest = content.substring(firstSpace + 1).trim();
                    }
                    HBox h = new HBox(8);
                    Label lblCount = new Label(count);
                    lblCount.setStyle("-fx-font-weight: bold; -fx-text-fill: #e2e8f0;");
                    Region colorBox = new Region();
                    colorBox.setPrefSize(12, 16);
                    if (content.contains("🟨")) {
                        colorBox.setStyle("-fx-background-color: #f6e05e; -fx-border-color: #d69e2e; -fx-border-radius: 3; -fx-background-radius: 3;");
                    } else {
                        colorBox.setStyle("-fx-background-color: transparent;");
                    }
                    Label lblName = new Label(rest.replace("🟨", "").trim());
                    lblName.setStyle("-fx-text-fill: #e2e8f0;");
                    HBox.setHgrow(lblName, Priority.ALWAYS);
                    h.getChildren().addAll(lblCount, colorBox, lblName);
                    setGraphic(h);
                }
            }
        });
        
        // Añadir doble click para abrir detalle del jugador
        listAmarillas.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                int index = listAmarillas.getSelectionModel().getSelectedIndex();
                if (index >= 0 && index < currentAmarillas.size()) {
                    JugadorDetalleController.abrirDetalleJugador(
                        currentAmarillas.get(index),
                        listAmarillas.getScene(),
                        "Europe Stats - Vista Pública"
                    );
                }
            }
        });
    }
    
    private void actualitzarRojas(int indexLliga) {
        if (listRojas == null || totsJugadors == null || totsJugadors.isEmpty()) {
            return;
        }
        
        int idLliga = lligaIds[indexLliga];
        String nomLliga = lligaNoms[indexLliga];
        
        // Actualizar título
        if (lblRojasTitol != null) {
            lblRojasTitol.setText("Top Targetes Vermelles");
        }
        
        // Filtrar y ordenar por tarjetas rojas de esta liga (top 10)
        currentRojas = totsJugadors.stream()
                .filter(j -> j.getIdLliga() == idLliga)
                .sorted(Comparator.comparingInt(Jugador::getTargetes_Vermelles).reversed())
                .limit(10)
                .collect(Collectors.toList());
        
        List<String> rojas = currentRojas.stream()
                .map(j -> String.format("%d 🟥  %s (%s)", 
                        j.getTargetes_Vermelles(), 
                        j.getNom(), 
                        j.getEquip() != null ? j.getEquip().getNom_Equip() : ""))
                .collect(Collectors.toList());
        
        listRojas.setItems(FXCollections.observableArrayList(rojas));
        
        // Renderizar caja roja en la celda en vez del emoji (mejora compatibilidad en Windows)
        listRojas.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                if (empty || s == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String content = s;
                    String count = "";
                    String rest = content;
                    int firstSpace = content.indexOf(' ');
                    if (firstSpace > 0) {
                        count = content.substring(0, firstSpace);
                        rest = content.substring(firstSpace + 1).trim();
                    }
                    HBox h = new HBox(8);
                    Label lblCount = new Label(count);
                    lblCount.setStyle("-fx-font-weight: bold; -fx-text-fill: #e2e8f0;");
                    Region colorBox = new Region();
                    colorBox.setPrefSize(12, 16);
                    if (content.contains("🟥")) {
                        colorBox.setStyle("-fx-background-color: #fc8181; -fx-border-color: #f56565; -fx-border-radius: 3; -fx-background-radius: 3;");
                    } else {
                        colorBox.setStyle("-fx-background-color: transparent;");
                    }
                    Label lblName = new Label(rest.replace("🟥", "").trim());
                    lblName.setStyle("-fx-text-fill: #e2e8f0;");
                    HBox.setHgrow(lblName, Priority.ALWAYS);
                    h.getChildren().addAll(lblCount, colorBox, lblName);
                    setGraphic(h);
                }
            }
        });
        
        // Añadir doble click para abrir detalle del jugador
        listRojas.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                int index = listRojas.getSelectionModel().getSelectedIndex();
                if (index >= 0 && index < currentRojas.size()) {
                    JugadorDetalleController.abrirDetalleJugador(
                        currentRojas.get(index),
                        listRojas.getScene(),
                        "Europe Stats - Vista Pública"
                    );
                }
            }
        });
    }

    @FXML
    private void handleLogin() {
        try {
            // Cargar la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/europestats/GUI/login.fxml"));
            Parent root = loader.load();

            // Crear una nueva ventana para el login
            Stage loginStage = new Stage();
            loginStage.setTitle("EUROPESTATS - Login Administració");
            
            // Configurar la icona
            App.configurarIcona(loginStage);
            
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.setResizable(false);
            loginStage.centerOnScreen();
            
            // Cerrar la ventana pública actual
            Stage currentStage = (Stage) imgLogoHeader.getScene().getWindow();
            currentStage.close();
            
            loginStage.show();

        } catch (IOException e) {
            System.err.println("❌ Error en carregar el login: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ============================================
    // MÉTODOS DEL MENÚ DESPLEGABLE DEL LOGO
    // ============================================
    
    @FXML
    private void handleInicio(ActionEvent event) {
        if (tabPane != null) {
            tabPane.getSelectionModel().selectFirst();
        }
        System.out.println("🏠 Navegando al inicio");
    }
    
    @FXML
    private void handleEstadisticas(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("📊 Estadístiques Disponibles");
        alert.setHeaderText("EUROPESTATS - Estadístiques 2023/24");
        alert.setContentText(
            "🏆 Lligues disponibles:\n" +
            "• La Liga (Espanya)\n" +
            "• Premier League (Anglaterra)\n" +
            "• Bundesliga (Alemanya)\n" +
            "• Serie A (Itàlia)\n" +
            "• Ligue 1 (França)\n" +
            "• Eredivisie (Països Baixos)\n" +
            "• Primeira Liga (Portugal)\n\n" +
            "📈 Dades: Classificació, Golejadors, Assistents, Targetes"
        );
        alert.showAndWait();
    }
    
    @FXML
    private void handleActualizar(ActionEvent event) {
        try {
            carregarDadesLligues();
            carregarJugadors();
            
            int indexActual = tabPane.getSelectionModel().getSelectedIndex();
            actualitzarGoleadores(indexActual);
            actualitzarAsistidores(indexActual);
            actualitzarAmarillas(indexActual);
            actualitzarRojas(indexActual);
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("🔄 Actualització");
            alert.setHeaderText(null);
            alert.setContentText("✅ Dades actualitzades correctament");
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("❌ Error al actualizar: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleAcercaDe(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ℹ️ Sobre EUROPESTATS");
        alert.setHeaderText("EUROPESTATS v1.0");
        alert.setContentText(
            "📊 Aplicació d'estadístiques del futbol europeu\n\n" +
            "🏟️ Temporada: 2023/24\n\n" +
            "📦 Dades: API-Football\n\n" +
            "💻 JavaFX 21 - Projecte DAM2"
        );
        alert.showAndWait();
    }
    
    @FXML
    private void handleEquipos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/europestats/GUI/equipos_view.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) imgLogoHeader.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("EUROPESTATS - Tots els Equips");
            stage.centerOnScreen();
            
        } catch (IOException e) {
            System.err.println("❌ Error al cargar la página de equipos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleJugadores(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/europestats/GUI/jugadores_view.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) imgLogoHeader.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("EUROPESTATS - Tots els Jugadors");
            stage.centerOnScreen();
            
        } catch (IOException e) {
            System.err.println("❌ Error al cargar la página de jugadores: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Selecciona la pestaña de la liga correspondiente por su ID
     * @param ligaId ID de la liga (API Football)
     */
    public void seleccionarLigaPorId(int ligaId) {
        if (tabPane == null) return;
        
        int index = switch (ligaId) {
            case 140 -> 0; // La Liga
            case 39 -> 1;  // Premier League
            case 78 -> 2;  // Bundesliga
            case 135 -> 3; // Serie A
            case 61 -> 4;  // Ligue 1
            case 88 -> 5;  // Eredivisie
            case 94 -> 6;  // Primeira Liga
            default -> 0;
        };
        
        tabPane.getSelectionModel().select(index);
    }
}
