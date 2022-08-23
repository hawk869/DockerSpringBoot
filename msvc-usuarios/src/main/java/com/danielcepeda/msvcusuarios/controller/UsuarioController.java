package com.danielcepeda.msvcusuarios.controller;

import com.danielcepeda.msvcusuarios.models.entity.Usuario;
import com.danielcepeda.msvcusuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public List<Usuario> listar(){
        return service.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id){
        Optional<Usuario> usuarioOptional = service.findById(id);
        if (usuarioOptional.isPresent()){
            return ResponseEntity.ok(usuarioOptional.get());
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result){
        if (service.porEmail(usuario.getEmail()).isPresent()){
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("mensaje", "Ya existe un usuario con en ese email"));
        }
        if (result.hasErrors()){
            return validar(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id){
        if (result.hasErrors()){
            return validar(result);
        }
        Optional<Usuario> usuarioOptional = service.findById(id);
        if (usuarioOptional.isPresent()){
            Usuario usuarioActualizado = usuarioOptional.get();
            if (!usuario.getEmail().equalsIgnoreCase(usuarioActualizado.getEmail()) && service.porEmail(usuario.getEmail()).isPresent()){
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("mensaje", "Ya existe un usuario con en ese email"));
            }
            usuarioActualizado.setNombre(usuario.getNombre());
            usuarioActualizado.setEmail(usuario.getEmail());
            usuarioActualizado.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(usuarioActualizado));
        }
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        Optional<Usuario> optionalUsuario = service.findById(id);
        if (optionalUsuario.isPresent()){
            service.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(e -> {
            errores.put(e.getField(), "El campo " + e.getField() + " " + e.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
