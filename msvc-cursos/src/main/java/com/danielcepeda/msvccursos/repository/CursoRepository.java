package com.danielcepeda.msvccursos.repository;

import com.danielcepeda.msvccursos.models.entity.Curso;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CursoRepository extends CrudRepository<Curso, Long> {

    @Modifying //ya que el query no es solo consulta, con esto nos aseguramos que se realize
    @Query("delete from CursoUsuario cu where cu.idUsuario=?1")
    void eliminarCursoUsuarioPorId(Long id);
}
