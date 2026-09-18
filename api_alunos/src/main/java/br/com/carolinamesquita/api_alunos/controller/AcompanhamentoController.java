package br.com.carolinamesquita.api_alunos.controller;

import br.com.carolinamesquita.api_alunos.dto.AcompanhamentoDTO;
import br.com.carolinamesquita.api_alunos.model.AcompanhamentoModel;
import br.com.carolinamesquita.api_alunos.model.AlunoModel;
import br.com.carolinamesquita.api_alunos.service.AcompanhamentoService;
import br.com.carolinamesquita.api_alunos.service.AlunoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/acompanhamentos")
@CrossOrigin(origins = "*")
public class AcompanhamentoController extends GenericController<AcompanhamentoModel> {

    private final AcompanhamentoService acompanhamentoService;
    private final AlunoService alunoService;

    protected AcompanhamentoController(AcompanhamentoService acompanhamentoService, AlunoService alunoService) {
        this.acompanhamentoService = acompanhamentoService;
        this.alunoService = alunoService;
    }

    @Override
    protected AcompanhamentoService getService() {
        return acompanhamentoService;
    }

    // ==============================================
    //  CRUD — COMPLETO
    // ==============================================

    @PostMapping("/cadastrar")
    public ResponseEntity<?> criar(@RequestBody AcompanhamentoDTO dto) {
        // Validações
        if (dto.getData() == null) {
            return ResponseEntity.badRequest().body("A data é obrigatória");
        }
        if (dto.getTexto() == null || dto.getTexto().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("O texto/descrição é obrigatório");
        }
        if (dto.getTexto().length() < 5) {
            return ResponseEntity.badRequest().body("O texto deve ter pelo menos 5 caracteres");
        }
        if (dto.getCodigoAluno() == null) {
            return ResponseEntity.badRequest().body("O código do aluno é obrigatório");
        }
        if (!alunoService.existe(dto.getCodigoAluno())) {
            return ResponseEntity.badRequest().body("Aluno não encontrado com o código informado");
        }

        // Converte DTO → Model
        AcompanhamentoModel acompanhamento = new AcompanhamentoModel();
        acompanhamento.setData(dto.getData());
        acompanhamento.setTexto(dto.getTexto());
        
        Optional<AlunoModel> aluno = alunoService.buscarPorId(dto.getCodigoAluno());
        aluno.ifPresent(acompanhamento::setAluno);

        AcompanhamentoModel novo = acompanhamentoService.salvar(acompanhamento);
        URI uri = URI.create("/api/acompanhamentos/" + novo.getCodigo());
        return ResponseEntity.created(uri).body(novo);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<AcompanhamentoModel>> listarTodos() {
        return ResponseEntity.ok(acompanhamentoService.listarTodos());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID codigo) {
        Optional<AcompanhamentoModel> resultado = acompanhamentoService.buscarPorId(codigo);
        
        if (resultado.isPresent()) {
            return ResponseEntity.ok(resultado.get());
        }
        
        String mensagem = "Registro com código " + codigo + " não encontrado";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem);
    }

    @PutMapping("/atualizar/{codigo}")
    public ResponseEntity<?> alterar(@PathVariable UUID codigo, @RequestBody AcompanhamentoDTO dto) {
        if (!acompanhamentoService.existe(codigo)) {
            return ResponseEntity.notFound().build();
        }
        if (dto.getData() == null) {
            return ResponseEntity.badRequest().body("A data é obrigatória");
        }
        if (dto.getTexto() == null || dto.getTexto().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("O texto/descrição é obrigatório");
        }
        if (dto.getTexto().length() < 5) {
            return ResponseEntity.badRequest().body("O texto deve ter pelo menos 5 caracteres");
        }
        if (dto.getCodigoAluno() == null || !alunoService.existe(dto.getCodigoAluno())) {
            return ResponseEntity.badRequest().body("Aluno não encontrado");
        }

        // Converte DTO → Model
        AcompanhamentoModel dadosAtualizados = new AcompanhamentoModel();
        dadosAtualizados.setCodigo(codigo);
        dadosAtualizados.setData(dto.getData());
        dadosAtualizados.setTexto(dto.getTexto());
        alunoService.buscarPorId(dto.getCodigoAluno()).ifPresent(dadosAtualizados::setAluno);

        AcompanhamentoModel atualizado = acompanhamentoService.atualizar(codigo, dadosAtualizados);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/remover/{codigo}")
    public ResponseEntity<?> remover(@PathVariable UUID codigo) {
        if (!acompanhamentoService.existe(codigo)) {
            return ResponseEntity.notFound().build();
        }
        acompanhamentoService.excluir(codigo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{codigo}/existe")
    public ResponseEntity<Boolean> existe(@PathVariable UUID codigo) {
        return ResponseEntity.ok(acompanhamentoService.existe(codigo));
    }

    @GetMapping("/contar")
    public ResponseEntity<Long> contar() {
        return ResponseEntity.ok(acompanhamentoService.contar());
    }

    
}