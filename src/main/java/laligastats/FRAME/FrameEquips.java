package laligastats.FRAME;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Comparator;

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
import laligastats.DAO.EquipDAO;

public class FrameEquips extends JFrame {
    private EquipDAO equipDAO = new EquipDAO();
    private JTable taulaEquips;
    private DefaultTableModel modelEquips;

    public FrameEquips() {
        setTitle("Gestió d'Equips");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columnes = { "Posició", "Nom", "Punts", "Partits", "Victòries", "Empats", "Derrotes" };
        modelEquips = new DefaultTableModel(columnes, 0);
        taulaEquips = new JTable(modelEquips);
        add(new JScrollPane(taulaEquips), BorderLayout.CENTER);

        JPanel panellBotons = new JPanel();
        JButton btnAfegir = new JButton("Afegir Equip");
        JButton btnModificar = new JButton("Modificar Equip");
        JButton btnEliminar = new JButton("Eliminar Equip");
        JButton btnEnrere = new JButton("Tornar");

        panellBotons.add(btnAfegir);
        panellBotons.add(btnModificar);
        panellBotons.add(btnEliminar);
        panellBotons.add(btnEnrere);
        add(panellBotons, BorderLayout.SOUTH);

        btnAfegir.addActionListener(e -> afegirNouEquip());

        btnModificar.addActionListener(e -> modificarEquipSeleccionat());

        btnEliminar.addActionListener(e -> eliminarEquipSeleccionat());
        
        btnEnrere.addActionListener(e -> {
            dispose();
            new InterficiePrincp().setVisible(true);
        });

        carregarEquips();
    }

    private void carregarEquips() {
        modelEquips.setRowCount(0);
        ArrayList<Equip> equips = equipDAO.obtenirTotsElsEquips();
        equips.sort(Comparator.comparingInt(Equip::getPunts).reversed());
        for (Equip eq : equips) {
            modelEquips.addRow(new Object[] {
                    eq.getPosicio(), eq.getNom_Equip(),
                    eq.getPunts(), eq.getPartits_Jugats(),
                    eq.getVictories(), eq.getEmpats(), eq.getDerrotes()
            });
        }
    }

    private void afegirNouEquip() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField tfNom = new JTextField();

        panel.add(new JLabel("Nom del nou equip:"));
        panel.add(tfNom);

        int result = JOptionPane.showConfirmDialog(this, panel, "Afegir Equip",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String nom = tfNom.getText().trim();

                // Comprovar si ja existeix l'equip pel nom
                if (equipDAO.obtenirEquipPerNom(nom) != null) {
                    JOptionPane.showMessageDialog(this, "L'equip '" + nom + "' ja existeix!");
                    return;
                }

                Equip nouEquip = new Equip(
                        0, // ID automàtic
                        0,
                        nom,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0, 0, 0, 0, 0, 0, 0 // Altres camps que no posem per ara
                );

                int nouID = equipDAO.insertarEquip(nouEquip); // Retorna ID generat
                if (nouID != -1) {
                    JOptionPane.showMessageDialog(this, "Equip afegit amb ID: " + nouID);
                    carregarEquips(); // Actualitzar taula
                } else {
                    JOptionPane.showMessageDialog(this, "Error afegint equip.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Els camps numèrics han de ser nombres enters!");
            }
        }
    }

    private void modificarEquipSeleccionat() {
        int fila = taulaEquips.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un equip abans de modificar.");
            return;
        }

        String nomEquip = (String) modelEquips.getValueAt(fila, 1); // Nom és la columna 1
        Equip equip = equipDAO.obtenirEquipPerNom(nomEquip);

        if (equip == null) {
            JOptionPane.showMessageDialog(this, "Error: Equip no trobat.");
            return;
        }

        // Panell amb JTextFields per modificar els camps
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField tfPosicio = new JTextField(String.valueOf(equip.getPosicio()));
        JTextField tfPunts = new JTextField(String.valueOf(equip.getPunts()));
        JTextField tfPartits = new JTextField(String.valueOf(equip.getPartits_Jugats()));
        JTextField tfVictories = new JTextField(String.valueOf(equip.getVictories()));
        JTextField tfEmpats = new JTextField(String.valueOf(equip.getEmpats()));
        JTextField tfDerrotes = new JTextField(String.valueOf(equip.getDerrotes()));

        panel.add(new JLabel("Posició:"));
        panel.add(tfPosicio);
        panel.add(new JLabel("Punts:"));
        panel.add(tfPunts);
        panel.add(new JLabel("Partits jugats:"));
        panel.add(tfPartits);
        panel.add(new JLabel("Victòries:"));
        panel.add(tfVictories);
        panel.add(new JLabel("Empats:"));
        panel.add(tfEmpats);
        panel.add(new JLabel("Derrotes:"));
        panel.add(tfDerrotes);

        int result = JOptionPane.showConfirmDialog(this, panel, "Modificar Equip",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                equip.setPosicio(Integer.parseInt(tfPosicio.getText()));
                equip.setPunts(Integer.parseInt(tfPunts.getText()));
                equip.setPartits_Jugats(Integer.parseInt(tfPartits.getText()));
                equip.setVictories(Integer.parseInt(tfVictories.getText()));
                equip.setEmpats(Integer.parseInt(tfEmpats.getText()));
                equip.setDerrotes(Integer.parseInt(tfDerrotes.getText()));

                if (equipDAO.actualitzarEquip(equip)) {
                    JOptionPane.showMessageDialog(this, "Equip actualitzat correctament!");
                    carregarEquips(); // refrescar taula
                } else {
                    JOptionPane.showMessageDialog(this, "Error actualitzant l'equip.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Revisa que tots els camps numèrics són correctes.");
            }
        }
    }

    private void eliminarEquipSeleccionat() {
    int fila = taulaEquips.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Selecciona un equip abans d'eliminar.");
        return;
    }

    String nomEquip = (String) modelEquips.getValueAt(fila, 1); // Nom és la columna 1

    int confirm = JOptionPane.showConfirmDialog(this,
            "Segur que vols eliminar l'equip '" + nomEquip + "'?",
            "Confirmar eliminació",
            JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        boolean eliminat = equipDAO.eliminarEquipPerNom(nomEquip);
        if (eliminat) {
            JOptionPane.showMessageDialog(this, "Equip eliminat correctament!");
            carregarEquips(); // refresquem taula
        } else {
            JOptionPane.showMessageDialog(this, "Error eliminant l'equip.");
        }
    }
}


}
