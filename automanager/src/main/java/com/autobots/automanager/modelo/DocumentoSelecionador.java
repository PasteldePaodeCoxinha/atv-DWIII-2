package com.autobots.automanager.modelo;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@Component
public class DocumentoSelecionador {
	public Documento selecionar(DocumentoRepositorio bancoDocumento, long id) {
		Documento selecionado = bancoDocumento.findById(id).orElseGet(null);
		return selecionado;
	}
}