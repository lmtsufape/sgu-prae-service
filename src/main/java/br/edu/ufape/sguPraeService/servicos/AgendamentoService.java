package br.edu.ufape.sguPraeService.servicos;


import br.edu.ufape.sguPraeService.auth.AuthenticatedUserProvider;
import br.edu.ufape.sguPraeService.dados.AgendamentoRepository;
import br.edu.ufape.sguPraeService.exceptions.GlobalAccessDeniedException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.AgendamentoNotFoundException;
import br.edu.ufape.sguPraeService.models.*;
import br.edu.ufape.sguPraeService.models.enums.ModalidadeAgendamento;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AgendamentoService implements br.edu.ufape.sguPraeService.servicos.interfaces.AgendamentoService {
    private final AgendamentoRepository repository;
    private final ModelMapper modelMapper;
    private final AuthenticatedUserProvider authenticatedUserProvider;


    @Override
    public Agendamento salvar(Agendamento entity) {
        return repository.save(entity);
    }

    @Override
    public Agendamento agendar(Vaga vaga, Estudante estudante, ModalidadeAgendamento modalidade) {
        // Regra 1: Bloquear mais de um agendamento por dia
        boolean jaPossuiAgendamento = repository.existsByEstudante_UserIdAndDataAndAtivoTrue(
                estudante.getUserId(), vaga.getCronograma().getData()
        );

        if (jaPossuiAgendamento) {
            // Nota: Se vocês tiverem uma exceção customizada para regras de negócio (ex: RegraDeNegocioException), use-a aqui.
            throw new IllegalArgumentException("Você já possui um agendamento ativo para esta data.");
        }

        Agendamento agendamento = new Agendamento();
        agendamento.setData(vaga.getCronograma().getData());
        agendamento.setVaga(vaga);
        agendamento.setEstudante(estudante);
        agendamento.setModalidade(modalidade); // Setando a nova modalidade
        return repository.save(agendamento);
    }

    @Override
    public Agendamento buscar(Long id) throws AgendamentoNotFoundException {
        Agendamento agendamento = repository.findById(id).orElseThrow(AgendamentoNotFoundException::new);
        if(!Objects.equals(agendamento.getEstudante().getUserId(), authenticatedUserProvider.getUserId())
                && !Objects.equals(agendamento.getVaga().getCronograma().getProfissional().getUserId(), authenticatedUserProvider.getUserId())){
            throw new GlobalAccessDeniedException("Você não tem permissão para acessar este recurso");
        }
        return agendamento;
    }

    @Override
    public Agendamento editar(Long Id, Agendamento entity) throws AgendamentoNotFoundException {
        Agendamento velhoAgendamento = buscar(Id);
        modelMapper.map(entity, velhoAgendamento);
        return salvar(velhoAgendamento);
    }

    @Override
    public void deletar(Long id) {
        Agendamento agendamento = buscar(id);
        agendamento.setAtivo(false);
        salvar(agendamento);
    }

    @Override
    public Page<Agendamento> listarAgendamentosPorEstudante(Estudante estudante, Pageable pageable) {
        return repository.findAllByEstudante_UserIdAndAtivoTrue(estudante.getUserId(), pageable);
    }
    @Override
    public Page<Agendamento> listarAgendamentosEstudanteAtual(Pageable pageable) {
        return repository.findAllByEstudante_UserIdAndAtivoTrue(authenticatedUserProvider.getUserId(), pageable);
    }
    @Override
    public Page<Agendamento> listarPorProfissional(Profissional profissional, Pageable pageable) {
        return repository.findAllByProfissionalUserId(profissional.getUserId(), pageable);
    }
    @Override
    public Page<Agendamento> listarPorProfissionalAtual(Pageable pageable) {
        return repository.findAllByProfissionalUserId(authenticatedUserProvider.getUserId(), pageable);
    }

    @Override
    public Agendamento alterarModalidade(Long id, ModalidadeAgendamento novaModalidade) throws AgendamentoNotFoundException {
        Agendamento agendamento = buscar(id); // O buscar() já verifica se o estudante dono é quem está acessando (segurança)

        // Junta a data do cronograma com a hora da vaga para criar o "Timestamp" exato do atendimento
        LocalDateTime dataHoraAgendamento = LocalDateTime.of(
                agendamento.getData(),
                agendamento.getVaga().getHoraInicio()
        );

        // Regra 2: Prazo limite de 2 horas antes
        if (LocalDateTime.now().plusHours(2).isAfter(dataHoraAgendamento)) {
            throw new IllegalArgumentException("A modalidade só pode ser alterada com até 2 horas de antecedência do horário agendado.");
        }

        agendamento.setModalidade(novaModalidade);
        return salvar(agendamento);
    }

}
