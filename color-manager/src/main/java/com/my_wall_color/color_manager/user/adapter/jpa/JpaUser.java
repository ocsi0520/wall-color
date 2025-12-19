package com.my_wall_color.color_manager.user.adapter.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "App_User")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JpaUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String password;
    private String name;


    private Boolean isAdmin;
}
