#!/bin/bash
# ---------------------------------------------------------------------------
# compila-maven.sh
# Compila, executa els tests i executa Main amb Maven.
#
# Ús:   ./compila-maven.sh
#
# Què fa:
#   1. Comprova que Maven i Java estan instal·lats
#   2. mvn clean compile : compila src/main/java       -> target/classes
#   3. mvn test          : compila i executa els tests (baixa JUnit sol)
#   4. Executa Main amb java
#
# Cal tenir Internet la primera vegada (Maven baixa JUnit i els seus plugins).
# ---------------------------------------------------------------------------
set -e
cd "$(dirname "$0")"

# ---------------------------------------------------------------------------
echo "=== 1. Comprovant l'entorn ==="
if ! command -v mvn >/dev/null 2>&1; then
    echo "ERROR: no he trobat 'mvn'. Instal·la Maven (https://maven.apache.org) i torna-ho a provar."
    exit 1
fi
if ! command -v java >/dev/null 2>&1; then
    echo "ERROR: no he trobat 'java'. Instal·la un JDK 17 o superior."
    exit 1
fi
mvn -version | head -n 1
java -version 2>&1 | head -n 1

# ---------------------------------------------------------------------------
echo
echo "=== 2. Compilant (mvn clean compile) ==="
# clean   : esborra la carpeta target/ anterior
# compile : compila src/main/java -> target/classes (Maven crida javac per sota)
mvn clean compile

# ---------------------------------------------------------------------------
echo
echo "=== 3. Executant els tests (mvn test) ==="
# Maven compila src/test/java, llança Surefire i aquest llança JUnit.
# Els informes queden a target/surefire-reports/
mvn test

# ---------------------------------------------------------------------------
echo
echo "=== 4. Executant Main ==="
# Maven no executa Main per defecte: ho fem nosaltres amb java.
java -Dfile.encoding=UTF-8 -cp target/classes Main

echo
echo "Informes dels tests: target/surefire-reports/"
