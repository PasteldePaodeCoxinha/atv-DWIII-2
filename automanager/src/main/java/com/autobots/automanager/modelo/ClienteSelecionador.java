package com.autobots.automanager.modelo;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@Component
public class ClienteSelecionador {
	public Cliente selecionar(ClienteRepositorio bancoCliente, long id) {
		Cliente selecionado = bancoCliente.findById(id).orElseGet(null);
		return selecionado;
	}
}