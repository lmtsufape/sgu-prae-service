package br.edu.ufape.sguPraeService.servicos;

import br.edu.ufape.sguPraeService.comunicacao.dto.pagamento.FolhaPagamentoResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.pagamento.ItemFolhaPagamentoResponse;
import br.edu.ufape.sguPraeService.dados.BeneficioRepository;
import br.edu.ufape.sguPraeService.dados.PagamentoRepository;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.PagamentoNotFoundException;
import br.edu.ufape.sguPraeService.models.Beneficio;
import br.edu.ufape.sguPraeService.models.Estudante;
import br.edu.ufape.sguPraeService.models.Pagamento;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.BooleanBuilder;
import br.edu.ufape.sguPraeService.models.QPagamento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class PagamentoService implements br.edu.ufape.sguPraeService.servicos.interfaces.PagamentoService {
    private final PagamentoRepository pagamentoRepository;
    private final ModelMapper modelMapper;
    private final BeneficioRepository beneficioRepository;

    @Override
    public List<Pagamento> listar() { return pagamentoRepository.findAll(); }

    @Override
    public Page<Pagamento> listar(Predicate predicate, Pageable pageable) {
        QPagamento qPagamento = QPagamento.pagamento;
        BooleanBuilder filtroBase = new BooleanBuilder();
        filtroBase.and(qPagamento.ativo.isTrue());

        Predicate predicadoFinal = filtroBase.and(predicate);
        return pagamentoRepository.findAll(predicadoFinal, pageable);
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
    public Pagamento salvarIndividual(Pagamento pagamento) { return pagamentoRepository.save(pagamento);}

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
        Beneficio beneficio = pagamento.getBeneficio();

        beneficio.getPagamentos().removeIf(p -> p.getId().equals(id));
        beneficioRepository.save(beneficio);
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

    public FolhaPagamentoResponse gerarFolhaPagamento(Integer ano, Integer mes, String numeroLote) {
        List<Pagamento> pagamentos;

        // 1. Busca
        if (numeroLote != null && !numeroLote.isBlank()) {
            pagamentos = pagamentoRepository.findByAnoReferenciaAndMesReferenciaAndNumeroLote(ano, mes, numeroLote);
        } else {
            pagamentos = pagamentoRepository.findByAnoReferenciaAndMesReferencia(ano, mes);
        }

        FolhaPagamentoResponse folha = new FolhaPagamentoResponse();
        folha.setAnoReferencia(ano);
        folha.setMesReferencia(mes);
        folha.setNumeroLote(numeroLote);

        // 2. Agrupamento por ID do Estudante (Entidade)
        Map<Long, List<Pagamento>> pagamentosMap = pagamentos.stream()
                .collect(Collectors.groupingBy(p -> p.getBeneficio().getEstudantes().getId()));

        List<ItemFolhaPagamentoResponse> itensFolha = new ArrayList<>();

        // 3. Processamento dos Itens
        pagamentosMap.forEach((estudanteId, lista) -> {
            ItemFolhaPagamentoResponse item = new ItemFolhaPagamentoResponse();
            Estudante estudante = lista.get(0).getBeneficio().getEstudantes();

            // Setamos o UUID para a Fachada usar depois
            item.setUserId(estudante.getUserId());

            // Dados Bancários locais
            if (estudante.getDadosBancarios() != null) {
                item.setBanco(estudante.getDadosBancarios().getBanco());
                item.setAgencia(estudante.getDadosBancarios().getAgencia());
                item.setConta(estudante.getDadosBancarios().getConta());
                item.setTipoConta(estudante.getDadosBancarios().getTipoConta());
            }

            // Cálculos
            BigDecimal total = lista.stream()
                    .map(Pagamento::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            item.setValorTotalRecebido(total);
            item.setQuantidadeBeneficios(lista.size());

            String descricoes = lista.stream()
                    .map(p -> p.getBeneficio().getTipoBeneficio().getDescricao())
                    .distinct()
                    .collect(Collectors.joining(", "));
            item.setDescricoesBeneficios(descricoes);

            itensFolha.add(item);
        });

        folha.setItens(itensFolha);

        // 4. Totais Gerais
        BigDecimal totalGeral = pagamentos.stream()
                .map(Pagamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        folha.setValorTotalGeral(totalGeral);
        folha.setQuantidadeTotalPagamentos(pagamentos.size());
        folha.setQuantidadeEstudantes(itensFolha.size());

        return folha;
    }

    public BigDecimal obterValorTotalPagamentosAtivos() {
        return pagamentoRepository.findTotalPagamentosAtivos();
    }

    @Override
    public List<Object[]> obterValorTotalPorTipoBeneficio() {
        return pagamentoRepository.findValorTotalPorTipoBeneficio();
    }
}
