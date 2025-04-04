package br.edu.ufape.sguPraeService.comunicacao.dto.dadosBancarios;

import br.edu.ufape.sguPraeService.models.DadosBancarios;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DadosBancariosRequest {
    private Long id;
    private String nomeTitular;
    private String conta;
    private String agencia;

    public DadosBancarios convertToEntity(DadosBancariosRequest request, ModelMapper modelMapper) {
        return modelMapper.map(request, DadosBancarios.class);
    }
}
