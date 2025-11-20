package steamboxd.view.textual;

import steamboxd.controller.DLCController;
import steamboxd.controller.JogoController;
import steamboxd.controller.UsuarioController;
import steamboxd.data.Sistema;
import steamboxd.util.ConsoleUtil;
import steamboxd.view.IAppView;
import java.util.List;
import steamboxd.model.Jogo;
import steamboxd.model.DLC;
import steamboxd.model.Midia;

/**
 * Produto Concreto.
 * Implementação da IAppView para uma interface textual.
 */
public class TextualAppView implements IAppView {

    /**
     * A View se conecta com os Controllers.
     */
    private final JogoController jogoController;
    private final DLCController dlcController;
    private final UsuarioController usuarioController;

    /**
     * A View precisa acessar o Sistema para Salvar/Carregar.
     */
    private final Sistema sistema;

    public TextualAppView() {
        this.jogoController = new JogoController();
        this.dlcController = new DLCController();
        this.usuarioController = new UsuarioController();
        this.sistema = Sistema.getInstance();
    }

    /**
     * Inicia e executa o loop principal da aplicação.
     * É responsável por:
     * <ul>
     * <li>Carregar os dados do sistema na inicialização.</li>
     * <li>Exibir o menu principal repetidamente.</li>
     * <li>Ler a entrada do usuário e despachar para os submenus apropriados.</li>
     * <li>Gerenciar o salvamento de dados.</li>
     * <li>Controlar a saída da aplicação.</li>
     * </ul>
     */
    @Override
    public void run() {
        System.out.println("=== Bem-vindo ao Steamboxd ===\n");

        sistema.carregarDados();

        boolean executando = true;
        while (executando) {
            exibirMenuPrincipal();
            int opcao = ConsoleUtil.lerInt("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    menuGerenciarJogos();
                    break;
                case 2:
                    menuGerenciarDLCs();
                    break;
                case 3:
                    menuGerenciarUsuarios();
                    break;
                case 4:
                    sistema.salvarDados();
                    break;
                case 0:
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

        System.out.println("Salvando dados antes de sair...");
        sistema.salvarDados();
        System.out.println("Até logo!");
    }

    /**
     * Menu Principal
     */
    private void exibirMenuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Gerenciar Jogos");
        System.out.println("2. Gerenciar DLCs");
        System.out.println("3. Gerenciar Usuários");
        System.out.println("4. Salvar Dados Manualmente");
        System.out.println("0. Sair e Salvar");
    }

    /**
     * Sub Menu 1
     */
    private void menuGerenciarJogos() {
        System.out.println("\n--- Gerenciar Jogos ---");
        System.out.println("1. Adicionar Jogo");
        System.out.println("2. Listar Jogos");
        System.out.println("3. Editar Jogo");
        System.out.println("4. Remover Jogo");
        System.out.println("0. Voltar");

        int opcao = ConsoleUtil.lerInt("Opção: ");

        switch (opcao) {
            case 1:
                adicionarJogo();
                break;
            case 2:
                listarJogos();
                break;
            case 3:
                editarJogo();
                break;
            case 4:
                removerJogo();
                break;
            case 0:
                return;
            default:
                System.out.println("Opção inválida.");
        }
    }

    /**
     * Gerencia o fluxo de interface de usuário para adicionar um novo jogo.
     */
    private void adicionarJogo() {
        System.out.println("\n-> Adicionar Novo Jogo");
        String titulo = ConsoleUtil.lerString("Título: ").trim();
        int ano = ConsoleUtil.lerIntOpcional("Ano de Lançamento: ", 0);
        List<String> generos = ConsoleUtil.lerListaStrings("Gêneros: ");
        List<String> plataformas = ConsoleUtil.lerListaStrings("Plataformas: ");
        String dev = ConsoleUtil.lerString("Desenvolvedora: ").trim();
        double preco = ConsoleUtil.lerDoubleOpcional("Preço (Enter para Grátis): ", 0.0);
        boolean multi = ConsoleUtil.lerBoolean("Tem multiplayer (s/n): ");

        try {
            jogoController.adicionarJogo(titulo, 0.0, ano, generos, plataformas, dev, multi, preco);
            System.out.println("\nJogo '" + titulo.trim() + "' adicionado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("\nErro de Validação: " + e.getMessage());
        }
    }

    /**
     * Gerencia o fluxo de interface para listar todos os jogos cadastrados.
     */
    private void listarJogos() {
        System.out.println("\n--- Lista de Jogos ---");
        var jogos = jogoController.listarJogos();

        if (jogos.isEmpty()) {
            System.out.println("Nenhum jogo cadastrado.");
            return;
        }

        for (var jogo : jogos) {
            String anoStr = (jogo.getAnoLancamento() == 0) ? "N/A" : String.valueOf(jogo.getAnoLancamento());

            String precoStr;
            if (jogo.getPreco() == 0.0) {
                precoStr = "Grátis";
            } else {
                precoStr = String.format("R$ %.2f", jogo.getPreco());
            }

            System.out.println("--------------------");
            System.out.println("Título: " + jogo.getTitulo() + " (" + anoStr + ")");
            System.out.println("Preço: " + precoStr);
            System.out.println("Desenvolvedora: " + (jogo.getDesenvolvedora().isEmpty() ? "N/A" : jogo.getDesenvolvedora()));

            String generosStr = jogo.getGeneros().isEmpty() ? "N/A" : String.join(", ", jogo.getGeneros());
            System.out.println("Gêneros: " + generosStr);

            String platStr = jogo.getPlataformas().isEmpty() ? "N/A" : String.join(", ", jogo.getPlataformas());
            System.out.println("Plataformas: " + platStr);

            System.out.println("Multiplayer: " + (jogo.isMultiplayer() ? "Sim" : "Não"));
            System.out.println("DLCs cadastradas: " + jogo.getDlcTitulos().size());
        }
        System.out.println("--------------------");
    }

    /**
     * Gerencia o fluxo de interface para editar um jogo cadastrado.
     */
    private void editarJogo() {
        System.out.println("\n-> Editar Jogo");

        String titulo = ConsoleUtil.lerString("Digite o título do jogo a editar: ");
        var jogo = jogoController.buscarJogo(titulo);

        if (jogo == null) {
            System.out.println("Erro: Jogo não encontrado.");
            return;
        }

        System.out.println("Jogo encontrado: " + jogo.getTitulo());

        boolean editando = true;
        while (editando) {
            System.out.println("\nO que você quer editar?");
            System.out.println("1. Editar Preço");
            System.out.println("2. Editar Ano");
            System.out.println("3. Adicionar Gênero");
            System.out.println("4. Adicionar Plataforma");
            System.out.println("0. Voltar");

            int opcao = ConsoleUtil.lerInt("Opção: ");
            boolean sucesso = false;

            try {
                switch (opcao) {
                    case 1:
                        double novoPreco = ConsoleUtil.lerDoubleOpcional("Novo Preço (Enter para Grátis): ", 0.0);
                        sucesso = jogoController.editarPreco(titulo, novoPreco);
                        break;
                    case 2:
                        int novoAno = ConsoleUtil.lerIntOpcional("Novo Ano: ", 0);
                        sucesso = jogoController.editarAno(titulo, novoAno);
                        break;
                    case 3:
                        String genero = ConsoleUtil.lerString("Novo Gênero: ").trim();
                        sucesso = jogoController.adicionarGenero(titulo, genero);
                        break;
                    case 4:
                        String plataforma = ConsoleUtil.lerString("Nova Plataforma: ").trim();
                        sucesso = jogoController.adicionarPlataforma(titulo, plataforma);
                        break;
                    case 0:
                        editando = false;
                        continue;
                    default:
                        System.out.println("Opção inválida.");
                }

                if (sucesso) {
                    System.out.println("\nAlteração realizada com sucesso!");
                } else if (editando) {
                    System.out.println("\nFalha ao realizar alteração (verifique os dados).");
                }

            } catch (IllegalArgumentException e) {
                System.out.println("\nErro de Validação: " + e.getMessage());
            }
        }
    }

    /**
     * Gerencia o fluxo de interface para remover um jogo cadastrado.
     */
    private void removerJogo() {
        System.out.println("\n-> Remover Jogo");

        String titulo = ConsoleUtil.lerString("Digite o título do jogo a remover: ");

        boolean confirma = ConsoleUtil.lerBoolean("Tem certeza que quer remover '" + titulo + "' (s/n)? ");

        if (confirma) {
            boolean sucesso = jogoController.removerJogo(titulo);
            if (sucesso) {
                System.out.println("Jogo removido com sucesso.");
            } else {
                System.out.println("Erro: Jogo não encontrado.");
            }
        } else {
            System.out.println("Remoção cancelada.");
        }
    }

    /**
     * Sub Menu 2
     */
    private void menuGerenciarDLCs() {
        System.out.println("\n--- Gerenciar DLCs ---");
        System.out.println("1. Adicionar DLC");
        System.out.println("2. Listar DLCs");
        System.out.println("3. Editar DLC");
        System.out.println("4. Remover DLC");
        System.out.println("0. Voltar");

        int opcao = ConsoleUtil.lerInt("Opção: ");

        switch (opcao) {
            case 1:
                adicionarDLC();
                break;
            case 2:
                listarDLCs();
                break;
            case 3:
                editarDLC();
                break;
            case 4:
                removerDLC();
                break;
            case 0:
                return;
            default:
                System.out.println("Opção inválida.");
        }
    }

    /**
     * Gerencia o fluxo de interface de usuário para adicionar uma nova DLC.
     */
    private void adicionarDLC() {
        System.out.println("\n-> Adicionar Nova DLC");
        String titulo = ConsoleUtil.lerString("Título: ").trim();
        String jogoBaseTitulo = ConsoleUtil.lerString("Título do Jogo Base: ").trim();
        int ano = ConsoleUtil.lerIntOpcional("Ano de Lançamento: ", 0);
        List<String> generos = ConsoleUtil.lerListaStrings("Gêneros: ");
        List<String> plataformas = ConsoleUtil.lerListaStrings("Plataformas: ");
        double preco = ConsoleUtil.lerDoubleOpcional("Preço (Enter para Grátis): ", 0.0);
        boolean ex = ConsoleUtil.lerBoolean("Tem expansão (s/n): ");

        try {
            dlcController.adicionarDLC(titulo, 0.0, ano, generos, plataformas, jogoBaseTitulo, ex, preco);
            System.out.println("DLC '" + titulo + "' adicionada com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("\nErro de Validação: " + e.getMessage());
        }
    }

    /**
     * Gerencia o fluxo de interface para listar todas as DLCs cadastradas.
     */
    private void listarDLCs() {
        System.out.println("\n--- Lista de DLCs ---");
        var dlcs = dlcController.listarDLCs();

        if (dlcs.isEmpty()) {
            System.out.println("Nenhuma DLC cadastrada.");
            return;
        }

        for (var dlc : dlcs) {
            String anoStr = (dlc.getAnoLancamento() == 0) ? "N/A" : String.valueOf(dlc.getAnoLancamento());

            String precoStr;
            if (dlc.getPreco() == 0.0) {
                precoStr = "Grátis";
            } else {
                precoStr = String.format("R$ %.2f", dlc.getPreco());
            }

            System.out.println("--------------------");
            System.out.println("Título: " + dlc.getTitulo() + " (" + anoStr + ")");
            System.out.println("Jogo Base: " + (dlc.getJogoBaseTitulo().isEmpty() ? "N/A" : dlc.getJogoBaseTitulo()));
            System.out.println("Preço: " + precoStr);

            String generosStr = dlc.getGeneros().isEmpty() ? "N/A" : String.join(", ", dlc.getGeneros());
            System.out.println("Gêneros: " + generosStr);

            String platStr = dlc.getPlataformas().isEmpty() ? "N/A" : String.join(", ", dlc.getPlataformas());
            System.out.println("Plataformas: " + platStr);

            System.out.println("Expansão: " + (dlc.isExpansao() ? "Sim" : "Não"));
        }
        System.out.println("--------------------");
    }

    /**
     * Gerencia o fluxo de interface para editar uma DLC cadastrada.
     */
    private void editarDLC() {
        System.out.println("\n-> Editar DLC");

        String titulo = ConsoleUtil.lerString("Digite o título da DLC a editar: ");
        var dlc = dlcController.buscarDLC(titulo);

        if (dlc == null) {
            System.out.println("Erro: DLC não encontrada.");
            return;
        }

        System.out.println("DLC encontrada: " + dlc.getTitulo());

        boolean editando = true;
        while (editando) {
            System.out.println("\nO que você quer editar?");
            System.out.println("1. Editar Preço");
            System.out.println("2. Editar Ano");
            System.out.println("3. Adicionar Gênero");
            System.out.println("4. Adicionar Plataforma");
            System.out.println("0. Voltar");

            int opcao = ConsoleUtil.lerInt("Opção: ");
            boolean sucesso = false;

            try {
                switch (opcao) {
                    case 1:
                        double novoPreco = ConsoleUtil.lerDoubleOpcional("Novo Preço (Enter para Grátis): ", 0.0);
                        sucesso = dlcController.editarPreco(titulo, novoPreco);
                        break;
                    case 2:
                        int novoAno = ConsoleUtil.lerIntOpcional("Novo Ano: ", 0);
                        sucesso = dlcController.editarAno(titulo, novoAno);
                        break;
                    case 3:
                        String genero = ConsoleUtil.lerString("Novo Gênero: ").trim();
                        sucesso = dlcController.adicionarGenero(titulo, genero);
                        break;
                    case 4:
                        String plataforma = ConsoleUtil.lerString("Nova Plataforma: ").trim();
                        sucesso = dlcController.adicionarPlataforma(titulo, plataforma);
                        break;
                    case 0:
                        editando = false;
                        continue;
                    default:
                        System.out.println("Opção inválida.");
                }

                if (sucesso) {
                    System.out.println("\nAlteração realizada com sucesso!");
                } else if (editando) {
                    System.out.println("\nFalha ao realizar alteração.");
                }

            } catch (IllegalArgumentException e) {
                System.out.println("\nErro de Validação: " + e.getMessage());
            }
        }
    }

    /**
     * Gerencia o fluxo de interface para remover uma DLC cadastrada.
     */
    private void removerDLC() {
        System.out.println("\n-> Remover DLC");

        String titulo = ConsoleUtil.lerString("Digite o título da DLC a remover: ");

        boolean confirma = ConsoleUtil.lerBoolean("Tem certeza que quer remover '" + titulo + "' (s/n)? ");

        if (confirma) {
            boolean sucesso = dlcController.removerDLC(titulo);
            if (sucesso) {
                System.out.println("DLC removida com sucesso.");
            } else {
                System.out.println("Erro: DLC não encontrada.");
            }
        } else {
            System.out.println("Remoção cancelada.");
        }
    }

    /**
     * Sub Menu 3
     */
    private void menuGerenciarUsuarios() {
        System.out.println("\n--- Gerenciar Usuários ---");
        System.out.println("1. Criar Usuário");
        System.out.println("2. Listar Usuários");
        System.out.println("3. Editar Usuário");
        System.out.println("4. Remover Usuário");
        System.out.println("5. Gerenciar Biblioteca de Usuário");
        System.out.println("0. Voltar");

        int opcao = ConsoleUtil.lerInt("Opção: ");

        switch (opcao) {
            case 1:
                criarUsuario();
                break;
            case 2:
                listarUsuarios();
                break;
            case 3:
                editarUsuario();
                break;
            case 4:
                removerUsuario();
                break;
            case 5:
                menuGerenciarBiblioteca();
                break;
            case 0:
                return;
            default:
                System.out.println("Opção inválida.");
        }
    }

    /**
     * Gerencia o fluxo de interface de usuário para adicionar um novo usuário.
     */
    private void criarUsuario() {
        System.out.println("\n-> Criar Novo Usuário");
        String nome = ConsoleUtil.lerString("Nome: ").trim();
        String email = ConsoleUtil.lerString("Email: ").trim();

        try {
            boolean sucesso = usuarioController.criarUsuario(nome, email);

            if (sucesso) {
                System.out.println("Usuário '" + nome + "' criado com sucesso!");
            } else {
                System.out.println("\nErro: O email '" + email + "' já está cadastrado.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\nErro de Validação: " + e.getMessage());
        }
    }
    /**
     * Gerencia o fluxo de interface para listar todos os usuarios cadastrados.
     */
    private void listarUsuarios() {
        System.out.println("\n--- Lista de Usuários ---");
        var usuarios = usuarioController.listarUsuarios();

        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        for (var usuario : usuarios) {
            System.out.println("--------------------");
            System.out.println("Nome: " + usuario.getNome());
            System.out.println("Email: " + usuario.getEmail());
            System.out.println("Mídias na biblioteca: " + usuario.getBiblioteca().size());
        }
        System.out.println("--------------------");
    }

    /**
     * Gerencia o fluxo de interface para editar um usuário cadastrado.
     */
    private void editarUsuario() {
        System.out.println("\n-> Editar Usuário");

        String emailAtual = ConsoleUtil.lerString("Digite o email do usuário a editar: ");
        var usuario = usuarioController.buscarUsuario(emailAtual);

        if (usuario == null) {
            System.out.println("Erro: Usuário não encontrado.");
            return;
        }

        System.out.println("Usuário encontrado: " + usuario.getNome());

        System.out.println("(Deixe em branco para não alterar)");
        String novoNome = ConsoleUtil.lerString("Novo nome: ");
        String novoEmail = ConsoleUtil.lerString("Novo email: ");

        if (novoNome.isBlank() && novoEmail.isBlank()) {
            System.out.println("Nenhuma alteração solicitada. Operação cancelada.");
            return;
        }

        try {
            boolean sucesso = usuarioController.editarUsuario(emailAtual, novoNome, novoEmail);

            if (sucesso) {
                System.out.println("Usuário editado com sucesso!");
            } else {
                System.out.println("Erro ao editar. O novo email já pode estar cadastrado.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\nErro de Validação: " + e.getMessage());
        }
    }

    /**
     * Gerencia o fluxo de interface para remover um usuário cadastrado.
     */
    private void removerUsuario() {
        System.out.println("\n-> Remover Usuário");

        String email = ConsoleUtil.lerString("Digite o email do Usuário a remover: ");

        var usuario = usuarioController.buscarUsuario(email);

        if (usuario == null) {
            System.out.println("Erro: Usuário não encontrado.");
            return;
        }

        boolean confirma = ConsoleUtil.lerBoolean("Tem certeza que quer remover '" + usuario.getNome() + "' (s/n)? ");

        if (confirma) {
            boolean sucesso = usuarioController.removerUsuario(email);
            if (sucesso) {
                System.out.println("Usuário removido com sucesso.");
            } else {
                System.out.println("Erro: Usuário não encontrado.");
            }
        } else {
            System.out.println("Remoção cancelada.");
        }
    }

    /**
     * Sub Menu 4
     */
    private void menuGerenciarBiblioteca() {
        System.out.println("\n--- Gerenciar Biblioteca de Usuário ---");

        String email = ConsoleUtil.lerString("Digite o email do usuário: ");
        var usuario = usuarioController.buscarUsuario(email);

        if (usuario == null) {
            System.out.println("Erro: Usuário não encontrado.");
            return;
        }

        System.out.println("Gerenciando biblioteca de: " + usuario.getNome());

        boolean gerenciando = true;
        while (gerenciando) {
            System.out.println("\nOpções da Biblioteca:");
            System.out.println("1. Adicionar Mídia à Biblioteca");
            System.out.println("2. Listar Mídias da Biblioteca");
            System.out.println("3. Editar Mídia da Biblioteca");
            System.out.println("4. Remover Mídia da Biblioteca");
            System.out.println("0. Voltar");

            int opcao = ConsoleUtil.lerInt("Opção: ");

            switch (opcao) {
                case 1:
                    adicionarMidiaNaBiblioteca(email);
                    break;
                case 2:
                    listarMidiasDaBiblioteca(email);
                    break;
                case 3:
                    editarMidiaDaBiblioteca(email);
                    break;
                case 4:
                    removerMidiaDaBiblioteca(email);
                    break;
                case 0:
                    gerenciando = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    /**
     * Gerencia o fluxo de interface para adicionar mídia na biblioteca do usuário cadastrado.
     */
    private void adicionarMidiaNaBiblioteca(String emailUsuario) {
        System.out.println("\n-> Adicionar Mídia na Biblioteca");
        System.out.println("Qual tipo de mídia deseja adicionar?");
        System.out.println("1. Jogo");
        System.out.println("2. DLC");

        int tipo = ConsoleUtil.lerInt("Tipo: ");
        String titulo = ConsoleUtil.lerString("Digite o título da mídia: ").trim();

        Midia midiaParaAdicionar = null;

        if (tipo == 1) {
            midiaParaAdicionar = jogoController.buscarJogo(titulo);
        } else if (tipo == 2) {
            midiaParaAdicionar = dlcController.buscarDLC(titulo);
        } else {
            System.out.println("Tipo inválido.");
            return;
        }

        if (midiaParaAdicionar == null) {
            System.out.println("Erro: Mídia não encontrada na loja.");
            return;
        }

        boolean sucesso = usuarioController.adicionarMidiaAoUsuario(emailUsuario, midiaParaAdicionar);

        if (sucesso) {
            System.out.println("'" + titulo + "' adicionado(a) à biblioteca!");
        } else {
            System.out.println("Erro: Não foi possível adicionar (talvez já esteja na biblioteca).");
        }
    }

    /**
     * Gerencia o fluxo de interface para listar mídias da biblioteca do usuário cadastrado.
     */
    private void listarMidiasDaBiblioteca(String emailUsuario) {
        System.out.println("\n--- Mídias na Biblioteca de " + emailUsuario + " ---");

        var biblioteca = usuarioController.listarBibliotecaUsuario(emailUsuario);

        if (biblioteca.isEmpty()) {
            System.out.println("A biblioteca está vazia.");
            return;
        }

        for (var midia : biblioteca) {
            System.out.println("--------------------");

            if (midia instanceof Jogo) {
                var jogo = (Jogo) midia;

                String anoStr = (jogo.getAnoLancamento() == 0) ? "N/A" : String.valueOf(jogo.getAnoLancamento());

                String precoStr;
                if (jogo.getPreco() == 0.0) {
                    precoStr = "Grátis";
                } else {
                    precoStr = String.format("R$ %.2f", jogo.getPreco());
                }

                System.out.println("Tipo: Jogo");
                System.out.println("Título: " + jogo.getTitulo() + " (" + anoStr + ")");
                System.out.println("Preço: " + precoStr);
                System.out.println("Nota: " + jogo.getNota());
                System.out.println("Desenvolvedora: " + (jogo.getDesenvolvedora().isEmpty() ? "N/A" : jogo.getDesenvolvedora()));
                System.out.println("Gêneros: " + (jogo.getGeneros().isEmpty() ? "N/A" : String.join(", ", jogo.getGeneros())));
                System.out.println("Plataformas: " + (jogo.getPlataformas().isEmpty() ? "N/A" : String.join(", ", jogo.getPlataformas())));
                System.out.println("Multiplayer: " + (jogo.isMultiplayer() ? "Sim" : "Não"));

            } else if (midia instanceof DLC) {
                var dlc = (DLC) midia;

                String anoStr = (dlc.getAnoLancamento() == 0) ? "N/A" : String.valueOf(dlc.getAnoLancamento());

                String precoStr;
                if (dlc.getPreco() == 0.0) {
                    precoStr = "Grátis";
                } else {
                    precoStr = String.format("R$ %.2f", dlc.getPreco());
                }

                System.out.println("Tipo: DLC");
                System.out.println("Título: " + dlc.getTitulo() + " (" + anoStr + ")");
                System.out.println("Jogo Base: " + dlc.getJogoBaseTitulo());
                System.out.println("Preço: R$ " + precoStr);
                System.out.println("Nota: " + dlc.getNota());
                System.out.println("Gêneros: " + (dlc.getGeneros().isEmpty() ? "N/A" : String.join(", ", dlc.getGeneros())));
                System.out.println("Plataformas: " + (dlc.getPlataformas().isEmpty() ? "N/A" : String.join(", ", dlc.getPlataformas())));
                System.out.println("Expansão: " + (dlc.isExpansao() ? "Sim" : "Não"));

            } else {
                System.out.println("Tipo: Mídia Desconhecida");
                System.out.println("Título: " + midia.getTitulo());
            }
        }
        System.out.println("--------------------");
    }

    /**
     * Gerencia o fluxo de interface para editar mídia da biblioteca do usuário cadastrado.
     */
    private void editarMidiaDaBiblioteca(String emailUsuario) {
        System.out.println("\n-> Editar Mídia da Biblioteca");
        String titulo = ConsoleUtil.lerString("Digite o título da mídia a editar: ").trim();

        var midia = usuarioController.buscarMidiaDoUsuario(emailUsuario, titulo);
        if (midia == null) {
            System.out.println("Erro: Mídia não encontrada nesta biblioteca.");
            return;
        }

        System.out.println("Editando: " + midia.getTitulo());


        boolean sucesso = false;

        double novaNota = ConsoleUtil.lerDoubleOpcional("Nova Nota: ", 0);
        sucesso = usuarioController.editarMidiaDoUsuario(emailUsuario, titulo, novaNota);

        if (sucesso) {
            System.out.println("Mídia editada com sucesso!");
            midia = usuarioController.buscarMidiaDoUsuario(emailUsuario, titulo);
        } else {
            System.out.println("Falha ao editar mídia, verifique se a nota está entre 0 e 10.");
        }
    }

    /**
     * Gerencia o fluxo de interface para remover mídia da biblioteca do usuário cadastrado.
     */
    private void removerMidiaDaBiblioteca(String emailUsuario) {
        System.out.println("\n-> Remover Mídia da Biblioteca");
        String titulo = ConsoleUtil.lerString("Digite o título da mídia a remover: ").trim();

        boolean confirma = ConsoleUtil.lerBoolean("Tem certeza (s/n)? ");
        if (confirma) {
            boolean sucesso = usuarioController.removerMidiaDoUsuario(emailUsuario, titulo);
            if (sucesso) {
                System.out.println("Mídia removida da biblioteca.");
            } else {
                System.out.println("Erro: Mídia não encontrada nesta biblioteca.");
            }
        } else {
            System.out.println("Remoção cancelada.");
        }
    }
}