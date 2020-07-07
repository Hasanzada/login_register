package com.example.login_register.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "urls")
@ToString(exclude = "urls")
@Entity
public class User {

    @Column(name = "user_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    @Size(min = 1, message = "This field cannot be empty.")
    private String fullName;

    @Column(unique = true)
    @NotNull
    @Size(min = 1, message = "This field cannot be empty.")
    private String username;

    @NotNull
    @Size(min = 1, message = "This field cannot be empty.")
    private String password;

    private String roles;

    @Transient
    @JsonIgnore
    private final String ROLES_DELIMITER = ":";

    public User(String fullName, String username, String password, String[] roles) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        setRoles(roles);
    }

    public String[] getRoles() {
        if (this.roles == null || this.roles.isEmpty()) return new String[]{};
        return this.roles.split(ROLES_DELIMITER);
    }

    public void setRoles(String[] roles) {
        this.roles = String.join(ROLES_DELIMITER, roles);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Url> urls = new HashSet<>();


    @Transient
    @JsonIgnore
    private String passwordConfirm;

    private boolean isEnabled;


}
