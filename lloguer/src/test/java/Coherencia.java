import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Utilitat de proves (no és una prova): comprova que la relació Persona-Cotxe
 * és coherent per les dues bandes.
 *
 * Regla que ha de complir sempre el sistema:
 *   - si un cotxe és a la llista d'una persona, aquella persona és el seu propietari;
 *   - si un cotxe té propietari, el cotxe és a la llista d'aquell propietari.
 */
final class Coherencia {

    private Coherencia() {
    }

    /** Banda Persona -> Cotxe: tots els cotxes de la llista diuen que són d'aquesta persona. */
    static void verificarPersones(Persona... persones) {
        for (Persona p : persones) {
            for (Cotxe c : p.getCotxes()) {
                assertSame(p, c.getPropietari(),
                        "El cotxe " + c.getMatricula() + " és a la llista de " + p.getNom()
                                + " però no en té el propietari assignat");
            }
        }
    }

    /** Banda Cotxe -> Persona: si el cotxe té propietari, és a la llista d'aquell propietari. */
    static void verificarCotxes(Cotxe... cotxes) {
        for (Cotxe c : cotxes) {
            if (c.getPropietari() != null) {
                assertTrue(c.getPropietari().getCotxes().contains(c),
                        "El cotxe " + c.getMatricula() + " diu que és de " + c.getPropietari().getNom()
                                + " però no és a la seva llista");
            }
        }
    }
}
