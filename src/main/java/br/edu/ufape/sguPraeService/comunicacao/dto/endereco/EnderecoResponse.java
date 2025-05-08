package br.edu.ufape.sguPraeService.comunicacao.dto.endereco;

import br.edu.ufape.sguPraeService.models.Endereco;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoResponse {
    private Long id;
    private String rua;
    private String cep;
    private String bairro;
    private String cidade;
    private String estado;
    private String numero;
    private String complemento;

    public EnderecoResponse(Endereco endereco, ModelMapper modelMapper) {
        if (endereco == null) throw new IllegalArgumentException("Endereço não pode ser nulo");
        else modelMapper.map(endereco, this);
    }
}
