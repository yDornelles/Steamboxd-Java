package steamboxd.controller;

import steamboxd.model.DLC;
import steamboxd.service.DLCService;
import java.util.List;

/**
 * Controller para operações relacionadas a DLCs.
 *
 * <p>Esta classe faz a ponte entre a camada View
 * e a camada de Serviço ({@link DLCService}).</p>
 *
 * <p>Sua responsabilidade é:
 * 1. Receber dados brutos da View.
 * 2. Orquestrar a execução da lógica de negócio (chamando o Service).
 * 3. Retornar os resultados (ou status) para a View.</p>
 */
public class DLCController {

    private final DLCService dlcService = new DLCService();

    public DLCController() {
    }

    /**
     * Cria um novo objeto DLC a partir dos dados brutos da View
     * e o envia para a camada de serviço para adição.
     */
    public void adicionarDLC(String titulo, double nota, int ano, List<String> generos,
                             List<String> plataformas, String jogoBaseTitulo, boolean expansao, double preco) {

        DLC dlc = new DLC(titulo, generos, nota, ano, plataformas, jogoBaseTitulo, expansao, preco);

        dlcService.adicionar(dlc);
    }

    public boolean removerDLC(String titulo) {
        return dlcService.remover(titulo);
    }

    public DLC buscarDLC(String titulo) {
        return dlcService.buscar(titulo);
    }

    public List<DLC> listarDLCs() {
        return dlcService.listarTodos();
    }

    public boolean editarJogoBase(String tituloDlc, String novoJogoBase) {
        return dlcService.editarJogoBase(tituloDlc, novoJogoBase);
    }

    public boolean editarExpansao(String titulo, boolean novaExpansao) {
        return dlcService.editarExpansao(titulo, novaExpansao);
    }

    public boolean editarAno(String titulo, int novoAno) {
        return dlcService.editarAno(titulo, novoAno);
    }

    public boolean editarPreco(String titulo, double novoPreco) {
        return dlcService.editarPreco(titulo, novoPreco);
    }

    public boolean adicionarGenero(String titulo, String genero) {
        return dlcService.adicionarGenero(titulo, genero);
    }

    public boolean adicionarPlataforma(String titulo, String plataforma) {
        return dlcService.adicionarPlataforma(titulo, plataforma);
    }

    public void atualizarGeneros(String titulo, List<String> novosGeneros) {
        dlcService.atualizarGeneros(titulo, novosGeneros);
    }

    public void atualizarPlataformas(String titulo, List<String> novasPlataformas) {
        dlcService.atualizarPlataformas(titulo, novasPlataformas);
    }
}
