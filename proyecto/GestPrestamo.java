import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class GestPrestamo extends JFrame {

    public GestPrestamo() {
        setTitle("Menú préstamos");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.gridx = 0;

        JButton btnAgregarPrestamo = new JButton("Hacer préstamo");
        JButton btnDevolverPrestamo = new JButton("Devolver préstamo");
        JButton btnVerHistorialPrestamo = new JButton("Historial de préstamos");
        JButton btnSalir = new JButton("Volver al menú principal");

        Dimension botonSize = new Dimension(300, 60);

        btnAgregarPrestamo.setPreferredSize(botonSize);
        btnAgregarPrestamo.addActionListener(e -> hacerPrestamo());
        btnAgregarPrestamo.setBackground(Color.WHITE);
        btnAgregarPrestamo.setFont(new Font("Manrope", Font.BOLD, 16));

        btnDevolverPrestamo.setPreferredSize(botonSize);
        btnDevolverPrestamo.addActionListener(e -> devolverPrestamo());
        btnDevolverPrestamo.setBackground(Color.WHITE);
        btnDevolverPrestamo.setFont(new Font("Manrope", Font.BOLD, 16));

        btnVerHistorialPrestamo.setPreferredSize(botonSize);
        btnVerHistorialPrestamo.addActionListener(e -> historialPrestamo());
        btnVerHistorialPrestamo.setBackground(Color.WHITE);
        btnVerHistorialPrestamo.setFont(new Font("Manrope", Font.BOLD, 16));

        btnSalir.setPreferredSize(botonSize);
        btnSalir.addActionListener(e -> dispose());
        btnSalir.setBackground(Color.WHITE);
        btnSalir.setFont(new Font("Manrope", Font.BOLD, 16));

        gbc.gridy = 0;
        panel.add(btnAgregarPrestamo, gbc);
        gbc.gridy = 1;
        panel.add(btnDevolverPrestamo, gbc);
        gbc.gridy = 2;
        panel.add(btnVerHistorialPrestamo, gbc);
        gbc.gridy = 3;
        panel.add(btnSalir, gbc);

        add(panel);
        setVisible(true);
    }

    public void hacerPrestamo() {
        JTextField txtCedula = new JTextField();
        JTextField txtIdLibro = new JTextField();

        Object[] campos = {
            "Cédula del usuario:", txtCedula,
            "ID del libro:", txtIdLibro,
        };

        int opcion = JOptionPane.showConfirmDialog(this, campos, "Nuevo Préstamo", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                int cedula = Integer.parseInt(txtCedula.getText());
                int idLibro = Integer.parseInt(txtIdLibro.getText());

                String sql = "INSERT INTO prestamo (cedula_usuario, id_libro, fecha_prestamo, estado) VALUES (?, ?, CURDATE(), 0)";
                try (Connection conn = ConexionBD.conectar();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, cedula);
                    ps.setInt(2, idLibro);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Préstamo registrado correctamente.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese números válidos.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar préstamo: " + ex.getMessage());
            }
        }
    }

    public void devolverPrestamo() {
        String input = JOptionPane.showInputDialog(this, "Ingrese el ID del préstamo a devolver:");
        if (input != null) {
            try {
                int idPrestamo = Integer.parseInt(input);
                String sqlCheck = "SELECT estado FROM prestamo WHERE id = ?";
                try (Connection conn = ConexionBD.conectar();
                     PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                    psCheck.setInt(1, idPrestamo);
                    ResultSet rs = psCheck.executeQuery();

                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(this, "No se encontró préstamo con ese ID.");
                        return;
                    }
                    boolean yaDevuelto = rs.getBoolean("estado");
                    if (yaDevuelto) {
                        JOptionPane.showMessageDialog(this, "Este préstamo ya fue devuelto.");
                        return;
                    }
                    String sqlUpdate = "UPDATE prestamo SET fecha_devolucion = CURDATE(), estado = 1 WHERE id = ?";
                    try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                        psUpdate.setInt(1, idPrestamo);
                        psUpdate.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Libro devuelto correctamente.");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido. Ingrese un número entero.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al devolver préstamo: " + ex.getMessage());
            }
        }
    }

    public void historialPrestamo() {
        String sql = "SELECT * FROM prestamo ORDER BY id DESC";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            StringBuilder sb = new StringBuilder();
            boolean hayDatos = false;

            while (rs.next()) {
                hayDatos = true;
                sb.append("ID préstamo: ").append(rs.getInt("id"))
                        .append("\nCédula estudiante: ").append(rs.getInt("cedula_usuario"))
                        .append("\nID libro: ").append(rs.getInt("id_libro"))
                        .append("\nFecha préstamo: ").append(rs.getDate("fecha_prestamo"))
                        .append("\nFecha devolución: ").append(rs.getDate("fecha_devolucion"))
                        .append("\nDevuelto: ").append(rs.getBoolean("estado") ? "Sí" : "No")
                        .append("\n\n");
            }
            if (!hayDatos) {
                JOptionPane.showMessageDialog(this, "No hay préstamos registrados.");
            } 
            JTextArea areaTexto = new JTextArea(sb.toString());
            areaTexto.setEditable(false);
            areaTexto.setLineWrap(true);
            areaTexto.setWrapStyleWord(true);

            JScrollPane scroll = new JScrollPane(areaTexto);
            scroll.setPreferredSize(new Dimension(350, 400));

            JOptionPane.showMessageDialog(
            this,
            scroll,
            "Lista de préstamos",
            JOptionPane.INFORMATION_MESSAGE
        );
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + ex.getMessage());
        }
    }
}