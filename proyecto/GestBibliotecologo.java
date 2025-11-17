import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GestBibliotecologo extends JFrame {
    public GestBibliotecologo() {
        setTitle("Menu Bibliotecologo");
        setSize(1280,720);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240,240,240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15,0,15,0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;

        JButton btnAgregarBibliotecologo = new JButton("Agregar bibliotecólogo");
        JButton btnBuscarBibliotecologo = new JButton("Buscar bibliotecólogo");
        JButton btnEliminarBibliotecologo = new JButton("Eliminar bibliotecólogo");
        JButton btnModificarBibliotecologo = new JButton("Modificar bibliotecólogo");
        JButton btnVolver = new JButton("Volver al menú principal");

        Dimension botonSize = new Dimension(300, 60);

        btnAgregarBibliotecologo.setPreferredSize(botonSize);
        btnAgregarBibliotecologo.addActionListener(e -> mostrarFormularioAgregar());
        btnAgregarBibliotecologo.setBackground(Color.WHITE);
        btnAgregarBibliotecologo.setFont(new Font("Manrope", Font.BOLD, 16));
        
        btnBuscarBibliotecologo.setPreferredSize(botonSize);
        btnBuscarBibliotecologo.addActionListener(e -> mostrarBibliotecologo());
        btnBuscarBibliotecologo.setBackground(Color.WHITE);
        btnBuscarBibliotecologo.setFont(new Font("Manrope", Font.BOLD, 16));
        
        btnEliminarBibliotecologo.setPreferredSize(botonSize);
        btnEliminarBibliotecologo.addActionListener( e -> eliminarBibliotecologo());
        btnEliminarBibliotecologo.setBackground(Color.WHITE);
        btnEliminarBibliotecologo.setFont(new Font("Manrope", Font.BOLD, 16));
        
        btnModificarBibliotecologo.setPreferredSize(botonSize);
        btnModificarBibliotecologo.addActionListener(e -> modificarBibliotecologo());
        btnModificarBibliotecologo.setBackground(Color.WHITE);
        btnModificarBibliotecologo.setFont(new Font("Manrope", Font.BOLD, 16));
        
        btnVolver.setPreferredSize(botonSize);
        btnVolver.addActionListener(e -> dispose());
        btnVolver.setBackground(Color.WHITE);
        btnVolver.setFont(new Font("Manrope", Font.BOLD, 16));

       gbc.gridy = 0;
       panel.add(btnAgregarBibliotecologo, gbc);
       gbc.gridy = 1;
       panel.add(btnBuscarBibliotecologo, gbc);
       gbc.gridy = 2;
       panel.add(btnEliminarBibliotecologo, gbc);
       gbc.gridy = 3;
       panel.add(btnModificarBibliotecologo, gbc);
       gbc.gridy = 4;
       panel.add(btnVolver, gbc);

    add(panel);    
    setLocationRelativeTo(null);
    setVisible(true);
    }

    public void agregarBibliotecologoBD(Bibliotecologo b) {
    String sql = "INSERT INTO bibliotecologo (cedula, pnombre, papellido) VALUES (?, ?, ?)";
    try (Connection conn = ConexionBD.conectar();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, b.getCedula());
        ps.setString(2, b.getPnombre());
        ps.setString(3, b.getPapellido());
        ps.executeUpdate();
        JOptionPane.showMessageDialog(this, "Bibliotecólogo agregado correctamente en la BD");
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al agregar bibliotecólogo: " + ex.getMessage());
    }
}

    public void mostrarFormularioAgregar() {
        JTextField nombre = new JTextField();
        JTextField apellido = new JTextField();
        JTextField cedula = new JTextField();
    
        Object[] campos = {
            "Nombre: ", nombre,
            "Apellido: ", apellido,
            "Cédula: ", cedula
        };

        int opcion = JOptionPane.showConfirmDialog(this, campos ,"Nuevo bibliotecólogo", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
            String nom = nombre.getText();
            String ape = apellido.getText();
            int ide = Integer.parseInt(cedula.getText());

            Bibliotecologo b = new Bibliotecologo(nom, ape, ide);
            agregarBibliotecologoBD(b);
        } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cédula invalido. Por favor ingrese un número entero.");
            }
        }
        }

    public void mostrarBibliotecologo() {
             String sql = "SELECT * FROM bibliotecologo";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            StringBuilder sb = new StringBuilder();
            boolean hayDatos = false;
            while (rs.next()) {
                hayDatos = true;
                sb.append("Nombre: ").append(rs.getString("pnombre"))
                .append("\nApellido: ").append(rs.getString("papellido"))
                .append("\nCédula: ").append(rs.getInt("cedula"))
                .append("\n\n");
            }
            if (!hayDatos) {
                JOptionPane.showMessageDialog(this, "No hay libros registrados"); 
                return;
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
            "Lista de bibliotecólogos",
            JOptionPane.INFORMATION_MESSAGE
        );

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar bibliotecologos: " + ex.getMessage());
        }
    }

    public void eliminarBibliotecologo() {
        String cedulaStr = JOptionPane.showInputDialog(this, "Ingrese la cédula del bibliotecologo a eliminar:");
        if (cedulaStr != null) {
            try {
                int ced = Integer.parseInt(cedulaStr);
                String sql = "DELETE FROM bibliotecologo WHERE cedula = ?";
                try (Connection conn = ConexionBD.conectar();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, ced);
                    int rows = ps.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Biblitoecologo eliminado correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(this, "No se encontró bibliotecologo con esa cédula.");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cédula inválida.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar bibliotecologo: " + ex.getMessage());
            }
        }
    }

    public void modificarBibliotecologo() {
        String cedulaStr = JOptionPane.showInputDialog(this, "Ingrese la cédula del bibliotecologo a modificar:");
        if (cedulaStr != null) {
            try {
                int ced = Integer.parseInt(cedulaStr);
                String sql = "SELECT * FROM bibliotecologo WHERE cedula = ?";
                try (Connection conn = ConexionBD.conectar();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, ced);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(this, "No se encontró bibliotecologo con esa cédula.");
                        return;
                    }
                    String nombreActual = rs.getString("pnombre");
                    String apellidoActual = rs.getString("papellido");
                    JTextField nombreField = new JTextField(nombreActual);
                    JTextField apellidoField = new JTextField(apellidoActual);

                    Object[] campos = {
                            "Nombre: ", nombreField,
                            "Apellido: ", apellidoField
                    };
                    int opcion = JOptionPane.showConfirmDialog(this, campos, "Modificar Biblioecologo", JOptionPane.OK_CANCEL_OPTION);
                    if (opcion == JOptionPane.OK_OPTION) {
                        String nuevoNombre = nombreField.getText();
                        String nuevoApellido = apellidoField.getText();

                        String updateSQL = "UPDATE bibliotecologo SET pnombre = ?, papellido = ?, WHERE cedula = ?";
                        try (PreparedStatement psUpdate = conn.prepareStatement(updateSQL)) {
                            psUpdate.setString(1, nuevoNombre);
                            psUpdate.setString(2, nuevoApellido);
                            psUpdate.setInt(3, ced);
                            psUpdate.executeUpdate();
                            JOptionPane.showMessageDialog(this, "Bibliotecologo modificado correctamente.");
                        }
                    }
                }
            } catch (NumberFormatException ex) {  //si la cedula no es un numero entero da error
                JOptionPane.showMessageDialog(this, "Cédula inválida.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al modificar bibliotecologo: " + ex.getMessage());
            }
        }
    }
}