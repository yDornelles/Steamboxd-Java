package steamboxd.service;

import steamboxd.data.Sistema;
import steamboxd.model.DLC;
import steamboxd.repository.DLCRepository;
import java.util.List;

/**
 * Implementação concreta do {@link MidiaService} para {@link DLC}.
 *
 * <p>Esta classe contém a lógica de negócio específica para DLCs e orquestra o {@link DLCRepository}.</p>
 */
public class DLCService implements MidiaService<DLC> {

    private DLCRepository repository;

    public DLCService() {
        this.repository = Sistema.getInstance().getDlcRepository();
    }

    @Override
    public void adicionar(DLC dlc) {
        repository.adicionar(dlc);
    }

    @Override
    public boolean remover(String titulo) {
        return repository.remover(titulo);
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