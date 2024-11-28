package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
	
	Cliente findByDocumentos(Documento documento);
	Cliente findByEndereco(Endereco endereco);
	Cliente findByTelefones(Telefone telefone);
}