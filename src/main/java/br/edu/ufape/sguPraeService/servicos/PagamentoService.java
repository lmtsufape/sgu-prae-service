package br.edu.ufape.sguPraeService.servicos;

import br.edu.ufape.sguPraeService.dados.BeneficioRepository;
import br.edu.ufape.sguPraeService.dados.PagamentoRepository;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.PagamentoNotFoundException;
import br.edu.ufape.sguPraeService.models.Beneficio;
import br.edu.ufape.sguPraeService.models.Pagamento;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service @RequiredArgsConstructor
public class PagamentoService implements br.edu.ufape.sguPraeService.servicos.interfaces.PagamentoService {
    private final PagamentoRepository pagamentoRepository;
    private final ModelMapper modelMapper;
    private final BeneficioRepository beneficioRepository;

    @Override
    public List<Pagamento> listar() { return pagamentoRepository.findAll(); }

    @Override
    public Page<Pagamento> listar(Pageable pageable) {
        return pagamentoRepository.findAll(pageable);
    }

    @Override
    public Pagamento buscar(Long id) throws PagamentoNotFoundException {
        return pagamentoRepository.findById(id).orElseThrow(PagamentoNotFoundException::new);
    }

    @Override
    public List<Pagamento> salvar(List<Pagamento> pagamentos) {
        return pagamentoRepository.saveAll(pagamentos);
    }

    @Override
    public Pagamento editar(Long id, Pagamento entity) throws PagamentoNotFoundException {
        Pagamento Pagamento = buscar(id);
        modelMapper.map(entity, Pagamento);
        return pagamentoRepository.save(Pagamento);
    }

    @Override
    public void desativar(Long id) throws PagamentoNotFoundException {
        Pagamento Pagamento = buscar(id);
        Pagamento.setAtivo(false);
        pagamentoRepository.save(Pagamento);
    }

    @Override
    @Transactional
    public void deletar(Long id) throws PagamentoNotFoundException {
        Pagamento pagamento = buscar(id);

        List<Beneficio> beneficios = beneficioRepository.findByPagamentos_Id(id);
        for (Beneficio aux : beneficios) {
            aux.getPagamentos().remove(pagamento);
            beneficioRepository.save(aux);
        }
        pagamentoRepository.delete(pagamento);
    }

    @Override
    public List<Pagamento> listarPorValor(BigDecimal min, BigDecimal max) {
        return pagamentoRepository.findByValorBetween(min, max);
    }

    @Override
    public Page<Pagamento> listarPorValor(BigDecimal min, BigDecimal max, Pageable pageable) {
        return pagamentoRepository.findByValorBetween(min, max, pageable);
    }

    @Override
    public List<Pagamento> listarPorEstudanteId(Long estudanteId) {
        return beneficioRepository.findAllByAtivoTrueAndEstudantes_Id(estudanteId).stream()
                .flatMap(aux -> aux.getPagamentos().stream())
                .filter(Pagamento::isAtivo)
                .sorted(Comparator.comparing(Pagamento::getData).reversed())
                .toList();
    }
}
