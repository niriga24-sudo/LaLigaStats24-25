======================================================================
CARPETA: DAO (DATA ACCESS OBJECT) - PERSISTÈNCIA I SEGURETAT
======================================================================
Aquesta capa actua com l'únic punt d'entrada i sortida cap a la base de 
dades MySQL 'Europe_Stats_23_24'. Implementa el patró DAO per separar 
la persistència de la lògica de presentació (JavaFX).

----------------------------------------------------------------------
CLASSE: EquipDAO.java
----------------------------------------------------------------------
Gestiona l'entitat 'equips' i la classificació europea.
- crearTaulaEquips(): Defineix l'esquema DDL inicial.
- insertarOActualitzarEquipComplet(): Implementa 'REPLACE INTO' per a 
  mantenir les dades de classificació (punts, victòries, etc.) al dia.

----------------------------------------------------------------------
CLASSE: JugadorDAO.java
----------------------------------------------------------------------
Gestiona l'entitat 'jugadors' i les seves mètriques de rendiment.
- crearTaulaJugador(): Estableix la integritat referencial (Foreign Key) 
  amb la taula d'equips.
- insertarOActualitzarJugador(): Sincronitzat amb el model d'11 columnes, 
  optimitzant la càrrega de dades filtrant jugadors sense minuts jugats.
- obtenirTotsJugadors(): Utilitza operacions 'JOIN' per reconstruir 
  objectes complexos en memòria (objectes Jugador que contenen objectes Equip).

----------------------------------------------------------------------
CLASSE: UsuariDAO.java
----------------------------------------------------------------------
Nova classe dedicada a la seguretat i control d'accés (RBAC).

Mètodes i Teoria de Programació:
- crearTaulaUsuaris(): Defineix l'estructura per emmagatzemar credencials. 
  L'email s'utilitza com a Primary Key per garantir la unicitat de comptes.
- hashPassword(String): Implementa seguretat criptogràfica mitjançant 
  l'algorisme SHA-256 (MessageDigest). Aquest mètode transforma la 
  contrasenya en un resum hexadecimal irreversible (Hashing), seguint 
  les millors pràctiques d'OWASP.
- inicialitzarAdmins(): Mètode d'aprovisionament que assegura l'existència 
  dels perfils 'nico@europestats.com' i 'pedro@europestats.com' amb les 
  seves credencials xifrades.
- validarLogin(): Realitza una cerca per email i compara el hash generat 
  en temps real amb el hash emmagatzemat. Si coincideixen, l'usuari és 
  autenticat sense que el sistema hagi de conèixer mai la contrasenya real.