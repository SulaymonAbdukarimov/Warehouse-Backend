package org.example.appwarehouse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)

    private String lastName;

    @Column(unique = true, nullable = false)
    private String  phoneNumber;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String password;

    private boolean active = true;

    @ManyToMany
    //Set qilinganini sababi warehouse yanna shu userga
    // biriktirilimasin degan maqsada
    private Set<Warehouse> warehouses;

}
