package br.edu.ufape.sguPraeService.comunicacao.dto.pagamento;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.modelmapper.ModelMapper;

import br.edu.ufape.sguPraeService.comunicacao.annotations.PagamentoValido;
import br.edu.ufape.sguPraeService.models.Auxilio;
import br.edu.ufape.sguPraeService.models.Pagamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@PagamentoValido
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PagamentoRequest {
	@NotNull(message = "Valor é obrigatório")
    @Positive(message="Valor inválido")
	private Long auxilioId;
    @NotNull(message = "Valor é obrigatório")
    @Positive(message="Valor inválido")
    private BigDecimal valor;
    @NotNull(message = "A data é obrigatória")
	private LocalDate data;

    public Pagamento convertToEntity(PagamentoRequest pagamentoRequest, ModelMapper modelMapper) {
        Pagamento entity = modelMapper.map(pagamentoRequest, Pagamento.class);
        Auxilio auxilio = new Auxilio();
        auxilio.setId(auxilioId);
        entity.setAuxilio(auxilio);
        return entity;
    }
}