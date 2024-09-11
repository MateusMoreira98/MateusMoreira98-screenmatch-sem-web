package br.com.moreira.screenmatch.model;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;


public class Episodio {
   private Integer temporada;
   private String titulo;
   private Integer numeroEpisodio;
   private Double avaliacao;
   private LocalDate datalancamento;

    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio) {
         this.temporada = numeroTemporada;
         this.titulo = dadosEpisodio.Titulo();
         this.numeroEpisodio = dadosEpisodio.Episodio();
        try {
            this.avaliacao = Double.valueOf(dadosEpisodio.Avaliacao());
        } catch (NumberFormatException ex) {
            this.avaliacao = 0.0;
        }
        try {
            this.datalancamento = LocalDate.parse(dadosEpisodio.Lancamento());
        } catch (DateTimeParseException ex) {
            this.datalancamento = null;
        }
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDatalancamento() {
        return datalancamento;
    }

    public void setDatalancamento(LocalDate datalancamento) {
        this.datalancamento = datalancamento;
    }

    @Override
    public String toString() {
        return "Temporada= " + temporada +
                ", Titulo= " + titulo + '\'' +
                ", Episodio= " + numeroEpisodio +
                ", Avaliacao= " + avaliacao +
                ", DataLancamento= " + datalancamento;
    }
}