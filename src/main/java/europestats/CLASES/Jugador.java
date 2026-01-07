package europestats.CLASES;

public class Jugador {
    private int ID;
    private String Nom;
    private String posicio;
    private Equip Equip;
    private int idLliga;
    private int Gols_marcats;
    private int Assistencies;
    private int Minuts;
    private int Targetes_Grogues;
    private int Targetes_Vermelles;
    private double Gols_per_90;
    private double Assist_per_90;

    public Jugador() {
    }

    public Jugador(int iD, String nom, String posicio, Equip equip, int idLliga, int gols_marcats, int assistencies,
            int minuts, int grogues, int vermelles, double gols_per_90, double assist_per_90) {
        this.ID = iD;
        this.Nom = nom;
        this.posicio = posicio;
        this.Equip = equip;
        this.idLliga = idLliga;
        this.Gols_marcats = gols_marcats;
        this.Assistencies = assistencies;
        this.Minuts = minuts;
        this.Targetes_Grogues = grogues;
        this.Targetes_Vermelles = vermelles;
        this.Gols_per_90 = gols_per_90;
        this.Assist_per_90 = assist_per_90;
    }

    // --- GETTERS ---
    public int getID() {
        return ID;
    }

    public String getNom() {
        return Nom;
    }

    public String getPosicio() {
        return posicio;
    }

    public Equip getEquip() {
        return Equip;
    }

    public int getIdLliga() {
        return idLliga;
    }

    public int getGols_marcats() {
        return Gols_marcats;
    }

    public int getAssistencies() {
        return Assistencies;
    }

    public int getMinuts() {
        return Minuts;
    }

    public int getTargetes_Grogues() {
        return Targetes_Grogues;
    }

    public int getTargetes_Vermelles() {
        return Targetes_Vermelles;
    }

    public double getGols_per_90() {
        return Gols_per_90;
    }

    public double getAssist_per_90() {
        return Assist_per_90;
    }

    // --- SETTERS ---
    public void setID(int ID) {
        this.ID = ID;
    }

    public void setNom(String nom) {
        this.Nom = nom;
    }

    public void setPosicio(String posicio) {
        this.posicio = posicio;
    }

    public void setEquip(Equip equip) {
        this.Equip = equip;
    }

    public void setIdLliga(int idLliga) {
        this.idLliga = idLliga;
    }

    public void setGols_marcats(int gols) {
        this.Gols_marcats = gols;
    }

    public void setAssistencies(int assist) {
        this.Assistencies = assist;
    }

    public void setMinuts(int minuts) {
        this.Minuts = minuts;
    }

    public void setTargetes_Grogues(int grogues) {
        this.Targetes_Grogues = grogues;
    }

    public void setTargetes_Vermelles(int vermelles) {
        this.Targetes_Vermelles = vermelles;
    }

    public void setGols_per_90(double g90) {
        this.Gols_per_90 = g90;
    }

    public void setAssist_per_90(double a90) {
        this.Assist_per_90 = a90;
    }

    // Dins de la classe Jugador.java afegeix aquest mètode:
    public int getIdEquipObjecte() {
        return (Equip != null) ? Equip.getID() : 0;
    }
}