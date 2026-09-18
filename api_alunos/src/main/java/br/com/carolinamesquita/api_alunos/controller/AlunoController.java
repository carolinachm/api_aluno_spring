package br.com.carolinamesquita.api_alunos.controller;

import br.com.carolinamesquita.api_alunos.model.AlunoModel;
import br.com.carolinamesquita.api_alunos.service.AlunoService;
import br.com.carolinamesquita.api_alunos.service.GenericService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/alunos")
@CrossOrigin(origins = "*")
public class AlunoController extends GenericController<AlunoModel> {

    private final AlunoService alunoService;

    protected AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @Override
    protected GenericService<AlunoModel> getService() {
        return alunoService;
    }

    // ==============================================
    //  CRUD — TODOS os métodos, COMPLETOS
    // ==============================================

    @PostMapping("/cadastrar")
    public ResponseEntity<?> criar(@RequestBody AlunoModel aluno) {
        if (aluno.getNome() == null || aluno.getNome().isEmpty()) {
            return ResponseEntity.badRequest().body("Informe um nome válido");
        }
        if (aluno.getNota1() < 0 || aluno.getNota1() > 10 || aluno.getNota2() < 0 || aluno.getNota2() > 10) {
            return ResponseEntity.badRequest().body("Verifique as notas");
        }
        AlunoModel novoAluno = alunoService.salvar(aluno);
        URI uri = URI.create("/api/alunos/" + novoAluno.getCodigo());
        return ResponseEntity.created(uri).body(novoAluno);
    }

    @GetMapping("/listar")
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

    @PutMapping("/atualizar/{codigo}")
    public ResponseEntity<?> alterar(@PathVariable UUID codigo, @RequestBody AlunoModel aluno) {
        if (!alunoService.existe(codigo)) {
            return ResponseEntity.notFound().build();
        }
        if (aluno.getNome() == null || aluno.getNome().isEmpty()) {
            return ResponseEntity.badRequest().body("Informe um nome válido");
        }
        if (aluno.getNota1() < 0 || aluno.getNota1() > 10 || aluno.getNota2() < 0 || aluno.getNota2() > 10) {
            return ResponseEntity.badRequest().body("Verifique as notas");
        }
        aluno.setCodigo(codigo);
        AlunoModel atualizado = alunoService.atualizar(codigo, aluno);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/remover/{codigo}")
    public ResponseEntity<?> remover(@PathVariable UUID codigo) {
        if (!alunoService.existe(codigo)) {
            return ResponseEntity.notFound().build();
        }
        alunoService.excluir(codigo);
        return ResponseEntity.ok().build();
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
    // 🎓 MÉTODOS ESPECÍFICOS — COMPLETOS
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