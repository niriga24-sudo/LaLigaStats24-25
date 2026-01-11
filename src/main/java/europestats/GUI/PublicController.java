package europestats.GUI;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import europestats.CLASES.Equip;
import europestats.CSV.LectorCSV;
import europestats.DAO.EquipDAO;
import europestats.MAIN.App;
import europestats.SERVEIS.SistemaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PublicController {

    @FXML
    private ImageView imgLogoHeader;
    
    @FXML
    private javafx.scene.control.TabPane tabPane;

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

    @FXML
    public void initialize() {
        try {
            System.out.println("🔵 PublicController - Iniciant...");
            carregarLogo();
            System.out.println("✅ Logo principal carregat");
            
            carregarLogosLligues();
            System.out.println("✅ Logos de lligues carregats");
            
            configurarNavegacio();
            System.out.println("✅ Navegació configurada");
            
            configurarTaules();
            System.out.println("✅ Taules configurades");
            
            carregarDadesLligues();
            System.out.println("✅ Dades de lligues carregades");
            
            System.out.println("✅ PublicController inicialitzat correctament");
        } catch (Exception e) {
            System.err.println("❌ ERROR en initialize() de PublicController: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void configurarNavegacio() {
        if (tabPane == null) return;
        
        // Array de todas las tablas
        TableView<?>[] taules = {tablaLaLiga, tablaPremier, tablaBundesliga, tablaSerieA, tablaLigue1, tablaEredivisie, tablaPrimeira};
        
        // Configurar política de redimensionado para todas las tablas
        for (TableView<?> taula : taules) {
            if (taula != null) {
                taula.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
        });
        
        // Mostrar la primera tabla por defecto
        if (taules[0] != null) {
            taules[0].setVisible(true);
            taules[0].setManaged(true);
        }
    }

    private void carregarLogo() {
        try {
            File file = new File("LOGO/logo.png");
            if (file.exists()) {
                Image image = new Image(file.toURI().toString(), 0, 40, true, true);
                imgLogoHeader.setImage(image);
            }
        } catch (Exception e) {
            System.err.println("No s'ha pogut carregar el logo: " + e.getMessage());
        }
    }

    private void carregarLogosLligues() {
        if (tabPane == null) return;
        
        String[] logos = {
            "LOGO/Logos_Ligas/LaLiga_2023_Vertical_Logo.svg.png",
            "LOGO/Logos_Ligas/premierLeague.png",
            "LOGO/Logos_Ligas/bundesliga.png",
            "LOGO/Logos_Ligas/serieA.png",
            "LOGO/Logos_Ligas/ligue1.png",
            "LOGO/Logos_Ligas/eredivisie.png",
            "LOGO/Logos_Ligas/primeiraLiga.png"
        };
        
        for (int i = 0; i < Math.min(logos.length, tabPane.getTabs().size()); i++) {
            try {
                File file = new File(logos[i]);
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString(), 0, 24, true, true);
                    ImageView imageView = new ImageView(image);
                    imageView.setPreserveRatio(true);
                    imageView.setFitHeight(24);
                    tabPane.getTabs().get(i).setGraphic(imageView);
                    tabPane.getTabs().get(i).setText(""); // Eliminar texto, solo icono
                }
            } catch (Exception e) {
                System.err.println("⚠️ Error carregant logo " + i + ": " + e.getMessage());
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
            List<Equip> totsEquips;
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

    private void carregarLliga(TableView<Equip> tabla, List<Equip> totsEquips, int idLliga) {
        List<Equip> equipsLliga = totsEquips.stream()
                .filter(e -> e.getIdLliga() == idLliga)
                .sorted((e1, e2) -> Integer.compare(e1.getPosicio(), e2.getPosicio()))
                .collect(Collectors.toList());

        ObservableList<Equip> dades = FXCollections.observableArrayList(equipsLliga);
        tabla.setItems(dades);
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
}
