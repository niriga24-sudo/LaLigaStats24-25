package laligastats.BBDD;

import laligastats.Connexio;

public class EliminarBBDD {
    public static void main(String[] args) {
       Boolean BBDDEliminada = Connexio.deleteBBDD();
       System.out.println(BBDDEliminada);
    }
}
