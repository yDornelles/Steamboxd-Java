package steamboxd.repository;

import java.util.ArrayList;
import java.util.List;
import steamboxd.model.DLC;

/**
 * Implementação concreta do {@link Repository} para gerenciar objetos {@link DLC}.
 *
 * <p>Esta classe armazena as DLCs em uma lista em memória e é responsável
 * pelas operações de CRUD sobre essa lista</p>
 */
public class DLCRepository implements Repository<DLC> {

    private List<DLC> dlcs;

    public DLCRepository() {
        this.dlcs = new ArrayList<>();
    }

    @Override
    public void adicionar(DLC dlc) {
        if (dlc != null && !existe(dlc.getTitulo())) {
            dlcs.add(dlc);
        }
    }

    @Override
    public boolean remover(String titulo) {
        return dlcs.removeIf(dlc -> dlc.getTitulo().equalsIgnoreCase(titulo));
    }

    @Override
    public DLC buscar(String titulo) {
        for (DLC dlc : dlcs) {
            if (dlc.getTitulo().equalsIgnoreCase(titulo)) {
                return dlc;
            }
        }
        return null;
    }

    @Override
    public List<DLC> listarTodos() {
        return new ArrayList<>(dlcs);
    }

    @Override
    public boolean existe(String titulo) {
        return buscar(titulo) != null;
    }

    @Override
    public void carregarDados(List<DLC> novosDados) {
        this.dlcs.clear();
        this.dlcs.addAll(novosDados);
    }
}
