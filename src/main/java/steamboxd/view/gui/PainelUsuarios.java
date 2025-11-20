package steamboxd.view.gui;

import steamboxd.controller.JogoController;
import steamboxd.controller.DLCController;
import steamboxd.controller.UsuarioController;
import steamboxd.model.Midia; //
import steamboxd.model.Usuario; //
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Painel de CRUD para Usuários e gerenciamento de suas Bibliotecas.
 */
public class PainelUsuarios extends JPanel {

    private final UsuarioController usuarioController;
    private final JogoController jogoController;
    private final DLCController dlcController;

    private final JTable tabelaUsuarios;
    private final DefaultTableModel modelUsuarios;
    private final JTable tabelaBiblioteca;
    private final DefaultTableModel modelBiblioteca;
    private JLabel lblBibliotecaDe;

    private Usuario usuarioSelecionado = null;

    public PainelUsuarios() {
        this.usuarioController = new UsuarioController();
        this.jogoController = new JogoController();
        this.dlcController = new DLCController();

        // Layout principal
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Painel de Usuarios ---
        JPanel painelSuperior = new JPanel(new BorderLayout(0, 5));
        painelSuperior.setBorder(BorderFactory.createTitledBorder("Gerenciar Usuários"));

        String[] colunasUsuarios = {"Nome", "Email", "Itens na Biblioteca"};
        modelUsuarios = new DefaultTableModel(colunasUsuarios, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaUsuarios = new JTable(modelUsuarios);
        tabelaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        painelSuperior.add(new JScrollPane(tabelaUsuarios), BorderLayout.CENTER);

        JPanel botoesUsuarios = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnAddUser = new JButton("Add Usuário");
        JButton btnEditUser = new JButton("Edit Usuário");
        JButton btnDelUser = new JButton("Del Usuário");
        botoesUsuarios.add(btnAddUser);
        botoesUsuarios.add(btnEditUser);
        botoesUsuarios.add(btnDelUser);
        painelSuperior.add(botoesUsuarios, BorderLayout.SOUTH);

        // --- Painel da Biblioteca ---
        JPanel painelInferior = new JPanel(new BorderLayout(0, 5));
        painelInferior.setBorder(BorderFactory.createTitledBorder("Biblioteca do Usuário"));

        lblBibliotecaDe = new JLabel("Selecione um usuário na tabela acima.");
        lblBibliotecaDe.setHorizontalAlignment(SwingConstants.CENTER);
        painelInferior.add(lblBibliotecaDe, BorderLayout.NORTH);

        String[] colunasBib = {"Título", "Tipo", "Sua Nota"};
        modelBiblioteca = new DefaultTableModel(colunasBib, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaBiblioteca = new JTable(modelBiblioteca);
        tabelaBiblioteca.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        painelInferior.add(new JScrollPane(tabelaBiblioteca), BorderLayout.CENTER);

        JPanel botoesBib = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnAddMidia = new JButton("Add Mídia");
        JButton btnEditMidia = new JButton("Editar Nota");
        JButton btnDelMidia = new JButton("Del Mídia");
        botoesBib.add(btnAddMidia);
        botoesBib.add(btnEditMidia);
        botoesBib.add(btnDelMidia);
        painelInferior.add(botoesBib, BorderLayout.SOUTH);

        // --- JSplitPane (Divisor) ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                painelSuperior,
                painelInferior);
        splitPane.setResizeWeight(0.45);
        add(splitPane, BorderLayout.CENTER);

        // --- Ações ---
        tabelaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaUsuarios.getSelectedRow() != -1) {
                int linha = tabelaUsuarios.getSelectedRow();
                String email = (String) modelUsuarios.getValueAt(linha, 1);
                usuarioSelecionado = usuarioController.buscarUsuario(email);
                atualizarTabelaBiblioteca();
            }
        });

        btnAddUser.addActionListener(e -> acaoAdicionarUsuario());
        btnEditUser.addActionListener(e -> acaoEditarUsuario());
        btnDelUser.addActionListener(e -> acaoRemoverUsuario());

        btnAddMidia.addActionListener(e -> acaoAdicionarMidia());
        btnEditMidia.addActionListener(e -> acaoEditarMidia());
        btnDelMidia.addActionListener(e -> acaoRemoverMidia());

        // --- Carrega dados iniciais ---
        atualizarTabelaUsuarios();
    }

    // --- Métodos de Atualização ---
    public void atualizarTabelaUsuarios() {
        modelUsuarios.setRowCount(0);
        List<Usuario> usuarios = usuarioController.listarUsuarios();
        for (Usuario u : usuarios) {
            modelUsuarios.addRow(new Object[]{
                    u.getNome(),
                    u.getEmail(),
                    u.getBiblioteca().size()
            });
        }
    }

    private void atualizarTabelaBiblioteca() {
        modelBiblioteca.setRowCount(0);
        if (usuarioSelecionado != null) {
            lblBibliotecaDe.setText("Biblioteca de: " + usuarioSelecionado.getNome());
            List<Midia> bib = usuarioSelecionado.getBiblioteca();
            for (Midia m : bib) {
                modelBiblioteca.addRow(new Object[]{
                        m.getTitulo(),
                        m.getTipo(),
                        m.getNota()
                });
            }
        } else {
            lblBibliotecaDe.setText("Selecione um usuário na tabela acima.");
        }
    }

    /**
     * Cria um formulário reutilizável para Usuário.
     */
    private JPanel criarFormularioUsuario(Usuario usuario) {
        JTextField txtNome = new JTextField(usuario != null ? usuario.getNome() : "");
        JTextField txtEmail = new JTextField(usuario != null ? usuario.getEmail() : "");

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        painel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        painel.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        painel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        painel.add(txtEmail, gbc);

        painel.putClientProperty("campos", new Object[]{txtNome, txtEmail});
        return painel;
    }

    // --- Métodos de Ação (CRUD Usuário) ---

    private void acaoAdicionarUsuario() {
        JPanel painelDialog = criarFormularioUsuario(null);
        int result = JOptionPane.showConfirmDialog(this, painelDialog, "Adicionar Novo Usuário", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Object[] campos = (Object[]) painelDialog.getClientProperty("campos");
            JTextField txtNome = (JTextField) campos[0];
            JTextField txtEmail = (JTextField) campos[1];

            try {
                boolean sucesso = usuarioController.criarUsuario(txtNome.getText(), txtEmail.getText());
                if (!sucesso) {
                    JOptionPane.showMessageDialog(this, "Erro: Email '" + txtEmail.getText() + "' já cadastrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
                atualizarTabelaUsuarios();
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void acaoEditarUsuario() {
        if (usuarioSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário primeiro.");
            return;
        }

        String emailOriginal = usuarioSelecionado.getEmail();

        JPanel painelDialog = criarFormularioUsuario(usuarioSelecionado);
        int result = JOptionPane.showConfirmDialog(this, painelDialog, "Editar Usuário: " + usuarioSelecionado.getNome(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Object[] campos = (Object[]) painelDialog.getClientProperty("campos");
            JTextField txtNome = (JTextField) campos[0];
            JTextField txtEmail = (JTextField) campos[1];

            String novoEmail = txtEmail.getText();
            String novoNome = txtNome.getText();

            try {
                boolean sucesso = usuarioController.editarUsuario(usuarioSelecionado.getEmail(), novoNome, novoEmail);

                if (!sucesso) {
                    JOptionPane.showMessageDialog(this, "Erro: Novo email '" + novoEmail + "' já está em uso.", "Erro", JOptionPane.ERROR_MESSAGE);
                    usuarioSelecionado = usuarioController.buscarUsuario(emailOriginal);
                } else {
                    usuarioSelecionado = usuarioController.buscarUsuario(novoEmail);
                }

                atualizarTabelaUsuarios();
                atualizarTabelaBiblioteca();

            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void acaoRemoverUsuario() {
        if (usuarioSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário primeiro.");
            return;
        }
        int resp = JOptionPane.showConfirmDialog(this, "Tem certeza que quer remover " + usuarioSelecionado.getNome() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            usuarioController.removerUsuario(usuarioSelecionado.getEmail());
            usuarioSelecionado = null;
            atualizarTabelaUsuarios();
            atualizarTabelaBiblioteca();
        }
    }

    // --- Métodos de Ação (CRUD Biblioteca) ---

    private void acaoAdicionarMidia() {
        if (usuarioSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário primeiro.");
            return;
        }

        String titulo = JOptionPane.showInputDialog(this, "Digite o título da Mídia (da Loja) para adicionar:");

        if (titulo == null || titulo.isBlank()) return;
        titulo = titulo.trim();

        Midia midia = jogoController.buscarJogo(titulo);
        if (midia == null) {
            midia = dlcController.buscarDLC(titulo);
        }

        if (midia == null) {
            JOptionPane.showMessageDialog(this, "Mídia não encontrada na Loja.", "Erro", JOptionPane.ERROR_MESSAGE);
        } else {
            boolean sucesso = usuarioController.adicionarMidiaAoUsuario(usuarioSelecionado.getEmail(), midia);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Mídia adicionada com sucesso!");
                atualizarTabelaBiblioteca();
                atualizarTabelaUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "Erro: Não foi possível adicionar (talvez já possua).", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void acaoEditarMidia() {
        if (usuarioSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário primeiro.");
            return;
        }
        int linha = tabelaBiblioteca.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma mídia da biblioteca.");
            return;
        }
        String titulo = (String) modelBiblioteca.getValueAt(linha, 0);
        double notaAtual = (Double) modelBiblioteca.getValueAt(linha, 2);

        String novaNotaStr = JOptionPane.showInputDialog(this, "Sua nota para '" + titulo + "':", notaAtual);
        if (novaNotaStr != null) {
            try {
                double novaNota = Double.parseDouble(novaNotaStr);
                if (novaNota < 0 || novaNota > 10) {
                    JOptionPane.showMessageDialog(this, "Nota deve ser entre 0 e 10.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                usuarioController.editarMidiaDoUsuario(usuarioSelecionado.getEmail(), titulo, novaNota);
                atualizarTabelaBiblioteca();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Nota inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void acaoRemoverMidia() {
        if (usuarioSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário primeiro.");
            return;
        }
        int linha = tabelaBiblioteca.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma mídia da biblioteca.");
            return;
        }
        String titulo = (String) modelBiblioteca.getValueAt(linha, 0);

        int resp = JOptionPane.showConfirmDialog(this, "Remover '" + titulo + "' da sua biblioteca?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            usuarioController.removerMidiaDoUsuario(usuarioSelecionado.getEmail(), titulo);
            atualizarTabelaBiblioteca();
            atualizarTabelaUsuarios();
        }
    }
}