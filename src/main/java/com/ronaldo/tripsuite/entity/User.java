package com.ronaldo.tripsuite.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@ToString
@Entity
@Table(name = "users", uniqueConstraints={
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Username is required")
    private String username;

    private String name;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

//    @Size(min = 4, max = 20, message = "Password must be between 4 and 20 characters")
    @JsonIgnore
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLE",
            joinColumns = {
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID")
            }
    )
    private Set<Role> role;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private List<Trip> trips;
}
