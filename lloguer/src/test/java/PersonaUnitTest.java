import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * PROVES UNITÀRIES - CAIXA BLANCA - classe Persona.
 *
 * Es mira el codi de Persona i es dissenyen casos per cobrir:
 *   - cada camí de cada mètode (totes les sentències s'executen);
 *   - les dues sortides de cada condició (if);
 *   - els bucles amb 0, 1 i n iteracions;
 *   - la integritat de l'estructura de dades interna (la llista de cotxes).
 *
 * Decisions del codi i casos que les cobreixen:
 *   afegirCotxe:   if (propietari != null)    cert -> CP-08        fals -> CP-07
 *   treureCotxe:   if (cotxes.remove(cotxe))  cert -> CP-09        fals -> CP-10
 *   mostrarCotxes: if (cotxes.isEmpty())      cert -> CP-12        fals -> CP-13, CP-14
 *   mostrarCotxes: for (...)                  0 voltes -> CP-12    1 volta -> CP-13    n voltes -> CP-14
 *   getCotxes:     integritat de la llista    CP-15
 */
class PersonaUnitTest {

    private Persona anna;
    private Persona jordi;
    private Cotxe seat;
    private Cotxe renault;
    private Cotxe toyota;

    @BeforeEach
    void preparar() {
        anna = new Persona("Anna", "12345678Z");
        jordi = new Persona("Jordi", "87654321X");
        seat = new Cotxe("1234BCD", "Seat", "Ibiza");
        renault = new Cotxe("5678FGH", "Renault", "Clio");
        toyota = new Cotxe("9012JKL", "Toyota", "Corolla");
    }

    @Test
    @DisplayName("CP-06 Una persona nova té les dades indicades i cap cotxe")
    void cp06_personaNova() {
        assertEquals("Anna", anna.getNom());
        assertEquals("12345678Z", anna.getDni());
        assertTrue(anna.getCotxes().isEmpty());
    }

    @Test
    @DisplayName("CP-07 afegirCotxe amb un cotxe lliure: camí normal")
    void cp07_afegirCotxeLliure() {
        anna.afegirCotxe(seat);

        assertEquals(1, anna.getCotxes().size());
        assertSame(seat, anna.getCotxes().get(0));
        assertSame(anna, seat.getPropietari());
    }

    @Test
    @DisplayName("CP-08 afegirCotxe amb un cotxe que ja té propietari: camí d'error")
    void cp08_afegirCotxeAmbPropietari() {
        anna.afegirCotxe(seat);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> jordi.afegirCotxe(seat));

        assertTrue(error.getMessage().contains("1234BCD"));
        assertTrue(jordi.getCotxes().isEmpty());
        assertEquals(1, anna.getCotxes().size());
        assertSame(anna, seat.getPropietari());
    }

    @Test
    @DisplayName("CP-09 treureCotxe d'un cotxe de la persona (remove cert)")
    void cp09_treureCotxePropi() {
        anna.afegirCotxe(seat);
        anna.afegirCotxe(renault);

        anna.treureCotxe(seat);

        assertEquals(1, anna.getCotxes().size());
        assertSame(renault, anna.getCotxes().get(0));
        assertNull(seat.getPropietari());
    }

    @Test
    @DisplayName("CP-10 treureCotxe d'un cotxe que no és de la persona (remove fals)")
    void cp10_treureCotxeAlie() {
        anna.afegirCotxe(seat);

        jordi.treureCotxe(seat);   // el Jordi no el té

        assertEquals(1, anna.getCotxes().size());
        assertSame(anna, seat.getPropietari());
        assertTrue(jordi.getCotxes().isEmpty());
    }

    @Test
    @DisplayName("CP-11 descripcio() amb 0 i amb 2 cotxes")
    void cp11_descripcioPersona() {
        assertEquals("Anna (12345678Z) - 0 cotxe(s)", anna.descripcio());

        anna.afegirCotxe(seat);
        anna.afegirCotxe(renault);

        assertEquals("Anna (12345678Z) - 2 cotxe(s)", anna.descripcio());
    }

    @Test
    @DisplayName("CP-12 mostrarCotxes sense cotxes: condició certa, bucle 0 voltes")
    void cp12_mostrarCotxesBuit() {
        String sortida = capturarMostrarCotxes(anna);

        assertTrue(sortida.contains("Cotxes de Anna:"));
        assertTrue(sortida.contains("(cap cotxe)"));
    }

    @Test
    @DisplayName("CP-13 mostrarCotxes amb 1 cotxe: condició falsa, bucle 1 volta")
    void cp13_mostrarCotxesUn() {
        anna.afegirCotxe(seat);

        String sortida = capturarMostrarCotxes(anna);

        assertTrue(sortida.contains(seat.descripcio()));
        assertFalse(sortida.contains("(cap cotxe)"));
    }

    @Test
    @DisplayName("CP-14 mostrarCotxes amb 3 cotxes: bucle n voltes")
    void cp14_mostrarCotxesVaris() {
        anna.afegirCotxe(seat);
        anna.afegirCotxe(renault);
        anna.afegirCotxe(toyota);

        String sortida = capturarMostrarCotxes(anna);

        assertTrue(sortida.contains(seat.descripcio()));
        assertTrue(sortida.contains(renault.descripcio()));
        assertTrue(sortida.contains(toyota.descripcio()));
    }

    @Test
    @DisplayName("CP-15 Integritat: modificar la llista rebuda de getCotxes() no ha de canviar la persona")
    void cp15_integritatLlistaInterna() {
        // Si getCotxes() retornés la llista interna, aquesta línia afegiria el cotxe
        // a la persona SENSE assignar-li el propietari: relació incoherent.
        anna.getCotxes().add(seat);

        assertEquals(0, anna.getCotxes().size(),
                "La llista interna de la persona ha canviat des de fora");
        assertNull(seat.getPropietari());
    }

    /**
     * Mostrar cotxes escriu per pantalla (System.out). Per poder comprovar el que
     * escriu, redirigim temporalment la sortida a un buffer i després la restaurem.
     */
    private String capturarMostrarCotxes(Persona persona) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            persona.mostrarCotxes();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }
}
