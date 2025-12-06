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
        String[] colunas = {"Título", "Jogo Base (Título)", "Ano", "Preço", "Expansão?", "Gêneros", "Plataformas"};
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

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        List<DLC> dlcs = dlcController.listarDLCs();
        for (DLC dlc : dlcs) {
            String generos = dlc.getGeneros().isEmpty() ? "N/A" : String.join(", ", dlc.getGeneros());
            String plataformas = dlc.getPlataformas().isEmpty() ? "N/A" : String.join(", ", dlc.getPlataformas());

            Object anoExibicao = (dlc.getAnoLancamento() == 0) ? "N/A" : dlc.getAnoLancamento();

            Object precoExibicao;
            if (dlc.getPreco() == 0.0) {
                precoExibicao = "Grátis";
            } else {
                precoExibicao = String.format("R$ %.2f", dlc.getPreco());
            }

            String jogoBaseExibicao = (dlc.getJogoBaseTitulo() == null || dlc.getJogoBaseTitulo().isBlank())
                    ? "N/A"
                    : dlc.getJogoBaseTitulo();

            tableModel.addRow(new Object[]{
                    dlc.getTitulo(),
                    jogoBaseExibicao,
                    anoExibicao,
                    precoExibicao,
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
     * Cria um formulário reutilizável para DLC.
     * Se receber dlc != null, preenche os campos para edição.
     */
    private JPanel criarFormularioDLC(DLC dlc) {
        JTextField txtTitulo = new JTextField(dlc != null ? dlc.getTitulo() : "");
        JTextField txtJogoBase = new JTextField(dlc != null ? dlc.getJogoBaseTitulo() : "");
        JTextField txtAno = new JTextField(dlc != null ? (dlc.getAnoLancamento() == 0 ? "" : String.valueOf(dlc.getAnoLancamento())) : "");
        JTextField txtPreco = new JTextField(dlc != null ? String.valueOf(dlc.getPreco()) : "");
        JCheckBox chkExpansao = new JCheckBox("É uma expansão?", dlc != null && dlc.isExpansao());

        // Listas (separadas por vírgula)
        String generosStr = dlc != null ? String.join(", ", dlc.getGeneros()) : "";
        JTextField txtGeneros = new JTextField(generosStr);

        String platStr = dlc != null ? String.join(", ", dlc.getPlataformas()) : "";
        JTextField txtPlataformas = new JTextField(platStr);

        // Bloqueia título na edição
        if (dlc != null) {
            txtTitulo.setEditable(false);
            txtTitulo.setEnabled(false);
        }

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        adicionarCampo(painel, gbc, 0, "Título:", txtTitulo);
        adicionarCampo(painel, gbc, 1, "Jogo Base (Título):", txtJogoBase);
        adicionarCampo(painel, gbc, 2, "Ano:", txtAno);
        adicionarCampo(painel, gbc, 3, "Gêneros (vírgula):", txtGeneros);
        adicionarCampo(painel, gbc, 4, "Plataformas (vírgula):", txtPlataformas);
        adicionarCampo(painel, gbc, 5, "Preço:", txtPreco);

        gbc.gridx = 1; gbc.gridy = 6;
        painel.add(chkExpansao, gbc);

        painel.putClientProperty("campos", new Object[]{txtTitulo, txtJogoBase, txtAno, txtGeneros, txtPlataformas, txtPreco, chkExpansao});
        return painel;
    }

    private void adicionarCampo(JPanel p, GridBagConstraints gbc, int y, String label, Component cmp) {
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0.0; p.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; p.add(cmp, gbc);
    }

    private void acaoAdicionar() {
        JPanel painelDialog = criarFormularioDLC(null);
        int result = JOptionPane.showConfirmDialog(this, painelDialog, "Adicionar Nova DLC", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            processarSalvar(painelDialog, true);
        }
    }

    private void acaoEditar() {
        DLC dlc = getDLCSelecionada();
        if (dlc == null) return;

        JPanel painelDialog = criarFormularioDLC(dlc);
        int result = JOptionPane.showConfirmDialog(this, painelDialog, "Editar DLC: " + dlc.getTitulo(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            processarSalvar(painelDialog, false);
        }
    }

    private void processarSalvar(JPanel painel, boolean isAdicao) {
        Object[] campos = (Object[]) painel.getClientProperty("campos");
        JTextField txtTitulo = (JTextField) campos[0];
        JTextField txtJogoBase = (JTextField) campos[1];
        JTextField txtAno = (JTextField) campos[2];
        JTextField txtGeneros = (JTextField) campos[3];
        JTextField txtPlataformas = (JTextField) campos[4];
        JTextField txtPreco = (JTextField) campos[5];
        JCheckBox chkExpansao = (JCheckBox) campos[6];

        try {
            String titulo = txtTitulo.getText().trim();

            int ano = 0;
            if (!txtAno.getText().isBlank()) ano = Integer.parseInt(txtAno.getText());

            String precoTxt = txtPreco.getText().replace(',', '.').trim();
            double preco = precoTxt.isEmpty() ? 0.0 : Double.parseDouble(precoTxt);

            List<String> generos = Arrays.stream(txtGeneros.getText().split(","))
                    .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
            List<String> plataformas = Arrays.stream(txtPlataformas.getText().split(","))
                    .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());

            if (isAdicao) {
                dlcController.adicionarDLC(titulo, 0.0, ano, generos, plataformas, txtJogoBase.getText(), chkExpansao.isSelected(), preco);
            } else {
                // MODO EDIÇÃO: Atualiza tudo
                dlcController.editarAno(titulo, ano);
                dlcController.editarPreco(titulo, preco);
                dlcController.editarJogoBase(titulo, txtJogoBase.getText());
                dlcController.editarExpansao(titulo, chkExpansao.isSelected());
                dlcController.atualizarGeneros(titulo, generos);
                dlcController.atualizarPlataformas(titulo, plataformas);
            }

            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Salvo com sucesso!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro: Ano ou Preço inválidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Validação", JOptionPane.WARNING_MESSAGE);
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