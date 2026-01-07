package europestats.CLASES;

public class Usuari {
    private String email;
    private String password;
    private String nom;
    private String rol;

    public Usuari(String email, String password, String nom, String rol) {
        this.email = email;
        this.password = password;
        this.nom = nom;
        this.rol = rol;
    }

    // GETTERS
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNom() {
        return nom;
    }

    public String getRol() {
        return rol;
    }

    // SETTERS
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
