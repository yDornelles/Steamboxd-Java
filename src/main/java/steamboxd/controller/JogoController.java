package steamboxd.controller;

import steamboxd.model.Jogo;
import steamboxd.service.JogoService;
import java.util.List;

/**
 * Controller para operações relacionadas a Jogos.
 *
 * <p>Esta classe faz a ponte entre a camada View
 * e a camada de Serviço ({@link JogoService}).</p>
 *
 * <p>Sua responsabilidade é:
 * 1. Receber dados brutos da View.
 * 2. Orquestrar a execução da lógica de negócio (chamando o Service).
 * 3. Retornar os resultados (ou status) para a View.</p>
 */
public class JogoController {

    private final JogoService jogoService = new JogoService();

    public JogoController() {
    }

    /**
     * Cria um novo objeto Jogo a partir dos dados brutos da View
     * e o envia para a camada de serviço para adição.
     */
    public void adicionarJogo(String titulo, double nota, int ano, List<String> generos, List<String> plataformas,
                              String desenvolvedora, boolean multiplayer, double preco) {

        Jogo jogo = new Jogo(titulo, generos, nota, ano, plataformas, desenvolvedora, multiplayer, preco);

        jogoService.adicionar(jogo);
    }

    public boolean removerJogo(String titulo) {
        return jogoService.remover(titulo);
    }

    public Jogo buscarJogo(String titulo) {
        return jogoService.buscar(titulo);
    }

    public List<Jogo> listarJogos() {
        return jogoService.listarTodos();
    }

    public boolean editarNota(String titulo, double novaNota) {
        return jogoService.editarNota(titulo, novaNota);
    }

    public boolean editarAno(String titulo, int novoAno) {
        return jogoService.editarAno(titulo, novoAno);
    }

    public boolean editarPreco(String titulo, double novoPreco) {
        return jogoService.editarPreco(titulo, novoPreco);
    }

    public boolean adicionarGenero(String titulo, String genero) {
        return jogoService.adicionarGenero(titulo, genero);
    }

    public boolean adicionarPlataforma(String titulo, String plataforma) {
        return jogoService.adicionarPlataforma(titulo, plataforma);
    }
}
