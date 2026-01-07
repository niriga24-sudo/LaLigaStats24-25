======================================================================
CARPETA: CLASES (DOMAIN MODELS)
======================================================================
Aquesta carpeta conté el nucli del Model de Domini de l'aplicació. Utilitza 
el patró POJO (Plain Old Java Object) per representar les entitats de 
negoci: equips i jugadors.

----------------------------------------------------------------------
CLASSE: Equip.java
----------------------------------------------------------------------
Representa l'abstracció d'un club de futbol dins d'una competició.

Mètodes i Conceptes Tècnics:
- Encapsulament: Tots els atributs (Punts, Gols, etc.) són privats, 
  garantint la integritat de les dades mitjançant Getters i Setters.
- Constructor Polimòrfic: Permet instanciar objectes amb tota la càrrega 
  estadística provinent de l'API (13 paràmetres), facilitant el mapeig 
  directe de la classificació de la lliga.
- toString(): Sobreescriptura (Override) per a la depuració ràpida del 
  nom de l'equip i els seus punts.

----------------------------------------------------------------------
CLASSE: Jugador.java
----------------------------------------------------------------------
Entitat que representa el rendiment individual d'un futbolista, 
mantenint una relació de composició amb la classe Equip.

Mètodes i Conceptes Tècnics:
- Relació d'Associació: El camp 'private Equip Equip' actua com una 
  referència d'objecte, simulant la Foreign Key de la base de dades a 
  nivell de memòria RAM.
- Lògica de Càlcul de Ràtios: Tot i que les dades són estàtiques, la 
  classe està dissenyada per rebre 'Gols_per_90' i 'Assist_per_90' com 
  a variables de tipus double, permetent anàlisis d'eficiència més 
  enllà dels números absoluts.
- Optimització d'Atributs: S'han eliminat les estadístiques de passades 
  per reduir la petjada en memòria i simplificar l'estructura de dades 
  cap a la interfície d'usuari o exportadors.