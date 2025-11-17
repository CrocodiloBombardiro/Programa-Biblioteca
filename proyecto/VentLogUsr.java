import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class VentLogUsr extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    public VentLogUsr() {
        setTitle("Inicio de sesión - Usuario");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.gridx = 0;

        JLabel lblUsuario = new JLabel("Nombre del usuario:");
        JLabel lblContrasena = new JLabel("Contraseña:");
        txtUsuario = new JTextField(25);
        txtContrasena = new JPasswordField(25);
        JButton btnIngresar = new JButton("Ingresar");

        lblUsuario.setFont(new Font("Manrope", Font.BOLD, 16));
        lblContrasena.setFont(new Font("Manrope", Font.BOLD, 16));
        btnIngresar.setFont(new Font("Manrope", Font.BOLD, 16));
        btnIngresar.setBackground(Color.WHITE);
        btnIngresar.setPreferredSize(new Dimension(120, 40));

        gbc.gridy = 0;
        panel.add(lblUsuario, gbc);
        gbc.gridy = 1;
        panel.add(txtUsuario, gbc);
        gbc.gridy = 2;
        panel.add(lblContrasena, gbc);
        gbc.gridy = 3;
        panel.add(txtContrasena, gbc);
        gbc.gridy = 4;
        panel.add(btnIngresar, gbc);

        btnIngresar.addActionListener(e -> verificarLogin());

        add(panel);
        setVisible(true);
    }

    private void verificarLogin() {
        String usuarioTxt = txtUsuario.getText().trim();
        String contrasenaTxt = new String(txtContrasena.getPassword());

        if (usuarioTxt.isEmpty() || contrasenaTxt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar nombre y cédula.");
            return;
        }
        try {
            int cedula = Integer.parseInt(contrasenaTxt);
            String sql = "SELECT * FROM usuario WHERE pnombre = ? AND cedula = ?";
            try (Connection conn = ConexionBD.conectar();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, usuarioTxt);
                ps.setInt(2, cedula);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso. Bienvenido, " + rs.getString("pnombre") + "!");
                    new MenuUsuario();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Nombre o cédula incorrectos.");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La cédula debe ser un número entero.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos: " + ex.getMessage());
        }
    }
}