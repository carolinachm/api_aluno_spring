package br.com.carolinamesquita.api_alunos.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import br.com.carolinamesquita.api_alunos.model.AcompanhamentoModel;
import br.com.carolinamesquita.api_alunos.repository.AcompanhamentoRepository;

@Service
public class AcompanhamentoService extends GenericService<AcompanhamentoModel> {

    private final AcompanhamentoRepository acompanhamentoRepository;

    // Construtor correto injetando o repositório de Acompanhamento
    public AcompanhamentoService(AcompanhamentoRepository acompanhamentoRepository) {
        this.acompanhamentoRepository = acompanhamentoRepository;
    }

    @Override
    protected JpaRepository<AcompanhamentoModel, UUID> getRepository() {
        return acompanhamentoRepository;
    }

    // Copia os campos específicos do seu modelo AcompanhamentoModel
    @Override
    protected void copiarDados(AcompanhamentoModel origem, AcompanhamentoModel destino) {
        // Atualize com os campos reais da sua entidade:
        destino.setData(origem.getData());
        destino.setTexto(null);
        destino.setAluno(origem.getAluno());
        // adicione/remova conforme os atributos que existem em AcompanhamentoModel
    }

    // ==============================================
    // MÉTODOS HERDADOS DO GenericService (disponíveis)
    // ==============================================

    @Override
    public AcompanhamentoModel salvar(AcompanhamentoModel entidade) {
        // se quiser validação específica, adicione aqui: validar(entidade);
        return super.salvar(entidade);
    }

    @Override
    public List<AcompanhamentoModel> listarTodos() {
        return super.listarTodos();
    }

    @Override
    public Optional<AcompanhamentoModel> buscarPorId(UUID id) {
        return super.buscarPorId(id);
    }

    @Override
    public AcompanhamentoModel atualizar(UUID id, AcompanhamentoModel dadosAtualizados) {
        return super.atualizar(id, dadosAtualizados);
    }

    @Override
    public void excluir(UUID id) {
        super.excluir(id);
    }

    @Override
    public boolean existe(UUID id) {
        return super.existe(id);
    }

    @Override
    public long contar() {
        return super.contar();
    }

    
}