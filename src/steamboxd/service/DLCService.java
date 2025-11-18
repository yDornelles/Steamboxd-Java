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
        DLC dlc = repository.buscar(titulo);
        if (dlc != null) {
            dlc.setAnoLancamento(novoAno);
            return true;
        }
        return false;
    }

    @Override
    public boolean editarPreco(String titulo, double novoPreco) {
        DLC dlc = repository.buscar(titulo);
        if (dlc != null) {
            dlc.setPreco(novoPreco);
            return true;
        }
        return false;
    }

    @Override
    public boolean adicionarGenero(String titulo, String genero) {
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