package laligastats;

public class Equip {
    private int ID;
    private int Posicio;
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

    public Equip(int iD, int posicio , String nom_Equip, int punts, int partits_Jugats, int victories, int empats, int derrotes,
            int gols_Marcats, int gols_Encaixats, int diferencia_Gols, int xuts_a_Porteria, int faltes,
            int targetes_Grogues, int targetes_Vermelles) {
        
        this.ID = iD;
        this.Posicio = posicio;
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

    

    @Override
    public String toString() {
        return "Equip [ID=" + ID + ", Posicio=" + Posicio + ", nom_Equip=" + nom_Equip + ", Punts=" + Punts
                + ", Partits_Jugats=" + Partits_Jugats + ", Victories=" + Victories + ", Empats=" + Empats
                + ", Derrotes=" + Derrotes + ", Gols_Marcats=" + Gols_Marcats + ", Gols_Encaixats=" + Gols_Encaixats
                + ", Diferencia_Gols=" + Diferencia_Gols + ", Xuts_a_Porteria=" + Xuts_a_Porteria + ", Faltes=" + Faltes
                + ", Targetes_Grogues=" + Targetes_Grogues + ", Targetes_Vermelles=" + Targetes_Vermelles + "]";
    }



    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public int getPosicio() {
        return Posicio;
    }

    public void setPosicio(int posicio) {
        Posicio = posicio;
    }

    public String getNom_Equip() {
        return nom_Equip;
    }

    public int getPunts() {
        return Punts;
    }

    public void setPunts(int punts) {
        Punts = punts;
    }

    public int getPartits_Jugats() {
        return Partits_Jugats;
    }

    public void setPartits_Jugats(int partits_Jugats) {
        Partits_Jugats = partits_Jugats;
    }

    public int getVictories() {
        return Victories;
    }

    public void setVictories(int victories) {
        Victories = victories;
    }

    public int getEmpats() {
        return Empats;
    }

    public void setEmpats(int empats) {
        Empats = empats;
    }

    public int getDerrotes() {
        return Derrotes;
    }

    public void setDerrotes(int derrotes) {
        Derrotes = derrotes;
    }

    public int getGols_Marcats() {
        return Gols_Marcats;
    }

    public void setGols_Marcats(int gols_Marcats) {
        Gols_Marcats = gols_Marcats;
    }

    public int getGols_Encaixats() {
        return Gols_Encaixats;
    }

    public void setGols_Encaixats(int gols_Encaixats) {
        Gols_Encaixats = gols_Encaixats;
    }

    public int getDiferencia_Gols() {
        return Diferencia_Gols;
    }

    public void setDiferencia_Gols(int diferencia_Gols) {
        Diferencia_Gols = diferencia_Gols;
    }

    public int getXuts_a_Porteria() {
        return Xuts_a_Porteria;
    }

    public void setXuts_a_Porteria(int xuts_a_Porteria) {
        Xuts_a_Porteria = xuts_a_Porteria;
    }

    public int getFaltes() {
        return Faltes;
    }

    public void setFaltes(int faltes) {
        Faltes = faltes;
    }

    public int getTargetes_Grogues() {
        return Targetes_Grogues;
    }

    public void setTargetes_Grogues(int targetes_Grogues) {
        Targetes_Grogues = targetes_Grogues;
    }

    public int getTargetes_Vermelles() {
        return Targetes_Vermelles;
    }

    public void setTargetes_Vermelles(int targetes_Vermelles) {
        Targetes_Vermelles = targetes_Vermelles;
    }
    
}

