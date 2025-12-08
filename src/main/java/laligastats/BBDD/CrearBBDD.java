package laligastats.BBDD;

import laligastats.Connexio;
import laligastats.DAO.EquipDAO;
import laligastats.DAO.JugadorDAO;

public class CrearBBDD {
    public static void main(String[] args) {
        Boolean BBDDCreada = Connexio.crearBBDD();
        System.out.println(BBDDCreada);

        EquipDAO eDAO = new EquipDAO();
        Boolean TaulaEquipsCreada = eDAO.crearTaulaEquips();
        System.out.println(TaulaEquipsCreada);

        JugadorDAO jDAO = new JugadorDAO();
        Boolean TaulaJugadorsCreada = jDAO.crearTaulaJugador();
        System.out.println(TaulaJugadorsCreada);
    }

}
