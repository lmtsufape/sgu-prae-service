package br.edu.ufape.sguPraeService.servicos;


import br.edu.ufape.sguPraeService.auth.AuthenticatedUserProvider;
import br.edu.ufape.sguPraeService.dados.AgendamentoRepository;
import br.edu.ufape.sguPraeService.exceptions.GlobalAccessDeniedException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.AgendamentoNotFoundException;
import br.edu.ufape.sguPraeService.models.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public Agendamento agendar(Vaga vaga, Estudante estudante) {
        Agendamento agendamento = new Agendamento();
        agendamento.setData(vaga.getCronograma().getData());
        agendamento.setVaga(vaga);
        agendamento.setEstudante(estudante);
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

}
