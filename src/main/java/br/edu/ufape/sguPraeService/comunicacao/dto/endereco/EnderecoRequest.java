package br.edu.ufape.sguPraeService.comunicacao.dto.endereco;

import br.edu.ufape.sguPraeService.comunicacao.annotations.CepValido;
import br.edu.ufape.sguPraeService.models.Endereco;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoRequest {

    @NotBlank(message = "Rua é obrigatória")
    private String rua;

    @NotBlank(message = "CEP é obrigatório")
    @CepValido
    private String cep;

    @NotBlank(message = "Bairro é obrigatório")
    private String bairro;

    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;

    @NotBlank(message = "Estado é obrigatório")
    private String estado;

    @NotBlank(message = "Número é obrigatório")
    private String numero;

    private String complemento;

    public Endereco convertToEntity(EnderecoRequest enderecoRequest, ModelMapper modelMapper) {
        return modelMapper.map(enderecoRequest, Endereco.class);
    }
}
