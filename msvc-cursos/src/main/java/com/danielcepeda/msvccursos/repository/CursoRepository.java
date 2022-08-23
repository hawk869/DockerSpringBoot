package com.danielcepeda.msvccursos.repository;

import com.danielcepeda.msvccursos.entity.Curso;
import org.springframework.data.repository.CrudRepository;

public interface CursoRepository extends CrudRepository<Curso, Long> {
}
