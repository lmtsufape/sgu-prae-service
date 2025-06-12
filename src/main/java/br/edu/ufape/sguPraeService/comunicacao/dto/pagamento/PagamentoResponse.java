package br.edu.ufape.sguPraeService.comunicacao.dto.pagamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import br.edu.ufape.sguPraeService.comunicacao.dto.estudante.EstudanteResponse;
import br.edu.ufape.sguPraeService.models.Estudante;
import br.edu.ufape.sguPraeService.models.Pagamento;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PagamentoResponse {

    private Long id;
    private BigDecimal valor;
    private LocalDate data;
    private List<EstudanteResponse> estudantes = new ArrayList<>();

    public PagamentoResponse(Pagamento pagamento, List<Estudante> estudantes, ModelMapper modelMapper) {
        if (pagamento == null)
            throw new IllegalArgumentException("Pagamento não pode ser nulo");
        estudantes.forEach(e -> this.estudantes.add(new EstudanteResponse(e, modelMapper)));
        modelMapper.map(pagamento, this);
    }
}
