package steamboxd.repository;

import java.util.List;

/**
 * Interface genérica que define o contrato do Padrão Repository.
 *
 * <p>O Padrão Repository tem como objetivo abstrair
 * a camada de acesso aos dados das regras de negócio.</p>
 *
 * <p>Esta interface define as operações básicas de CRUD que todo repositório de dados deve fornecer.</p>
 * @param <T> O tipo do Modelo que o repositório gerencia.
 */
public interface Repository<T> {

    void adicionar(T item);

    boolean remover(String chave);

    T buscar(String chave);

    List<T> listarTodos();

    boolean existe(String chave);

    void carregarDados(List<T> novosDados);
}
