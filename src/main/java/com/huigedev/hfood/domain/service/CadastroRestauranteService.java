package com.huigedev.hfood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huigedev.hfood.domain.exception.RestauranteNaoEncontradoException;
import com.huigedev.hfood.domain.model.Cozinha;
import com.huigedev.hfood.domain.model.Restaurante;
import com.huigedev.hfood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {
	
//	private static final String MSG_RESTAURANTE_NAO_ENCONTRADO 
//		= "Não existe um cadastro de restaurante com código %d";

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		
		Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);
					
		restaurante.setCozinha(cozinha);
		
		return restauranteRepository.save(restaurante);
	}
	
	public Restaurante buscarOuFalhar(Long restauranteId) {
		return restauranteRepository.findById(restauranteId)
			.orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
	}
}
