package laligastats;

public class Jugador {
    private final int ID;
    private int Posicio;
    private final String Nom;
    private Equip Equip;
    private int Gols_marcats;
    private int Partits;
    private double Gols_x_Partit;
    private int Posicio_Assistencies;
    private int Assistencies;
    private double Assist_x_Partit;
    private int Posicio_Passades;
    private int Passades_Completades;
    private int Passades_Totals;

    public Jugador(int iD, int posicio, String nom, Equip equip, int gols_marcats, int partits,
            double gols_x_Partit, int posicio_Assistencies, int assistencies, double assist_x_Partit,
            int posicio_Passades, int passades_Completades, int passades_Totals) {
        this.ID = iD;
        this.Posicio = posicio;
        this.Nom = nom;
        this.Equip = equip;
        this.Gols_marcats = gols_marcats;
        this.Partits = partits;
        this.Gols_x_Partit = gols_x_Partit;
        this.Posicio_Assistencies = posicio_Assistencies;
        this.Assistencies = assistencies;
        this.Assist_x_Partit = assist_x_Partit;
        this.Posicio_Passades = posicio_Passades;
        this.Passades_Completades = passades_Completades;
        this.Passades_Totals = passades_Totals;
    }

    

    @Override
    public String toString() {
        return "Jugador [ID=" + ID + ", Posicio=" + Posicio + ", Nom=" + Nom + ", Equip=" + Equip.getNom_Equip() + ", Gols_marcats="
                + Gols_marcats + ", Partits=" + Partits + ", Gols_x_Partit=" + Gols_x_Partit + ", Posicio_Assistencies="
                + Posicio_Assistencies + ", Assistencies=" + Assistencies + ", Assist_x_Partit=" + Assist_x_Partit
                + ", Posicio_Passades=" + Posicio_Passades + ", Passades_Completades=" + Passades_Completades
                + ", Passades_Totals=" + Passades_Totals + "]";
    }



    public int getID() {
        return ID;
    }

    public int getPosicio() {
        return Posicio;
    }

    public void setPosicio(int posicio) {
        Posicio = posicio;
    }

    public String getNom() {
        return Nom;
    }

    public Equip getEquip() {
        return Equip;
    }

    public void setEquip(Equip equip) {
        Equip = equip;
    }

    public int getGols_marcats() {
        return Gols_marcats;
    }

    public void setGols_marcats(int gols_marcats) {
        Gols_marcats = gols_marcats;
    }

    public int getPartits() {
        return Partits;
    }

    public void setPartits(int partits) {
        Partits = partits;
    }

    public double getGols_x_Partit() {
        return Gols_x_Partit;
    }

    public void setGols_x_Partit(double gols_x_Partit) {
        Gols_x_Partit = gols_x_Partit;
    }

    public int getPosicio_Assistencies() {
        return Posicio_Assistencies;
    }

    public void setPosicio_Assistencies(int posicio_Assistencies) {
        Posicio_Assistencies = posicio_Assistencies;
    }

    public int getAssistencies() {
        return Assistencies;
    }

    public void setAssistencies(int assistencies) {
        Assistencies = assistencies;
    }

    public double getAssist_x_Partit() {
        return Assist_x_Partit;
    }

    public void setAssist_x_Partit(double assist_x_Partit) {
        Assist_x_Partit = assist_x_Partit;
    }

    public int getPosicio_Passades() {
        return Posicio_Passades;
    }

    public void setPosicio_Passades(int posicio_Passades) {
        Posicio_Passades = posicio_Passades;
    }

    public int getPassades_Completades() {
        return Passades_Completades;
    }

    public void setPassades_Completades(int passades_Completades) {
        Passades_Completades = passades_Completades;
    }

    public int getPassades_Totals() {
        return Passades_Totals;
    }

    public void setPassades_Totals(int passades_Totals) {
        Passades_Totals = passades_Totals;
    }

    
}
