import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestLibro extends JFrame {
    public GestLibro() {
        setTitle("Menu Libro");
        setSize(1280,720);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240,240,240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15,0,15,0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;

        JButton btnAgregarLibro = new JButton("Agregar libro");
        JButton btnBuscarLibro = new JButton("Buscar libro");
        JButton btnEliminarLibro = new JButton("Eliminar libro");
        JButton btnModificarLibro = new JButton("Modificar libro");
        JButton btnVolver = new JButton("Volver al menú principal");

        Dimension botonSize = new Dimension(300,60);

        btnAgregarLibro.setPreferredSize(botonSize);
        btnAgregarLibro.addActionListener(e -> mostrarFormularioAgregar());
        btnAgregarLibro.setBackground(Color.WHITE);
        btnAgregarLibro.setFont(new Font("Manrope", Font.BOLD, 16));

        btnBuscarLibro.setPreferredSize(botonSize);
        btnBuscarLibro.addActionListener(e -> mostrarLibros());
        btnBuscarLibro.setBackground(Color.WHITE);
        btnBuscarLibro.setFont(new Font("Manrope", Font.BOLD, 16));

        btnEliminarLibro.setPreferredSize(botonSize);
        btnEliminarLibro.addActionListener(e -> eliminarLibros());
        btnEliminarLibro.setBackground(Color.WHITE);
        btnEliminarLibro.setFont(new Font("Manrope", Font.BOLD, 16));

        btnModificarLibro.setPreferredSize(botonSize);
        btnModificarLibro.addActionListener(e -> modificarLibros());
        btnModificarLibro.setBackground(Color.WHITE);
        btnModificarLibro.setFont(new Font("Manrope", Font.BOLD, 16));

        btnVolver.setPreferredSize(botonSize);
        btnVolver.addActionListener(e -> dispose());
        btnVolver.setBackground(Color.WHITE);
        btnVolver.setFont(new Font("Manrope", Font.BOLD, 16));

        gbc.gridy = 0;
        panel.add(btnAgregarLibro, gbc);
        gbc.gridy = 1;
        panel.add(btnBuscarLibro, gbc);
        gbc.gridy = 2;
        panel.add(btnEliminarLibro, gbc);
        gbc.gridy = 3;
        panel.add(btnModificarLibro, gbc);
        gbc.gridy = 4;
        panel.add(btnVolver, gbc);

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void agregarLibroBD(String titulo, String autor, String anioPublicacion, String editorial, String genero) {
        String sql = "INSERT INTO libro (titulo, autor, anio_publicacion, editorial, genero) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, titulo);
            ps.setString(2, autor);
            ps.setString(3, anioPublicacion);
            ps.setString(4, editorial);
            ps.setString(5, genero);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Libro agregado correctamente en la BD");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar el libro: " + ex.getMessage());
        }
    }

    public void mostrarFormularioAgregar() {
        JTextField titulo = new JTextField();
        JTextField autor = new JTextField();
        JTextField anioP = new JTextField();
        JTextField editorial = new JTextField();
        JTextField genero = new JTextField();

        Object[] campos = {
            "Título: ", titulo,
            "Autor: ", autor,
            "Año de publicación (ej. 1997): ", anioP,
            "Editorial: ", editorial,
            "Género: ", genero
        };

        int opcion = JOptionPane.showConfirmDialog(this, campos ,"Nuevo Libro", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                String tit = titulo.getText().trim();
                String aut = autor.getText().trim();
                String anP = anioP.getText().trim();
                String ed = editorial.getText().trim();
                String gen = genero.getText().trim();

                if (tit.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El título no puede estar vacío.");
                    return;
                }
                agregarLibroBD(tit, aut, anP, ed, gen);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al leer datos: " + ex.getMessage());
            }
        }
    }

    public void mostrarLibros() {
        String sql = "SELECT * FROM libro";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            StringBuilder sb = new StringBuilder();
            boolean hayDatos = false;

            while (rs.next()) {
                hayDatos = true;
                sb.append("ID: ").append(rs.getInt("id"))
                        .append("\nTítulo: ").append(rs.getString("titulo"))
                        .append("\nAutor: ").append(rs.getString("autor"))
                        .append("\nGénero: ").append(rs.getString("genero"))
                        .append("\nAño: ").append(rs.getString("anio_publicacion"))
                        .append("\nEditorial: ").append(rs.getString("editorial"))
                        .append("\n\n");
            }
            if (!hayDatos) {
                JOptionPane.showMessageDialog(this, "No hay libros registrados.");
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
            "Lista de libros",
            JOptionPane.INFORMATION_MESSAGE
        );

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar libros: " + ex.getMessage());
        }
    }

    public void eliminarLibros() {
    String idStr = JOptionPane.showInputDialog(this, "Ingrese el ID del libro a eliminar:");
    if (idStr == null) return;

    try {
        int id = Integer.parseInt(idStr);

        String deletePrestamosSql = "DELETE FROM prestamo WHERE id_libro = ?";
        String deleteLibroSql = "DELETE FROM libro WHERE id = ?";

        try (Connection conn = ConexionBD.conectar()) {
            conn.setAutoCommit(false); // inicio transacción
            try (PreparedStatement psPrest = conn.prepareStatement(deletePrestamosSql);
                 PreparedStatement psLib = conn.prepareStatement(deleteLibroSql)) {

                psPrest.setInt(1, id);
                int borradosPrest = psPrest.executeUpdate();

                psLib.setInt(1, id);
                int borradosUsr = psLib.executeUpdate();

                if (borradosUsr > 0) {
                    conn.commit();
                    JOptionPane.showMessageDialog(this, "Libro eliminado correctamente.\nPréstamos eliminados: " + borradosPrest);
                } else {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "No se encontró un libro con ese ID.");
                }
            } catch (SQLException l) {
                conn.rollback();
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + l.getMessage());
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


    public void modificarLibros() {
        String idStr = JOptionPane.showInputDialog(this, "Ingrese el ID del libro a modificar:");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                String sql = "SELECT * FROM libro WHERE id = ?";
                try (Connection conn = ConexionBD.conectar();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(this, "No se encontró libro con ese ID.");
                        return;
                    }
                    String tituloActual = rs.getString("titulo");
                    String autorActual = rs.getString("autor");
                    String generoActual = rs.getString("genero");
                    String anioActual = rs.getString("anio_publicacion");
                    String editorialActual = rs.getString("editorial");

                    JTextField tituloField = new JTextField(tituloActual);
                    JTextField autorField = new JTextField(autorActual);
                    JTextField generoField = new JTextField(generoActual);
                    JTextField anioField = new JTextField(String.valueOf(anioActual));
                    JTextField editorialField = new JTextField(editorialActual);

                    Object[] campos = {
                            "Título:", tituloField,
                            "Autor:", autorField,
                            "Género:", generoField,
                            "Año:", anioField,
                            "Editorial:", editorialField
                    };
                    int opcion = JOptionPane.showConfirmDialog(this, campos, "Modificar libro", JOptionPane.OK_CANCEL_OPTION);
                    if (opcion == JOptionPane.OK_OPTION) {
                        String nuevoTitulo = tituloField.getText().trim();
                        String nuevoAutor = autorField.getText().trim();
                        String nuevoGenero = generoField.getText().trim();
                        String nuevaEditorial = editorialField.getText().trim();
                        String nuevoAnio = anioField.getText().trim();
                    
                        String updateSQL = "UPDATE libro SET titulo = ?, autor = ?, genero = ?, anio_publicacion = ?, editorial = ? WHERE id = ?";
                        try (PreparedStatement psUpdate = conn.prepareStatement(updateSQL)) {
                            psUpdate.setString(1, nuevoTitulo);
                            psUpdate.setString(2, nuevoAutor);
                            psUpdate.setString(3, nuevoGenero);
                            psUpdate.setString(4, nuevoAnio);      
                            psUpdate.setString(5, nuevaEditorial);
                            psUpdate.setInt(6, id);
                            psUpdate.executeUpdate();
                            JOptionPane.showMessageDialog(this, "Libro modificado correctamente.");
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido. Debe ser un número entero.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al modificar libro: " + ex.getMessage());
            }
        }
    }
}