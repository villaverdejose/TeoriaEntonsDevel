import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * PROVES DE CAIXA NEGRA.
 *
 * Aquestes proves NO miren el codi: només comproven que el programa fa el que
 * diuen els requisits (RF1..RF7, vegeu docs/02_ESPECIFICACIO_DE_PROVES.md),
 * usant només els mètodes públics.
 *
 * Tècniques de disseny:
 *   - Partició d'equivalència: nombre de cotxes (0 / 1 / diversos) i estat del cotxe
 *     (lliure / amb propietari / null).
 *   - Valors límit: 0 i 1 cotxes.
 *   - Casos positius (+) i negatius (-): els negatius intenten "boicotejar" el sistema.
 */
class CaixaNegraTest {

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
    @DisplayName("CP-16 (+) RF1 Una persona pot no tenir cap cotxe (límit inferior)")
    void cp16_zeroCotxes() {
        assertEquals(0, anna.getCotxes().size());
    }

    @Test
    @DisplayName("CP-17 (+) RF1 Una persona pot tenir un cotxe (límit)")
    void cp17_unCotxe() {
        anna.afegirCotxe(seat);

        assertEquals(1, anna.getCotxes().size());
        assertSame(anna, seat.getPropietari());
    }

    @Test
    @DisplayName("CP-18 (+) RF1 Una persona pot tenir diversos cotxes")
    void cp18_diversosCotxes() {
        anna.afegirCotxe(seat);
        anna.afegirCotxe(renault);
        anna.afegirCotxe(toyota);

        assertEquals(3, anna.getCotxes().size());
        assertSame(anna, seat.getPropietari());
        assertSame(anna, renault.getPropietari());
        assertSame(anna, toyota.getPropietari());
    }

    @Test
    @DisplayName("CP-19 (-) RF2/RF4 Un cotxe d'una persona no es pot donar a una altra")
    void cp19_cotxeJaTePropietari() {
        anna.afegirCotxe(seat);

        assertThrows(IllegalStateException.class, () -> jordi.afegirCotxe(seat));

        assertSame(anna, seat.getPropietari());
        assertEquals(1, anna.getCotxes().size());
        assertEquals(0, jordi.getCotxes().size());
    }

    @Test
    @DisplayName("CP-20 (-) RF4 El mateix cotxe no es pot afegir dues vegades a la mateixa persona")
    void cp20_cotxeDuplicat() {
        anna.afegirCotxe(seat);

        assertThrows(IllegalStateException.class, () -> anna.afegirCotxe(seat));

        assertEquals(1, anna.getCotxes().size());
    }

    @Test
    @DisplayName("CP-21 (+) RF5 Venda: el cotxe canvia de propietari")
    void cp21_venda() {
        anna.afegirCotxe(seat);

        anna.treureCotxe(seat);
        jordi.afegirCotxe(seat);

        assertSame(jordi, seat.getPropietari());
        assertEquals(0, anna.getCotxes().size());
        assertEquals(1, jordi.getCotxes().size());
    }

    @Test
    @DisplayName("CP-22 (+) RF6 Format del text de descripcio() de persona i de cotxe")
    void cp22_formatDescripcions() {
        anna.afegirCotxe(seat);

        assertEquals("Anna (12345678Z) - 1 cotxe(s)", anna.descripcio());
        assertEquals("Seat Ibiza [1234BCD] - propietari: Anna", seat.descripcio());
    }

    @Test
    @DisplayName("CP-23 (-) RF7 Afegir un cotxe null ha de donar un error controlat")
    void cp23_afegirCotxeNull() {
        assertThrows(IllegalArgumentException.class, () -> anna.afegirCotxe(null));

        assertTrue(anna.getCotxes().isEmpty());
    }

    @Test
    @DisplayName("CP-24 (-) RF7 Treure un cotxe null no ha de fallar ni canviar res")
    void cp24_treureCotxeNull() {
        anna.afegirCotxe(seat);

        anna.treureCotxe(null);

        assertEquals(1, anna.getCotxes().size());
    }
}
