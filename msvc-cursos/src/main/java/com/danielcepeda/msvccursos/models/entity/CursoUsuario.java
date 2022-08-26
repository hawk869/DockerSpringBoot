package com.danielcepeda.msvccursos.models.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Entity @Table(name = "cursos_usuarios")
public class CursoUsuario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, name = "usuario_id")
    private Long idUsuario;

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (!(obj instanceof CursoUsuario)){
            return false;
        }
        CursoUsuario o = (CursoUsuario) obj;
        return this.idUsuario != null && this.idUsuario.equals(o.idUsuario);
    }
}
