public class Cotxe {

    private String matricula;
    private String marca;
    private String model;
    private Persona propietari;   // un cotxe només té un propietari (o cap)

    public Cotxe(String matricula, String marca, String model) {
        this.matricula = matricula;
        this.marca = marca;
        this.model = model;
        this.propietari = null;   // en crear-lo encara no té propietari
    }

    public String getMatricula() {
        return matricula;
    }

    public String getMarca() {
        return marca;
    }

    public String getModel() {
        return model;
    }

    public Persona getPropietari() {
        return propietari;
    }

    /**
     * Normalment no es crida directament: el propietari s'assigna a través
     * de Persona.afegirCotxe(), perquè així la relació queda coherent per les dues bandes.
     */
    public void setPropietari(Persona propietari) {
        this.propietari = propietari;
    }

    /** Retorna un text amb les dades del cotxe i el nom del propietari. */
    public String descripcio() {
        // Atenció: usem propietari.getNom() i NO propietari.descripcio().
        // Si la descripció de Cotxe cridés la de Persona, i aquesta la de Cotxe... bucle infinit.
        String nomPropietari;
        if (propietari == null) {
            nomPropietari = "sense propietari";
        } else {
            nomPropietari = propietari.getNom();
        }
        return marca + " " + model + " [" + matricula + "] - propietari: " + nomPropietari;
    }
}
