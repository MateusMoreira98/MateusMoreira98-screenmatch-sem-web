package br.com.moreira.screenmatch.service;

public interface IConverteDados {
     <T> T obterDados(String json, Class<T> classe);
}
