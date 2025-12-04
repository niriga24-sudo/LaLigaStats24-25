package laligastats;

public class Jugador {
    private int ID;
    private int Posicio;
    private int Nom;
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

    public Jugador(int iD, int posicio, int nom, laligastats.Equip equip, int gols_marcats, int partits,
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

    
}
