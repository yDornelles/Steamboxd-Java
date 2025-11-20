package steamboxd.service;

import steamboxd.data.Sistema;
import steamboxd.model.DLC;
import steamboxd.model.Jogo;
import steamboxd.repository.DLCRepository;
import steamboxd.repository.JogoRepository;
import java.util.List;

/**
 * Implementação concreta do {@link MidiaService} para {@link DLC}.
 *
 * <p>Esta classe contém a lógica de negócio específica para DLCs e orquestra o {@link DLCRepository}.</p>
 */
public class DLCService implements MidiaService<DLC> {

    private DLCRepository repository;
    private JogoRepository jogoRepository;

    public DLCService() {
        this.repository = Sistema.getInstance().getDlcRepository();
        this.jogoRepository = Sistema.getInstance().getJogoRepository();
    }

    @Override
    public void adicionar(DLC dlc) {
        if (dlc.getTitulo() != null) {
            dlc.setTitulo(dlc.getTitulo().trim());
        }
        if (dlc.getJogoBaseTitulo() != null) {
            dlc.setJogoBaseTitulo(dlc.getJogoBaseTitulo().trim());
        }
        if (dlc.getTitulo() == null || dlc.getTitulo().isBlank()) {
            throw new IllegalArgumentException("O título da DLC é obrigatório.");
        }
        if (dlc.getAnoLancamento() > 2025) {
            throw new IllegalArgumentException("O ano de lançamento deve ser válido (até 2025).");
        }
        if (dlc.getAnoLancamento() != 0 && dlc.getAnoLancamento() < 1950) {
            throw new IllegalArgumentException("Ano inválido (muito antigo).");
        }
        if (dlc.getPreco() < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }

        repository.adicionar(dlc);

        String jogoTitulo = dlc.getJogoBaseTitulo();
        if (jogoTitulo != null && !jogoTitulo.isBlank()) {
            Jogo jogo = jogoRepository.buscar(jogoTitulo);
            if (jogo != null) {
                List<String> dlcTitulos = jogo.getDlcTitulos();
                if (!dlcTitulos.contains(dlc.getTitulo())) {
                    dlcTitulos.add(dlc.getTitulo());
                    jogo.setDlcTitulos(dlcTitulos);
                }
            }
        }
    }

    @Override
    public boolean remover(String titulo) {
        DLC dlc = repository.buscar(titulo);
        if (dlc == null) {
            return false;
        }

        boolean removido = repository.remover(titulo);

        if (removido) {
            String jogoTitulo = dlc.getJogoBaseTitulo();
            if (jogoTitulo != null && !jogoTitulo.isBlank()) {
                Jogo jogo = jogoRepository.buscar(jogoTitulo);
                if (jogo != null) {
                    List<String> dlcTitulos = jogo.getDlcTitulos();
                    dlcTitulos.remove(dlc.getTitulo());
                    jogo.setDlcTitulos(dlcTitulos);
                }
            }
            Sistema.getInstance().getUsuarioRepository().removerMidiaDeTodos(titulo);
        }
        return removido;
    }

    @Override
    public DLC buscar(String titulo) {
        return repository.buscar(titulo);
    }

    @Override
    public List<DLC> listarTodos() {
        return repository.listarTodos();
    }

    @Override
    public boolean editarNota(String titulo, double novaNota) {
        DLC dlc = repository.buscar(titulo);
        if (dlc != null) {
            dlc.setNota(novaNota);
            return true;
        }
        return false;
    }

    @Override
    public boolean editarAno(String titulo, int novoAno) {
        if (novoAno > 2025) {
            throw new IllegalArgumentException("O ano de lançamento deve ser válido (até 2025).");
        }
        if (novoAno != 0 && novoAno < 1950) {
            throw new IllegalArgumentException("Ano inválido (muito antigo).");
        }

        DLC dlc = repository.buscar(titulo);
        if (dlc != null) {
            dlc.setAnoLancamento(novoAno);
            return true;
        }
        return false;
    }

    @Override
    public boolean editarPreco(String titulo, double novoPreco) {
        if (novoPreco < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }

        DLC dlc = repository.buscar(titulo);
        if (dlc != null) {
            dlc.setPreco(novoPreco);
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
        DLC dlc = repository.buscar(titulo);
        if (dlc == null) {
            return false;
        }
        List<String> generosAtuais = dlc.getGeneros();
        generosAtuais.add(genero);
        dlc.setGeneros(generosAtuais);
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
        DLC dlc = repository.buscar(titulo);
        if (dlc == null) {
            return false;
        }
        List<String> plataformasAtuais = dlc.getPlataformas();
        plataformasAtuais.add(plataforma);
        dlc.setPlataformas(plataformasAtuais);
        return true;
    }
}