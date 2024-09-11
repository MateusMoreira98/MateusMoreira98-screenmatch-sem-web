package br.com.moreira.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodio(@JsonAlias("Title") String Titulo,
                            @JsonAlias("Episode") Integer Episodio,
                            @JsonAlias("imdbRating") String Avaliacao,
                            @JsonAlias("Released") String Lancamento) {
}
