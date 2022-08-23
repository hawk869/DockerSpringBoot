package com.danielcepeda.msvcusuarios.service;

import com.danielcepeda.msvcusuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> findAll();
    Optional<Usuario> findById(Long id);
    Usuario save(Usuario usuario);
    void delete(Long id);
    Optional<Usuario> porEmail(String email);
}
