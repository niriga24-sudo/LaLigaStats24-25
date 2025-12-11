package laligastats.FRAME;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import laligastats.CLASES.Equip;
import laligastats.CLASES.Jugador;
import laligastats.CSV.GestorCSV;
import laligastats.DAO.JugadorDAO;

public class FrameJugadors extends JFrame {
    private JugadorDAO jugadorDAO = new JugadorDAO();
    private JTable taulaJugadors;
    private DefaultTableModel modelJugadors;

    public FrameJugadors() {
        setTitle("Gestió de Jugadors");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columnes = { "ID", "Nom", "Equip", "Gols", "Partits", "Assistències", "Passades" };
        modelJugadors = new DefaultTableModel(columnes, 0);
        taulaJugadors = new JTable(modelJugadors);
        add(new JScrollPane(taulaJugadors), BorderLayout.CENTER);

        JPanel panellBotons = new JPanel();
        JButton btnAfegir = new JButton("Afegir Jugador");
        JButton btnModificar = new JButton("Modificar Jugador");
        JButton btnEliminar = new JButton("Eliminar Jugador");
        JButton btnEnrere = new JButton("Tornar");

        panellBotons.add(btnAfegir);
        panellBotons.add(btnModificar);
        panellBotons.add(btnEliminar);
        panellBotons.add(btnEnrere);
        add(panellBotons, BorderLayout.SOUTH);

        btnAfegir.addActionListener(e -> afegirNouJugador());

        btnModificar.addActionListener(e -> modificarJugadorSeleccionat());

        btnEliminar.addActionListener(e -> eliminarJugadorSeleccionat());

        btnEnrere.addActionListener(e -> {
            dispose();
            new InterficiePrincp().setVisible(true);
        });

        carregarJugadors();
    }

    private void carregarJugadors() {
        modelJugadors.setRowCount(0);
        ArrayList<Jugador> jugadors = jugadorDAO.obtenirTotsJugadors();
        for (Jugador j : jugadors) {
            modelJugadors.addRow(new Object[] {
                    j.getID(), j.getNom(), j.getEquip().getNom_Equip(),
                    j.getGols_marcats(), j.getPartits(),
                    j.getAssistencies(), j.getPassades_Completades()
            });
        }
    }

    private void afegirNouJugador() {

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField tfNom = new JTextField();
        JTextField tfEquip = new JTextField(); // Equip introduït manualment

        panel.add(new JLabel("Nom:"));
        panel.add(tfNom);
        panel.add(new JLabel("Equip:"));
        panel.add(tfEquip);

        int result = JOptionPane.showConfirmDialog(this, panel, "Afegir ",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String nom = tfNom.getText().trim();
            String nomEquip = tfEquip.getText().trim();

            Equip equipSeleccionat = GestorCSV.cercarEquipxNom(nomEquip);

            if (equipSeleccionat == null) {
                JOptionPane.showMessageDialog(this, "L'equip '" + nomEquip + "' no existeix!");
                return;
            }

            Jugador nouJugador = new Jugador(
                    0, // ID s'assignarà automàticament
                    0, // Posicio
                    nom,
                    equipSeleccionat,
                    0,
                    0,
                    0, // Gols x Partit
                    0, // Posicio_Assistencies
                    0, // Assistencies
                    0, // Assist_x_Partit
                    0, // Posicio_Passades
                    0, // Passades_Completades
                    0 // Passades_Totals
            );

            int nouID = new JugadorDAO().insertarJugador(nouJugador);
            if (nouID != -1) {
                JOptionPane.showMessageDialog(this, "Jugador afegit amb ID: " + nouID);
                carregarJugadors(); // Actualitzar taula
            } else {
                JOptionPane.showMessageDialog(this, "Error afegint jugador.");
            }
        }

    }

    private void modificarJugadorSeleccionat() {
        int fila = taulaJugadors.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un jugador abans de modificar.");
            return;
        }

        int idJugador = (int) modelJugadors.getValueAt(fila, 0);
        Jugador jugador = jugadorDAO.obtenirJugadorxID(idJugador);

        if (jugador == null) {
            JOptionPane.showMessageDialog(this, "Error: Jugador no trobat.");
            return;
        }

        // Crear panell amb JTextFields per editar els camps
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField tfNom = new JTextField(jugador.getNom());
        JTextField tfGols = new JTextField(String.valueOf(jugador.getGols_marcats()));
        JTextField tfPartits = new JTextField(String.valueOf(jugador.getPartits()));
        JTextField tfAssistencies = new JTextField(String.valueOf(jugador.getAssistencies()));
        JTextField tfPassades = new JTextField(String.valueOf(jugador.getPassades_Completades()));

        panel.add(new JLabel("Nom:"));
        panel.add(tfNom);
        panel.add(new JLabel("Gols:"));
        panel.add(tfGols);
        panel.add(new JLabel("Partits:"));
        panel.add(tfPartits);
        panel.add(new JLabel("Assistències:"));
        panel.add(tfAssistencies);
        panel.add(new JLabel("Passades:"));
        panel.add(tfPassades);

        int result = JOptionPane.showConfirmDialog(this, panel, "Modificar Jugador",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                jugador.setNom(tfNom.getText());
                jugador.setGols_marcats(Integer.parseInt(tfGols.getText()));
                jugador.setPartits(Integer.parseInt(tfPartits.getText()));
                jugador.setAssistencies(Integer.parseInt(tfAssistencies.getText()));
                jugador.setPassades_Completades(Integer.parseInt(tfPassades.getText()));

                if (jugadorDAO.actualitzarJugador(jugador)) {
                    JOptionPane.showMessageDialog(this, "Jugador actualitzat correctament!");
                    carregarJugadors(); // refresquem la taula
                } else {
                    JOptionPane.showMessageDialog(this, "Error actualitzant el jugador.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Revisa que tots els camps numèrics són correctes.");
            }
        }
    }

    private void eliminarJugadorSeleccionat() {
        int fila = taulaJugadors.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un jugador abans d'eliminar.");
            return;
        }

        int idJugador = (int) modelJugadors.getValueAt(fila, 0);
        String nomJugador = (String) modelJugadors.getValueAt(fila, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Estàs segur que vols eliminar el jugador: " + nomJugador + "?",
                "Confirmar eliminació",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = jugadorDAO.eliminarJugador(idJugador);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Jugador eliminat correctament!");
                carregarJugadors(); // refresquem la taula
            } else {
                JOptionPane.showMessageDialog(this, "Error eliminant el jugador.");
            }
        }
    }

}
