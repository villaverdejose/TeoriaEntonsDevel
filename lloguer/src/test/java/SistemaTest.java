import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * PROVES DE SISTEMA.
 *
 * Es prova el programari com un tot, forçant situacions extremes.
 *
 *   CP-29  Prova de resistència (estrès): moltíssims cotxes en una sola persona.
 *   CP-30  Prova de recuperació (adaptada): després d'errors provocats, el sistema
 *          ha de quedar en un estat coherent i seguir funcionant.
 *
 * Prova de seguretat: NO APLICABLE a aquest sistema (no hi ha usuaris, contrasenyes
 * ni xarxa). L'únic accés "il·legítim" possible és modificar la llista interna des
 * de fora, i ja el cobreix CP-15.
 */
class SistemaTest {

    private Persona anna;
    private Persona jordi;
    private Cotxe seat;

    @BeforeEach
    void preparar() {
        anna = new Persona("Anna", "12345678Z");
        jordi = new Persona("Jordi", "87654321X");
        seat = new Cotxe("1234BCD", "Seat", "Ibiza");
    }

    @Test
    @DisplayName("CP-29 Estrès: una persona amb 100.000 cotxes")
    void cp29_estres() {
        final int total = 100_000;

        assertTimeout(Duration.ofSeconds(10), () -> {
            for (int i = 0; i < total; i++) {
                anna.afegirCotxe(new Cotxe("MAT-" + i, "Marca", "Model"));
            }
        });

        assertEquals("Anna (12345678Z) - 100000 cotxe(s)", anna.descripcio());

        List<Cotxe> cotxes = anna.getCotxes();
        assertEquals(total, cotxes.size());
        assertSame(anna, cotxes.get(total - 1).getPropietari());

        anna.treureCotxe(cotxes.get(total - 1));
        assertEquals("Anna (12345678Z) - 99999 cotxe(s)", anna.descripcio());
    }

    @Test
    @DisplayName("CP-30 Recuperació: després d'errors provocats el sistema és coherent i continua funcionant")
    void cp30_recuperacio() {
        anna.afegirCotxe(seat);

        // Provoquem errors
        assertThrows(IllegalStateException.class, () -> jordi.afegirCotxe(seat));
        assertThrows(IllegalStateException.class, () -> anna.afegirCotxe(seat));

        // L'estat no s'ha malmès
        Coherencia.verificarPersones(anna, jordi);
        Coherencia.verificarCotxes(seat);
        assertEquals(1, anna.getCotxes().size());
        assertEquals(0, jordi.getCotxes().size());

        // I el sistema segueix funcionant amb normalitat
        anna.treureCotxe(seat);
        jordi.afegirCotxe(seat);

        Coherencia.verificarPersones(anna, jordi);
        Coherencia.verificarCotxes(seat);
        assertSame(jordi, seat.getPropietari());
    }
}
