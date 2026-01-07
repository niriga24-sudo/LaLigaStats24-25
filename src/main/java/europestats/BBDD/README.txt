CARPETA: BBDD
Objectiu: Gestió del cicle de vida de la base de dades 'Europe_Stats_23_24'.

======================================================================
CLASSE: Connexio.java
----------------------------------------------------------------------
Gestiona els paràmetres de connexió JDBC per a MySQL.
- NOM_BBDD: Europe_Stats_23_24
- getConnection(): Connexió general al servidor.
- getConnectionBBDD(): Connexió específica a l'esquema del projecte.

======================================================================
CLASSE: CrearBBDD.java
----------------------------------------------------------------------
Conté la lògica de construcció de l'esquema.
- crearBBDD(): DROP i CREATE de la base de dades (ús destructiu).
- inicialitzarEstructura(): Crida els mètodes de EquipDAO i JugadorDAO 
  per crear les taules 'IF NOT EXISTS'. Ideal per a l'ús diari sense 
  perdre dades.

======================================================================
CLASSE: EliminarBBDD.java
----------------------------------------------------------------------
Mètode de seguretat per netejar el servidor de la base de dades 
especificada a Connexio.