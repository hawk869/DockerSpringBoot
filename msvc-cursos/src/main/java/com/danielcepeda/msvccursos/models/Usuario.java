package com.danielcepeda.msvccursos.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Usuario {

    private Long id;
    private String nombre;
    private String email;
    private String password;
}
