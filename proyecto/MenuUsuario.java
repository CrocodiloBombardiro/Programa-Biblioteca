import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.sql.*;

public class MenuUsuario extends JFrame {
    public MenuUsuario() {

        setTitle("Gestion Biblioteca");
        setSize(1280,720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240,240,240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;


        JButton btnLibro = new JButton("Ver libros");
        JButton btnPrestamo = new JButton("Gestionar prestamos");
        JButton btnSalir = new JButton("Salir");

        Dimension botonSize = new Dimension(300, 60);

        btnLibro.setPreferredSize(botonSize);
        btnLibro.setBackground(Color.WHITE);
        btnLibro.setFont(new Font("Manrope", Font.BOLD, 16));
        btnPrestamo.setPreferredSize(botonSize);
        btnPrestamo.setBackground(Color.WHITE);
        btnPrestamo.setFont(new Font("Manrope", Font.BOLD, 16));
        btnSalir.setPreferredSize(botonSize);
        btnSalir.setBackground(Color.WHITE);
        btnSalir.setFont(new Font("Manrope", Font.BOLD, 16));

        gbc.gridy = 0;
        panel.add(btnLibro, gbc);
        gbc.gridy = 1;
        panel.add(btnPrestamo, gbc);
        gbc.gridy = 2;
        panel.add(btnSalir, gbc);


        btnLibro.addActionListener(e -> verLibros());
        btnPrestamo.addActionListener(e -> new GestPrestamo());
        btnSalir.addActionListener(e -> System.exit(0));

        add(panel);
        setVisible(true);
    }

    public void verLibros() {
        String sql = "SELECT id, titulo, autor, anio_publicacion, editorial, genero FROM libro";
        StringBuilder sb = new StringBuilder();
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            boolean hayLibros = false;
            while (rs.next()) {
                hayLibros = true;
                sb.append("Título: ").append(rs.getString("titulo"))
                  .append("\nAutor: ").append(rs.getString("autor"))
                  .append("\nAño de publicación: ").append(rs.getInt("anio_publicacion"))
                  .append("\nEditorial: ").append(rs.getString("editorial"))
                  .append("\nGénero: ").append(rs.getString("genero"))
                  .append("\nID: ").append(rs.getInt("id"))
                  .append("\n\n");
            }
            if (!hayLibros) {
                JOptionPane.showMessageDialog(this, "No hay libros registrados.");
            } else {
                JOptionPane.showMessageDialog(this, sb.toString(), "Lista de libros", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener los libros: " + e.getMessage());
            e.printStackTrace();
        }
    }
}