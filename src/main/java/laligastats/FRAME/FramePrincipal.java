package laligastats.FRAME;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;

public class InterficiePrincp extends JFrame {

    public InterficiePrincp() {
        setTitle("LaLiga Stats 24-25");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centrar la finestra

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton btnEquips = new JButton("Configurar Equips");
        JButton btnJugadors = new JButton("Configurar Jugadors");

        add(btnEquips, gbc);
        gbc.gridy = 1;
        add(btnJugadors, gbc);

        btnEquips.addActionListener(e -> {
            dispose();
            new FrameEquips().setVisible(true);
        });

        btnJugadors.addActionListener(e -> {
            dispose();
            new FrameJugadors().setVisible(true);
        });
    }

    public static void main(String[] args) {
        new InterficiePrincp().setVisible(true);
    }
}
