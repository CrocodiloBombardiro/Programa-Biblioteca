import java.sql.*;

public class TestConexion {
    public static void main(String[] args) {
        // Agregamos allowPublicKeyRetrieval=true
        String url = "jdbc:mysql://127.0.0.1:3306/biblioteca_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String user = "root";
        String pass = "hola123@";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ Conexión exitosa!");
            conn.close();
        } catch (Exception e) {
            System.out.println("❌ Error al conectar con la base de datos");
            e.printStackTrace();
        }
    }
}
