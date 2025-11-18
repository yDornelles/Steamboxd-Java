package steamboxd.view.gui;

import steamboxd.view.IAppView;
import steamboxd.view.UIFactory;

/**
 * Fábrica Concreta (GUI).
 * Sabe como criar a implementação da View Gráfica.
 */
public class GraficaUIFactory implements UIFactory {

    @Override
    public IAppView criarAppView() {
        return new GraficaAppView();
    }
}