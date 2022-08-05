package com.huigedev.hfood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.huigedev.hfood.domain.exception.CidadeNaoEncontradoException;
import com.huigedev.hfood.domain.exception.EntidadeEmUsoExcepetion;
import com.huigedev.hfood.domain.model.Cidade;
import com.huigedev.hfood.domain.model.Estado;
import com.huigedev.hfood.domain.repository.CidadeRepository;

@Service
public class CadastroCidadeService {

	private static final String MSG_CIDADE_EM_USO
		= "Cidade de código %d não pode ser removida, pois está em uso";

//	private static final String MSG_CIDADE_NAO_ENCONTRADA
//		= "Não existe um cadastro de cidade com código %d";

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroEstadoService cadastroEstado;
	
	public Cidade salvar(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();
		
		Estado estado = cadastroEstado.buscarOuFalhar(estadoId);
		
		cidade.setEstado(estado);
		
		return cidadeRepository.save(cidade);
	}
	
	public void excluir(Long cidadeId) {
        try {
            cidadeRepository.deleteById(cidadeId);
            
        } catch (EmptyResultDataAccessException e) {
            throw new CidadeNaoEncontradoException(cidadeId);
        
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoExcepetion(
                String.format(MSG_CIDADE_EM_USO, cidadeId));
        }
    }
	
	public Cidade buscarOuFalhar(Long cidadeId) {
		return cidadeRepository.findById(cidadeId)
				.orElseThrow(() -> new CidadeNaoEncontradoException(cidadeId));
	}
	
}
