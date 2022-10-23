package com.danielcepeda.msvccursos.clients;

import com.danielcepeda.msvccursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@FeignClient(name = "msvc-usuarios", url = "${msvc.usuarios.url}")
@FeignClient(name = "msvc-usuarios")
public interface UsuarioClientRest {

    @GetMapping("/{id}")
    Usuario detalleUsuario(@PathVariable Long id);
    @PostMapping("/")
    Usuario crearUsuario(@RequestBody Usuario usuario);
    @GetMapping("/usuarios-por-curso")
    List<Usuario> obtenerAlumnosPorCurso(@RequestParam Iterable<Long> usuariosIds);
}
