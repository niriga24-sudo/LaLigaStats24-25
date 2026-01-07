======================================================================
CARPETA: API (NETWORKING & PARSING)
======================================================================
Capa encarregada de la comunicació asíncrona amb el servei extern 
Football-API i la transformació de dades JSON a objectes del domini.

Mètodes i Teoria de Programació:
- Protocol HTTP/2: Utilitza 'HttpClient' per realitzar peticions GET.
- Deserialització GSON: Transforma cadenes JSON complexes en arbres 
  d'objectes Java (RespostaAPI) mitjançant reflexió.
- Gestió de Taxa de Peticions (Rate Limiting): Implementa 'Thread.sleep()' 
  per respectar el límit de la versió gratuïta de l'API (evitant l'error 429).
- Mapeig de Dades (Data Mapping): El mètode 'mapejarItemAJugador' realitza 
  la neteja i transformació de tipus (casting) de les dades crues 
  de l'API cap als constructors de les nostres CLASES.