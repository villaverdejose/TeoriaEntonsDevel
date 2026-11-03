# 02 · Especificació de proves

> Documents del tipus **Especificació de proves** (IEEE 829): disseny de les proves, casos de prova i procediments. Etapa del PowerPoint: *Disseny i construcció dels casos de prova*.

## 1. Disseny de les proves

### 1.1 Requisits (base de la caixa negra)

La caixa negra necessita saber què ha de fer el programa **sense mirar el codi**. Aquests són els requisits d'aquest exemple (el professor els pot adaptar):

| Id | Requisit |
|---|---|
| RF1 | Una persona pot tenir 0, 1 o diversos cotxes |
| RF2 | Un cotxe només pot tenir un propietari (o cap) |
| RF3 | La relació és coherent per les dues bandes |
| RF4 | No es pot afegir un cotxe que ja té propietari (error `IllegalStateException`) |
| RF5 | Es pot treure un cotxe a una persona; el cotxe es queda sense propietari. Si la persona no té aquell cotxe, no passa res. Després es pot afegir a una altra persona (venda) |
| RF6 | `descripcio()` de persona: `nom (dni) - N cotxe(s)`. `descripcio()` de cotxe: `marca model [matrícula] - propietari: X` o `sense propietari` |
| RF7 | Una entrada `null` no ha de provocar un error inesperat: `afegirCotxe(null)` ha de donar `IllegalArgumentException`; `treureCotxe(null)` no ha de fer res |

### 1.2 Tècniques de disseny de casos

| Tècnica | Què fa | On s'aplica |
|---|---|---|
| **Caixa blanca** | Mira el codi i dissenya casos per executar tots els camins, sentències, condicions (cert i fals) i bucles (0, 1 i n voltes) | `CotxeUnitTest`, `PersonaUnitTest` |
| **Caixa negra** | Mira només els requisits; usa partició d'equivalència i valors límit | `CaixaNegraTest` |
| **Casos positius (+)** | Situacions normals i previsibles | |
| **Casos negatius (−)** | Situacions atípiques per "boicotejar" el sistema i comprovar-ne la robustesa | |

**Particions d'equivalència (caixa negra):**

| Entrada | Classes d'equivalència |
|---|---|
| Nombre de cotxes d'una persona | 0 · 1 · diversos (valors límit: 0 i 1) |
| Cotxe que s'afegeix | lliure · amb propietari (d'una altra persona) · ja és de la mateixa persona · `null` |
| Cotxe que es treu | de la persona · d'una altra persona · `null` |

## 2. Dades de prova

Cada classe de prova crea les mateixes dades abans de cada cas (`@BeforeEach`), de manera que cap cas depèn dels altres:

| Objecte | Dades |
|---|---|
| `anna` | `Persona("Anna", "12345678Z")` |
| `jordi` | `Persona("Jordi", "87654321X")` |
| `seat` | `Cotxe("1234BCD", "Seat", "Ibiza")` |
| `renault` | `Cotxe("5678FGH", "Renault", "Clio")` |
| `toyota` | `Cotxe("9012JKL", "Toyota", "Corolla")` |

## 3. Casos de prova

Un cas de prova és un conjunt d'**entrades**, **condicions d'execució** (precondicions) i **resultats esperats** (postcondicions).

### 3.1 Unitat · caixa blanca · `Cotxe` (`CotxeUnitTest`)

| Id | Entrada / precondició | Resultat esperat |
|---|---|---|
| CP-01 | Crear `Cotxe("1234BCD","Seat","Ibiza")` | Els getters retornen les dades; el propietari és `null` |
| CP-02 | Cotxe nou; `setPropietari(anna)` | `getPropietari()` és `anna` |
| CP-03 | Cotxe sense propietari; `descripcio()` *(condició certa)* | `Seat Ibiza [1234BCD] - propietari: sense propietari` |
| CP-04 | Cotxe amb propietari Anna; `descripcio()` *(condició falsa)* | `Seat Ibiza [1234BCD] - propietari: Anna` |
| CP-05 | `setPropietari(anna)` i després `setPropietari(null)` | El cotxe queda sense propietari |

### 3.2 Unitat · caixa blanca · `Persona` (`PersonaUnitTest`)

| Id | Entrada / precondició | Resultat esperat |
|---|---|---|
| CP-06 | Crear `Persona("Anna","12345678Z")` | Getters correctes; llista de cotxes buida |
| CP-07 | `anna.afegirCotxe(seat)` (cotxe lliure) | Llista = [seat]; propietari de seat = anna |
| CP-08 | Anna té seat; `jordi.afegirCotxe(seat)` | `IllegalStateException` amb la matrícula al missatge; Jordi 0 cotxes; Anna 1; seat continua sent d'Anna |
| CP-09 | Anna té seat i renault; `treureCotxe(seat)` | Llista = [renault]; seat sense propietari |
| CP-10 | Anna té seat; `jordi.treureCotxe(seat)` | No canvia res |
| CP-11 | `descripcio()` amb 0 cotxes i amb 2 cotxes | `Anna (12345678Z) - 0 cotxe(s)` / `... - 2 cotxe(s)` |
| CP-12 | `mostrarCotxes()` sense cotxes *(condició certa, bucle 0 voltes)* | Surt `Cotxes de Anna:` i `(cap cotxe)` |
| CP-13 | `mostrarCotxes()` amb 1 cotxe *(condició falsa, bucle 1 volta)* | Surt la descripció del cotxe; no surt `(cap cotxe)` |
| CP-14 | `mostrarCotxes()` amb 3 cotxes *(bucle n voltes)* | Surten les 3 descripcions |
| CP-15 | `anna.getCotxes().add(seat)` (modificar la llista rebuda) | La persona continua amb 0 cotxes; seat sense propietari *(integritat de l'estructura de dades)* |

### 3.3 Caixa negra (`CaixaNegraTest`)

| Id | +/− | Req. | Entrada / precondició | Resultat esperat |
|---|---|---|---|---|
| CP-16 | + | RF1 | Persona sense cap cotxe afegit | 0 cotxes |
| CP-17 | + | RF1 | Afegir 1 cotxe | 1 cotxe; propietari correcte |
| CP-18 | + | RF1 | Afegir 3 cotxes | 3 cotxes; tots amb propietari = anna |
| CP-19 | − | RF2, RF4 | Anna té seat; Jordi intenta afegir seat | `IllegalStateException`; seat continua sent d'Anna |
| CP-20 | − | RF4 | Anna afegeix seat dos cops | `IllegalStateException`; 1 sol cotxe |
| CP-21 | + | RF5 | Anna treu seat i Jordi l'afegeix | Propietari = jordi; Anna 0 cotxes; Jordi 1 |
| CP-22 | + | RF6 | Anna amb seat; `descripcio()` de persona i de cotxe | Text exacte segons RF6 |
| CP-23 | − | RF7 | `anna.afegirCotxe(null)` | `IllegalArgumentException`; Anna continua sense cotxes |
| CP-24 | − | RF7 | Anna té seat; `anna.treureCotxe(null)` | No falla; Anna continua amb 1 cotxe |

### 3.4 Integració (`IntegracioTest`)

| Id | Tipus | Entrada / precondició | Resultat esperat |
|---|---|---|---|
| CP-25 | Big bang | Escenari complet: assignar 3 cotxes, intentar duplicar un, vendre un | Anna 1 cotxe, Jordi 2; relació coherent per les dues bandes |
| CP-26 | Ascendent | `Cotxe` ja provat; s'afegeix `Persona` i es fan moltes operacions (afegir, treure, passar d'una persona a l'altra) comprovant la coherència a cada pas | La relació és coherent sempre |
| CP-27 | Descendent | Persona + **stub** de Cotxe que diu "sóc lliure"; `afegirCotxe` | Persona crida `setPropietari` 1 cop amb ella mateixa |
| CP-28 | Descendent | Persona + stub que diu "sóc del Jordi"; `afegirCotxe` | `IllegalStateException`; `setPropietari` no es crida; Anna 0 cotxes |

### 3.5 Sistema (`SistemaTest`)

| Id | Tipus | Entrada / precondició | Resultat esperat |
|---|---|---|---|
| CP-29 | Estrès | Afegir 100.000 cotxes a una persona, després treure'n un | Tot en menys de 10 s; la descripció diu 100000 i després 99999 cotxes |
| CP-30 | Recuperació | Provocar dos errors (cotxe duplicat i cotxe d'un altre); després fer una venda normal | Després dels errors l'estat és coherent; la venda posterior funciona |

## 4. Criteris de passa / no passa

- Un cas **passa** si el resultat obtingut és idèntic a l'esperat.
- Un cas **no passa** si hi ha qualsevol diferència (valor, tipus d'excepció, estat posterior).
- Un cas que no passa genera una incidència al document 03.

## 5. Cobertura de caixa blanca (matriu decisió → casos)

| Mètode | Decisió / bucle | Cas que la cobreix |
|---|---|---|
| `Persona.afegirCotxe` | `cotxe == null` *(afegida a la v1.1)* · cert | CP-23 |
| | `cotxe == null` · fals | CP-07 i la resta |
| | `cotxe.getPropietari() != null` · cert | CP-08, CP-19, CP-20, CP-28 |
| | `cotxe.getPropietari() != null` · fals | CP-07, CP-17, CP-18 |
| `Persona.treureCotxe` | `cotxes.remove(cotxe)` · cert | CP-09, CP-21 |
| | `cotxes.remove(cotxe)` · fals | CP-10, CP-24 |
| `Persona.mostrarCotxes` | `cotxes.isEmpty()` · cert | CP-12 |
| | `cotxes.isEmpty()` · fals | CP-13, CP-14 |
| | bucle `for` · 0 voltes | CP-12 |
| | bucle `for` · 1 volta | CP-13 |
| | bucle `for` · n voltes | CP-14 |
| `Persona.getCotxes` | integritat de la llista interna | CP-15 |
| `Cotxe.descripcio` | `propietari == null` · cert | CP-03 |
| | `propietari == null` · fals | CP-04 |

> Aquesta matriu es pot comprovar automàticament amb una eina de cobertura com **JaCoCo** (opcional, no inclosa al `pom.xml`).

## 6. Procediments de prova

**Executar totes les proves automàtiques** (des de la carpeta del projecte):

```bash
mvn test
```

**Executar només un cas** (re-test), per exemple CP-15 i CP-23:

```bash
mvn test -Dtest="PersonaUnitTest#cp15_integritatLlistaInterna,CaixaNegraTest#cp23_afegirCotxeNull"
```

També es pot fer des de l'IDE (clic dret sobre el test → *Run*).

**Ordre recomanat d'execució:** unitat → caixa negra → integració → sistema → validació (manual).

**Registre dels resultats:** document 03.
