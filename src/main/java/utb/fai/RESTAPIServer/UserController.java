package utb.fai.RESTAPIServer;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping 

public class UserController {

    private final UserRepository repository;
    public UserController(UserRepository repository){
        this.repository = repository;
    }

    private static boolean isValid(String value, String part) {
        return value != null && value.toLowerCase().contains(part.toLowerCase());
    }

    
    // GET      Získání seznamu uživatelů                /users          N/A 
    @GetMapping 
    public List<MyUser> getEveryUser(){
        return repository.findAll();
    }

    // GET      Vyhledání uživatelů                      /users/search   Volitelné query parametry name, email, phoneNumber
    @GetMapping("/search")
    public List<MyUser> search(
        @RequestParam (required = false) String name, 
        @RequestParam (required = false) String email, 
        @RequestParam (required = false) String phoneNumber){
            return repository.findAll().stream()
                .filter(u -> name == null || isValid(u.getname(), name))
                .filter(u -> email == null || isValid(u.getemail(), email))
                .filter(u -> phoneNumber == null || isValid(u.getphoneNumber(), phoneNumber))
                .toList();
        }

    // GET      Získání informací o jednom uživateli     /users/{id}     ID uživatele v cestě
    @GetMapping("/{id}")
    public ResponseEntity<MyUser> getSpecificUser(@PathVariable Long id){
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // POST     Vytvoření nového uživatele               /users          Data nového uživatele
    @PostMapping 
    public ResponseEntity<MyUser> createUser(@RequestBody(required = false) MyUser user) {
        if (user == null || !user.isUserDataValid()) {return ResponseEntity.badRequest().build();}
        user.setid(null);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(user));
        } catch (Exception e) {return ResponseEntity.badRequest().build();}
    }


    // PUT      Aktualizace existujícího uživatele       /users/{id}     ID uživatele v cestě, data uživatele v těle
    @PostMapping("/{id}")
    public ResponseEntity<MyUser> updateExistingUser(@PathVariable Long id, @RequestBody(required = false) MyUser data){
        Optional<MyUser> existing  = repository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (data == null || !data.isUserDataValid()) {
            return ResponseEntity.badRequest().build();
        }
        try {MyUser user = existing.get();
            user.setname(data.getname());
            user.setemail(data.getemail());
            user.setphoneNumber(data.getphoneNumber());
            return ResponseEntity.ok(repository.save(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE   Smazání uživatele                        /users/{id}     ID uživatele v cestě
    @DeleteMapping("/{id}")
    public ResponseEntity<MyUser> deleteOneUser(@PathVariable Long id) {
        try {
            Optional<MyUser> existing = repository.findById(id);
            if (existing.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            repository.deleteById(id);
            return ResponseEntity.ok(existing.get());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE   Smazání všech uživatelů                  /users          N/A         
    @DeleteMapping
    public ResponseEntity<List<MyUser>> deleteAllUsers() {
        try {repository.deleteAll();
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
