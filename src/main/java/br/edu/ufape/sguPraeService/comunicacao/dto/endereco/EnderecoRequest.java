package br.edu.ufape.sguPraeService.comunicacao.dto.endereco;

import br.edu.ufape.sguPraeService.comunicacao.annotations.CepValido;
import br.edu.ufape.sguPraeService.models.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 3, max = 60, message = "A rua deve ter entre 3 e 60 caracteres")
    private String rua;

    @NotBlank(message = "CEP é obrigatório")
    @CepValido
    private String cep;

    @Size(min = 3, max = 60, message = "O bairro deve ter entre 3 e 60 caracteres")
    @NotBlank(message = "Bairro é obrigatório")
    private String bairro;

    @Size(min = 3, max = 60, message = "A cidade deve ter entre 3 e 60 caracteres")
    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;

    @Size(min = 2, max = 20, message = "O estado deve ter entre 2 e 20 caracteres")
    @NotBlank(message = "Estado é obrigatório")
    private String estado;

    @NotBlank(message = "Número é obrigatório")
    @Size(min = 1, max = 10, message = "O número deve ter entre 1 e 10 caracteres")
    private String numero;

    @Size(min = 1, max = 100, message = "O número deve ter entre 1 e 100 caracteres")
    private String complemento;

    public Endereco convertToEntity(EnderecoRequest enderecoRequest, ModelMapper modelMapper) {
        return modelMapper.map(enderecoRequest, Endereco.class);
    }
}
