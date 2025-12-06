package steamboxd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Modela um Usuário no sistema.
 *
 * <p>Esta classe armazena informações de identificação
 * e a coleção de mídias ({@link Midia}) que o usuário possui.</p>
 *
 * <p>Implementa {@link Serializable} para permitir que objetos
 * de Usuário sejam facilmente salvos em disco (persistência de dados).</p>
 */
public class Usuario implements Serializable {

    private String nome;
    private String email;
    private List<Midia> biblioteca;

    /**
     * Construtor completo para criar um Usuário com uma biblioteca pré-existente.
     *
     * <p>Cria uma <strong>cópia defensiva</strong> da lista de biblioteca
     * para proteger o encapsulamento. Modificações na lista original
     * (passada como parâmetro) não afetarão este objeto.</p>
     */
    public Usuario(String nome, String email, List<Midia> biblioteca) {
        this.nome = nome;
        this.email = email;
        this.biblioteca = new ArrayList<>(biblioteca);
    }

    /**
     * Construtor parcial para criar um novo Usuário (sem biblioteca).
     *
     * <p>Inicializa a biblioteca como uma lista vazia para
     * evitar {@link NullPointerException} em operações futuras.</p>
     */
    public Usuario(String nome, String email) {
        this.nome = nome;
        this.email = email;
        this.biblioteca = new ArrayList<>();
    }

    /**
     * Construtor vazio.
     * <p>
     * Extremamente necessário para a persistência em JSON.
     * </p>
     */
    public Usuario() {
        this.nome = "";
        this.email = "";
        this.biblioteca = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null) {
            throw new IllegalArgumentException("O nome do usuário é obrigatório.");
        }

        nome = nome.trim();

        if (nome.isBlank()) {
            throw new IllegalArgumentException("O novo nome não pode ser vazio.");
        }

        if (nome.matches(".*\\d.*")) {
            throw new IllegalArgumentException("O nome do usuário não pode conter números.");
        }

        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("O email do usuário é obrigatório.");
        }

        email = email.trim();

        if (email.isBlank()) {
            throw new IllegalArgumentException("O novo email não pode ser vazio.");
        }

        if (!email.contains("@gmail.com")) {
            throw new IllegalArgumentException("O email deve ser válido (conter '@').");
        }

        if (email.contains(" ")) {
            throw new IllegalArgumentException("O email não pode conter espaços.");
        }

        this.email = email;
    }

    /**
     * Retorna uma <strong>cópia</strong> da biblioteca de mídias do usuário.
     *
     * <p>A lista retornada é uma cópia defensiva. Modificações nela
     * <strong>não</strong> afetarão a biblioteca interna
     * do usuário, protegendo o encapsulamento.</p>
     *
     * @return Uma cópia da lista da biblioteca.
     */
    public List<Midia> getBiblioteca() {
        return new ArrayList<>(biblioteca);
    }

    /**
     * Define a biblioteca de mídias do usuário.
     *
     * <p>Cria uma <strong>cópia defensiva</strong> da lista fornecida
     * para garantir que modificações externas na lista original não afetem
     * o estado interno do objeto.</p>
     *
     * @param biblioteca A nova lista de mídias.
     */
    public void setBiblioteca(List<Midia> biblioteca) {
        this.biblioteca = new ArrayList<>(biblioteca);
    }
}