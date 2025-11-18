package steamboxd.view.gui;

import steamboxd.controller.DLCController;
import steamboxd.model.DLC;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Painel de CRUD (com GUI) para a entidade DLC.
 */
public class PainelDLCs extends JPanel {

    private final DLCController dlcController;
    private final JTable tabela;
    private final DefaultTableModel tableModel;

    public PainelDLCs() {
        this.dlcController = new DLCController();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Tabela ---
        String[] colunas = {"Título", "Jogo Base", "Ano", "Preço", "Expansão?", "Gêneros", "Plataformas"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(tableModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // --- Painel de Botões ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnAdicionar = new JButton("Adicionar DLC");
        JButton btnEditar = new JButton("Editar DLC");
        JButton btnRemover = new JButton("Remover DLC");
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
        List<DLC> dlcs = dlcController.listarDLCs();
        for (DLC dlc : dlcs) {
            String generos = String.join(", ", dlc.getGeneros());
            String plataformas = String.join(", ", dlc.getPlataformas());

            tableModel.addRow(new Object[]{
                    dlc.getTitulo(),
                    dlc.getJogoBase(),
                    dlc.getAnoLancamento(),
                    dlc.getPreco(),
                    dlc.isExpansao() ? "Sim" : "Não",
                    generos,
                    plataformas
            });
        }
    }

    private DLC getDLCSelecionada() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma DLC na tabela.", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        String titulo = (String) tableModel.getValueAt(linhaSelecionada, 0);
        return dlcController.buscarDLC(titulo);
    }

    /**
     * Formulário de adição.
     */
    private JPanel criarFormularioDLC() {
        JTextField txtTitulo = new JTextField();
        JTextField txtJogoBase = new JTextField();
        JTextField txtAno = new JTextField();
        JTextField txtPreco = new JTextField();
        JCheckBox chkExpansao = new JCheckBox("É uma expansão?");
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
        painel.add(new JLabel("Jogo Base:"), gbc);
        gbc.gridx = 1; gbc.gridy = linha++; gbc.weightx = 1.0;
        painel.add(txtJogoBase, gbc);

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

        gbc.gridx = 1; gbc.gridy = linha++; gbc.weightx = 1.0;
        painel.add(chkExpansao, gbc);

        painel.putClientProperty("campos", new Object[]{txtTitulo, txtJogoBase, txtAno, txtGeneros, txtPlataformas, txtPreco, chkExpansao});
        return painel;
    }

    /**
     * Chama o controller com os dados do novo formulário.
     */
    private void acaoAdicionar() {
        JPanel painelDialog = criarFormularioDLC();
        int result = JOptionPane.showConfirmDialog(this, painelDialog, "Adicionar Nova DLC", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Object[] campos = (Object[]) painelDialog.getClientProperty("campos");
            JTextField txtTitulo = (JTextField) campos[0];
            JTextField txtJogoBase = (JTextField) campos[1];
            JTextField txtAno = (JTextField) campos[2];
            JTextField txtGeneros = (JTextField) campos[3];
            JTextField txtPlataformas = (JTextField) campos[4];
            JTextField txtPreco = (JTextField) campos[5];
            JCheckBox chkExpansao = (JCheckBox) campos[6];

            try {
                List<String> generos = Arrays.stream(txtGeneros.getText().split(","))
                        .map(String::trim).collect(Collectors.toList());
                List<String> plataformas = Arrays.stream(txtPlataformas.getText().split(","))
                        .map(String::trim).collect(Collectors.toList());

                dlcController.adicionarDLC(
                        txtTitulo.getText(),
                        0.0,
                        Integer.parseInt(txtAno.getText()),
                        generos,
                        plataformas,
                        txtJogoBase.getText(),
                        chkExpansao.isSelected(),
                        Double.parseDouble(txtPreco.getText())
                );
                atualizarTabela();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Erro: Ano e Preço devem ser números.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Mostra um menu de opções.
     */
    private void acaoEditar() {
        DLC dlc = getDLCSelecionada();
        if (dlc == null) return;

        String titulo = dlc.getTitulo();

        Object[] options = {"Editar Preço", "Editar Ano", "Adicionar Gênero", "Adicionar Plataforma", "Cancelar"};
        int escolha = JOptionPane.showOptionDialog(this,
                "O que você quer editar em '" + titulo + "'?",
                "Editar DLC",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        boolean sucesso = false;
        try {
            switch (escolha) {
                case 0: // Editar Preço
                    String novoPrecoStr = JOptionPane.showInputDialog(this, "Novo Preço:", dlc.getPreco());
                    if (novoPrecoStr != null) {
                        sucesso = dlcController.editarPreco(titulo, Double.parseDouble(novoPrecoStr));
                    }
                    break;
                case 1: // Editar Ano
                    String novoAnoStr = JOptionPane.showInputDialog(this, "Novo Ano:", dlc.getAnoLancamento());
                    if (novoAnoStr != null) {
                        sucesso = dlcController.editarAno(titulo, Integer.parseInt(novoAnoStr));
                    }
                    break;
                case 2: // Adicionar Gênero
                    String novoGenero = JOptionPane.showInputDialog(this, "Adicionar Gênero:");
                    if (novoGenero != null && !novoGenero.isBlank()) {
                        sucesso = dlcController.adicionarGenero(titulo, novoGenero);
                    }
                    break;
                case 3: // Adicionar Plataforma
                    String novaPlataforma = JOptionPane.showInputDialog(this, "Adicionar Plataforma:");
                    if (novaPlataforma != null && !novaPlataforma.isBlank()) {
                        sucesso = dlcController.adicionarPlataforma(titulo, novaPlataforma);
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
        DLC dlc = getDLCSelecionada();
        if (dlc == null) return;

        int resp = JOptionPane.showConfirmDialog(this, "Remover '" + dlc.getTitulo() + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            dlcController.removerDLC(dlc.getTitulo());
            atualizarTabela();
        }
    }
}