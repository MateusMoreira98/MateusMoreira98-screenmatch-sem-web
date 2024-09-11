package br.com.moreira.screenmatch.principal;

import br.com.moreira.screenmatch.model.DadosEpisodio;
import br.com.moreira.screenmatch.model.DadosSerie;
import br.com.moreira.screenmatch.model.DadosTemporadas;
import br.com.moreira.screenmatch.model.Episodio;
import br.com.moreira.screenmatch.service.ConsumoAPI;
import br.com.moreira.screenmatch.service.ConverteDados;
import ch.qos.logback.core.encoder.JsonEscapeUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    Scanner leitura = new Scanner(System.in);

    private ConsumoAPI consumo = new ConsumoAPI();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY =  "&apikey=4f5be9c3";

    private ConverteDados conversor = new ConverteDados();


    public void exibirMenu(){

        System.out.println("Digite o nome de uma Serie que Deseja: ");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterHttp(
                ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporadas> temporadas = new ArrayList<>();

		for (int i = 1; i <= dados.TotalTemporadas(); i++) {
			json = consumo.obterHttp(ENDERECO + nomeSerie.replace(" ", "+") +"&season=" + i + API_KEY);
			DadosTemporadas dadosTemporadas = conversor.obterDados(json, DadosTemporadas.class);
			temporadas.add(dadosTemporadas);
		}

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodio().stream())
                .collect(Collectors.toList());

        System.out.println();
        System.out.println("Os Top 5 Episodios mais Bem Votados: ");
        dadosEpisodios.stream()
            .filter(e -> !e.Avaliacao().equalsIgnoreCase("N/A"))
            .sorted(Comparator.comparing(DadosEpisodio::Avaliacao).reversed())
            .limit(5).forEach(System.out::println);

        System.out.println();
        System.out.println("===========================================");
        System.out.println("Detalhes das Temporadas e seus respectivos Episodios: ");
        System.out.println("===========================================");
        System.out.println();

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodio().stream()
                        .map(d -> new Episodio(t.numero(),d))
                ) .collect(Collectors.toList());

        episodios.forEach(System.out::println);
    }
}
