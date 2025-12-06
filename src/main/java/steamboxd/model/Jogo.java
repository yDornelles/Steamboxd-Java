package steamboxd.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Modela um Jogo como um tipo de Mídia.
 *
 * <p>Esta classe estende {@link Midia} para adicionar atributos
 * específicos de um jogo.</p>
 */
public class Jogo extends Midia {

    private String desenvolvedora;
    private boolean multiplayer;
    private double preco;
    private List<String> dlcTitulos;

    /**
     * Construtor completo para criar um Jogo com todos os detalhes.
     * <p>
     * Garante a segurança contra nulos
     * convertendo listas e strings nulas para valores padrão.
     * </p>
     */
    public Jogo (String titulo, List<String> generos, double nota, int anoLancamento, List<String> plataformas, String desenvolvedora, boolean multiplayer, double preco) {
        super(titulo, generos != null ? generos : List.of(), nota, anoLancamento, plataformas != null ? plataformas : List.of());
        this.desenvolvedora = desenvolvedora != null ? desenvolvedora : "";
        this.multiplayer = multiplayer;
        this.preco = preco;
        this.dlcTitulos = new ArrayList<>();
    }

    /**
     * Construtor intermediário para criação parcial.
     * <p>
     * Inicializa o jogo com dados essenciais e valores padrão
     * para os demais campos.
     * </p>
     */
    public Jogo(String titulo, List<String> generos, List<String> plataformas) {
        super(titulo, generos, 0.0, 0, plataformas);
        this.desenvolvedora = "";
        this.multiplayer = false;
        this.preco = 0.0;
        this.dlcTitulos = new ArrayList<>();
    }

    /**
     * Construtor parcial para busca ou referência.
     * <p>
     * Útil quando apenas o título (chave) é necessário para
     * identificar o jogo.
     * </p>
     */
    public Jogo (String titulo) {
        super(titulo);
        this.dlcTitulos = new ArrayList<>();
    }

    /**
     * Construtor vazio.
     * <p>
     * Extremamente necessário para a persistência em JSON.
     * </p>
     */
    public Jogo () {
        super();
        this.desenvolvedora = "";
        this.multiplayer = false;
        this.preco = 0.0;
        this.dlcTitulos = new ArrayList<>();
    }

    /**
     * Construtor de Cópia (para clonagem).
     * Cria um novo Jogo que é uma cópia exata de outro.
     */
    public Jogo(Jogo original) {
        super(original);
        this.desenvolvedora = original.desenvolvedora;
        this.multiplayer = original.multiplayer;
        this.preco = original.preco;
        this.dlcTitulos = new ArrayList<>(original.dlcTitulos);
    }

    public List<String> getDlcTitulos() {
        return new ArrayList<>(dlcTitulos);
    }

    public void setDlcTitulos(List<String> dlcTitulos) {
        if (dlcTitulos == null) {
            this.dlcTitulos = new ArrayList<>();
        } else {
            this.dlcTitulos = new ArrayList<>(dlcTitulos);
        }
    }

    public String getDesenvolvedora() {
        return desenvolvedora;
    }

    public void setDesenvolvedora(String desenvolvedora) {
        if (desenvolvedora == null) {
            this.desenvolvedora = "";
        } else {
            this.desenvolvedora = desenvolvedora.trim();
        }
    }

    public boolean isMultiplayer() {
        return multiplayer;
    }

    public void setMultiplayer(boolean multiplayer) {
        this.multiplayer = multiplayer;
    }

    @Override
    public double getPreco() {
        return preco;
    }

    @Override
    public void setPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }
        this.preco = preco;
    }

    /**
     * Retorna o tipo da mídia, implementando o contrato de {@link Midia}.
     *
     * @return A string literal "Jogo", identificando este objeto.
     */
    @Override
    public String getTipo() {
        return "Jogo";
    }
}
