
import java.time.LocalDate;

public class Prestamo {
    private int idPrestamo;
    private Usuario estudiante;
    private Libro libro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private boolean devuelto;

    public Prestamo(int idPrestamo,Usuario estudiante, Libro libro) {
        this.idPrestamo = idPrestamo;
        this.estudiante = estudiante;
        this.libro = libro;
        this.fechaPrestamo = LocalDate.now();
        this.fechaDevolucion = fechaPrestamo.plusDays(14);
        this.devuelto = false;
    }
    public int getIdPrestamo() {
        return idPrestamo;
    }
    public Usuario getEstudiante() {
        return estudiante;
    }
    public Libro getLibro() {
        return libro;
    }
    public boolean isDevuelto() {
        return devuelto;
    }
    public void devolverLibro() {
        this.devuelto = true;
    }
    public String mostrarPrestamo() {
        return "ID prestamo: "+idPrestamo+"\nEstudiante: "+estudiante.getPNombre()+" "+estudiante.getPapellido()+"\nLibro: "+libro.getTitulo()+"\nFecha Préstamo: "+fechaPrestamo+"\nFecha devolución: "+fechaDevolucion + "\nEstado: "+(devuelto ? "Devuelto":"Pendiente")+"\n";
    }
}