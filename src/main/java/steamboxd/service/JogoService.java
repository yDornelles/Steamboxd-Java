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
        if (jogo.getTitulo() != null) {
            jogo.setTitulo(jogo.getTitulo().trim());
        }
        if (jogo.getDesenvolvedora() != null) {
            jogo.setDesenvolvedora(jogo.getDesenvolvedora().trim());
        }
        if (jogo.getTitulo() == null || jogo.getTitulo().isBlank()) {
            throw new IllegalArgumentException("O título do jogo é obrigatório.");
        }
        if (jogo.getAnoLancamento() > 2025) {
            throw new IllegalArgumentException("O ano de lançamento deve ser válido (até 2025).");
        }
        if (jogo.getAnoLancamento() != 0 && jogo.getAnoLancamento() < 1950) {
            throw new IllegalArgumentException("Ano inválido (muito antigo).");
        }
        if (jogo.getPreco() < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }
        repository.adicionar(jogo);
    }

    @Override
    public boolean remover(String titulo) {
        boolean removido = repository.remover(titulo);

        if (removido) {
            Sistema.getInstance().getUsuarioRepository().removerMidiaDeTodos(titulo);
        }
        return removido;
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
    public boolean editarAno(String titulo, int novoAno) {
        if (novoAno > 2025) {
            throw new IllegalArgumentException("O ano de lançamento deve ser válido (até 2025).");
        }
        if (novoAno != 0 && novoAno < 1950) {
            throw new IllegalArgumentException("Ano inválido (muito antigo).");
        }

        Jogo jogo = repository.buscar(titulo);
        if (jogo != null) {
            jogo.setAnoLancamento(novoAno);
            return true;
        }
        return false;
    }

    @Override
    public boolean editarPreco(String titulo, double novoPreco) {
        if (novoPreco < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }

        Jogo jogo = repository.buscar(titulo);
        if (jogo != null) {
            jogo.setPreco(novoPreco);
            return true;
        }
        return false;
    }

    public boolean editarDesenvolvedora(String titulo, String novaDev) {
        Jogo jogo = repository.buscar(titulo);
        if (jogo != null) {
            if (novaDev == null || novaDev.isBlank()) {
                jogo.setDesenvolvedora("");
            } else {
                jogo.setDesenvolvedora(novaDev.trim());
            }
            return true;
        }
        return false;
    }

    public boolean editarMultiplayer(String titulo, boolean novoStatus) {
        Jogo jogo = repository.buscar(titulo);
        if (jogo != null) {
            jogo.setMultiplayer(novoStatus);
            return true;
        }
        return false;
    }

    @Override
    public boolean adicionarGenero(String titulo, String genero) {
        if (genero != null) {
            genero = genero.trim();
        }
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
        if (plataforma != null) {
            plataforma = plataforma.trim();
        }
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

    public boolean atualizarGeneros(String titulo, List<String> novosGeneros) {
        var midia = repository.buscar(titulo);
        if (midia != null) {
            midia.setGeneros(novosGeneros);
            return true;
        }
        return false;
    }

    public boolean atualizarPlataformas(String titulo, List<String> novasPlataformas) {
        var midia = repository.buscar(titulo);
        if (midia != null) {
            midia.setPlataformas(novasPlataformas);
            return true;
        }
        return false;
    }
}