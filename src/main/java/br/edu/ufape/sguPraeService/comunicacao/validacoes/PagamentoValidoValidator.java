package br.edu.ufape.sguPraeService.comunicacao.validacoes;

import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ufape.sguPraeService.comunicacao.annotations.PagamentoValido;
import br.edu.ufape.sguPraeService.comunicacao.dto.pagamento.PagamentoRequest;
import br.edu.ufape.sguPraeService.exceptions.AuxilioNotFoundException;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import br.edu.ufape.sguPraeService.models.Auxilio;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PagamentoValidoValidator implements ConstraintValidator<PagamentoValido, PagamentoRequest> {

    @Autowired
    private Fachada fachada;

    @Override
    public boolean isValid(PagamentoRequest pagamentoRequest, ConstraintValidatorContext context) {
        if (pagamentoRequest == null) {
            return false;
        }

        if (pagamentoRequest.getAuxilioId() != null) {
            Auxilio auxilio = null;
            try {
                auxilio = fachada.buscarAuxilio(pagamentoRequest.getAuxilioId());
            } catch (AuxilioNotFoundException e) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Auxílio não encontrado")
                        .addPropertyNode("auxilioId")
                        .addConstraintViolation();
                e.printStackTrace();
                return false;
            }
            if (pagamentoRequest.getData().isAfter(auxilio.getFimBolsa())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Data do pagamento excede o termino do auxílio")
                        .addPropertyNode("data")
                        .addConstraintViolation();
                return false;
            }

        }

        return true;
    }
}