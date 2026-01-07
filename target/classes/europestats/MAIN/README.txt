======================================================================
CARPETA: main (ORQUESTRADOR DE SISTEMA)
======================================================================
Aquesta carpeta conté la classe d'entrada que governa el flux de treball 
de tota l'aplicació, actuant com un Tauler de Control.

Conceptes de Programació i Disseny:
- Arquitectura en Capes: El Main no sap COM s'escriu un CSV o COM es 
  crida l'API; només delega les tasques a les classes especialitzades. 
  Això es coneix com a 'Alt Cohesió i Baix Acoblament'.
- Gestió de Dependències: Coordina l'ordre d'execució. Per exemple, 
  estableix que no es poden importar jugadors sense haver verificat 
  primer l'existència de la base de dades (Fase 1).
- Escalabilitat Multilliga: El disseny permet l'expansió a qualsevol 
  competició reconeguda per l'API (ID 140, 39, 135...) simplement 
  invocant el mètode parametritzat del GestorAPI.
- Manteniment Preventiu: Inclou crides comentades a 'EliminarBBDD' per 
  facilitar el depurament (debugging) del sistema durant la fase de 
  desenvolupament sense haver d'obrir eines externes com phpMyAdmin.