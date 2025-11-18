package steamboxd.data.dao;

import steamboxd.data.DadosSistema;

/**
 * Define o contrato para a camada de persistência de dados.
 * Esta interface abstrai a lógica de salvamento e carregamento.
 */
public interface PersistenciaDAO {

    /**
     * Serializa e salva o estado completo do sistema em um destino.
     *
     * @param dados O objeto contêiner {@link DadosSistema}
     * com todas as listas a serem salvas.
     * @param nomeArquivo O arquivo steamboxd.json.
     * @throws Exception Se ocorrer um erro de I/O (escrita) ou serialização.
     */
    void salvar(DadosSistema dados, String nomeArquivo) throws Exception;

    /**
     * Carrega e deserializa o estado completo do sistema de uma origem.
     *
     * @param nomeArquivo O arquivo steamboxd.json.
     * @return O objeto {@link DadosSistema} preenchido com os dados
     * carregados do arquivo.
     * @throws Exception Se ocorrer um erro de I/O (leitura),
     * deserialização, ou se o arquivo não for encontrado.
     */
    DadosSistema carregar(String nomeArquivo) throws Exception;
}