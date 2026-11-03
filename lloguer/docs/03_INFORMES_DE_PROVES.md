import java.util.ArrayList;
import java.util.List;

public class Persona {

    private String nom;
    private String dni;
    private List<Cotxe> cotxes;   // una persona pot tenir molts cotxes

    public Persona(String nom, String dni) {
        this.nom = nom;
        this.dni = dni;
        this.cotxes = new ArrayList<>();   // la llista comença buida
    }

    public String getNom() {
        return nom;
    }

    public String getDni() {
        return dni;
    }

    /**
     * Retorna una CÒPIA de la llista: si es retornés la llista interna, des de fora
     * es podrien afegir cotxes sense passar per afegirCotxe() i la relació quedaria
     * incoherent (INC-002).
     */
    public List<Cotxe> getCotxes() {
        return new ArrayList<>(cotxes);
    }

    /**
     * Afegeix un cotxe a la persona i, alhora, indica al cotxe qui n'és el propietari.
     * Les dues bandes de la relació s'han de mantenir sempre coherents.
     */
    public void afegirCotxe(Cotxe cotxe) {
        if (cotxe == null) {   // INC-001: abans això provocava un NullPointerException
            throw new IllegalArgumentException("El cotxe no pot ser null");
        }
        if (cotxe.getPropietari() != null) {
            throw new IllegalStateException(
                    "El cotxe " + cotxe.getMatricula() + " ja té propietari: " + cotxe.getPropietari().getNom());
        }
        cotxes.add(cotxe);
        cotxe.setPropietari(this);
    }

    /** Treu un cotxe de la persona: el cotxe es queda sense propietari. */
    public void treureCotxe(Cotxe cotxe) {
        if (cotxes.remove(cotxe)) {
            cotxe.setPropietari(null);
        }
    }

    public void mostrarCotxes() {
        System.out.println("Cotxes de " + nom + ":");
        if (cotxes.isEmpty()) {
            System.out.println("   (cap cotxe)");
        }
        for (Cotxe c : cotxes) {
            System.out.println("   - " + c.descripcio());
        }
    }

    /** Retorna un text amb les dades de la persona. */
    public String descripcio() {
        return nom + " (" + dni + ") - " + cotxes.size() + " cotxe(s)";
    }
}
