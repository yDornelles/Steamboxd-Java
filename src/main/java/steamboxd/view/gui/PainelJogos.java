package steamboxd.view.gui;

import steamboxd.controller.JogoController;
import steamboxd.model.Jogo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Painel de CRUD (com GUI) para a entidade Jogo.
 */
public class PainelJogos extends JPanel {

    private final JogoController jogoController;
    private final JTable tabela;
    private final DefaultTableModel tableModel;

    public PainelJogos() {
        this.jogoController = new JogoController();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Tabela ---
        String[] colunas = {"Título", "Ano", "Preço", "Dev", "Multiplayer", "Gêneros", "Plataformas", "Nº DLCs"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(tableModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // --- Painel de Botões ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnAdicionar = new JButton("Adicionar Jogo");
        JButton btnEditar = new JButton("Editar Jogo");
        JButton btnRemover = new JButton("Remover Jogo");
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRemover);
        add(painelBotoes, BorderLayout.SOUTH);

        // --- Ações ---
        btnAdicionar.addActionListener(e -> acaoAdicionar());
        btnEditar.addActionListener(e -> acaoEditar());
        btnRemover.addActionListener(e -> acaoRemover());

        // --- Carrega dados iniciais ---
        atualizarTabela();
    }

    /**
     * Preenche a tabela.
     */
    public void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Jogo> jogos = jogoController.listarJogos();
        for (Jogo jogo : jogos) {
            List<String> listaGeneros = jogo.getGeneros();
            String generos = listaGeneros.isEmpty() ? "N/A" : String.join(", ", listaGeneros);

            List<String> listaPlataformas = jogo.getPlataformas();
            String plataformas = listaPlataformas.isEmpty() ? "N/A" : String.join(", ", listaPlataformas);

            Object anoExibicao = (jogo.getAnoLancamento() == 0) ? "N/A" : jogo.getAnoLancamento();

            String devExibicao = jogo.getDesenvolvedora().isEmpty() ? "N/A" : jogo.getDesenvolvedora();

            Object precoExibicao;
            if (jogo.getPreco() == 0.0) {
                precoExibicao = "Grátis";
            } else {
                precoExibicao = String.format("R$ %.2f", jogo.getPreco());
            }

            tableModel.addRow(new Object[]{
                    jogo.getTitulo(),
                    anoExibicao,
                    precoExibicao,
                    devExibicao,
                    jogo.isMultiplayer() ? "Sim" : "Não",
                    generos,
                    plataformas,
                    jogo.getDlcTitulos().size()
            });
        }
    }

    /**
     * Pega o Jogo selecionado na tabela.
     */
    private Jogo getJogoSelecionado() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um jogo na tabela.", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        String titulo = (String) tableModel.getValueAt(linhaSelecionada, 0);
        return jogoController.buscarJogo(titulo);
    }

    /**
     * Formulário de adição.
     */
    private JPanel criarFormularioJogo() {
        JTextField txtTitulo = new JTextField();
        JTextField txtAno = new JTextField();
        JTextField txtPreco = new JTextField();
        JTextField txtDev = new JTextField();
        JCheckBox chkMulti = new JCheckBox("Multiplayer?");
        JTextField txtGeneros = new JTextField();
        JTextField txtPlataformas = new JTextField();

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int linha = 0;
        gbc.gridx = 0; gbc.gridy = linha; gbc.weightx = 0.0;
        painel.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.gridy = linha++; gbc.weightx = 1.0;
        painel.add(txtTitulo, gbc);

        gbc.gridx = 0; gbc.gridy = linha; gbc.weightx = 0.0;
        painel.add(new JLabel("Ano:"), gbc);
        gbc.gridx = 1; gbc.gridy = linha++; gbc.weightx = 1.0;
        painel.add(txtAno, gbc);

        gbc.gridx = 0; gbc.gridy = linha; gbc.weightx = 0.0;
        painel.add(new JLabel("Gêneros (separados por vírgula):"), gbc);
        gbc.gridx = 1; gbc.gridy = linha++; gbc.weightx = 1.0;
        painel.add(txtGeneros, gbc);

        gbc.gridx = 0; gbc.gridy = linha; gbc.weightx = 0.0;
        painel.add(new JLabel("Plataformas (separados por vírgula):"), gbc);
        gbc.gridx = 1; gbc.gridy = linha++; gbc.weightx = 1.0;
        painel.add(txtPlataformas, gbc);

        gbc.gridx = 0; gbc.gridy = linha; gbc.weightx = 0.0;
        painel.add(new JLabel("Preço:"), gbc);
        gbc.gridx = 1; gbc.gridy = linha++; gbc.weightx = 1.0;
        painel.add(txtPreco, gbc);

        gbc.gridx = 0; gbc.gridy = linha; gbc.weightx = 0.0;
        painel.add(new JLabel("Desenvolvedora:"), gbc);
        gbc.gridx = 1; gbc.gridy = linha++; gbc.weightx = 1.0;
        painel.add(txtDev, gbc);

        gbc.gridx = 1; gbc.gridy = linha++; gbc.weightx = 1.0;
        painel.add(chkMulti, gbc);

        painel.putClientProperty("campos", new Object[]{txtTitulo, txtAno, txtGeneros, txtPlataformas, txtPreco, txtDev, chkMulti});
        return painel;
    }

    /**
     * Chama o controller com os dados do novo formulário.
     */
    private void acaoAdicionar() {
        JPanel painelDialog = criarFormularioJogo();
        int result = JOptionPane.showConfirmDialog(this, painelDialog, "Adicionar Novo Jogo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Object[] campos = (Object[]) painelDialog.getClientProperty("campos");
            JTextField txtTitulo = (JTextField) campos[0];
            JTextField txtAno = (JTextField) campos[1];
            JTextField txtGeneros = (JTextField) campos[2];
            JTextField txtPlataformas = (JTextField) campos[3];
            JTextField txtPreco = (JTextField) campos[4];
            JTextField txtDev = (JTextField) campos[5];
            JCheckBox chkMulti = (JCheckBox) campos[6];

            try {
                List<String> generos = Arrays.stream(txtGeneros.getText().split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
                List<String> plataformas = Arrays.stream(txtPlataformas.getText().split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());

                int ano = 0;
                if (!txtAno.getText().isBlank()) {
                    ano = Integer.parseInt(txtAno.getText());
                }

                String precoTxt = txtPreco.getText().replace(',', '.').trim();
                double preco = 0.0;
                if (!precoTxt.isEmpty()) {
                    preco = Double.parseDouble(precoTxt);
                }

                jogoController.adicionarJogo(
                        txtTitulo.getText(),
                        0.0,
                        ano,
                        generos,
                        plataformas,
                        txtDev.getText(),
                        chkMulti.isSelected(),
                        preco
                );
                atualizarTabela();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Erro: Ano e Preço devem ser números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {

                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Mostra um menu de opções.
     */
    private void acaoEditar() {
        Jogo jogo = getJogoSelecionado();
        if (jogo == null) return;

        String titulo = jogo.getTitulo();

        Object[] options = {"Editar Preço", "Editar Ano", "Adicionar Gênero", "Adicionar Plataforma", "Cancelar"};
        int escolha = JOptionPane.showOptionDialog(this,
                "O que você quer editar em '" + titulo + "'?",
                "Editar Jogo",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        boolean sucesso = false;
        try {
            switch (escolha) {
                case 0: // Editar Preço
                    String novoPrecoStr = JOptionPane.showInputDialog(this, "Novo Preço:", jogo.getPreco());
                    if (novoPrecoStr != null) {
                        String precoTratado = novoPrecoStr.replace(",", ".").trim();
                        double novoPreco = 0.0;
                        if (!precoTratado.isEmpty()) {
                            novoPreco = Double.parseDouble(precoTratado);
                        }
                        sucesso = jogoController.editarPreco(titulo, novoPreco);
                    }
                    break;
                case 1: // Editar Ano
                    String valorAtual = (jogo.getAnoLancamento() == 0) ? "" : String.valueOf(jogo.getAnoLancamento());
                    String novoAnoStr = JOptionPane.showInputDialog(this, "Novo Ano (deixe vazio para 'N/A'):", valorAtual);

                    if (novoAnoStr != null) {
                        int novoAno = 0;
                        if (!novoAnoStr.isBlank()) {
                            novoAno = Integer.parseInt(novoAnoStr);
                        }
                        sucesso = jogoController.editarAno(titulo, novoAno);
                    }
                    break;
                case 2: // Adicionar Gênero
                    String novoGenero = JOptionPane.showInputDialog(this, "Adicionar Gênero:");
                    if (novoGenero != null && !novoGenero.isBlank()) {
                        String[] lista = novoGenero.split(",");
                        for (String g : lista) {
                            if (!g.trim().isEmpty()) {
                                sucesso = jogoController.adicionarGenero(titulo, g.trim());
                            }
                        }
                    }
                    break;
                case 3: // Adicionar Plataforma
                    String novaPlataforma = JOptionPane.showInputDialog(this, "Adicionar Plataforma:");
                    if (novaPlataforma != null && !novaPlataforma.isBlank()) {
                        String[] lista = novaPlataforma.split(",");
                        for (String p : lista) {
                            if (!p.trim().isEmpty()) {
                                sucesso = jogoController.adicionarPlataforma(titulo, p.trim());
                            }
                        }
                    }
                    break;
                case 4: // Cancelar
                default:
                    return;
            }

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Alteração realizada com sucesso!");
                atualizarTabela();
            } else {
                atualizarTabela();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor numérico inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void acaoRemover() {
        Jogo jogo = getJogoSelecionado();
        if (jogo == null) return;

        int resp = JOptionPane.showConfirmDialog(this,
                "Tem certeza que quer remover o jogo '" + jogo.getTitulo() + "'?",
                "Confirmar Remoção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (resp == JOptionPane.YES_OPTION) {
            jogoController.removerJogo(jogo.getTitulo());
            atualizarTabela();
        }
    }
}