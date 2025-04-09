package br.edu.ufape.sguPraeService.comunicacao.dto.profissional;

import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.FuncionarioResponse;
import br.edu.ufape.sguPraeService.models.Profissional;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class ProfissionalResponse {

    Long id;
    FuncionarioResponse tecnico;
    String especialidade;

    public ProfissionalResponse(Profissional profissional, ModelMapper modelMapper){
        if (profissional == null) throw new IllegalArgumentException("Profissional não pode ser nulo");
        else modelMapper.map(profissional, this);
    }
}
