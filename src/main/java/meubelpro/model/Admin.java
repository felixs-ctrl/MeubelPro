package meubelpro.model;

public class Admin {
    private int id;
    private String username;
    private String namaLengkap;
    private String email;

    public Admin(int id, String username, String namaLengkap, String email) {
        this.id = id; this.username = username; this.namaLengkap = namaLengkap; this.email = email;
    }
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getNamaLengkap() { return namaLengkap; }
    public String getEmail() { return email; }
}
