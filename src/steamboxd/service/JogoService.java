package steamboxd.service;

import steamboxd.data.Sistema;
import steamboxd.model.Jogo;
import steamboxd.repository.JogoRepository;
import java.util.List;

/**
 * Implementação concreta do {@link MidiaService} para {@link Jogo}.
 *
 * <p>Esta classe contém a lógica de negócio específica para DLCs e orquestra o {@link JogoRepository}.</p>
 */
public class JogoService implements MidiaService<Jogo> {

    private JogoRepository repository;

    public JogoService() {
        this.repository = Sistema.getInstance().getJogoRepository();
    }

    @Override
    public void adicionar(Jogo jogo) {
        repository.adicionar(jogo);
    }

    @Override
    public boolean remover(String titulo) {
        return repository.remover(titulo);
    }

    @Override
    public Jogo buscar(String titulo) {
        return repository.buscar(titulo);
    }

    @Override
    public List<Jogo> listarTodos() {
        return repository.listarTodos();
    }

    @Override
    public boolean editarNota(String titulo, double novaNota) {
        Jogo jogo = repository.buscar(titulo);
        if (jogo != null) {
            jogo.setNota(novaNota);
            return true;
        }
        return false;
    }

    @Override
    public boolean editarAno(String titulo, int novoAno) {
        Jogo jogo = repository.buscar(titulo);
        if (jogo != null) {
            jogo.setAnoLancamento(novoAno);
            return true;
        }
        return false;
    }

    @Override
    public boolean editarPreco(String titulo, double novoPreco) {
        Jogo jogo = repository.buscar(titulo);
        if (jogo != null) {
            jogo.setPreco(novoPreco);
            return true;
        }
        return false;
    }

    @Override
    public boolean adicionarGenero(String titulo, String genero) {
        if (genero == null || genero.isBlank()) {
            return false;
        }
        Jogo jogo = repository.buscar(titulo);
        if (jogo == null) {
            return false;
        }
        List<String> generosAtuais = jogo.getGeneros();
        generosAtuais.add(genero);
        jogo.setGeneros(generosAtuais);
        return true;
    }

    @Override
    public boolean adicionarPlataforma(String titulo, String plataforma) {
        if (plataforma == null || plataforma.isBlank()) {
            return false;
        }
        Jogo jogo = repository.buscar(titulo);
        if (jogo == null) {
            return false;
        }
        List<String> plataformasAtuais = jogo.getPlataformas();
        plataformasAtuais.add(plataforma);
        jogo.setPlataformas(plataformasAtuais);
        return true;
    }
}