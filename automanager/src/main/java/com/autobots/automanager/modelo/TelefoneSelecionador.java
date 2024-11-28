package com.autobots.automanager.modelo;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@Component
public class TelefoneSelecionador {
	public Telefone selecionar(TelefoneRepositorio bancoTelefone, long id) {
		Telefone selecionado = bancoTelefone.findById(id).orElseGet(null);
		return selecionado;
	}
}