package br.edu.ufape.sguPraeService.servicos;



import br.edu.ufape.sguPraeService.auth.AuthenticatedUserProvider;
import br.edu.ufape.sguPraeService.dados.CancelamentoAgendamentoRepository;
import br.edu.ufape.sguPraeService.exceptions.GlobalAccessDeniedException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.CancelamentoNotFoundException;
import br.edu.ufape.sguPraeService.models.CancelamentoAgendamento;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class CancelamentoService implements br.edu.ufape.sguPraeService.servicos.interfaces.CancelamentoService {
    private final CancelamentoAgendamentoRepository cancelamentoAgendamentoRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;


    @Override
    public CancelamentoAgendamento salvar(CancelamentoAgendamento entity) {
        entity.setDataCancelamento(LocalDateTime.now());
        return cancelamentoAgendamentoRepository.save(entity);
    }

    @Override
    public CancelamentoAgendamento buscar(Long id) {
        CancelamentoAgendamento cancelamento = cancelamentoAgendamentoRepository.findById(id).orElseThrow(CancelamentoNotFoundException::new);
        if(!Objects.equals(cancelamento.getAgendamento().getEstudante().getUserId(), authenticatedUserProvider.getUserId())
                && !Objects.equals(cancelamento.getAgendamento().getVaga().getCronograma().getProfissional().getUserId(), authenticatedUserProvider.getUserId())){
            throw new GlobalAccessDeniedException("Você não tem permissão para acessar este recurso");
        }
        return cancelamentoAgendamentoRepository.findById(id).orElse(null);
    }

    @Override
    public List<CancelamentoAgendamento> listar() {
        return cancelamentoAgendamentoRepository.findAll();
    }

    @Override
    public List<CancelamentoAgendamento> ListarCancelamentosPorEstudante(UUID userId) {
        return cancelamentoAgendamentoRepository.findAllByAgendamento_Estudante_UserId(userId);
    }

    @Override
    public List<CancelamentoAgendamento> ListarCancelamentosPorProfissional(UUID userId) {
        return cancelamentoAgendamentoRepository.findAllByProfissionalUserId(userId);
    }

    @Override
    public List<CancelamentoAgendamento> ListarPorEstudanteAtual() {
        return cancelamentoAgendamentoRepository.findAllByAgendamento_Estudante_UserId(authenticatedUserProvider.getUserId());
    }

    @Override
    public List<CancelamentoAgendamento> ListarPorProfissionalAtual() {
        return cancelamentoAgendamentoRepository.findAllByProfissionalUserId(authenticatedUserProvider.getUserId());
    }
}
