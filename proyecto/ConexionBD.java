import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    // Agregado allowPublicKeyRetrieval=true
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/biblioteca_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "hola123@"; // o tu contraseña

    public static Connection conectar() {
        Connection conexion = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("✅ Conexión exitosa a la base de datos biblioteca_db");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ No se encontró el driver MySQL. Verificá el .jar en tu classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar con la base de datos");
            e.printStackTrace();
        }
        return conexion;
    }

    public static void main(String[] args) {
        conectar(); // prueba de conexión
    }
}
