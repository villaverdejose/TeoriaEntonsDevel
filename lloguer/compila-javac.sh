#!/bin/bash
# ---------------------------------------------------------------------------
# compila-javac.sh
# Compila i executa el projecte NOMÉS amb javac i java (sense Maven).
#
# Ús:   ./compila-javac.sh
#
# Què fa:
#   1. Compila src/main/java       -> out/main
#   2. Executa Main (programa de demostració)
#   3. Si hi ha JUnit a la carpeta lib/, també compila i executa els tests
# ---------------------------------------------------------------------------
set -e
cd "$(dirname "$0")"

OUT=out
LIB=lib

# El separador del classpath és ':' a Linux/macOS i ';' a Windows (Git Bash)
case "$(uname -s)" in
    MINGW*|CYGWIN*|MSYS*) SEP=";" ;;
    *)                    SEP=":" ;;
esac

# Comencem de zero cada vegada
rm -rf "$OUT"
mkdir -p "$OUT/main" "$OUT/test"

# ---------------------------------------------------------------------------
echo "=== 1. Compilant el codi (src/main/java) ==="
# -encoding UTF-8 : perquè els accents es llegeixin bé
# -d out/main     : carpeta on es deixen els .class
javac -encoding UTF-8 -d "$OUT/main" src/main/java/*.java
echo "OK: .class generats a $OUT/main"

# ---------------------------------------------------------------------------
echo
echo "=== 2. Executant Main ==="
# -cp out/main : on són els .class
# Main         : la classe que té el main()
java -Dfile.encoding=UTF-8 -cp "$OUT/main" Main

# ---------------------------------------------------------------------------
# Els tests necessiten JUnit, que és una llibreria externa: javac no la baixa
# sol, l'has de posar tu a la carpeta lib/.
JUNIT=$(ls "$LIB"/junit-platform-console-standalone-*.jar 2>/dev/null | head -n 1 || true)

echo
if [ -z "$JUNIT" ]; then
    echo "=== 3. Tests: SALTATS (no hi ha JUnit a $LIB/) ==="
    echo "Per executar-los amb javac:"
    echo "  1. Crea la carpeta:  mkdir -p $LIB"
    echo "  2. Baixa el fitxer 'junit-platform-console-standalone-1.11.4.jar' de Maven Central"
    echo "     (https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/)"
    echo "  3. Deixa'l dins de $LIB/ i torna a executar aquest script."
    echo "(Amb Maven això és automàtic: vegeu compila-maven.sh)"
    exit 0
fi

echo "=== 3. Compilant els tests (src/test/java) ==="
# -cp : necessita el codi ja compilat I la llibreria JUnit
javac -encoding UTF-8 -cp "$OUT/main$SEP$JUNIT" -d "$OUT/test" src/test/java/*.java
echo "OK: tests compilats a $OUT/test"

echo
echo "=== 4. Executant els tests ==="
# El jar 'console-standalone' porta JUnit sencer i busca els tests pel classpath
java -Dfile.encoding=UTF-8 -jar "$JUNIT" execute \
    --class-path "$OUT/main$SEP$OUT/test" \
    --scan-class-path
