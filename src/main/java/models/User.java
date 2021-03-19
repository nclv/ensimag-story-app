package models;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class User implements Serializable {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = -4930879145976782706L;
    private long id;
    private String name;
    private String password;
}

// public class User implements Serializable {

//     private static final long serialVersionUID = 7237307841819964259L;
    
//     private long id;
//     private String name;
//     // private String email;
//     private String password;
    
//     public User(long id, String name) {
//         this.id = id;
//         this.name = name;
//     }

//     public long getId() {
//         return id;
//     }

//     public void setId(long id) {
//         this.id = id;
//     }

//     public String getName() {
//         return name;
//     }

//     public void setName(String name) {
//         this.name = name;
//     }

//     public String getPassword() {
//         return password;
//     }

//     public void setPassword(String password) {
//         this.password = password;
//     }

//     @Override
//     public String toString() {
//         return "User [id=" + id + ", name=" + name + ", password=" + password + "]";
//     }
// }
