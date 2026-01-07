package europestats.CLASES;

public class Equip {
    private int ID;
    private int idLliga;
    private String Lliga;
    private int Posicio;
    private String nom_Equip;
    private int Punts;
    private int Partits_Jugats;
    private int Victories;
    private int Empats;
    private int Derrotes;
    private int Gols_Marcats;
    private int Gols_Encaixats;
    private int Diferencia_Gols;

    // CONSTRUCTOR CORREGIT: Ordre ID -> idLliga -> Lliga -> Posicio -> nom_Equip
    public Equip(int iD, int idLliga, String lliga, int posicio, String nom_Equip, int punts, int partits_Jugats, int victories, int empats, int derrotes, int gols_Marcats, int gols_Encaixats, int diferencia_Gols) {

        this.ID = iD;
        this.idLliga = idLliga;
        this.Lliga = lliga; // 3a posició (String)
        this.Posicio = posicio; // 4a posició (int)
        this.nom_Equip = nom_Equip; // 5a posició (String)
        this.Punts = punts;
        this.Partits_Jugats = partits_Jugats;
        this.Victories = victories;
        this.Empats = empats;
        this.Derrotes = derrotes;
        this.Gols_Marcats = gols_Marcats;
        this.Gols_Encaixats = gols_Encaixats;
        this.Diferencia_Gols = diferencia_Gols;
    }

    @Override
    public String toString() {
        return "Equip [ID=" + ID + ", Lliga=" + Lliga + ", Equip=" + nom_Equip + ", Punts=" + Punts + "]";
    }

    // Getters i Setters
    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public int getIdLliga() {
        return idLliga;
    }

    public void setIdLliga(int idLliga) {
        this.idLliga = idLliga;
    }

    public String getLliga() {
        return Lliga;
    }

    public void setLliga(String lliga) {
        Lliga = lliga;
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

    public void setNom_Equip(String nom_Equip) {
        this.nom_Equip = nom_Equip;
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
}
