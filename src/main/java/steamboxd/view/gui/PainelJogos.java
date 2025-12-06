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

    public void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Jogo> jogos = jogoController.listarJogos();
        for (Jogo jogo : jogos) {
            // Filtra listas vazias para N/A
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
     * Cria um painel de formulário reutilizável.
     * Se receber um jogo (modo edição), preenche os campos.
     * Se receber null (modo adição), deixa vazio.
     */
    private JPanel criarFormularioJogo(Jogo jogo) {
        JTextField txtTitulo = new JTextField(jogo != null ? jogo.getTitulo() : "");
        JTextField txtAno = new JTextField(jogo != null ? (jogo.getAnoLancamento() == 0 ? "" : String.valueOf(jogo.getAnoLancamento())) : "");
        JTextField txtPreco = new JTextField(jogo != null ? String.valueOf(jogo.getPreco()) : "");
        JTextField txtDev = new JTextField(jogo != null ? jogo.getDesenvolvedora() : "");
        JCheckBox chkMulti = new JCheckBox("Multiplayer?", jogo != null && jogo.isMultiplayer());

        // Converte listas para String (vírgula)
        String generosStr = jogo != null ? String.join(", ", jogo.getGeneros()) : "";
        JTextField txtGeneros = new JTextField(generosStr);

        String platStr = jogo != null ? String.join(", ", jogo.getPlataformas()) : "";
        JTextField txtPlataformas = new JTextField(platStr);

        // Bloqueia título na edição
        if (jogo != null) {
            txtTitulo.setEditable(false);
            txtTitulo.setEnabled(false);
        }

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Método auxiliar para adicionar linhas (deixa o código limpo)
        adicionarCampo(painel, gbc, 0, "Título:", txtTitulo);
        adicionarCampo(painel, gbc, 1, "Ano:", txtAno);
        adicionarCampo(painel, gbc, 2, "Preço:", txtPreco);
        adicionarCampo(painel, gbc, 3, "Desenvolvedora:", txtDev);
        adicionarCampo(painel, gbc, 4, "Gêneros (vírgula):", txtGeneros);
        adicionarCampo(painel, gbc, 5, "Plataformas (vírgula):", txtPlataformas);

        gbc.gridx = 1; gbc.gridy = 6;
        painel.add(chkMulti, gbc);

        painel.putClientProperty("campos", new Object[]{txtTitulo, txtAno, txtPreco, txtDev, txtGeneros, txtPlataformas, chkMulti});
        return painel;
    }

    private void adicionarCampo(JPanel p, GridBagConstraints gbc, int y, String label, Component cmp) {
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0.0; p.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; p.add(cmp, gbc);
    }

    private void acaoAdicionar() {
        JPanel painelDialog = criarFormularioJogo(null);
        int result = JOptionPane.showConfirmDialog(this, painelDialog, "Adicionar Novo Jogo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            processarSalvar(painelDialog, true);
        }
    }

    private void acaoEditar() {
        Jogo jogo = getJogoSelecionado();
        if (jogo == null) return;

        JPanel painelDialog = criarFormularioJogo(jogo);
        int result = JOptionPane.showConfirmDialog(this, painelDialog, "Editar Jogo: " + jogo.getTitulo(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            processarSalvar(painelDialog, false);
        }
    }

    /**
     * Processa os dados do formulário e chama o controller.
     */
    private void processarSalvar(JPanel painel, boolean isAdicao) {
        Object[] campos = (Object[]) painel.getClientProperty("campos");
        JTextField txtTitulo = (JTextField) campos[0];
        JTextField txtAno = (JTextField) campos[1];
        JTextField txtPreco = (JTextField) campos[2];
        JTextField txtDev = (JTextField) campos[3];
        JTextField txtGeneros = (JTextField) campos[4];
        JTextField txtPlataformas = (JTextField) campos[5];
        JCheckBox chkMulti = (JCheckBox) campos[6];

        try {
            String titulo = txtTitulo.getText().trim();

            int ano = 0;
            if (!txtAno.getText().isBlank()) ano = Integer.parseInt(txtAno.getText());

            String precoTxt = txtPreco.getText().replace(',', '.').trim();
            double preco = precoTxt.isEmpty() ? 0.0 : Double.parseDouble(precoTxt);

            // Listas: Split, Trim e Filter (Empty)
            List<String> generos = Arrays.stream(txtGeneros.getText().split(","))
                    .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());

            List<String> plataformas = Arrays.stream(txtPlataformas.getText().split(","))
                    .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());

            if (isAdicao) {
                jogoController.adicionarJogo(titulo, 0.0, ano, generos, plataformas, txtDev.getText(), chkMulti.isSelected(), preco);
            } else {
                // MODO EDIÇÃO: Atualiza tudo
                jogoController.editarAno(titulo, ano);
                jogoController.editarPreco(titulo, preco);
                jogoController.editarDesenvolvedora(titulo, txtDev.getText());
                jogoController.editarMultiplayer(titulo, chkMulti.isSelected());
                jogoController.atualizarGeneros(titulo, generos);
                jogoController.atualizarPlataformas(titulo, plataformas);
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