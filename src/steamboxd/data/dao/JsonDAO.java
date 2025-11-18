package steamboxd.data.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import steamboxd.data.DadosSistema;
import steamboxd.model.DLC;
import steamboxd.model.Jogo;
import steamboxd.model.Midia;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * Implementação da interface {@link PersistenciaDAO} que utiliza
 * a biblioteca Gson para salvar e carregar dados em formato JSON.
 *
 * <p>Esta implementação específica foca em criar um arquivo .json
 * legível por humanos.</p>
 */
public class JsonDAO implements PersistenciaDAO {

    private final Gson gson;

    public JsonDAO() {
        RuntimeTypeAdapterFactory<Midia> adapter = RuntimeTypeAdapterFactory
                .of(Midia.class, "type")
                .registerSubtype(Jogo.class, "Jogo")
                .registerSubtype(DLC.class, "DLC");

        this.gson = new GsonBuilder()
                .registerTypeAdapterFactory(adapter)
                .setPrettyPrinting()
                .create();
    }

    /**
     * {@inheritDoc}
     * <p>Esta implementação usa Gson para converter o objeto
     * {@link DadosSistema} em uma string JSON e salvá-la
     * no arquivo especificado.</p>
     */
    @Override
    public void salvar(DadosSistema dados, String nomeArquivo) throws Exception {
        try (Writer writer = new FileWriter(nomeArquivo)) {
            gson.toJson(dados, writer);
        }
    }

    /**
     * {@inheritDoc}
     * <p>Esta implementação usa Gson para ler o arquivo .json
     * e convertê-lo de volta em uma instância de
     * {@link DadosSistema}.</p>
     */
    @Override
    public DadosSistema carregar(String nomeArquivo) throws Exception {
        try (Reader reader = new FileReader(nomeArquivo)) {
            return gson.fromJson(reader, DadosSistema.class);
        }
    }
}