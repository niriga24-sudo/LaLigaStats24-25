======================================================================
CARPETA: JSON (DATA TRANSFER OBJECTS - DTO)
======================================================================
Aquesta carpeta actua com la capa de deserialització del projecte. Conté 
les estructures de dades que fan de "mirall" de la resposta JSON enviada 
per l'API externa (API-Football).

----------------------------------------------------------------------
CLASSE: RespostaAPI.java
----------------------------------------------------------------------
Aquesta és una "Classe Contenidora" que utilitza el model de classes 
internes (Inner Classes) per replicar l'arbre jeràrquic del fitxer JSON.

Mètodes i Conceptes Tècnics:
- Deserialització GSON: La llibreria GSON utilitza aquesta classe com a 
  mapa per transformar text pla (JSON) en objectes Java vius. Cada camp 
  de la classe ha de coincidir exactament amb la clau del JSON.
- Classes Estàtiques Internes: S'utilitzen 'public static class' (com 
  PlayerAPI, StatsAPI, etc.) per organitzar les dades sense necessitat 
  d'instanciar la classe pare, permetent una estructura niuada que 
  estalvia memòria i organitza el codi.
- Anotacions @SerializedName: Conceptes clau de programació on el nom 
  de la variable en Java ha de ser diferent al de la font original.
    Exemple: @SerializedName("for") s'usa perquè 'for' és una paraula 
    reservada (bucle) en Java i no es pot usar com a nom de variable.
- Gestió de Paginació: La subclasse 'PagingAPI' és fonamental per a la 
  lògica de control del GestorAPI, ja que permet llegir el total de 
  pàgines i automatitzar la descàrrega de llistes llargues de jugadors.
- Abstracció de dades: Aquesta classe no conté lògica de negoci, només 
  atributs públics per a un accés ultraràpid durant el procés de mapeig 
  cap a les classes definitives del domini (Jugador i Equip).