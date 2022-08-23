package com.danielcepeda.msvccursos.service;

import com.danielcepeda.msvccursos.entity.Curso;
import com.danielcepeda.msvccursos.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service @Transactional
public class CursoServiceImpl implements CursoService{

    @Autowired
    private CursoRepository repository;

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
}
