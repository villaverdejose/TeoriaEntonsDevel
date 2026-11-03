# `javac` vs Maven

`javac` és el **compilador** de Java. Maven és una **eina de construcció** (*build tool*) que, entre moltes altres coses, crida `javac` per tu.

## `javac`: només compila

Pren fitxers `.java` i genera fitxers `.class`. Res més. Tu has de dir-li tot:

```bash
javac -d out src/main/java/*.java
javac -cp out:junit.jar -d out-test src/test/java/*.java
java -cp out:out-test:junit.jar ...    # i executar-ho també és cosa teva
```

Si el projecte té llibreries (com JUnit), has de baixar-te els `.jar` a mà, saber quins altres `.jar` necessiten, i posar-los tots al `-cp` en l'ordre correcte.

## Maven: gestiona tot el procés

Maven llegeix un fitxer `pom.xml` on descrius el projecte, i amb una sola ordre fa tot això:

| Pas | Què fa |
|---|---|
| Dependències | Baixa JUnit i tot el que JUnit necessita d'Internet (Maven Central) |
| Compilar | Crida `javac` per a `src/main/java` |
| Compilar proves | Crida `javac` per a `src/test/java`, amb JUnit al classpath |
| Provar | Llança Surefire, que executa els tests |
| Empaquetar | Genera el `.jar` (`mvn package`) |

Tot això va pel mateix camí fix, i per això els projectes Maven sempre tenen `src/main/java` i `src/test/java`: és una convenció que Maven espera.

## Comparació

| | `javac` | Maven |
|---|---|---|
| Què és | Un compilador | Una eina de construcció |
| Fa | Compilar `.java` → `.class` | Baixar llibreries, compilar, provar, empaquetar |
| Llibreries externes | Les gestiones tu | Les declares al `pom.xml` i les baixa |
| Executar tests | No | Sí (via Surefire) |
| Configuració | Opcions a la línia d'ordres | Fitxer `pom.xml` |
| Necessita Internet | No | Sí, la primera vegada (per baixar dependències) |

## Per què importa en aquest projecte

- L'`exercici-persones-cotxes` (el simple) es compila només amb `javac`, perquè no té cap llibreria externa. És adequat per a un primer contacte.
- El `proves-persones-cotxes` fa servir JUnit, que és una llibreria externa. Per això porta `pom.xml` i es llança amb `mvn test`. Amb `javac` sol hauries de baixar JUnit a mà i muntar el classpath.

Una manera de dir-ho a classe: `javac` és el motor, i Maven és qui el posa en marxa, li porta el que necessita i després executa les proves.

Maven no és l'única opció. **Gradle** fa la mateixa feina amb un altre format, i els IDE (Eclipse, IntelliJ, NetBeans) també poden compilar i executar proves sense que escriguis cap ordre. Per sota, fan el mateix: crida a `javac` i a JUnit.
