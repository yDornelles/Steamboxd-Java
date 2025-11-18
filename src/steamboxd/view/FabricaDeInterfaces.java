package steamboxd.view;

import steamboxd.view.textual.TextualUIFactory;
import steamboxd.view.gui.GraficaUIFactory;

/**
 * Classe Estática.
 * Define qual fábrica concreta será usada.
 */
public class FabricaDeInterfaces {

    // TipoInterface.GRAFICA ou TipoInterface.TEXTUAL
    private static final TipoInterface TIPO_ATUAL = TipoInterface.TEXTUAL;

    /**
     * Método estático que retorna a fábrica concreta baseada na configuração.
     * @return uma implementação de UIFactory.
     */
    public static UIFactory getFactory() {
        switch (TIPO_ATUAL) {
            case TEXTUAL:
                return new TextualUIFactory();
            case GRAFICA:
                return new GraficaUIFactory();
            default:
                throw new IllegalStateException("Tipo de interface não configurado.");
        }
    }
}