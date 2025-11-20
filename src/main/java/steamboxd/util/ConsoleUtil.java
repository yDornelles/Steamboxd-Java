package steamboxd.util;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe utilitária estática para centralizar a leitura de dados do console.
 *
 * <p>Esta classe resolve dois problemas comuns do {@link Scanner}:
 * 1. Tratamento de entrada inválida.
 * 2. O '\n' deixado por métodos como nextInt().
 * </p>
 *
 * <p>Por ser uma classe puramente utilitária, ela não pode ser instanciada.</p>
 */
public class ConsoleUtil {

    /**
     * Instância única e estática do Scanner, compartilhada por todos os métodos.
     */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Construtor privado.
     * Impede que esta classe utilitária seja instanciada.
     */
    private ConsoleUtil() {
    }

    /**
     * Consome o caractere de nova linha ('\n') restante no buffer do scanner.
     *
     * Métodos como {@code nextInt()}, {@code nextDouble()}
     * leem o número, mas deixam o '\n' no buffer.
     * Se uma chamada a {@code nextLine()} ocorrer em seguida, ela lerá
     * esse '\n' e retornará uma string vazia, "pulando" a entrada
     * do usuário. Este método limpa esse '\n'.</p>
     */
    private static void limparBuffer() {
        scanner.nextLine();
    }

    /**
     * Lê uma linha de texto completa do console.
     *
     * @param mensagem O prompt a ser exibido ao usuário.
     * @return A string lida.
     */
    public static String lerString(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    /**
     * Lê um número inteiro, com tratamento de erro.
     * <p>O método fica em loop até que um inteiro válido seja digitado.</p>
     *
     * @param mensagem O prompt a ser exibido.
     * @return O inteiro lido.
     */
    public static int lerInt(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                int valor = scanner.nextInt();
                limparBuffer();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("Erro: Valor inválido. Digite apenas números inteiros.");
                limparBuffer();
            }
        }
    }

    /**
     * Lê um número inteiro, mas permite deixar em branco (Enter).
     * Se deixar em branco, retorna o valor padrão.
     *
     * @param mensagem O prompt a ser exibido.
     * @param valorPadrao O valor a retornar se for vazio.
     * @return O inteiro lido ou o padrão.
     */
    public static int lerIntOpcional(String mensagem, int valorPadrao) {
        while (true) {
            System.out.print(mensagem);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return valorPadrao;
            }

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Erro: Valor inválido. Digite um número inteiro ou deixe em branco.");
            }
        }
    }

    /**
     * Lê um número decimal, mas permite deixar em branco.
     * Aceita tanto ponto (29.90) quanto vírgula (29,90).
     *
     * @param mensagem O prompt a ser exibido.
     * @param valorPadrao O valor a retornar se for vazio.
     * @return O double lido ou o padrão.
     */
    public static double lerDoubleOpcional(String mensagem, double valorPadrao) {
        while (true) {
            System.out.print(mensagem);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return valorPadrao;
            }

            try {
                // Substitui vírgula por ponto para garantir que o Java entenda
                return Double.parseDouble(input.replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Erro: Valor inválido. Digite um número decimal ou deixe em branco.");
            }
        }
    }

    /**
     * Lê uma resposta booleana.
     * <p>O método força o usuário a digitar 's' ou 'n'.</p>
     *
     * @param mensagem O prompt.
     * @return {@code true} se o usuário digitar 's', {@code false} se digitar 'n'.
     */
    public static boolean lerBoolean(String mensagem) {
        while (true) {
            String input = lerString(mensagem).trim().toLowerCase();
            if ("s".equals(input)) {
                return true;
            } else if ("n".equals(input)) {
                return false;
            }
            System.out.println("Erro: Digite apenas 's' (sim) ou 'n' (não).");
        }
    }

    /**
     * Lê uma lista de strings do usuário.
     * <p>O usuário pode digitar múltiplos itens, um por linha.
     * A entrada é finalizada quando o usuário digita '0'.</p>
     *
     * @param mensagem O prompt inicial.
     * @return A lista ({@link ArrayList}) de strings lidas.
     */
    public static List<String> lerListaStrings(String mensagem) {
        List<String> lista = new ArrayList<>();
        System.out.println(mensagem + " (Digite '0' para parar):");

        while (true) {
            System.out.print("- ");
            String item = scanner.nextLine().trim();

            if ("0".equals(item)) {
                break;
            }
            if (!item.isBlank()) {
                lista.add(item);
            }
        }
        return lista;
    }
}