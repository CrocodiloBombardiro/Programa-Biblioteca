import javax.swing.JTextField;

public class Usuario {
    private String Pnombre;
    private String Papellido;
    private int cedula;
    private String curso;

    public Usuario(String Pnombre, String Papellido, int cedula, String curso) {
        this.Pnombre = Pnombre;
        this.cedula = cedula;
        this.Papellido = Papellido;
        this.curso = curso;
    }
    public Usuario(String nom, JTextField id) {
    }
    public String getPNombre() {
        return Pnombre;
    }
    public String getPapellido() {
        return Papellido;
    }
    public int getCedula() {
        return cedula;
    }
    public String getCurso() {
        return curso;
    }
    public void setPNombre(String Pnombre) {
        this.Pnombre = Pnombre;
    }
    public void setPapellido(String Papellido) {
        this.Papellido = Papellido;
    }
    public void setCedula(int cedula) {
        this.cedula = cedula;
    }
    public void setCurso(String curso) {
        this.curso = curso;
    }
    public void MostrarDatos() {
        System.out.println("Nombre del estudiante: " + Pnombre);
        System.out.println("Apellido del estudiante: " + Papellido);
        System.out.println("ID del estudiante: " + cedula);  
        System.out.println("Curso del estudiante: "+curso);
    }    
}