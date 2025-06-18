package br.edu.ufape.sguPraeService.comunicacao.dto.dadosBancarios;

import br.edu.ufape.sguPraeService.models.DadosBancarios;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DadosBancariosRequest {


    @NotBlank(message = "O nome do titular é obrigatório.")
    @Size(max = 100, message = "O nome do titular não pode exceder 100 caracteres.")
    private String nomeTitular;


    @NotBlank(message = "O número da conta é obrigatório.")
    @Pattern(regexp = "\\d{1,12}", message = "A conta deve conter apenas dígitos (1 a 12 dígitos).")
    private String conta;


    @NotBlank(message = "A agência é obrigatória.")
    @Pattern(regexp = "\\d{4}", message = "A agência deve conter exatamente 4 dígitos.")
    private String agencia;


    @NotBlank(message = "O nome do banco é obrigatório.")
    @Size(max = 50, message = "O nome do banco não pode exceder 50 caracteres.")
    private String banco;

    @NotBlank(message = "O tipo de conta é obrigatório.")
    @Pattern(
            regexp = "^(CORRENTE|POUPANCA)$",
            message = "O tipo de conta deve ser 'CORRENTE' ou 'POUPANCA'."
    )
    private String tipoConta;

    public DadosBancarios convertToEntity(DadosBancariosRequest request, ModelMapper modelMapper) {
        return modelMapper.map(request, DadosBancarios.class);
    }
}
