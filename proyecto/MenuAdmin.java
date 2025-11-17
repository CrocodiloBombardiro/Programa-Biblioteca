import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuAdmin extends JFrame {
    public MenuAdmin() {
        setTitle("Gestion biblioteca");
        setSize(1280,720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel( new GridBagLayout());
        panel.setBackground(new Color(240,240,240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;

        JButton btnEstudiante = new JButton("Gestionar usuarios");
        JButton btnLibro = new JButton("Gestionar libros");
        JButton btnPrestamo = new JButton("Gestionar prestamos");
        JButton btnBibliotecologo = new JButton("Gestionar bibliotecologos");
        JButton btnSalir = new JButton("Salir");

        Dimension botonSize = new Dimension(300, 60);

        btnEstudiante.setPreferredSize(botonSize);
        btnEstudiante.addActionListener(e -> new GestUsuario());
        btnEstudiante.setBackground(Color.WHITE);
        btnEstudiante.setFont(new Font("Manrope", Font.BOLD, 16));
        
        btnLibro.setPreferredSize(botonSize);
        btnLibro.addActionListener(e -> new GestLibro());
        btnLibro.setBackground(Color.WHITE);
        btnLibro.setFont(new Font("Manrope", Font.BOLD, 16));
        
        btnPrestamo.setPreferredSize(botonSize);
        btnPrestamo.addActionListener(e -> new GestPrestamo());
        btnPrestamo.setBackground(Color.WHITE);
        btnPrestamo.setFont(new Font("Manrope", Font.BOLD, 16));
        
        btnBibliotecologo.setPreferredSize(botonSize);
        btnBibliotecologo.addActionListener(e -> new GestBibliotecologo());
        btnBibliotecologo.setBackground(Color.WHITE);
        btnBibliotecologo.setFont(new Font("Manrope", Font.BOLD, 16));
        
        btnSalir.setPreferredSize(botonSize);
        btnSalir.addActionListener(e -> System.exit(0));
        btnSalir.setBackground(Color.WHITE);
        btnSalir.setFont(new Font("Manrope", Font.BOLD, 16));

        gbc.gridy = 0;
        panel.add(btnEstudiante, gbc);
        gbc.gridy = 1;
        panel.add(btnLibro, gbc);
        gbc.gridy = 2;
        panel.add(btnBibliotecologo, gbc);
        gbc.gridy = 3;
        panel.add(btnPrestamo, gbc);
        gbc.gridy = 4;
        panel.add(btnSalir, gbc);

        add(panel);
        setVisible(true);
    }
}