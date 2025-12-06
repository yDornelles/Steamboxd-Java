package steamboxd.data.dao;

import steamboxd.data.DadosSistema;
import steamboxd.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TxtDAO implements PersistenciaDAO {

    private static final String SEPARADOR = ";";

    @Override
    public void salvar(DadosSistema dados, String nomeArquivo) throws Exception {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {

            // 1. Salvar Jogos
            for (Jogo j : dados.getJogos()) {
                String generos = String.join(",", j.getGeneros());
                String plataformas = String.join(",", j.getPlataformas());

                writer.printf("JOGO%s%s%s%d%s%.2f%s%s%s%b%s%s%s%s%n",
                        SEPARADOR, j.getTitulo(),
                        SEPARADOR, j.getAnoLancamento(),
                        SEPARADOR, j.getPreco(),
                        SEPARADOR, j.getDesenvolvedora(),
                        SEPARADOR, j.isMultiplayer(),
                        SEPARADOR, generos,
                        SEPARADOR, plataformas);
            }

            // 2. Salvar DLCs
            for (DLC d : dados.getDlcs()) {
                String generos = String.join(",", d.getGeneros());
                String plataformas = String.join(",", d.getPlataformas());

                writer.printf("DLC%s%s%s%d%s%.2f%s%s%s%b%s%s%s%s%n",
                        SEPARADOR, d.getTitulo(),
                        SEPARADOR, d.getAnoLancamento(),
                        SEPARADOR, d.getPreco(),
                        SEPARADOR, d.getJogoBaseTitulo(),
                        SEPARADOR, d.isExpansao(),
                        SEPARADOR, generos,
                        SEPARADOR, plataformas);
            }

            // 3. Salvar Usuários
            for (Usuario u : dados.getUsuarios()) {
                writer.printf("USUARIO%s%s%s%s%n", SEPARADOR, u.getNome(), SEPARADOR, u.getEmail());

                for (Midia m : u.getBiblioteca()) {
                    writer.printf("BIBLIOTECA%s%s%s%s%s%.2f%s%d%s%.2f%s%s%n",
                            SEPARADOR, u.getEmail(),
                            SEPARADOR, m.getTitulo(),
                            SEPARADOR, m.getNota(),
                            SEPARADOR, m.getAnoLancamento(),
                            SEPARADOR, m.getPreco(),
                            SEPARADOR, m.getTipo());
                }
            }
        }
    }

    @Override
    public DadosSistema carregar(String nomeArquivo) throws Exception {
        List<Jogo> jogos = new ArrayList<>();
        List<DLC> dlcs = new ArrayList<>();
        List<Usuario> usuarios = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                // Split com -1 para manter campos vazios
                String[] partes = linha.split(SEPARADOR, -1);
                String tipo = partes[0];

                try {
                    switch (tipo) {
                        case "JOGO":
                            String tituloJ = getCampo(partes, 1);
                            int anoJ = parseInt(getCampo(partes, 2));
                            double precoJ = parseDouble(getCampo(partes, 3));
                            String dev = getCampo(partes, 4);
                            boolean multi = Boolean.parseBoolean(getCampo(partes, 5));
                            List<String> gensJ = parseLista(getCampo(partes, 6));
                            List<String> platsJ = parseLista(getCampo(partes, 7));

                            jogos.add(new Jogo(tituloJ, gensJ, 0.0, anoJ, platsJ, dev, multi, precoJ));
                            break;

                        case "DLC":
                            String tituloD = getCampo(partes, 1);
                            int anoD = parseInt(getCampo(partes, 2));
                            double precoD = parseDouble(getCampo(partes, 3));
                            String jogoBase = getCampo(partes, 4);
                            boolean exp = Boolean.parseBoolean(getCampo(partes, 5));
                            List<String> gensD = parseLista(getCampo(partes, 6));
                            List<String> platsD = parseLista(getCampo(partes, 7));

                            dlcs.add(new DLC(tituloD, gensD, 0.0, anoD, platsD, jogoBase, exp, precoD));
                            break;

                        case "USUARIO":
                            usuarios.add(new Usuario(partes[1], partes[2]));
                            break;

                        case "BIBLIOTECA":
                            String email = partes[1];
                            String tituloM = partes[2];
                            double nota = parseDouble(partes[3]);
                            int anoM = parseInt(partes[4]);
                            double precoM = parseDouble(partes[5]);
                            String tipoM = partes[6];

                            Usuario dono = usuarios.stream()
                                    .filter(u -> u.getEmail().equalsIgnoreCase(email))
                                    .findFirst().orElse(null);

                            if (dono != null) {
                                Midia m;
                                if (tipoM.equals("Jogo")) {
                                    m = new Jogo(tituloM, new ArrayList<>(), nota, anoM, new ArrayList<>(), "", false, precoM);
                                    // Tenta recuperar dados extras se o jogo existir na loja carregada
                                    Jogo original = jogos.stream().filter(j -> j.getTitulo().equals(tituloM)).findFirst().orElse(null);
                                    if(original != null) {
                                        m.setGeneros(original.getGeneros());
                                        m.setPlataformas(original.getPlataformas());
                                        ((Jogo)m).setDesenvolvedora(original.getDesenvolvedora());
                                        ((Jogo)m).setMultiplayer(original.isMultiplayer());
                                    }
                                } else {
                                    m = new DLC(tituloM, new ArrayList<>(), nota, anoM, new ArrayList<>(), "", false, precoM);
                                    DLC original = dlcs.stream().filter(d -> d.getTitulo().equals(tituloM)).findFirst().orElse(null);
                                    if(original != null) {
                                        m.setGeneros(original.getGeneros());
                                        m.setPlataformas(original.getPlataformas());
                                        ((DLC)m).setJogoBaseTitulo(original.getJogoBaseTitulo());
                                        ((DLC)m).setExpansao(original.isExpansao());
                                    }
                                }

                                // Adiciona na lista interna do usuário
                                List<Midia> lib = dono.getBiblioteca();
                                lib.add(m);
                                dono.setBiblioteca(lib);
                            }
                            break;
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao ler linha: " + linha + " -> " + e.getMessage());
                }
            }
        }
        return new DadosSistema(jogos, dlcs, usuarios);
    }

    // Métodos auxiliares para evitar erros de parse
    private String getCampo(String[] partes, int indice) {
        return (indice < partes.length) ? partes[indice] : "";
    }

    private int parseInt(String s) {
        if (s == null || s.isEmpty()) return 0;
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return 0; }
    }

    private double parseDouble(String s) {
        if (s == null || s.isEmpty()) return 0.0;
        try { return Double.parseDouble(s.replace(",", ".")); } catch (NumberFormatException e) { return 0.0; }
    }

    private List<String> parseLista(String dados) {
        if (dados == null || dados.isEmpty() || dados.equals("N/A")) return new ArrayList<>();
        return Arrays.stream(dados.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}