package br.edu.ufape.sguPraeService.servicos;

import br.edu.ufape.sguPraeService.dados.AuxilioRepository;
import br.edu.ufape.sguPraeService.dados.PagamentoRepository;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.PagamentoNotFoundException;
import br.edu.ufape.sguPraeService.models.Auxilio;
import br.edu.ufape.sguPraeService.models.Pagamento;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @RequiredArgsConstructor
public class PagamentoService implements br.edu.ufape.sguPraeService.servicos.interfaces.PagamentoService {
    private final PagamentoRepository pagamentoRepository;
    private final ModelMapper modelMapper;
    private final AuxilioRepository auxilioRepository;

    @Override
    public List<Pagamento> listar() { return pagamentoRepository.findAll(); }

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

        List<Auxilio> auxilios = auxilioRepository.findByPagamentos_Id(id);
        for (Auxilio aux : auxilios) {
            aux.getPagamentos().remove(pagamento);
            auxilioRepository.save(aux);
        }
        pagamentoRepository.delete(pagamento);
    }
}
