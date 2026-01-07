======================================================================
CARPETA: SEGURETAT (MIDDLEWARE & SESSION MANAGEMENT)
======================================================================
Aquesta carpeta conté la lògica intermediària que controla l'accés a les 
funcionalitats de l'aplicació. El seu objectiu és garantir que només els 
usuaris autoritzats (admins) puguin realitzar canvis a la base de dades.

----------------------------------------------------------------------
CLASSE: Sessio.java (ESTAT DE L'APLICACIÓ)
----------------------------------------------------------------------
Implementa l'emmagatzematge temporal de la identitat de l'usuari.

Mètodes i Conceptes Tècnics:
- Patró Singleton: Garanteix una única instància global de la sessió. 
  Això permet que qualsevol pantalla de JavaFX pugui consultar 'Sessio.getInstancia()' 
  per saber quin usuari hi ha connectat sense haver de passar objectes 
  entre controladors.
- Gestió d'Estat: Manté variables volàtils (email, activa) que es 
  destrueixen automàticament en tancar el programa, assegurant que 
  no quedin credencials en memòria RAM de forma permanent.

----------------------------------------------------------------------
CLASSE: GestorSeguretat.java (CAPA DE SERVEI)
----------------------------------------------------------------------
Actua com el controlador lògic que connecta la interfície d'usuari amb 
la persistència d'usuaris (UsuariDAO).

Mètodes i Conceptes Tècnics:
- Desacoblament (Decoupling): JavaFX no sap com es valida un usuari ni 
  com funciona el xifrat SHA-256; només invoca 'intentarLogin()'. 
  Aquesta abstracció facilita el manteniment futur del codi.
- Flux d'Autenticació: 
    1. Rep credencials de la interfície.
    2. Delega la validació a UsuariDAO (comprovació de hashes).
    3. Si és positiu, inicialitza el Singleton de Sessió.
- Seguretat Passiva: El mètode 'esUsuariAutenticat()' permet bloquejar 
  o desbloquejar botons de la interfície (com "Importar API") en temps 
  real segons l'estat de la sessió activa.