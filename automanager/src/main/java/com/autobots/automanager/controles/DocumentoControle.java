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
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.AdicionadorLinkDocumento;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.modelo.DocumentoSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {
	@Autowired
	private DocumentoRepositorio repositorio;
	@Autowired
	private DocumentoSelecionador selecionador;
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	@Autowired
	private ClienteSelecionador clienteSelecionador;
	@Autowired
	private AdicionadorLinkDocumento adicionadorLink;

	@GetMapping("/documento/{id}")
	public ResponseEntity<Documento> obterDocumento(@PathVariable long id) {
		Documento documento = selecionador.selecionar(repositorio, id);
		if (documento == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(documento);
			return new ResponseEntity<Documento>(documento, HttpStatus.FOUND);
		}
	}

	@GetMapping("/documentos")
	public ResponseEntity<List<Documento>> obterDocumentos() {
		List<Documento> documentos = repositorio.findAll();
		
		if (documentos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(documentos);
			return new ResponseEntity<List<Documento>>(documentos, HttpStatus.FOUND);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarDocumento(@RequestBody Documento documento) {
		try {
			if (documento.getId() == null) {
				repositorio.save(documento);
				return new ResponseEntity<>(HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarDocumento(@RequestBody Documento atualizacao) {
		try {
			Documento documento = repositorio.getById(atualizacao.getId());
			
			if (documento.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				DocumentoAtualizador atualizador = new DocumentoAtualizador();
				atualizador.atualizar(documento, atualizacao);
				repositorio.save(documento);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirDocumento(@RequestBody Documento exclusao) {
		try {
			Documento documento = repositorio.getById(exclusao.getId());
			Cliente cliente = clienteRepositorio.findByDocumentos(documento);
			
			if (documento.getId() == null || cliente.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				cliente.setDocumentos(cliente.getDocumentos().stream().filter(d -> d.getId() != documento.getId()).toList());
				repositorio.delete(documento);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("/cliente/{id}")
	public ResponseEntity<List<Documento>> pegarDocumentosCliente(@PathVariable long id){
		Cliente cliente = clienteSelecionador.selecionar(clienteRepositorio, id);
		
		if (cliente.getId() == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			List<Documento> documentos = cliente.getDocumentos();
			adicionadorLink.adicionarLink(documentos);
			return new ResponseEntity<List<Documento>>(documentos, HttpStatus.FOUND);
		}
	}
}
