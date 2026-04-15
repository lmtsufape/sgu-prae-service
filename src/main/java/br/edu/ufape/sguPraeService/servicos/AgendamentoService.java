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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

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

        //  1. TRAVA DE TIMEOUT DE 24 HORAS
        Optional<Agendamento> ultimoAgendamento = repository.findTopByEstudante_UserIdOrderByDataCriacaoDesc(estudante.getUserId());

        if (ultimoAgendamento.isPresent() && ultimoAgendamento.get().getDataCriacao() != null) {
            LocalDateTime dataUltimaCriacao = ultimoAgendamento.get().getDataCriacao();

            // Verifica se o tempo decorrido desde o último agendamento é menor que 24 horas
            if (ChronoUnit.HOURS.between(dataUltimaCriacao, LocalDateTime.now()) < 24) {
                LocalDateTime liberacao = dataUltimaCriacao.plusHours(24);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

                throw new IllegalArgumentException(
                        "Você só pode realizar um novo agendamento a cada 24 horas. " +
                                "Seu próximo agendamento estará liberado em: " + liberacao.format(formatter)
                );
            }
        }

        //  2. TRAVA DE 1 AGENDAMENTO POR DIA DO EVENTO
        boolean jaPossuiAgendamentoNaData = repository.existsByEstudante_UserIdAndDataAndAtivoTrue(
                estudante.getUserId(), vaga.getCronograma().getData()
        );

        if (jaPossuiAgendamentoNaData) {
            throw new IllegalArgumentException("Você já possui um agendamento ativo para a data deste cronograma.");
        }

        // 3. SALVAMENTO NORMAL
        Agendamento agendamento = new Agendamento();
        agendamento.setData(vaga.getCronograma().getData());
        agendamento.setVaga(vaga);
        agendamento.setEstudante(estudante);
        agendamento.setModalidade(modalidade);
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
