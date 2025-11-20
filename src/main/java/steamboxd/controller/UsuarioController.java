package steamboxd.controller;

import steamboxd.model.Midia;
import steamboxd.model.Usuario;
import steamboxd.service.UsuarioService;
import java.util.List;

/**
 * Controller para operações relacionadas a Usuários e suas Bibliotecas.
 *
 * <p>Esta classe faz a ponte entre a View e a camada de serviço
 * ({@link UsuarioService}).</p>
 *
 * <p>É responsável por duas frentes:
 * 1. Gerenciar o CRUD de objetos {@link Usuario}.
 * 2. Gerenciar o conteúdo da biblioteca ({@link Midia}) de um usuário.
 * </p>
 */
public class UsuarioController {

    private final UsuarioService usuarioService = new UsuarioService();

    public UsuarioController() {
    }

    // --- Gerenciamento de Usuário (CRUD) ---

    public boolean criarUsuario(String nome, String email) {
        Usuario usuario = new Usuario(nome, email);
        return usuarioService.adicionarUsuario(usuario);
    }

    public Usuario buscarUsuario(String email) {
        return usuarioService.buscarUsuario(email);
    }

    public boolean removerUsuario(String email) {
        return usuarioService.removerUsuario(email);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    public boolean editarUsuario(String emailAtual, String novoNome, String novoEmail) {
        return usuarioService.editarUsuario(emailAtual, novoNome, novoEmail);
    }

    // --- Gerenciamento da Biblioteca do Usuário ---

    public boolean adicionarMidiaAoUsuario(String emailUsuario, Midia midia) {
        return usuarioService.adicionarMidia(emailUsuario, midia);
    }

    public boolean removerMidiaDoUsuario(String emailUsuario, String tituloMidia) {
        return usuarioService.removerMidia(emailUsuario, tituloMidia);
    }

    public Midia buscarMidiaDoUsuario(String emailUsuario, String tituloMidia) {
        return usuarioService.buscarMidia(emailUsuario, tituloMidia);
    }

    public boolean editarMidiaDoUsuario(String emailUsuario, String titulo, Double novaNota) {
        return usuarioService.editarMidia(emailUsuario, titulo, novaNota);
    }

    public List<Midia> listarBibliotecaUsuario(String emailUsuario) {
        return usuarioService.listarBiblioteca(emailUsuario);
    }
}
