package laligastats.BBDD;

import laligastats.Connexio;

public class CrearBBDD {
    public static void main(String[] args) {
       Boolean BBDDCreada = Connexio.deleteBBDD();
       System.out.println(BBDDCreada);
    }
    
}
