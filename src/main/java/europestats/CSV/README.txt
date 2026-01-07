======================================================================
CARPETA: CSV (IO OPERATIONS)
======================================================================
Mòdul de sortida de dades que permet la interoperabilitat entre la 
base de dades SQL i eines d'anàlisi externa com Excel o Python.

Mètodes i Teoria de Programació:
- Stream de Sortida (IO Streams): Utilitza 'PrintWriter' i 'FileWriter' 
  per escriure dades de forma seqüencial al disc dur.
- Internacionalització (Locale): L'ús de 'Locale.US' garanteix que els 
  números decimals utilitzin el punt (.), evitant errors de lectura en 
  programes estadístics internacionals.
- Gestió de Fitxers (File API): El mètode 'verificarCarpeta' utilitza 
  la classe File per realitzar operacions de sistema (mkdir), assegurant 
  que l'entorn d'execució sigui robust.