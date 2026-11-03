import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * PROVES D'INTEGRACIÓ.
 *
 * Es comprova com interactuen els mòduls ja provats individualment
 * (Cotxe amb CotxeUnitTest, Persona amb PersonaUnitTest).
 * Jerarquia del programa:   Main (dalt)  ->  Persona  ->  Cotxe (baix)
 *
 *   CP-25  Integració NO incremental (big bang): tots els mòduls de cop.
 *   CP-26  Integració incremental ASCENDENT (bottom-up): Cotxe ja provat sol,
 *          ara s'hi afegeix Persona al damunt i es prova la parella.
 *   CP-27, CP-28  Integració incremental DESCENDENT (top-down): es comença per
 *          Persona (nivell superior) i el Cotxe de sota se simula amb un stub.
 */
class IntegracioTest {

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
    @DisplayName("CP-25 Big bang: escenari complet (assignar, intentar duplicar, vendre)")
    void cp25_bigBang() {
        anna.afegirCotxe(seat);
        anna.afegirCotxe(renault);
        jordi.afegirCotxe(toyota);

        assertThrows(IllegalStateException.class, () -> jordi.afegirCotxe(seat));

        anna.treureCotxe(renault);
        jordi.afegirCotxe(renault);

        assertEquals(1, anna.getCotxes().size());
        assertEquals(2, jordi.getCotxes().size());
        Coherencia.verificarPersones(anna, jordi);
        Coherencia.verificarCotxes(seat, renault, toyota);
    }

    @Test
    @DisplayName("CP-26 Ascendent: la relació es manté coherent després de moltes operacions")
    void cp26_ascendentCoherencia() {
        anna.afegirCotxe(seat);
        anna.afegirCotxe(renault);
        anna.afegirCotxe(toyota);
        Coherencia.verificarPersones(anna, jordi);
        Coherencia.verificarCotxes(seat, renault, toyota);

        anna.treureCotxe(renault);
        Coherencia.verificarPersones(anna, jordi);
        Coherencia.verificarCotxes(seat, renault, toyota);
        assertNull(renault.getPropietari());

        jordi.afegirCotxe(renault);
        Coherencia.verificarPersones(anna, jordi);
        Coherencia.verificarCotxes(seat, renault, toyota);

        jordi.treureCotxe(renault);
        anna.afegirCotxe(renault);
        Coherencia.verificarPersones(anna, jordi);
        Coherencia.verificarCotxes(seat, renault, toyota);

        assertEquals(3, anna.getCotxes().size());
        assertEquals(0, jordi.getCotxes().size());
    }

    @Test
    @DisplayName("CP-27 Descendent amb stub: Persona assigna el propietari a un cotxe lliure")
    void cp27_descendentStubLliure() {
        CotxeStub cotxeSimulat = new CotxeStub(null);   // el stub diu "sóc lliure"

        anna.afegirCotxe(cotxeSimulat);

        assertEquals(1, cotxeSimulat.getCridesSetPropietari());
        assertSame(anna, cotxeSimulat.getUltimPropietariAssignat());
        assertEquals(1, anna.getCotxes().size());
    }

    @Test
    @DisplayName("CP-28 Descendent amb stub: Persona rebutja un cotxe que (segons el stub) té propietari")
    void cp28_descendentStubOcupat() {
        CotxeStub cotxeSimulat = new CotxeStub(jordi);   // el stub diu "sóc del Jordi"

        assertThrows(IllegalStateException.class, () -> anna.afegirCotxe(cotxeSimulat));

        assertEquals(0, cotxeSimulat.getCridesSetPropietari());
        assertEquals(0, anna.getCotxes().size());
    }
}
