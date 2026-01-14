package europestats.GUI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import europestats.CLASES.Equip;
import europestats.CSV.LectorCSV;
import europestats.DAO.EquipDAO;
import europestats.SERVEIS.SistemaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class EquiposController {

    @FXML
    private ImageView imgLogoHeader;
    
    @FXML
    private TextField txtBuscar;
    
    @FXML
    private Label lblResultados;
    
    @FXML
    private Label lblTotalEquipos;
    
    @FXML
    private TableView<Equip> tablaEquipos;
    
    @FXML
    private TableColumn<Equip, String> colLiga, colEquipo;
    
    @FXML
    private TableColumn<Equip, Integer> colPosicion, colPuntos, colPJ, colVictorias, colEmpates, colDerrotas, colGF, colGC, colDG;
    
    private final EquipDAO equipDAO = new EquipDAO();
    private final SistemaService sistemaService = new SistemaService();
    private List<Equip> todosEquipos;
    private ObservableList<Equip> equiposFiltrados;

    @FXML
    public void initialize() {
        try {
            System.out.println("🔵 EquiposController - Iniciant...");
            carregarLogo();
            configurarColumnas();
            carregarEquipos();
            System.out.println("✅ EquiposController inicialitzat correctament");
        } catch (Exception e) {
            System.err.println("❌ ERROR en initialize() de EquiposController: " + e.getMessage());
            e.printStackTrace();
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
        colLiga.setCellValueFactory(new PropertyValueFactory<>("Lliga"));
        colEquipo.setCellValueFactory(new PropertyValueFactory<>("nom_Equip"));

        // Mostrar escudo a la izquierda del nombre del equipo
        colEquipo.setCellFactory(column -> new TableCell<Equip, String>() {
            private final ImageView iv = new ImageView();
            private final HBox box = new HBox(8);
            {
                iv.setFitHeight(22); // ajusta al recuadro de la tabla
                iv.setPreserveRatio(true);
                box.setAlignment(Pos.CENTER_LEFT);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Equip e = getTableRow() != null ? getTableRow().getItem() : null;
                    iv.setImage(null);
                    if (e != null) {
                        // buscar escudo para el equipo
                        String path = buscarEscudoLocal(e);
                        if (path != null && !path.isEmpty()) {
                            java.io.File f = new java.io.File(path);
                            if (f.exists()) {
                                iv.setImage(new javafx.scene.image.Image(f.toURI().toString(), 24, 24, true, true));
                            }
                        }
                    }
                    javafx.scene.control.Label lbl = new javafx.scene.control.Label(item);
                    lbl.setStyle("-fx-text-fill: #c9d1d9; -fx-font-size: 13px;");
                    box.getChildren().clear();
                    box.getChildren().addAll(iv, lbl);
                    setGraphic(box);
                    setText(null);
                }
            }
        });
        colPosicion.setCellValueFactory(new PropertyValueFactory<>("Posicio"));
        colPuntos.setCellValueFactory(new PropertyValueFactory<>("Punts"));
        colPJ.setCellValueFactory(new PropertyValueFactory<>("Partits_Jugats"));
        colVictorias.setCellValueFactory(new PropertyValueFactory<>("Victories"));
        colEmpates.setCellValueFactory(new PropertyValueFactory<>("Empats"));
        colDerrotas.setCellValueFactory(new PropertyValueFactory<>("Derrotes"));
        colGF.setCellValueFactory(new PropertyValueFactory<>("Gols_Marcats"));
        colGC.setCellValueFactory(new PropertyValueFactory<>("Gols_Encaixats"));
        colDG.setCellValueFactory(new PropertyValueFactory<>("Diferencia_Gols"));
        
        // Política de redimensionamiento
        tablaEquipos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Doble click para abrir detalle del equipo
        tablaEquipos.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                Equip equipSeleccionat = tablaEquipos.getSelectionModel().getSelectedItem();
                if (equipSeleccionat != null) {
                    abrirDetalleEquipo(equipSeleccionat);
                }
            }
        });
    }
    
    /**
     * Abre la vista de detalle del equipo seleccionado
     */
    private void abrirDetalleEquipo(Equip equipo) {
        try {
            Scene escenaActual = tablaEquipos.getScene();
            String tituloActual = ((Stage) escenaActual.getWindow()).getTitle();
            EquipoDetalleController.abrirDetalleEquipo(equipo, escenaActual, tituloActual);
        } catch (Exception e) {
            System.err.println("❌ Error al abrir detalle del equipo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void carregarEquipos() {
        try {
            // Intentar cargar desde BBDD, si falla usar CSV
            if (sistemaService.isBBDDConnectada()) {
                System.out.println("🌐 Carregant equips des de BBDD...");
                todosEquipos = equipDAO.obtenirTotsElsEquips();
            } else {
                System.out.println("📁 Carregant equips des de CSV...");
                todosEquipos = LectorCSV.carregarEquipsDesDeFitxer("DATA/equips.csv");
            }
            
            if (todosEquipos == null) {
                todosEquipos = new ArrayList<>();
            }
            
            equiposFiltrados = FXCollections.observableArrayList(todosEquipos);
            tablaEquipos.setItems(equiposFiltrados);
            
            actualizarContadores(todosEquipos.size(), todosEquipos.size());
            System.out.println("✅ Carregats " + todosEquipos.size() + " equips");
        } catch (Exception e) {
            System.err.println("❌ Error carregant equips des de BBDD, intentant CSV...");
            try {
                todosEquipos = LectorCSV.carregarEquipsDesDeFitxer("DATA/equips.csv");
                if (todosEquipos == null) {
                    todosEquipos = new ArrayList<>();
                }
                equiposFiltrados = FXCollections.observableArrayList(todosEquipos);
                tablaEquipos.setItems(equiposFiltrados);
                actualizarContadores(todosEquipos.size(), todosEquipos.size());
                System.out.println("✅ Carregats " + todosEquipos.size() + " equips des de CSV");
            } catch (Exception ex) {
                System.err.println("❌ Error carregant equips: " + ex.getMessage());
                todosEquipos = new ArrayList<>();
                equiposFiltrados = FXCollections.observableArrayList();
                tablaEquipos.setItems(equiposFiltrados);
                actualizarContadores(0, 0);
            }
        }
    }
    
    @FXML
    private void handleBuscar() {
        String filtro = txtBuscar.getText().toLowerCase().trim();
        
        if (filtro.isEmpty()) {
            equiposFiltrados.setAll(todosEquipos);
            lblResultados.setText("Mostrant tots els equips");
        } else {
            List<Equip> resultados = todosEquipos.stream()
                .filter(e -> e.getNom_Equip().toLowerCase().contains(filtro) ||
                            e.getLliga().toLowerCase().contains(filtro))
                .collect(Collectors.toList());
            
            equiposFiltrados.setAll(resultados);
            lblResultados.setText("Resultats per: \"" + txtBuscar.getText() + "\"");
        }
        
        actualizarContadores(equiposFiltrados.size(), todosEquipos.size());
    }
    
    @FXML
    private void handleLimpiar() {
        txtBuscar.clear();
        equiposFiltrados.setAll(todosEquipos);
        lblResultados.setText("Mostrant tots els equips");
        actualizarContadores(todosEquipos.size(), todosEquipos.size());
    }
    
    private void actualizarContadores(int mostrados, int total) {
        if (mostrados == total) {
            lblTotalEquipos.setText("Total: " + total + " equips");
        } else {
            lblTotalEquipos.setText("Mostrant " + mostrados + " de " + total + " equips");
        }
    }

    /**
     * Buscar escudo localmente (similar a EquipoDetalleController)
     */
    private String buscarEscudoLocal(Equip e) {
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
        // Ya estamos aquí, no hacer nada
    }

    @FXML
    private void handleJugadores() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/europestats/GUI/jugadores_view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) imgLogoHeader.getScene().getWindow();
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
