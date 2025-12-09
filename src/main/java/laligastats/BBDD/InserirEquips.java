package laligastats.BBDD;
import laligastats.DAO.EquipDAO;

public class InserirEquips {

public static void main(String[] args) {
    EquipDAO eDAO = new EquipDAO();
    Boolean inserits = eDAO.insertarCSVEquipsaBBDD();
    System.out.println(inserits);
}
 
}
