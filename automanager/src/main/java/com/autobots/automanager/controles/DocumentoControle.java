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

	@GetMapping("/documento/{id}")
	public Documento obterDocumento(@PathVariable long id) {
		return selecionador.selecionar(repositorio, id);
	}

	@GetMapping("/documentos")
	public List<Documento> obterDocumentos() {
		List<Documento> documentos = repositorio.findAll();
		return documentos;
	}

	@PostMapping("/cadastro")
	public void cadastrarDocumento(@RequestBody Documento documento) {
		repositorio.save(documento);
	}

	@PutMapping("/atualizar")
	public void atualizarDocumento(@RequestBody Documento atualizacao) {
		Documento documento = repositorio.getById(atualizacao.getId());
		DocumentoAtualizador atualizador = new DocumentoAtualizador();
		atualizador.atualizar(documento, atualizacao);
		repositorio.save(documento);
	}

	@DeleteMapping("/excluir")
	public void excluirDocumento(@RequestBody Documento exclusao) {
		Documento documento = repositorio.getById(exclusao.getId());
		Cliente cliente = clienteRepositorio.findByDocumentos(documento);
		cliente.setDocumentos(cliente.getDocumentos().stream().filter(d -> d.getId() != documento.getId()).toList());
		repositorio.delete(documento);
	}
	
	@GetMapping("/cliente/{id}")
	public List<Documento> pegarDocumentosCliente(@PathVariable long id){
		Cliente cliente = clienteSelecionador.selecionar(clienteRepositorio, id);
		return cliente.getDocumentos();
	}
}
