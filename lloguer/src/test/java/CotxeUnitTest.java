import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * PROVES UNITÀRIES - CAIXA BLANCA - classe Cotxe.
 *
 * Es mira el codi de Cotxe i es dissenyen casos perquè s'executin totes les
 * sentències i les dues sortides (cert / fals) de cada condició.
 *
 * Decisió de Cotxe.descripcio():  if (propietari == null)
 *     cert  -> CP-03          fals -> CP-04
 */
class CotxeUnitTest {

    private Cotxe seat;
    private Persona anna;

    @BeforeEach
    void preparar() {
        seat = new Cotxe("1234BCD", "Seat", "Ibiza");
        anna = new Persona("Anna", "12345678Z");
    }

    @Test
    @DisplayName("CP-01 Un cotxe nou té les dades indicades i cap propietari")
    void cp01_cotxeNou() {
        assertEquals("1234BCD", seat.getMatricula());
        assertEquals("Seat", seat.getMarca());
        assertEquals("Ibiza", seat.getModel());
        assertNull(seat.getPropietari());
    }

    @Test
    @DisplayName("CP-02 setPropietari assigna el propietari")
    void cp02_assignarPropietari() {
        seat.setPropietari(anna);

        assertSame(anna, seat.getPropietari());
    }

    @Test
    @DisplayName("CP-03 descripcio() d'un cotxe sense propietari (condició certa)")
    void cp03_descripcioSensePropietari() {
        assertEquals("Seat Ibiza [1234BCD] - propietari: sense propietari", seat.descripcio());
    }

    @Test
    @DisplayName("CP-04 descripcio() d'un cotxe amb propietari (condició falsa)")
    void cp04_descripcioAmbPropietari() {
        seat.setPropietari(anna);

        assertEquals("Seat Ibiza [1234BCD] - propietari: Anna", seat.descripcio());
    }

    @Test
    @DisplayName("CP-05 setPropietari(null) deixa el cotxe sense propietari")
    void cp05_treurePropietari() {
        seat.setPropietari(anna);
        seat.setPropietari(null);

        assertNull(seat.getPropietari());
        assertEquals("Seat Ibiza [1234BCD] - propietari: sense propietari", seat.descripcio());
    }
}
