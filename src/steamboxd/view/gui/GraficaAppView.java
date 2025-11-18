package steamboxd.view.gui;

import steamboxd.data.Sistema;
import steamboxd.view.IAppView;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Component;

/**
 * Produto Concreto (GUI).
 * Implementação da IAppView para uma interface gráfica (Swing).
 * Esta é a janela principal (JFrame) da aplicação.
 */
public class GraficaAppView implements IAppView {

    private final Sistema sistema;

    public GraficaAppView() {
        this.sistema = Sistema.getInstance();
    }

    @Override
    public void run() {
        // Garante que o código Swing rode na Thread de Eventos
        SwingUtilities.invokeLater(() -> {

            try {
                // Aplica o tema nativo
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Se der erro, não faz nada e usa o padrão feio do Java
                e.printStackTrace();
            }

            sistema.carregarDados();

            // Cria a Janela Principal
            JFrame frame = new JFrame("SteamBoxd - Gerenciador");
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);

            // Cria a Barra de Menu (Salvar/Carregar)
            JMenuBar menuBar = new JMenuBar();
            JMenu menuArquivo = new JMenu("Arquivo");

            JMenuItem itemSalvar = new JMenuItem("Salvar");
            itemSalvar.addActionListener(e -> {
                sistema.salvarDados(); //
                JOptionPane.showMessageDialog(frame, "Dados salvos com sucesso!");
            });

            JMenuItem itemCarregar = new JMenuItem("Carregar");
            itemCarregar.addActionListener(e -> {
                sistema.carregarDados();
                JOptionPane.showMessageDialog(frame, "Dados recarregados! Troque de aba para ver as atualizações.");
            });

            JMenuItem itemSair = new JMenuItem("Sair");
            itemSair.addActionListener(e -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));

            menuArquivo.add(itemSalvar);
            menuArquivo.add(itemCarregar);
            menuArquivo.addSeparator();
            menuArquivo.add(itemSair);
            menuBar.add(menuArquivo);
            frame.setJMenuBar(menuBar);

            // Cria as Abas (JTabbedPane)
            JTabbedPane tabbedPane = new JTabbedPane();

            PainelJogos painelJogos = new PainelJogos();
            PainelDLCs painelDLCs = new PainelDLCs();
            PainelUsuarios painelUsuarios = new PainelUsuarios();

            // Adiciona os painéis de CRUD
            tabbedPane.addTab("Jogos", painelJogos);
            tabbedPane.addTab("DLCs", painelDLCs);
            tabbedPane.addTab("Usuários", painelUsuarios);

            tabbedPane.addChangeListener(e -> {
                Component abaSelecionada = tabbedPane.getSelectedComponent();

                if (abaSelecionada == painelJogos) {
                    painelJogos.atualizarTabela();
                }
                else if (abaSelecionada == painelUsuarios) {
                    painelUsuarios.atualizarTabelaUsuarios();
                }
            });

            frame.add(tabbedPane);

            // Lógica de "Salvar ao Sair"
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    int resp = JOptionPane.showConfirmDialog(frame,
                            "Deseja salvar os dados antes de sair?",
                            "Sair",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE);

                    if (resp == JOptionPane.YES_OPTION) {
                        sistema.salvarDados(); //
                        System.exit(0);
                    } else if (resp == JOptionPane.NO_OPTION) {
                        System.exit(0);
                    }
                }
            });

            // Exibe a janela
            frame.setVisible(true);
        });
    }
}