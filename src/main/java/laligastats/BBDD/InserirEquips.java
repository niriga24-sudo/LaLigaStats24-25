package laligastats.BBDD;

import laligastats.DAO.EquipDAO;

public class InserirEquips {

    public static void main(String[] args) {
        EquipDAO eDAO = new EquipDAO();

        if (eDAO.insertarCSVEquipsaBBDD()) {
            try {
                System.out.println("Equips inserits correctament");
            } catch (Exception e) {
                System.err.println("Error al inserir els equips");
            }
        }
    }

}
