package steamboxd.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe base abstrata para todas as mídias.
 *
 * <p>Esta classe define o "contrato" e os atributos comuns que
 * toda mídia no sistema deve ter.</p>
 *
 * <p>Implementa {@link Serializable} para permitir que todos os seus
 * subtipos (Jogo, DLC) possam ser facilmente persistidos (salvos)
 * em arquivos.</p>
 */
public abstract class Midia implements Serializable {

    private String titulo;
    private List<String> generos;
    private double nota;
    private int anoLancamento;
    private List<String> plataformas;

    /**
     * Construtor completo e principal da classe Midia.
     *
     * <p>Garante a segurança contra nulos
     * e cria <strong>cópias defensivas</strong> das listas para garantir
     * o encapsulamento.</p>
     */
    public Midia (String titulo, List<String> generos, double nota, int anoLancamento, List<String> plataformas) {
        this.titulo = titulo != null ? titulo : "Sem título";
        this.generos = generos != null ? new ArrayList<>(generos) : new ArrayList<>();
        this.nota = nota;
        this.anoLancamento = anoLancamento;
        this.plataformas = plataformas != null ? new ArrayList<>(plataformas) : new ArrayList<>();
    }

    /**
     * Construtor parcial para criar uma Mídia apenas com o título.
     * <p>
     * Chama o construtor completo
     * para definir valores padrão e evitar duplicação de código.
     * </p>
     */
    public Midia (String titulo) {
        this(titulo, new ArrayList<>(), 0.0, 0, new ArrayList<>());
    }

    /**
     * Construtor vazio.
     * <p>
     * Extremamente necessário para a persistência em JSON.
     * </p>
     */
    public Midia () {
        this("Sem título");
    }

    /**
     * Construtor de Cópia (para clonagem).
     * Cria uma nova Mídia baseada em uma original.
     * É 'protected' para ser usado apenas pelas classes filhas (Jogo, DLC).
     */
    protected Midia(Midia original) {
        this(original.titulo, original.generos, original.nota, original.anoLancamento, original.plataformas);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título da mídia é obrigatório.");
        }
        this.titulo = titulo.trim();
    }

    public List<String> getGeneros() {
        return new ArrayList<>(generos);
    }

    public void setGeneros(List<String> generos) {
        if (generos == null) {
            this.generos = new ArrayList<>();
        } else {
            this.generos = new ArrayList<>(generos);
        }
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        if (nota < 0 || nota > 10) {
            throw new IllegalArgumentException("A nota deve ser entre 0 e 10.");
        }
        this.nota = nota;
    }

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        if (anoLancamento > 2026) {
            throw new IllegalArgumentException("O ano de lançamento não pode ser futuro.");
        }
        if (anoLancamento != 0 && anoLancamento < 1950) {
            throw new IllegalArgumentException("Ano inválido (muito antigo).");
        }
        this.anoLancamento = anoLancamento;
    }

    public List<String> getPlataformas() {
        return new ArrayList<>(plataformas);
    }

    public void setPlataformas(List<String> plataformas) {
        if (plataformas == null) {
            this.plataformas = new ArrayList<>();
        } else {
            this.plataformas = new ArrayList<>(plataformas);
        }
    }

    /**
     * Método de contrato abstrato.
     * @return Uma String representando o tipo da Mídia.
     */
    public abstract String getTipo();

    public abstract double getPreco();

    public abstract void setPreco(double preco);
}
