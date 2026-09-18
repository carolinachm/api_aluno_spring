package br.com.carolinamesquita.api_alunos.service;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class GenericService<T> {

    protected abstract JpaRepository<T, UUID> getRepository();

    public T salvar(T entidade) {
        return getRepository().save(entidade);
    }

    public List<T> listarTodos() {
        return getRepository().findAll();
    }

    public Optional<T> buscarPorId(UUID id) {
        return getRepository().findById(id);
    }

    // Adicionado: método genérico de atualização
    public T atualizar(UUID id, T dadosAtualizados) {
        return getRepository().findById(id)
            .map(entidadeExistente -> {
                copiarDados(dadosAtualizados, entidadeExistente);
                return getRepository().save(entidadeExistente);
            })
            .orElseThrow(() -> new RuntimeException("Registro não encontrado com código: " + id));
    }

    //  Adicionado: método genérico de exclusão
    public void excluir(UUID id) {
        if (!getRepository().existsById(id)) {
            throw new RuntimeException("Registro não encontrado com código: " + id);
        }
        getRepository().deleteById(id);
    }

    public boolean existe(UUID id) {
        return getRepository().existsById(id);
    }

    public long contar() {
        return getRepository().count();
    }

    //  Método auxiliar para sobrescrever nas filhas
    protected void copiarDados(T origem, T destino) {
        // Implementação nas classes filhas
    }
}