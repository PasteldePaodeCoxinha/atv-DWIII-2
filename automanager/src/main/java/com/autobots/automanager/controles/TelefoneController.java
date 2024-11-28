package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.AdicionadorLinkTelefone;
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
	@Autowired
	private AdicionadorLinkTelefone adicionadorLink;

	@GetMapping("/telefone/{id}")
	public ResponseEntity<Telefone> obterTelefone(@PathVariable long id) {
		Telefone telefone = selecionador.selecionar(repositorio, id);
		if (telefone == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(telefone);
			return new ResponseEntity<Telefone>(telefone, HttpStatus.FOUND);
		}
	}

	@GetMapping("/telefones")
	public ResponseEntity<List<Telefone>> obterTelefones() {
		List<Telefone> telefones = repositorio.findAll();
		
		if (telefones.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(telefones);
			return new ResponseEntity<List<Telefone>>(telefones, HttpStatus.FOUND);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarTelefone(@RequestBody Telefone telefone) {
		try {
			if (telefone.getId() == null) {
				repositorio.save(telefone);
				return new ResponseEntity<>(HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarTelefone(@RequestBody Telefone atualizacao) {
		try {
			Telefone telefone = repositorio.getById(atualizacao.getId());
			
			if (telefone.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				TelefoneAtualizador atualizador = new TelefoneAtualizador();
				atualizador.atualizar(telefone, atualizacao);
				repositorio.save(telefone);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirTelefone(@RequestBody Telefone exclusao) {
		try {
			Telefone telefone = repositorio.getById(exclusao.getId());
			Cliente cliente = clienteRepositorio.findByTelefones(telefone);
			
			if (telefone.getId() == null || cliente.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				cliente.setTelefones(cliente.getTelefones().stream().filter(d -> d.getId() != telefone.getId()).toList());
				repositorio.delete(telefone);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("/cliente/{id}")
	public ResponseEntity<List<Telefone>> pegarTelefonesCliente(@PathVariable long id){
		Cliente cliente = clienteSelecionador.selecionar(clienteRepositorio, id);
		
		if (cliente.getId() == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			List<Telefone> telefones = cliente.getTelefones();
			adicionadorLink.adicionarLink(telefones);
			return new ResponseEntity<List<Telefone>>(telefones, HttpStatus.FOUND);
		}
	}
}
