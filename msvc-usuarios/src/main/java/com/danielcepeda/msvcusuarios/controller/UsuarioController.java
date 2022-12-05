package com.danielcepeda.msvcusuarios.controller;

import com.danielcepeda.msvcusuarios.models.entity.Usuario;
import com.danielcepeda.msvcusuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
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

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Environment env;

    @GetMapping("/crash")
    public void crash(){
        ((ConfigurableApplicationContext)context).close();
    }
    @GetMapping
    public Map<String, Object> listar(){
        Map<String, Object> body = new HashMap<>();
        body.put("Usuarios", service.findAll());
        body.put("podinfo", env.getProperty("MY_POD_NAME") + ": " + env.getProperty("MY_POD_IP"));
        body.put("texto", env.getProperty("config.texto"));
//        return Collections.singletonMap("Usuarios", service.findAll());
        return body;
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
        if (result.hasErrors()){
            return validar(result);
        }
        if (service.existeEmail(usuario.getEmail())){
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("mensaje", "Ya existe un usuario con en ese email"));
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
            if (!usuario.getEmail().isEmpty() &&
                    !usuario.getEmail().equalsIgnoreCase(usuarioActualizado.getEmail()) &&
                    service.porEmail(usuario.getEmail()).isPresent()){
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
    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> usuariosIds){
        return ResponseEntity.ok(service.listarPorIds(usuariosIds));
    }
    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(e -> {
            errores.put(e.getField(), "El campo " + e.getField() + " " + e.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
