package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.AuxilioNotFoundException;
import br.edu.ufape.sguPraeService.models.Auxilio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface AuxilioService {
    List<Auxilio> listar();

    Auxilio buscar(Long id) throws AuxilioNotFoundException;

    Auxilio salvar(Auxilio entity);

    Auxilio editar(Long id, Auxilio entity) throws AuxilioNotFoundException;

    void deletar(Long id) throws AuxilioNotFoundException;

    public BigDecimal calcularValorProporcional(Auxilio auxilio, LocalDate inicio, LocalDate fim);
}
