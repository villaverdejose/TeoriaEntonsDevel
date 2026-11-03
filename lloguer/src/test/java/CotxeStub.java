/**
 * STUB (mòdul simulat) de Cotxe, només per a proves d'integració descendent.
 *
 * Substitueix el Cotxe real per un que:
 *   - retorna sempre el propietari que li diem nosaltres (resposta preparada);
 *   - apunta quantes vegades i amb qui l'ha cridat Persona.
 *
 * Així es pot provar Persona sense dependre del comportament real de Cotxe.
 * (Aquest és l'únic lloc del projecte on hi ha herència i @Override: és codi de proves.)
 */
class CotxeStub extends Cotxe {

    private final Persona propietariSimulat;
    private int cridesSetPropietari = 0;
    private Persona ultimPropietariAssignat = null;

    CotxeStub(Persona propietariSimulat) {
        super("0000AAA", "Stub", "Simulat");
        this.propietariSimulat = propietariSimulat;
    }

    @Override
    public Persona getPropietari() {
        return propietariSimulat;   // resposta preparada, no la del Cotxe real
    }

    @Override
    public void setPropietari(Persona propietari) {
        cridesSetPropietari++;
        ultimPropietariAssignat = propietari;
    }

    int getCridesSetPropietari() {
        return cridesSetPropietari;
    }

    Persona getUltimPropietariAssignat() {
        return ultimPropietariAssignat;
    }
}
