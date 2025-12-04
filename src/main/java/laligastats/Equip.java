package laligastats;

public class Equip {
    private int ID;
    private final String nom_Equip;
    private int Punts;
    private int Partits_Jugats;
    private int Victories;
    private int Empats;
    private int Derrotes;
    private int Gols_Marcats;
    private int Gols_Encaixats;
    private int Diferencia_Gols;
    private int Xuts_a_Porteria;
    private int Faltes;
    private int Targetes_Grogues;
    private int Targetes_Vermelles;

    public Equip(int iD, String nom_Equip, int punts, int partits_Jugats, int victories, int empats, int derrotes,
            int gols_Marcats, int gols_Encaixats, int diferencia_Gols, int xuts_a_Porteria, int faltes,
            int targetes_Grogues, int targetes_Vermelles) {
        
        this.ID = iD;
        this.nom_Equip = nom_Equip;
        this.Punts = punts;
        this.Partits_Jugats = partits_Jugats;
        this.Victories = victories;
        this.Empats = empats;
        this.Derrotes = derrotes;
        this.Gols_Marcats = gols_Marcats;
        this.Gols_Encaixats = gols_Encaixats;
        this.Diferencia_Gols = diferencia_Gols;
        this.Xuts_a_Porteria = xuts_a_Porteria;
        this.Faltes = faltes;
        this.Targetes_Grogues = targetes_Grogues;
        this.Targetes_Vermelles = targetes_Vermelles;
    }

    
}

