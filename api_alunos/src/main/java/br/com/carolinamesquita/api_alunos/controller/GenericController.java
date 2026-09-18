package br.com.carolinamesquita.api_alunos.controller;

import br.com.carolinamesquita.api_alunos.service.GenericService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class GenericController<T> {

    protected abstract GenericService<T> getService();
    
    protected UUID getCodigo(T entidade) {
        try {
            return (UUID) entidade.getClass().getMethod("getCodigo").invoke(entidade);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter o código", e);
        }
    }
    
    protected void setCodigo(T entidade, UUID codigo) {
        try {
            entidade.getClass().getMethod("setCodigo", UUID.class).invoke(entidade, codigo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao definir o código", e);
        }
    }

    // REMOVEMOS @PostMapping, @PutMapping, etc. daqui
    // Apenas a lógica, sem mapeamento de rota
    protected ResponseEntity<?> criarInterno(T entidade) {
        T novaEntidade = getService().salvar(entidade);
        URI uri = URI.create("/api/" + getNomeRecurso() + "/" + getCodigo(novaEntidade));
        return ResponseEntity.created(uri).body(novaEntidade);
    }

    protected ResponseEntity<List<T>> listarTodosInterno() {
        return ResponseEntity.ok(getService().listarTodos());
    }

    protected ResponseEntity<?> buscarPorIdInterno(UUID codigo) {
        Optional<T> resultado = getService().buscarPorId(codigo);
        if (resultado.isPresent()) {
            return ResponseEntity.ok(resultado.get());
        }
        return ResponseEntity.notFound().build();
    }

    protected ResponseEntity<?> alterarInterno(UUID codigo, T entidade) {
        if (!getService().existe(codigo)) {
            return ResponseEntity.notFound().build();
        }
        setCodigo(entidade, codigo);
        return ResponseEntity.ok(getService().atualizar(codigo, entidade));
    }

    protected ResponseEntity<?> removerInterno(UUID codigo) {
        if (!getService().existe(codigo)) {
            return ResponseEntity.notFound().build();
        }
        getService().excluir(codigo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{codigo}/existe")
    public ResponseEntity<Boolean> existe(@PathVariable UUID codigo) {
        return ResponseEntity.ok(getService().existe(codigo));
    }

    @GetMapping("/contar")
    public ResponseEntity<Long> contar() {
        return ResponseEntity.ok(getService().contar());
    }

    private String getNomeRecurso() {
        return this.getClass().getSimpleName().replace("Controller", "").toLowerCase() + "s";
    }
}