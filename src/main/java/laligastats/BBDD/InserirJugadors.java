package laligastats.BBDD;

import laligastats.DAO.JugadorDAO;

public class InserirJugadors {

    public static void main(String[] args) {
        JugadorDAO jDAO = new JugadorDAO();
        Boolean inserits = jDAO.insertarCSVJugadorsaBBDD();
        System.out.println(inserits);
    }
}
