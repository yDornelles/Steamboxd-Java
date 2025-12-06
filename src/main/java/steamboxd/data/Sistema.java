package steamboxd.data;

import steamboxd.data.dao.PersistenciaDAO;
import steamboxd.data.dao.TxtDAO;
import steamboxd.repository.DLCRepository;
import steamboxd.repository.JogoRepository;
import steamboxd.repository.UsuarioRepository;
import java.io.File;

/**
 * Padrão Singleton para centralizar o acesso aos dados.
 * Garante que existe apenas UMA instância de cada repositório,
 * para que todos os controllers e services acessem a mesma lista de dados.
 */
public class Sistema {

    private static Sistema instance;

    // Os repositórios são criados uma única vez.
    private final JogoRepository jogoRepository;
    private final DLCRepository dlcRepository;
    private final UsuarioRepository usuarioRepository;

    private final PersistenciaDAO dao;
    private String arquivoAtual = "steamboxd.txt";

    /**
     * Construtor privado para impedir a criação de novas instâncias
     * fora desta classe.
     */
    private Sistema() {
        this.jogoRepository = new JogoRepository();
        this.dlcRepository = new DLCRepository();
        this.usuarioRepository = new UsuarioRepository();
        this.dao = new TxtDAO();
    }

    /**
     * Ponto de acesso global para a única instância do Sistema.
     * @return A instância única do Sistema.
     */
    public static synchronized Sistema getInstance() {
        if (instance == null) {
            instance = new Sistema();
        }
        return instance;
    }

    // --- MÉTODOS DE PERSISTÊNCIA ---

    public void setArquivoAtual(String caminho) {
        this.arquivoAtual = caminho;
    }

    /**
     * Coleta todos os dados dos repositórios,
     * empacota e solicita ao DAO para salvar.
     */
    public void salvarDados() {
        try {
            DadosSistema dados = new DadosSistema(
                    jogoRepository.listarTodos(),
                    dlcRepository.listarTodos(),
                    usuarioRepository.listarTodos()
            );
            dao.salvar(dados, arquivoAtual);
            System.out.println("Dados salvos com sucesso em " + arquivoAtual);

        } catch (Exception e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Usa o arquivo padrão ou o último utilizado.
     */
    public void carregarDados() {
        carregarDados(this.arquivoAtual);
    }

    /**
     * Solicita ao DAO para carregar os dados
     * e os distribui para os repositórios.
     * Carrega de um caminho específico.
     */
    public void carregarDados(String caminho) {
        this.arquivoAtual = caminho;
        File arquivo = new File(caminho);

        // Lida com o cenário de "primeira execução":
        // Se o arquivo de save não existe, não é um erro.
        // Apenas starto o sistema com os repositórios vazios.
        if (!arquivo.exists()) {
            System.out.println("Arquivo de save (" + caminho + ") não encontrado. Começando com dados vazios.");
            return;
        }
        try {
            DadosSistema dados = dao.carregar(caminho);

            jogoRepository.carregarDados(dados.getJogos());
            dlcRepository.carregarDados(dados.getDlcs());
            usuarioRepository.carregarDados(dados.getUsuarios());

            System.out.println("Dados carregados com sucesso de " + caminho);

        } catch (Exception e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Getters públicos para que os Services possam acessar os repositórios

    public JogoRepository getJogoRepository() {
        return jogoRepository;
    }

    public DLCRepository getDlcRepository() {
        return dlcRepository;
    }

    public UsuarioRepository getUsuarioRepository() {
        return usuarioRepository;
    }
}