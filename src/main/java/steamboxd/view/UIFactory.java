package steamboxd.view;

/**
 * Interface (Fábrica Abstrata) que define o contrato
 * para criar os componentes da interface.
 */
public interface UIFactory {
    /**
     * Cria e retorna a implementação da visão principal.
     * @return um objeto que implementa IAppView
     */
    IAppView criarAppView();
}