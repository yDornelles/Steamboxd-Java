package steamboxd.view.textual;

import steamboxd.view.IAppView;
import steamboxd.view.UIFactory;

/**
 * Fábrica Concreta
 * Sabe como criar os componentes de uma UI Textual.
 */
public class TextualUIFactory implements UIFactory {

    @Override
    public IAppView criarAppView() {
        return new TextualAppView();
    }
}