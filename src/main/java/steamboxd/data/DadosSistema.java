package steamboxd.data;

import steamboxd.model.DLC;
import steamboxd.model.Jogo;
import steamboxd.model.Usuario;
import java.io.Serializable;
import java.util.List;

/**
 * Um "contêiner" de dados projetado especificamente para persistência.
 *
 * <p>Esta classe agrupa todas as listas de dados principais do sistema
 * (jogos, DLCs, usuários) em um único objeto. Sua única finalidade
 * é facilitar o processo de salvamento e carregamento
 */
public class DadosSistema implements Serializable {

    private final List<Jogo> jogos;
    private final List<DLC> dlcs;
    private final List<Usuario> usuarios;

    public DadosSistema(List<Jogo> jogos, List<DLC> dlcs, List<Usuario> usuarios) {
        this.jogos = jogos;
        this.dlcs = dlcs;
        this.usuarios = usuarios;
    }

    public List<Jogo> getJogos() {
        return jogos;
    }

    public List<DLC> getDlcs() {
        return dlcs;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }
}