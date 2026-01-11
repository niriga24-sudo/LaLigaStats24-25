#!/bin/bash
cd "/Volumes/Toshiba 1TB/DAM2n/Acces_Dades/VSCode_Java_Workspace/LaLigaStats24-25"

echo "=== INICIANDO CAPTURA DE LOGS ===" > app_debug.log
echo "Fecha: $(date)" >> app_debug.log
echo "" >> app_debug.log

# Ejecutar la aplicación como lo hace VS Code
/usr/bin/env /Users/nico/.vscode/extensions/redhat.java-1.51.0-darwin-arm64/jre/21.0.9-macosx-aarch64/bin/java \
  @/var/folders/hs/t3ykz2jd00z5rgpnhj1w71zc0000gn/T/cp_a1doahp267n8l9soukeryoos0.argfile \
  -m europestats/europestats.MAIN.App >> app_debug.log 2>&1 &

# Capturar el PID
PID=$!
echo "PID de la aplicación: $PID"

# Esperar 10 segundos
sleep 10

# Mostrar los logs
echo ""
echo "=== CONTENIDO DE LOS LOGS ==="
cat app_debug.log

# Matar el proceso si sigue corriendo
if ps -p $PID > /dev/null; then
   echo ""
   echo "La aplicación sigue ejecutándose. Deteniéndola..."
   kill $PID
fi
