import steamboxd.view.FabricaDeInterfaces;
import steamboxd.view.IAppView;
import steamboxd.view.UIFactory;

/**
 * Ponto de entrada principal da aplicação.
 * Responsável por inicializar a Fábrica Abstrata
 * e executar a implementação da View (seja textual ou gráfica).
 */
public class Main {

    public static void main(String[] args) {

        // Pede à fábrica estática a fábrica correta
        UIFactory factory = FabricaDeInterfaces.getFactory();

        // A fábrica (TextualUIFactory ou GraficaUIFactory)
        // cria o produto (TextualAppView ou GraficaAppView)
        IAppView app = factory.criarAppView();

        app.run();
    }
}