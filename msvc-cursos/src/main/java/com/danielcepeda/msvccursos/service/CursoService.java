package com.danielcepeda.msvccursos.service;

import com.danielcepeda.msvccursos.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    List<Curso> listar();
    Optional<Curso> porID(Long id);
    Curso guardar(Curso curso);
    void eliminar(Long id);
}
