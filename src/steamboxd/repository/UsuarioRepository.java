package steamboxd.repository;

import java.util.ArrayList;
import java.util.List;
import steamboxd.model.Usuario;
import steamboxd.model.Midia;

/**
 * Implementação concreta do {@link Repository} para gerenciar objetos {@link Usuario}.
 *
 * <p>Esta classe armazena os Usuarios em uma lista em memória e é responsável
 * pelas operações de CRUD sobre essa lista</p>
 */
public class UsuarioRepository implements Repository<Usuario> {

    private List<Usuario> usuarios;

    public UsuarioRepository() {
        this.usuarios = new ArrayList<>();
    }

    @Override
    public void adicionar(Usuario usuario) {
        if (usuario != null && !existe(usuario.getEmail())) {
            usuarios.add(usuario);
        }
    }

    @Override
    public boolean remover(String email) {
        return usuarios.removeIf(usuario -> usuario.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public Usuario buscar(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equalsIgnoreCase(email)) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }

    @Override
    public boolean existe(String email) {
        return buscar(email) != null;
    }

    @Override
    public void carregarDados(List<Usuario> novosDados) {
        this.usuarios.clear();
        this.usuarios.addAll(novosDados);
    }

    /**
     * Remove uma mídia (pelo título) da biblioteca de TODOS os usuários.
     * Usado quando uma mídia é deletada da loja.
     */
    public void removerMidiaDeTodos(String tituloMidia) {
        for (Usuario usuario : usuarios) {
            List<Midia> biblioteca = usuario.getBiblioteca(); //

            boolean alterou = biblioteca.removeIf(m -> m.getTitulo().equalsIgnoreCase(tituloMidia));

            if (alterou) {
                usuario.setBiblioteca(biblioteca); //
            }
        }
    }
}
