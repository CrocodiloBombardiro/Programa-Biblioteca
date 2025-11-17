import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class VentSelLog extends JFrame {
    public VentSelLog() {
        setTitle("Selección logueo");
        setSize(1280,720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240,240,240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15,0,15,0);
        gbc.gridx =0;
        gbc.fill = GridBagConstraints.NONE;

        JButton logAdm = new JButton("Ingresar como administrador");
        JButton logUsr = new JButton("Ingresar como usuario");

        Dimension botonSize = new Dimension(300, 60);
        logAdm.setPreferredSize(botonSize);
        logUsr.setPreferredSize(botonSize);
        logAdm.setBackground(Color.WHITE);
        logAdm.setFont(new Font("Manrope", Font.BOLD, 16));
        logUsr.setBackground(Color.WHITE);
        logUsr.setFont(new Font("Manrope", Font.BOLD, 16));

        gbc.gridy = 0;
        panel.add(logAdm, gbc);
        gbc.gridy = 1;
        panel.add(logUsr, gbc);

        logAdm.addActionListener(e -> { new VentLogAdm(); dispose();});
        logUsr.addActionListener(e -> { new VentLogUsr(); dispose();});

        add(panel);
        
        setVisible(true);
    }
    public static void main(String[] args) {
        new VentSelLog();
    }
}