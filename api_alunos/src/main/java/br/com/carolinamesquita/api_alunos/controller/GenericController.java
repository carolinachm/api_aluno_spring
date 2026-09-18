package br.com.carolinamesquita.api_alunos.controller;

import br.com.carolinamesquita.api_alunos.service.GenericService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class GenericController<T> {

    private final GenericService<T> service;

    protected GenericController(GenericService<T> service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody T entidade) {
        try {
            T salvo = service.salvar(entidade);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<T>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // ✅ Sem orElse — forma clara e direta
    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID codigo) {
        Optional<T> resultado = service.buscarPorId(codigo);
        
        if (resultado.isPresent()) {
            return ResponseEntity.ok(resultado.get());
        }
        
        String mensagem = "Registro com código " + codigo + " não encontrado";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> atualizar(@PathVariable UUID codigo, @RequestBody T entidade) throws IllegalArgumentException {
        try {
            T atualizado = service.atualizar(codigo, entidade);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> excluir(@PathVariable UUID codigo) {
        try {
            service.excluir(codigo);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{codigo}/existe")
    public ResponseEntity<Boolean> existe(@PathVariable UUID codigo) {
        return ResponseEntity.ok(service.existe(codigo));
    }

    @GetMapping("/contar")
    public ResponseEntity<Long> contar() {
        return ResponseEntity.ok(service.contar());
    }
}