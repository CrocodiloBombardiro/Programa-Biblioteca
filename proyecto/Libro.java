public class Libro {
    private String titulo;
    private String autor;
    private String anioPublicacion;
    private String editorial;
    private String genero;
    private int id;

    public Libro(String titulo, String autor, String anioPublicacion, int id, String genero, String editorial) {
        this.titulo = titulo;
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
        this.id = id;
        this.genero = genero;
        this.editorial = editorial;
    }
    public Libro(Object object, Object object2, int i, int j, Object object3, Object object4) {
    }
    public String getTitulo() {
        return titulo;
    }
    public String getAutor() {
        return autor;
    }
    public String getAnioPublicacion() {
        return anioPublicacion;
    }
    public int getId() {
        return id;
    }
    public String getEditorial() {
        return editorial;
    }
    public String getGenero() {
        return genero;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public void setAnioPublicacion(String anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public void MostrarDatos() {
        System.out.println("Titulo del libro: " + titulo);
        System.out.println("Autor del libro " + autor);
        System.out.println("Genero del libro: " + genero);
        System.out.println("Editorial del libro: " + editorial);
        System.out.println("Año de publicacion: " + anioPublicacion);
        System.out.println("ID del libro: " + id);
    }
}