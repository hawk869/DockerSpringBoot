package com.danielcepeda.msvccursos.service;

import com.danielcepeda.msvccursos.clients.UsuarioClientRest;
import com.danielcepeda.msvccursos.models.Usuario;
import com.danielcepeda.msvccursos.models.entity.Curso;
import com.danielcepeda.msvccursos.models.entity.CursoUsuario;
import com.danielcepeda.msvccursos.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service @Transactional
public class CursoServiceImpl implements CursoService{

    @Autowired
    private CursoRepository repository;
    @Autowired
    private UsuarioClientRest clientRest;

    @Override @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) repository.findAll();
    }

    @Override @Transactional(readOnly = true)
    public Optional<Curso> porID(Long id) {
        return repository.findById(id);
    }

    @Override
    public Curso guardar(Curso curso) {
        return repository.save(curso);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void eliminarCursoUsuarioPorId(Long id) {
        repository.eliminarCursoUsuarioPorId(id);
    }

    @Override @Transactional(readOnly = true)
    public Optional<Curso> porIdDeUsuarios(Long id) {
        Optional<Curso> optionalCurso = repository.findById(id);
        if (optionalCurso.isPresent()){
            Curso curso = optionalCurso.get();
            if (!curso.getCursoUsuarios().isEmpty()){
                List<Long> usuariosIds = curso.getCursoUsuarios().stream()
                        .map(CursoUsuario::getIdUsuario)
                        .collect(Collectors.toList());
//                .map(cursoUsuario -> cursoUsuario.getIdUsuario())
//                        .collect(Collectors.toList());
                List<Usuario> usuarios = clientRest.obtenerAlumnosPorCurso(usuariosIds);
                curso.setUsuarios(usuarios);
            }
            return Optional.of(curso);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> optional = repository.findById(cursoId);
        if (optional.isPresent()){
            Usuario usuarioMsvc = clientRest.detalleUsuario(usuario.getId());
            Curso curso = optional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setIdUsuario(usuarioMsvc.getId());
            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> optional = repository.findById(cursoId);
        if (optional.isPresent()){
            Usuario usuarioNuevoMsvc = clientRest.crearUsuario(usuario);
            Curso curso = optional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setIdUsuario(usuarioNuevoMsvc.getId());
            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioNuevoMsvc);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> optional = repository.findById(cursoId);
        if (optional.isPresent()) {
            Usuario usuarioMsvc = clientRest.detalleUsuario(usuario.getId());
            Curso curso = optional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setIdUsuario(usuarioMsvc.getId());
            curso.removeCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }
}
