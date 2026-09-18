package br.com.carolinamesquita.api_alunos.service;

import br.com.carolinamesquita.api_alunos.model.AlunoModel;
import br.com.carolinamesquita.api_alunos.repository.AlunoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AlunoService extends GenericService<AlunoModel> {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @Override
    protected JpaRepository<AlunoModel, UUID> getRepository() {
        return alunoRepository;
    }

    // Copia os campos específicos de Aluno
    @Override
    protected void copiarDados(AlunoModel origem, AlunoModel destino) {
        destino.setNome(origem.getNome());
        destino.setNota1(origem.getNota1());
        destino.setNota2(origem.getNota2());
    }

    // ==============================================
    // 🎓 MÉTODOS ESPECÍFICOS DE ALUNO
    // ==============================================

    public AlunoModel salvar(AlunoModel aluno) {
        validar(aluno);
        return super.salvar(aluno);
    }

    public Double calcularMedia(UUID codigo) {
        AlunoModel aluno = buscarPorId(codigo)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        
        if (aluno.getNota1() == null || aluno.getNota2() == null) {
            throw new IllegalStateException("Notas incompletas para cálculo da média");
        }
        
        return (aluno.getNota1() + aluno.getNota2()) / 2;
    }

    public boolean estaAprovado(UUID codigo) {
        return calcularMedia(codigo) >= 6.0;
    }

    public List<AlunoModel> listarAprovados() {
        return alunoRepository.findAll()
            .stream()
            .filter(a -> a.getNota1() != null && a.getNota2() != null)
            .filter(a -> (a.getNota1() + a.getNota2()) / 2 >= 6.0)
            .toList();
    }

    private void validar(AlunoModel aluno) {
        if (aluno.getNome() == null || aluno.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do aluno é obrigatório");
        }
        if (aluno.getNome().length() < 3) {
            throw new IllegalArgumentException("O nome deve ter pelo menos 3 caracteres");
        }
        if (aluno.getNota1() != null && (aluno.getNota1() < 0 || aluno.getNota1() > 10)) {
            throw new IllegalArgumentException("Nota 1 deve estar entre 0 e 10");
        }
        if (aluno.getNota2() != null && (aluno.getNota2() < 0 || aluno.getNota2() > 10)) {
            throw new IllegalArgumentException("Nota 2 deve estar entre 0 e 10");
        }
    }
}