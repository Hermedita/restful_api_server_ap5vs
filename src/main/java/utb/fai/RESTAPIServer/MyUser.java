package utb.fai.RESTAPIServer;

import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table (name = "users")
// Database entity representing a user in the system
public class MyUser {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;

    public MyUser() {}

    public MyUser(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    private static final Pattern VALID_EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$");
    private static final Pattern VALID_PHONE = Pattern.compile("^\\+420\\d{9}$");

    @JsonIgnore
    public boolean isUserDataValid() {
        if (name == null || name.isBlank()) return false;
        if (email == null || email.isBlank() || !VALID_EMAIL.matcher(email).matches()) return false;
        if (phoneNumber == null || phoneNumber.isBlank() || !VALID_PHONE.matcher(phoneNumber).matches()) return false;
        // Add your validation logic here (e.g., email and phone number format validation)
        return true;
    }

    // Getters and setters
    public Long getid(){return id;}
    public void setid(Long id){this.id = id;}

    public String getname(){return name;}
    public void setname(String name){this.name = name;}

    public String getemail(){return email;}
    public void setemail(String email){this.email = email;}

    public String getphoneNumber(){return phoneNumber;}
    public void setphoneNumber(String phoneNumber){this.phoneNumber = phoneNumber;}
    
}
