package br.com.moreira.screenmatch.principal;

import br.com.moreira.screenmatch.model.DadosEpisodio;
import br.com.moreira.screenmatch.model.DadosSerie;
import br.com.moreira.screenmatch.model.DadosTemporadas;
import br.com.moreira.screenmatch.model.Episodio;
import br.com.moreira.screenmatch.service.ConsumoAPI;
import br.com.moreira.screenmatch.service.ConverteDados;
import ch.qos.logback.core.encoder.JsonEscapeUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

        List<Episodio> episodio = temporadas.stream()
                .flatMap(t -> t.episodio().stream()
                        .map(d -> new Episodio(t.numero(),d))
                ) .collect(Collectors.toList());

        episodio.forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodio().stream()
                        .map(d -> new Episodio(t.numero(),d))
                ) .collect(Collectors.toList());

        episodios.forEach(System.out::println);



        System.out.println();
        System.out.println("===========================================");
        System.out.println("A partir de que ano você deseja ver os episodios? ");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano,1,1);

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getDatalancamento() != null && e.getDatalancamento().isAfter(dataBusca))
                .filter( filme -> filme.getAvaliacao() >= 3.0)

                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " Episódio: " + e.getTitulo() +
                                " Avaliacao: " + e.getAvaliacao() +
                                " Data Lancamento: " + e.getDatalancamento().format(formatador)
                ));

           // Busca por avaliações

        System.out.println();
        System.out.println("===========================================");
        System.out.println("Avaliação Detalhada: ");
        System.out.println("===========================================");
        System.out.println();
        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics estatistica = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Média: " + estatistica.getAverage());
        System.out.println("Melhor episódio: " + estatistica.getMax());
        System.out.println("Pior episódio: " + estatistica.getMin());
        System.out.println("Quantidade: " + estatistica.getCount());






    }
}
