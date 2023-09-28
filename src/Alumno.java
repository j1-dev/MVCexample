import java.io.Serializable;

public class Alumno implements Serializable {
    public static final long serialVersionUID = 1L;
    private String DNI;
    private String nombre;

    public Alumno() {
    }

    public Alumno(String DNI, String nombre) {
        this.DNI = DNI;
        this.nombre = nombre;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
