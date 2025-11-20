package steamboxd.service;

import java.util.List;

/**
 * Interface genérica que define o contrato da camada de Serviço
 * para qualquer entidade do tipo {@link main.java.steamboxd.model.Midia}.
 *
 * <p>Esta interface abstrai a lógica de negócio. Ela garante que todos
 * os serviços forneçam as mesmas operações básicas, permitindo que os Controllers interajam
 * com eles de forma consistente.</p>
 *
 * @param <T> O tipo do Modelo que este serviço gerencia.
 */
public interface MidiaService<T> {

    void adicionar(T item);

    boolean remover(String titulo);

    T buscar(String titulo);

    List<T> listarTodos();

    boolean editarNota(String titulo, double novaNota);

    boolean editarAno(String titulo, int novoAno);

    boolean editarPreco(String titulo, double novoPreco);

    boolean adicionarGenero(String titulo, String genero);

    boolean adicionarPlataforma(String titulo, String plataforma);
}
