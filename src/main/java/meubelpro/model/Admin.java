package meubelpro.model;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", length = 50)
    private String username;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Column(name = "nama_lengkap", length = 100)
    private String namaLengkap;

    @Column(name = "email", length = 100)
    private String email;

    public Admin() {
    }

    // constructor untuk kompatibilitas dengan DAO lama.
    public Admin(int id, String username, String namaLengkap, String email) {
        this(id, username, null, namaLengkap, email);
    }

    // constructor lengkap.
    public Admin(int id, String username, String password,
                 String namaLengkap, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
