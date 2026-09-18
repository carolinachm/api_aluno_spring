package br.com.carolinamesquita.api_alunos.controller;

import br.com.carolinamesquita.api_alunos.model.AlunoModel;
import br.com.carolinamesquita.api_alunos.service.AlunoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/alunos")
@CrossOrigin(origins = "*")
public class AlunoController {

    private final AlunoService alunoService;

    protected AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    // ==============================================
    // CRUD — MESMO PADRÃO DO GenericController
    // ==============================================

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody AlunoModel entidade) {
        try {
            AlunoModel salvo = alunoService.salvar(entidade);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<AlunoModel>> listarTodos() {
        return ResponseEntity.ok(alunoService.listarTodos());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID codigo) {
        Optional<AlunoModel> resultado = alunoService.buscarPorId(codigo);
        
        if (resultado.isPresent()) {
            return ResponseEntity.ok(resultado.get());
        }
        
        String mensagem = "Registro com código " + codigo + " não encontrado";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> atualizar(@PathVariable UUID codigo, @RequestBody AlunoModel entidade) throws IllegalArgumentException {
        try {
            AlunoModel atualizado = alunoService.atualizar(codigo, entidade);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> excluir(@PathVariable UUID codigo) {
        try {
            alunoService.excluir(codigo);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{codigo}/existe")
    public ResponseEntity<Boolean> existe(@PathVariable UUID codigo) {
        return ResponseEntity.ok(alunoService.existe(codigo));
    }

    @GetMapping("/contar")
    public ResponseEntity<Long> contar() {
        return ResponseEntity.ok(alunoService.contar());
    }

    // ==============================================
    // MÉTODOS ESPECÍFICOS DE ALUNO
    // ==============================================

    @GetMapping("/{codigo}/media")
    public ResponseEntity<?> calcularMedia(@PathVariable UUID codigo) {
        Double media = alunoService.calcularMedia(codigo);
        
        if (media != null) {
            return ResponseEntity.ok(media);
        }
        
        String mensagem = "Aluno com código " + codigo + " não encontrado";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem);
    }

    @GetMapping("/{codigo}/aprovado")
    public ResponseEntity<?> verificarAprovacao(@PathVariable UUID codigo) {
        Boolean aprovado = alunoService.estaAprovado(codigo);
        
        if (aprovado != null) {
            return ResponseEntity.ok(aprovado);
        }
        
        String mensagem = "Aluno com código " + codigo + " não encontrado";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem);
    }

    @GetMapping("/aprovados")
    public ResponseEntity<List<AlunoModel>> listarAprovados() {
        List<AlunoModel> aprovados = alunoService.listarAprovados();
        return ResponseEntity.ok(aprovados);
    }
}