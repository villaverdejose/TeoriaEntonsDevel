# Proves de programari: persones i cotxes

Aplicació dels tipus de proves del tema *Disseny i realització de proves* (M05 · RA3) sobre l'exemple simple de **persones i cotxes** (una persona té diversos cotxes; un cotxe només té una persona).

## Estructura

```
proves-persones-cotxes/
├── pom.xml                         Maven + JUnit 5
├── README.md
├── compila-javac.sh                Compilar i executar amb javac
├── compila-maven.sh                Compilar, provar i executar amb Maven
├── docs/                           Documentació de proves (estil IEEE 829)
│   ├── 01_PLA_DE_PROVES.md
│   ├── 02_ESPECIFICACIO_DE_PROVES.md
│   ├── 03_INFORMES_DE_PROVES.md
│   └── 04_PROVES_DE_VALIDACIO_MANUALS.md
├── src/main/java/                  Codi v1.1 (corregit)
│   ├── Persona.java
│   ├── Cotxe.java
│   └── Main.java
├── src/test/java/                  Proves automàtiques (30 casos)
│   ├── CotxeUnitTest.java          CP-01..05   unitat · caixa blanca
│   ├── PersonaUnitTest.java        CP-06..15   unitat · caixa blanca
│   ├── CaixaNegraTest.java         CP-16..24   caixa negra
│   ├── IntegracioTest.java         CP-25..28   integració
│   ├── SistemaTest.java            CP-29..30   sistema
│   ├── CotxeStub.java              stub per a la integració descendent
│   └── Coherencia.java             utilitat per comprovar la relació
└── v1.0-original/Persona.java      Versió original, amb 2 defectes
```

## Del PowerPoint al projecte

| Al PowerPoint | Al projecte |
|---|---|
| Planificació: abast, estratègia, criteris de sortida | `docs/01_PLA_DE_PROVES.md` |
| Disseny dels casos: positius i negatius, dades de prova | `docs/02_ESPECIFICACIO_DE_PROVES.md` |
| Prova d'unitat | `CotxeUnitTest`, `PersonaUnitTest` |
| Caixa blanca (camins, condicions, bucles, estructures de dades) | `CotxeUnitTest`, `PersonaUnitTest` + matriu de cobertura al document 02 |
| Caixa negra (entrades i sortides, límits) | `CaixaNegraTest` |
| Integració big bang | `IntegracioTest` · CP-25 |
| Integració incremental ascendent | `IntegracioTest` · CP-26 |
| Integració incremental descendent | `IntegracioTest` · CP-27 i CP-28 (amb `CotxeStub`) |
| Prova de sistema: estrès | `SistemaTest` · CP-29 |
| Prova de sistema: recuperació | `SistemaTest` · CP-30 |
| Prova de sistema: seguretat | No aplicable (justificat al pla) |
| Prova de validació: alfa i beta | `docs/04_PROVES_DE_VALIDACIO_MANUALS.md` (manuals) |
| Execució manual vs. automatitzada | `Main` (manual) · JUnit (automàtica) |
| Bug tracker, re-test i regressió | `docs/03_INFORMES_DE_PROVES.md` (INC-001 i INC-002) |
| Avaluació i tancament | `docs/03_INFORMES_DE_PROVES.md`, secció 4 |
| Documentació IEEE 829 (pla, especificació, informes) | Carpeta `docs/` |

## Scripts de compilació i execució

| Script | Què fa |
|---|---|
| `./compila-javac.sh` | Compila amb `javac` i executa `Main`. Els tests només s'hi executen si poses `junit-platform-console-standalone-1.11.4.jar` a una carpeta `lib/` |
| `./compila-maven.sh` | Fa `mvn clean compile`, `mvn test` i executa `Main`. Baixa JUnit sol (cal Internet la primera vegada) |

A Windows, executa'ls des de Git Bash o WSL.

## Executar les proves

Amb Maven (Java 17+):

```bash
mvn test
```

Resultat esperat amb el codi actual (v1.1): **30 proves, 0 errors**.

Sense Maven: importa la carpeta a Eclipse, IntelliJ o NetBeans, marca `src/main/java` com a codi i `src/test/java` com a proves, afegeix la llibreria JUnit 5 (Jupiter) i fes *Run* sobre la carpeta de proves.

## Demostració a classe: el cicle d'un defecte

El projecte porta dos defectes reals a la versió original que les proves detecten. Es pot repetir el cicle amb l'alumnat:

1. **Detectar.** Guarda la v1.1 i posa la v1.0 al seu lloc:
   ```bash
   cp src/main/java/Persona.java Persona-v1.1.java
   cp v1.0-original/Persona.java src/main/java/Persona.java
   mvn test
   ```
   Resultat esperat: **30 proves, 2 falles** (CP-15 i CP-23).
2. **Documentar.** Omplir la incidència al document 03 (ja hi són INC-001 i INC-002 com a exemple).
3. **Corregir.** Tornar a la v1.1: `cp Persona-v1.1.java src/main/java/Persona.java`.
4. **Re-test.** Executar només els dos casos que fallaven (comanda al document 02, secció 6).
5. **Regressió.** `mvn test`: tots els casos han de passar.

## Nota sobre `() ->` a les proves

Als casos negatius surt `assertThrows(IllegalStateException.class, () -> jordi.afegirCotxe(seat));`. El `() -> ...` vol dir què es passa un tros de codi que *no s'executa fins que `assertThrows` el crida dins d'un `try/catch`*, per comprovar que llança l'excepció esperada. És la forma estàndard d'escriure aquestes proves a JUnit 5.

## Nota sobre l'herència a les proves

`CotxeStub` és l'únic lloc amb `extends` i `@Override`. És un *stub* (mòdul simulat) necessari per mostrar la integració descendent. L'herència contè els mateixos atributs i funcions què al pare (extends) i l'@Override redefineix funcions declarades al pare.
