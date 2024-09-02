package br.com.moreira.screenmatch;

import br.com.moreira.screenmatch.model.DadosEpisodio;
import br.com.moreira.screenmatch.model.DadosSerie;
import br.com.moreira.screenmatch.service.ConsumoAPI;
import br.com.moreira.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		var consumoApi = new ConsumoAPI();
		var json = consumoApi.obterHTTP("https://www.omdbapi.com/?t=gilmore+girls&apikey=4f5be9c3");
		System.out.println(json);

		ConverteDados conversor = new ConverteDados();

		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);

		json = consumoApi.obterHTTP("https://www.omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=4f5be9c3");
		DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);
		System.out.println(dadosEpisodio);
	}
}
