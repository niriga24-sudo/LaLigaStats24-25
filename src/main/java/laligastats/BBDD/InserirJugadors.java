package laligastats.BBDD;

import laligastats.DAO.JugadorDAO;

public class InserirJugadors {

    public static void main(String[] args) {
        JugadorDAO jDAO = new JugadorDAO();

        if (jDAO.insertarCSVJugadorsaBBDD()) {
            try {
                System.out.println("Jugadors inserits correctament");
            } catch (Exception e) {
                System.err.println("Error al inserir els jugadors");
            }
        }
    }
}
