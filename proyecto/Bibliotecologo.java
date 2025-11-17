public class Bibliotecologo {
    private String Pnombre;
    private String Papellido;
    private int Cedula;

    public Bibliotecologo(String Pnombre, String Papellido, int Cedula) {
        this.Pnombre = Pnombre;
        this.Papellido = Papellido;
        this.Cedula = Cedula;
    }
    public String getPnombre() {
        return Pnombre;
    }
    public String getPapellido() {
        return Papellido;
    }
    public int getCedula() {
        return Cedula;
    }
    public void setPnombre(String Pnombre) {
        this.Pnombre = Pnombre;
    }
    public void setPapellido(String Papellido) {
        this.Papellido = Papellido;
    }
    public void setCedula(int Cedula) {
        this.Cedula = Cedula;
    }
    public void MostrarDatos() {
        System.out.println("Nombre del bibliotecólogo: " + Pnombre);
        System.out.println("Apellido del bibliotecólogo: " + Papellido);
        System.out.println("ID del bibliotecólogo: " + Cedula);
    }
}
