package com.autobots.automanager.modelo;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@Component
public class EnderecoSelecionador {
	public Endereco selecionar(EnderecoRepositorio bancoEndereco, long id) {
		Endereco selecionado = bancoEndereco.findById(id).orElseGet(null);
		return selecionado;
	}
}
