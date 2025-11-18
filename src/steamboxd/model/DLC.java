package steamboxd.model;

import java.util.List;

/**
 * Modela uma DLC como um tipo de Mídia.
 * <p>Esta classe estende {@link Midia}, adicionando atributos específicos
 * de uma DLC</p>
 */
public class DLC extends Midia {

    private String jogoBase;
    private boolean expansao;
    private double preco;

    /**
     * Construtor completo para criar uma DLC com todos os detalhes.
     * <p>
     * Garante a segurança contra nulos
     * convertendo listas e strings nulas para valores padrão.
     * </p>
     */
    public DLC (String titulo, List<String> generos, double nota, int anoLancamento, List<String> plataformas, String jogoBase, boolean expansao, double preco) {
        super(titulo, generos != null ? generos : List.of(), nota, anoLancamento, plataformas != null ? plataformas : List.of());
        this.jogoBase = jogoBase != null ? jogoBase : "";
        this.expansao = expansao;
        this.preco = preco;
    }

    /**
     * Construtor parcial para criar uma DLC com informações essenciais.
     * <p>
     * Útil quando apenas o título e o jogo base são conhecidos,
     * inicializando os demais campos com valores padrão.
     * </p>
     */
    public DLC (String titulo, String jogoBase) {
        super(titulo);
        this.jogoBase = jogoBase != null ? jogoBase : "";
        this.expansao = false;
        this.preco = 0.0;
    }

    /**
     * Construtor vazio.
     * <p>
     * Extremamente necessário para a persistência em JSON.
     * </p>
     */
    public DLC () {
        super();
        this.jogoBase = "";
        this.expansao = false;
        this.preco = 0.0;
    }

    /**
     * Construtor de Cópia (para clonagem).
     * Cria uma nova DLC que é uma cópia exata de outra.
     */
    public DLC(DLC original) {
        super(original);
        this.jogoBase = original.jogoBase;
        this.expansao = original.expansao;
        this.preco = original.preco;
    }

    public String getJogoBase() {
        return jogoBase;
    }

    public void setJogoBase(String jogoBase) {
        this.jogoBase = jogoBase;
    }

    public boolean isExpansao() {
        return expansao;
    }

    public void setExpansao(boolean expansao) {
        this.expansao = expansao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    /**
     * Retorna o tipo da mídia, implementando o contrato de {@link Midia}.
     * @return A string literal "DLC", identificando este objeto.
     */
    @Override
    public String getTipo() {
        return "DLC";
    }
}
