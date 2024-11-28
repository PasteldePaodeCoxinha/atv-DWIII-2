package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.modelo.TelefoneSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;


@RestController
@RequestMapping("/telefone")
public class TelefoneController {
	@Autowired
	private TelefoneRepositorio repositorio;
	@Autowired
	private TelefoneSelecionador selecionador;
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	@Autowired
	private ClienteSelecionador clienteSelecionador;

	@GetMapping("/telefone/{id}")
	public Telefone obterTelefone(@PathVariable long id) {
		return selecionador.selecionar(repositorio, id);
	}

	@GetMapping("/telefones")
	public List<Telefone> obterTelefones() {
		List<Telefone> telefones = repositorio.findAll();
		return telefones;
	}

	@PostMapping("/cadastro")
	public void cadastrarTelefones(@RequestBody Telefone telefone) {
		repositorio.save(telefone);
	}

	@PutMapping("/atualizar")
	public void atualizarTelefone(@RequestBody Telefone atualizacao) {
		Telefone telefone = repositorio.getById(atualizacao.getId());
		TelefoneAtualizador atualizador = new TelefoneAtualizador();
		atualizador.atualizar(telefone, atualizacao);
		repositorio.save(telefone);
	}

	@DeleteMapping("/excluir")
	public void excluirTelefone(@RequestBody Telefone exclusao) {
		Telefone telefone = repositorio.getById(exclusao.getId());
		Cliente cliente = clienteRepositorio.findByTelefones(telefone);
		cliente.setTelefones(cliente.getTelefones().stream().filter(t -> t.getId() != telefone.getId()).toList());
		repositorio.delete(telefone);
	}
	
	@GetMapping("/cliente/{id}")
	public List<Telefone> pegarTelefonesCliente(@PathVariable long id){
		Cliente cliente = clienteSelecionador.selecionar(clienteRepositorio, id);
		return cliente.getTelefones();
	}
}
