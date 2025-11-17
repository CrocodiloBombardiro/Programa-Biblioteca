import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; 

public class GestUsuario extends JFrame {

    public GestUsuario() {
        setTitle("Menu usuario");
        setSize(1280,720);

        JPanel panel = new JPanel(new GridBagLayout()); //creamos el panel donde estaran los botones
        panel.setBackground(new Color(240,240,240)); //color del fondo

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;

        JButton btnAgregarEstudiante = new JButton("Agregar usuario");
        JButton btnBuscarEstudiante = new JButton("Buscar usuario");
        JButton btnEliminarEstudiante = new JButton("Eliminar usuario");
        JButton btnModificarEstudiante = new JButton("Modificar usuario");
        JButton btnVolver = new JButton("Volver al menú principal");

        Dimension botonSize = new Dimension(300, 60);

        btnAgregarEstudiante.setPreferredSize(botonSize);
        btnAgregarEstudiante.addActionListener(e -> mostrarFormularioAgregar());
        btnAgregarEstudiante.setBackground(Color.WHITE);
        btnAgregarEstudiante.setFont(new Font("Manrope", Font.BOLD, 16));
        
        btnBuscarEstudiante.setPreferredSize(botonSize);
        btnBuscarEstudiante.addActionListener(e -> mostrarEstudiantes());
        btnBuscarEstudiante.setBackground(Color.WHITE);
        btnBuscarEstudiante.setFont(new Font("Manrope", Font.BOLD, 16));
        
        btnEliminarEstudiante.setPreferredSize(botonSize);
        btnEliminarEstudiante.addActionListener( e -> eliminarEstudiante());
        btnEliminarEstudiante.setBackground(Color.WHITE);
        btnEliminarEstudiante.setFont(new Font("Manrope", Font.BOLD, 16));
        
        btnModificarEstudiante.setPreferredSize(botonSize);
        btnModificarEstudiante.addActionListener(e -> modificarEstudiante());
        btnModificarEstudiante.setBackground(Color.WHITE);
        btnModificarEstudiante.setFont(new Font("Manrope", Font.BOLD, 16));
        
        btnVolver.setPreferredSize(botonSize);
        btnVolver.addActionListener(e -> dispose());
        btnVolver.setBackground(Color.WHITE);
        btnVolver.setFont(new Font("Manrope", Font.BOLD, 16));

        gbc.gridy = 0;
        panel.add(btnAgregarEstudiante, gbc);
        gbc.gridy = 1;
        panel.add(btnBuscarEstudiante, gbc);
        gbc.gridy = 2;
        panel.add(btnEliminarEstudiante, gbc);
        gbc.gridy = 3;
        panel.add(btnModificarEstudiante, gbc);
        gbc.gridy = 4;
        panel.add(btnVolver, gbc);

add(panel);
setLocationRelativeTo(null);

        setVisible(true);
    }

public void agregarEstudianteBD(Usuario e) {
    String sql = "INSERT INTO usuario (cedula, pnombre, papellido, curso) VALUES (?, ?, ?, ?)";
    try (Connection conn = ConexionBD.conectar();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, e.getCedula());
        ps.setString(2, e.getPNombre());
        ps.setString(3, e.getPapellido());
        ps.setString(4, e.getCurso());
        ps.executeUpdate();
        JOptionPane.showMessageDialog(this, "Usuario agregado correctamente en la BD");
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al agregar al usuario: " + ex.getMessage());
    }
}
public void mostrarFormularioAgregar() {
    JTextField nombre = new JTextField();
    JTextField apellido = new JTextField();
    JTextField id = new JTextField();
    JTextField curso = new JTextField();
    
    Object[] campos = {
        "Nombre: ", nombre,
        "Apellido: ", apellido,
        "Cédula: ", id,
        "Curso: ", curso
    };

    int opcion = JOptionPane.showConfirmDialog(this, campos ,"Nuevo usuario", JOptionPane.OK_CANCEL_OPTION);
    if (opcion == JOptionPane.OK_OPTION) {
        try {
        String nom = nombre.getText();
        String ape = apellido.getText();
        int ide = Integer.parseInt(id.getText());
        String cur = curso.getText();

        Usuario e = new Usuario(nom, ape, ide, cur);
        agregarEstudianteBD(e);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cédula invalido. Por favor ingrese un numero entero.");
        }
    }
}

public void mostrarEstudiantes() {
     String sql = "SELECT * FROM usuario";
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
                .append("\nCurso: ").append(rs.getString("curso"))
                .append("\n\n");
            }
            if (!hayDatos) {
                JOptionPane.showMessageDialog(this, "No hay usuarios registrados.");
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
            "Lista de usuarios",
            JOptionPane.INFORMATION_MESSAGE
        );

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + ex.getMessage());
        }
    }

public void eliminarEstudiante() {
    String cedulaStr = JOptionPane.showInputDialog(this, "Ingrese la cédula del usuario a eliminar:");
    if (cedulaStr == null) return;

    try {
        int ced = Integer.parseInt(cedulaStr);

        String deletePrestamosSql = "DELETE FROM prestamo WHERE cedula_usuario = ?";
        String deleteUsuarioSql = "DELETE FROM usuario WHERE cedula = ?";

        try (Connection conn = ConexionBD.conectar()) {
            conn.setAutoCommit(false); // inicio transacción
            try (PreparedStatement psPrest = conn.prepareStatement(deletePrestamosSql);
                 PreparedStatement psUsr = conn.prepareStatement(deleteUsuarioSql)) {

                psPrest.setInt(1, ced);
                int borradosPrest = psPrest.executeUpdate();

                psUsr.setInt(1, ced);
                int borradosUsr = psUsr.executeUpdate();

                if (borradosUsr > 0) {
                    conn.commit();
                    JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.\nPréstamos eliminados: " + borradosPrest);
                } else {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "No se encontró un usuario con esa cédula.");
                }
            } catch (SQLException e) {
                conn.rollback();
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Cédula inválida.");
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
    }
}


public void modificarEstudiante() {
            String cedulaStr = JOptionPane.showInputDialog(this, "Ingrese la cédula del usuario a modificar:");
        if (cedulaStr != null) {
            try {
                int ced = Integer.parseInt(cedulaStr);
   
                String sql = "SELECT * FROM usuario WHERE cedula = ?";
                try (Connection conn = ConexionBD.conectar();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, ced);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(this, "No se encontró un usuario con esa cédula.");
                        return;
                    }
                    String nombreActual = rs.getString("pnombre");
                    String apellidoActual = rs.getString("papellido");
                    String cursoActual = rs.getString("curso");

                    JTextField nombreField = new JTextField(nombreActual);
                    JTextField apellidoField = new JTextField(apellidoActual);
                    JTextField cursoField = new JTextField(cursoActual);

                    Object[] campos = {
                            "Nombre: ", nombreField,
                            "Apellido: ", apellidoField,
                            "Curso: ", cursoField
                    };

                    int opcion = JOptionPane.showConfirmDialog(this, campos, "Modificar usuario", JOptionPane.OK_CANCEL_OPTION);
                    if (opcion == JOptionPane.OK_OPTION) {
                        String nuevoNombre = nombreField.getText();
                        String nuevoApellido = apellidoField.getText();
                        String nuevoCurso = cursoField.getText();

                        String updateSQL = "UPDATE usuario SET pnombre = ?, papellido = ?, curso = ? WHERE cedula = ?";
                        try (PreparedStatement psUpdate = conn.prepareStatement(updateSQL)) {
                            psUpdate.setString(1, nuevoNombre);
                            psUpdate.setString(2, nuevoApellido);
                            psUpdate.setString(3, nuevoCurso);
                            psUpdate.setInt(4, ced);
                            psUpdate.executeUpdate();
                            JOptionPane.showMessageDialog(this, "Usuario modificado correctamente.");
                        }
                    }
                }
            } catch (NumberFormatException ex) {  //si la cedula no es un numero entero da error
                JOptionPane.showMessageDialog(this, "Cédula inválida.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al modificar usuario: " + ex.getMessage());
            }
        }
    }
}