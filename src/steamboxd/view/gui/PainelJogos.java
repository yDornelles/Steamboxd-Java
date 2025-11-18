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
        String[] colunas = {"Título", "Ano", "Preço", "Dev", "Multiplayer", "Gêneros", "Plataformas"};
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
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Jogo> jogos = jogoController.listarJogos();
        for (Jogo jogo : jogos) {
            String generos = String.join(", ", jogo.getGeneros());
            String plataformas = String.join(", ", jogo.getPlataformas());

            tableModel.addRow(new Object[]{
                    jogo.getTitulo(),
                    jogo.getAnoLancamento(),
                    jogo.getPreco(),
                    jogo.getDesenvolvedora(),
                    jogo.isMultiplayer() ? "Sim" : "Não",
                    generos,
                    plataformas
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
                // Converte as strings de vírgula em Listas
                List<String> generos = Arrays.stream(txtGeneros.getText().split(","))
                        .map(String::trim).collect(Collectors.toList());
                List<String> plataformas = Arrays.stream(txtPlataformas.getText().split(","))
                        .map(String::trim).collect(Collectors.toList());

                jogoController.adicionarJogo(
                        txtTitulo.getText(),
                        0.0,
                        Integer.parseInt(txtAno.getText()),
                        generos,
                        plataformas,
                        txtDev.getText(),
                        chkMulti.isSelected(),
                        Double.parseDouble(txtPreco.getText())
                );
                atualizarTabela();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Erro: Ano e Preço devem ser números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
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
                        sucesso = jogoController.editarPreco(titulo, Double.parseDouble(novoPrecoStr));
                    }
                    break;
                case 1: // Editar Ano
                    String novoAnoStr = JOptionPane.showInputDialog(this, "Novo Ano:", jogo.getAnoLancamento());
                    if (novoAnoStr != null) {
                        sucesso = jogoController.editarAno(titulo, Integer.parseInt(novoAnoStr));
                    }
                    break;
                case 2: // Adicionar Gênero
                    String novoGenero = JOptionPane.showInputDialog(this, "Adicionar Gênero:");
                    if (novoGenero != null && !novoGenero.isBlank()) {
                        sucesso = jogoController.adicionarGenero(titulo, novoGenero);
                    }
                    break;
                case 3: // Adicionar Plataforma
                    String novaPlataforma = JOptionPane.showInputDialog(this, "Adicionar Plataforma:");
                    if (novaPlataforma != null && !novaPlataforma.isBlank()) {
                        sucesso = jogoController.adicionarPlataforma(titulo, novaPlataforma);
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
                JOptionPane.showMessageDialog(this, "Falha ao realizar alteração.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor numérico inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
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