package steamboxd.service;

import steamboxd.data.Sistema;
import steamboxd.model.DLC;
import steamboxd.model.Jogo;
import steamboxd.model.Usuario;
import steamboxd.model.Midia;
import steamboxd.repository.UsuarioRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação concreta do {@link MidiaService} para {@link Usuario}.
 *
 * <p>Esta classe contém a lógica de negócio específica para DLCs e orquestra o {@link UsuarioRepository}.</p>
 */
public class UsuarioService {

    private UsuarioRepository repository;

    public UsuarioService() {
        this.repository = Sistema.getInstance().getUsuarioRepository();
    }

    public boolean adicionarUsuario(Usuario usuario) {
        if (usuario.getNome() != null) {
            usuario.setNome(usuario.getNome().trim());
        }
        if (usuario.getEmail() != null) {
            usuario.setEmail(usuario.getEmail().trim());
        }
        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do usuário é obrigatório.");
        }
        if (usuario.getNome().matches(".*\\d.*")) {
            throw new IllegalArgumentException("O nome do usuário não pode conter números.");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException("O email do usuário é obrigatório.");
        }
        if (!usuario.getEmail().contains("@gmail.com")) {
            throw new IllegalArgumentException("O email deve ser válido (conter '@gmail.com').");
        }
        if (usuario.getEmail().contains(" ")) {
            throw new IllegalArgumentException("O email não pode conter espaços.");
        }

        if (repository.existe(usuario.getEmail())) {
            return false;
        }
        repository.adicionar(usuario);
        return true;
    }

    public boolean removerUsuario(String email) {
        return repository.remover(email);
    }

    public Usuario buscarUsuario(String email) {
        return repository.buscar(email);
    }

    public List<Usuario> listarUsuarios() {
        return repository.listarTodos();
    }

    public boolean editarUsuario(String emailAtual, String novoNome, String novoEmail) {
        Usuario user = repository.buscar(emailAtual);
        if (user == null) {
            return false;
        }

        if (novoNome != null) {
            novoNome = novoNome.trim();
            if (novoNome.isBlank()) {
                throw new IllegalArgumentException("O novo nome não pode ser vazio.");
            }
            if (novoNome.matches(".*\\d.*")) {
                throw new IllegalArgumentException("O nome do usuário não pode conter números.");
            }
            user.setNome(novoNome);
        }

        if (novoEmail != null && !novoEmail.equalsIgnoreCase(emailAtual)) {
            novoEmail = novoEmail.trim();
            if (novoEmail.isBlank()) {
                throw new IllegalArgumentException("O novo email não pode ser vazio.");
            }
            if (!novoEmail.contains("@gmail.com")) {
                throw new IllegalArgumentException("O novo email deve ser válido (conter '@gmail.com').");
            }
            if (novoEmail.contains(" ")) {
                throw new IllegalArgumentException("O email não pode conter espaços.");
            }

            if (repository.existe(novoEmail)) {
                return false;
            }
            user.setEmail(novoEmail);
        }
        return true;
    }

    public boolean adicionarMidia(String email, Midia midia) {
        Usuario user = repository.buscar(email);
        if (user != null && midia != null) {

            List<Midia> bibliotecaAtual = user.getBiblioteca();
            for (Midia m : bibliotecaAtual) {
                if (m.getTitulo().equalsIgnoreCase(midia.getTitulo())) {
                    return false;
                }
            }

            Midia midiaClone;

            if (midia instanceof Jogo) {
                midiaClone = new Jogo((Jogo) midia);
            } else if (midia instanceof DLC) {
                midiaClone = new DLC((DLC) midia);
            } else {
                throw new IllegalArgumentException("Tipo de Mídia desconhecido para clonagem: " + midia.getClass());
            }

            List<Midia> biblioteca = user.getBiblioteca();
            biblioteca.add(midiaClone);
            user.setBiblioteca(biblioteca);
            return true;
        }
        return false;
    }

    public boolean removerMidia(String email, String tituloMidia) {
        Usuario user = repository.buscar(email);
        if (user != null) {
            List<Midia> biblioteca = user.getBiblioteca();
            boolean removido = biblioteca.removeIf(midia -> midia.getTitulo().equalsIgnoreCase(tituloMidia));
            user.setBiblioteca(biblioteca);
            return removido;
        }
        return false;
    }

    public boolean editarMidia(String email, String titulo, Double novaNota) {
        Usuario user = repository.buscar(email);
        if (user == null){
            return false;
        }
        Midia midia = buscarMidia(email, titulo);

        if (midia == null) {
            return false;
        }

        if (novaNota != null) {
            if (novaNota >= 0 && novaNota <= 10) {
                midia.setNota(novaNota);
            } else {
                return false;
            }
        }
        return true;
    }

    public Midia buscarMidia(String email, String tituloMidia) {
        Usuario user = repository.buscar(email);
        if (user != null) {
            for (Midia midia : user.getBiblioteca()) {
                if (midia.getTitulo().equalsIgnoreCase(tituloMidia)) {
                    return midia;
                }
            }
        }
        return null;
    }

    public List<Midia> listarBiblioteca(String email) {
        Usuario user = repository.buscar(email);
        if (user == null) {
            return List.of();
        }
        return new ArrayList<>(user.getBiblioteca());
    }
}
