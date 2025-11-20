package steamboxd.repository;

import java.util.ArrayList;
import java.util.List;
import steamboxd.model.Jogo;

/**
 * Implementação concreta do {@link Repository} para gerenciar objetos {@link Jogo}.
 *
 * <p>Esta classe armazena os Jogos em uma lista em memória e é responsável
 * pelas operações de CRUD sobre essa lista</p>
 */
public class JogoRepository implements Repository<Jogo> {

    private List<Jogo> jogos;

    public JogoRepository() {
        this.jogos = new ArrayList<>();
    }

    @Override
    public void adicionar(Jogo jogo) {
        if (jogo != null && !existe(jogo.getTitulo())) {
            jogos.add(jogo);
        }
    }

    @Override
    public boolean remover(String titulo) {
        return jogos.removeIf(jogo -> jogo.getTitulo().equalsIgnoreCase(titulo));
    }

    @Override
    public Jogo buscar(String titulo) {
        for (Jogo jogo : jogos) {
            if (jogo.getTitulo().equalsIgnoreCase(titulo)) {
                return jogo;
            }
        }
        return null;
    }

    @Override
    public List<Jogo> listarTodos() {
        return new ArrayList<>(jogos);
    }

    @Override
    public boolean existe(String titulo) {
        return buscar(titulo) != null;
    }

    @Override
    public void carregarDados(List<Jogo> novosDados) {
        this.jogos.clear();
        this.jogos.addAll(novosDados);
    }
}