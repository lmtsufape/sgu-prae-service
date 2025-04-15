package br.edu.ufape.sguPraeService.fachada;


import br.edu.ufape.sguPraeService.auth.AuthenticatedUserProvider;
import br.edu.ufape.sguPraeService.auth.RabbitAuthServiceClient;
import br.edu.ufape.sguPraeService.comunicacao.dto.estudante.CredorResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.estudante.EstudanteResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.profissional.ProfissionalResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.AlunoResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.FuncionarioResponse;
import br.edu.ufape.sguPraeService.exceptions.*;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.*;
import br.edu.ufape.sguPraeService.models.*;
import br.edu.ufape.sguPraeService.servicos.interfaces.*;
import br.edu.ufape.sguPraeService.models.Cronograma;
import br.edu.ufape.sguPraeService.models.Profissional;
import br.edu.ufape.sguPraeService.models.TipoAtendimento;
import br.edu.ufape.sguPraeService.servicos.interfaces.CronogramaService;
import br.edu.ufape.sguPraeService.servicos.interfaces.ProfissionalService;
import br.edu.ufape.sguPraeService.servicos.interfaces.TipoAtendimentoService;
import br.edu.ufape.sguPraeService.servicos.interfaces.VagaService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Component @RequiredArgsConstructor
public class Fachada {
    private final ProfissionalService profissionalService;
    private final TipoAtendimentoService tipoAtendimentoService;
    private final CronogramaService cronogramaService;
    private final VagaService vagaService;
    private final EnderecoService enderecoService;
    private final DadosBancariosService dadosBancariosService;
    private final TipoEtniaService tipoEtniaService;
    private final EstudanteService estudanteService;
    private final AuthServiceHandler authServiceHandler;
    private final ModelMapper modelMapper;
    private final RabbitAuthServiceClient rabbitAuthServiceClient;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final AgendamentoService agendamentoService;
    private final CancelamentoService cancelamentoService;
    private final TipoBolsaService tipoBolsaService;
    private final TipoAuxilioService tipoAuxilioService;
    private final AuxilioService auxilioService;


    @Value("${authClient.client-id}")
    private String clientId;

    Logger logger = LoggerFactory.getLogger(Fachada.class);


    // ------------------- Profissional ------------------- //
    public List<ProfissionalResponse> listarProfissionais() {
        List<ProfissionalResponse> profissionalResponse = new ArrayList<>();
        List<Profissional> profissionais = profissionalService.listar();
        if (profissionais.isEmpty()) { return profissionalResponse; }
        List<UUID> userIds = profissionais.stream().map(Profissional::getUserId).toList();
        logger.info("Profissionals encontrados: {}", userIds);
        List<FuncionarioResponse> usuarios = authServiceHandler.buscarTecnicos(userIds);
        for (int i = 0; i < profissionais.size(); i++) {
            Profissional profissional = profissionais.get(i);
            FuncionarioResponse usuario = usuarios.get(i);
            ProfissionalResponse response = new ProfissionalResponse(profissional, modelMapper);
            response.setTecnico(usuario);
            profissionalResponse.add(response);
        }
        return profissionalResponse;
    }

    public ProfissionalResponse buscarProfissional(Long id) throws ProfissionalNotFoundException {
        Profissional profissional = profissionalService.buscar(id);
        FuncionarioResponse userInfo = authServiceHandler.buscarTecnicoPorId(profissional.getUserId());
        ProfissionalResponse response = new ProfissionalResponse(profissional, modelMapper);
        response.setTecnico(userInfo);
        return response;
    }

    public ProfissionalResponse salvarProfissional(Profissional profissional) {
        UUID userId = authenticatedUserProvider.getUserId();
        profissional.setUserId(userId);
        Profissional novoProfissional = profissionalService.salvar(profissional);
        FuncionarioResponse response = authServiceHandler.getTecnicoInfo();
        rabbitAuthServiceClient.assignRoleToUser(userId.toString(), clientId, "profissional");
        ProfissionalResponse profissionalResponse = new ProfissionalResponse(novoProfissional, modelMapper);
        profissionalResponse.setTecnico(response);
        return profissionalResponse;
    }

    public ProfissionalResponse editarProfissional(Profissional profissional) throws ProfissionalNotFoundException {
        UUID userId = authenticatedUserProvider.getUserId();
        Profissional novoProfissional = profissionalService.editar(userId, profissional);
        FuncionarioResponse response = authServiceHandler.getTecnicoInfo();
        ProfissionalResponse profissionalResponse = new ProfissionalResponse(novoProfissional, modelMapper);
        profissionalResponse.setTecnico(response);
        return profissionalResponse;
    }

    public void deletarProfissional(Long id) throws ProfissionalNotFoundException {
        profissionalService.deletar(id);
    }

    // ================== Estudante  ================== //

    @CircuitBreaker(name = "authServiceClient", fallbackMethod = "fallbackSalvarEstudante")
    public EstudanteResponse salvarEstudante(Estudante estudante, Long tipoEtniaId) throws TipoEtniaNotFoundException {
        UUID userId = authenticatedUserProvider.getUserId();
        estudante.setTipoEtnia(tipoEtniaService.buscarTipoEtnia(tipoEtniaId));
        estudante.setUserId(userId);

        Estudante novoEstudante = estudanteService.salvarEstudante(estudante);
        AlunoResponse userInfo = authServiceHandler.getAlunoInfo();
        rabbitAuthServiceClient.assignRoleToUser(userId.toString(), clientId, "estudante");
        EstudanteResponse response = new EstudanteResponse(novoEstudante, modelMapper);
        response.setAluno(userInfo);
        return response;
    }

    public EstudanteResponse buscarEstudante(Long id) throws EstudanteNotFoundException {
        Estudante estudante = estudanteService.buscarEstudante(id);
        AlunoResponse userInfo = authServiceHandler.buscarAlunoPorId(estudante.getUserId());
        EstudanteResponse response = new EstudanteResponse(estudante, modelMapper);
        response.setAluno(userInfo);
        return response;
    }

    public List<EstudanteResponse> listarEstudantes() {
        List<EstudanteResponse> estudanteResponse = new ArrayList<>();
        List<Estudante> estudantes = estudanteService.listarEstudantes();
        if (estudantes.isEmpty()) {
            return estudanteResponse;
        }
        List<UUID> userIds = estudantes.stream()
                .map(Estudante::getUserId)
                .toList();
        logger.info("Estudantes encontrados: {}", userIds);
        List<AlunoResponse> usuarios = authServiceHandler.buscarAlunos(userIds);
        for (int i = 0; i < estudantes.size(); i++) {
            Estudante estudante = estudantes.get(i);
            AlunoResponse usuario = usuarios.get(i);
            EstudanteResponse response = new EstudanteResponse(estudante, modelMapper);
            response.setAluno(usuario);
            estudanteResponse.add(response);
        }
        return estudanteResponse;
    }
    @CircuitBreaker(name = "authServiceClient", fallbackMethod = "fallbackAtualizarEstudante")
    public EstudanteResponse atualizarEstudante(Estudante estudante, Long tipoEtniaId) throws EstudanteNotFoundException, TipoEtniaNotFoundException {
        UUID userId = authenticatedUserProvider.getUserId();
        estudante.setTipoEtnia(tipoEtniaService.buscarTipoEtnia(tipoEtniaId));
        Estudante velhoEstudante = estudanteService.buscarPorUserId(userId);
        estudante.setEndereco(enderecoService.editarEndereco(velhoEstudante.getEndereco().getId(),estudante.getEndereco()));
        Estudante novoEstudante = estudanteService.atualizarEstudante(estudante, velhoEstudante);
        EstudanteResponse response = new EstudanteResponse(novoEstudante, modelMapper);
        AlunoResponse userInfo = authServiceHandler.getAlunoInfo();
        response.setAluno(userInfo);
        return response;
    }

    public void deletarEstudante(Long id) throws EstudanteNotFoundException {
        estudanteService.deletarEstudante(id);
    }

    public List<CredorResponse> listarCredoresComAuxiliosAtivos() {
    List<Estudante> estudantes = estudanteService.listarEstudantesComAuxilioAtivo();
    List<UUID> userIds = estudantes.stream().map(Estudante::getUserId).toList();
    List<AlunoResponse> alunos = authServiceHandler.buscarAlunos(userIds);

    List<CredorResponse> credores = new ArrayList<>();
    for (int i = 0; i < estudantes.size(); i++) {
        Estudante estudante = estudantes.get(i);
        AlunoResponse aluno = alunos.get(i);

        estudante.getAuxilios().stream()
            .filter(Auxilio::isAtivo)
            .forEach(auxilio -> credores.add(new CredorResponse(aluno, estudante.getDadosBancarios(), auxilio)));
        }

    return credores;
    }

public List<CredorResponse> listarCredoresPorAuxilio(Long auxilioId) {
    List<Estudante> estudantes = estudanteService.listarEstudantesPorAuxilioId(auxilioId);
    List<UUID> userIds = estudantes.stream().map(Estudante::getUserId).toList();
    List<AlunoResponse> alunos = authServiceHandler.buscarAlunos(userIds);

    List<CredorResponse> credores = new ArrayList<>();
    for (int i = 0; i < estudantes.size(); i++) {
        Estudante estudante = estudantes.get(i);
        AlunoResponse aluno = alunos.get(i);

        estudante.getAuxilios().stream()
            .filter(auxilio -> auxilio.getId().equals(auxilioId))
            .forEach(auxilio -> credores.add(new CredorResponse(aluno, estudante.getDadosBancarios(), auxilio)));
        }

    return credores;
    }

    // ================== TipoEtnia  ================== //


    public TipoEtnia salvarTipoEtnia(TipoEtnia tipoEtnia) {
        return tipoEtniaService.salvarTipoEtnia(tipoEtnia);
    }

    public TipoEtnia buscarTipoEtnia(Long id) throws TipoEtniaNotFoundException {
        return tipoEtniaService.buscarTipoEtnia(id);
    }

    public List<TipoEtnia> listarTiposEtnia() {
        return tipoEtniaService.listarTiposEtnia();
    }

    public TipoEtnia atualizarTipoEtnia(Long id, TipoEtnia tipoEtnia) throws TipoEtniaNotFoundException {
        return tipoEtniaService.atualizarTipoEtnia(id, tipoEtnia);
    }

    public void deletarTipoEtnia(Long id) throws TipoEtniaNotFoundException {
        tipoEtniaService.deletarTipoEtnia(id);
    }


    // ================== Endereco  ================== //

    public List<Endereco> listarEnderecos() {
        return enderecoService.listarEnderecos();
    }

    public Endereco buscarEndereco(Long id) {
        try {
            return enderecoService.buscarEndereco(id);
        } catch (EnderecoNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Endereco criarEndereco(Endereco endereco) {
        return enderecoService.criarEndereco(endereco);
    }

    public void excluirEndereco(Long id) {
        enderecoService.excluirEndereco(id);
    }

    public Endereco editarEndereco(Long id, Endereco enderecoAtualizado) {
        return enderecoService.editarEndereco(id, enderecoAtualizado);
    }

    // ================== Dados Bancarios  ================== //

    public DadosBancarios salvarDadosBancarios(DadosBancarios dadosBancarios) {
        return dadosBancariosService.salvarDadosBancarios(dadosBancarios);
    }

    public List<DadosBancarios> listarDadosBancarios() {
        return dadosBancariosService.listarDadosBancarios();
    }

    public DadosBancarios buscarDadosBancarios(Long id) {
        return dadosBancariosService.buscarDadosBancarios(id);
    }

    public void deletarDadosBancarios(Long id) {
        dadosBancariosService.deletarDadosBancarios(id);
    }

    public DadosBancarios atualizarDadosBancarios(Long id, DadosBancarios novosDadosBancarios) {
        return dadosBancariosService.atualizarDadosBancarios(id, novosDadosBancarios);
    }


    // ------------------- TipoAtendimento ------------------- //
    public List<TipoAtendimento> listarTipoAtendimentos() {
        return tipoAtendimentoService.listar();
    }

    public TipoAtendimento buscarTipoAtendimento(Long id) throws TipoAtendimentoNotFoundException {
        return tipoAtendimentoService.buscar(id);
    }

    public TipoAtendimento salvarTipoAtendimento(TipoAtendimento tipoAtendimento) {
        return tipoAtendimentoService.salvar(tipoAtendimento);
    }

    public TipoAtendimento editarTipoAtendimento(Long id, TipoAtendimento tipoAtendimento) throws TipoAtendimentoNotFoundException {
        return tipoAtendimentoService.editar(id, tipoAtendimento);
    }

    public void deletarTipoAtendimento(Long id) throws TipoAtendimentoNotFoundException {
        tipoAtendimentoService.deletar(id);
    }

    public TipoAtendimento deletarHorarioTipoAtendimento(Long id, int index) throws TipoAtendimentoNotFoundException {
        return tipoAtendimentoService.deletarHorario(id, index);
    }

    // ------------------- Cronograma ------------------- //
    public List<Cronograma> listarCronogramasPorProfissional(){

        return cronogramaService.listarPorProfissional(authenticatedUserProvider.getUserId());
    }

    public List<Cronograma> listarCronogramas() {
        return cronogramaService.listar();
    }

    public List<Cronograma> listarCronogramasPorTipoAtendimento(Long tipoAtendimentoId) {
        return cronogramaService.listarPorTipoAtendimento(tipoAtendimentoId);
    }

    public Cronograma buscarCronograma(Long id) throws CronogramaNotFoundException {
        return cronogramaService.buscar(id);
    }

    @Transactional
    public Cronograma salvarCronograma(Cronograma cronograma, Long tipoAtendimentoId) throws TipoAtendimentoNotFoundException {
        UUID userId = authenticatedUserProvider.getUserId();
        TipoAtendimento tipoAtendimento = buscarTipoAtendimento(tipoAtendimentoId);
        Profissional profissional = profissionalService.buscarPorUserId(userId);
        cronograma.setProfissional(profissional);
        cronograma.setTipoAtendimento(tipoAtendimento);
        List<Vaga> vagas = vagaService.gerarVagas(tipoAtendimento.getHorarios(), tipoAtendimento.getTempoAtendimento());
        vagas.forEach(vaga -> vaga.setCronograma(cronograma));
        cronograma.setVagas(vagas);
        return cronogramaService.salvar(cronograma);
    }

    // ------------------- Agendamento ------------------- //

    @Transactional
    public Agendamento agendarVaga(Long id) throws VagaNotFoundException, UnavailableVagaException {
        try {
            Vaga vaga = vagaService.buscar(id);
            Estudante estudante = estudanteService.buscarPorUserId(authenticatedUserProvider.getUserId());

            if (vaga.isDisponivel()) {
                vaga.setDisponivel(false);
                vagaService.salvar(vaga);
                return agendamentoService.agendar(vaga, estudante);
            }

            throw new UnavailableVagaException();

        } catch (ObjectOptimisticLockingFailureException e) {
            throw new UnavailableVagaException();
        }
    }

    @Transactional
    public CancelamentoAgendamento cancelarAgendamento(Long id, CancelamentoAgendamento cancelamento) throws AgendamentoNotFoundException {
        Agendamento agendamento = agendamentoService.buscar(id);
        if(!Objects.equals(agendamento.getEstudante().getUserId(), authenticatedUserProvider.getUserId())
                && !Objects.equals(agendamento.getVaga().getCronograma().getProfissional().getUserId(),
                authenticatedUserProvider.getUserId())){
            throw new GlobalAccessDeniedException("Você não tem permissão para acessar este recurso");
        }

        Vaga vaga = agendamento.getVaga();
        vaga.setDisponivel(true);
        vagaService.salvar(vaga);
        agendamento.setAtivo(false);
        agendamentoService.salvar(agendamento);
        cancelamento.setAgendamento(agendamento);
        return cancelamentoService.salvar(cancelamento);
    }

    public Agendamento buscarAgendamento(Long id) throws AgendamentoNotFoundException {
        return agendamentoService.buscar(id);
    }

    public List<Agendamento> listarAgendamentosPorEstudante(Long estudanteId) {
        Estudante estudante = estudanteService.buscarEstudante(estudanteId);
        return agendamentoService.listarAgendamentosPorEstudante(estudante);
    }

    public List<Agendamento> listarAgendamentosPorProfissional(Long userId) {
        Profissional profissional = profissionalService.buscar(userId);
        return agendamentoService.listarPorProfissional(profissional);
    }

    public List<Agendamento> listarAgendamentoPorEstudanteAtual(){
        return agendamentoService.listarAgendamentosEstudanteAtual();
    }

    public List<Agendamento> listarAgendamentoPorProfissionalAtual(){
        return agendamentoService.listarPorProfissionalAtual();
    }

    public List<CancelamentoAgendamento> listarCancelamentosPorEstudanteAtual(){
        return cancelamentoService.ListarPorEstudanteAtual();
    }

    public List<CancelamentoAgendamento> listarCancelamentosPorProfissionalAtual(){
        return cancelamentoService.ListarPorProfissionalAtual();
    }

    public CancelamentoAgendamento buscarCancelamento(Long id) throws CancelamentoNotFoundException {
        return cancelamentoService.buscar(id);
    }

  // ------------------- TipoBolsa ------------------- //
    public List<TipoBolsa> listarTipoBolsas() {
        return tipoBolsaService.listar();
    }

    public TipoBolsa buscarTipoBolsa(Long id) throws TipoBolsaNotFoundException {
        return tipoBolsaService.buscar(id);
    }

    public TipoBolsa salvarTipoBolsa(TipoBolsa tipoBolsa) {
        return tipoBolsaService.salvar(tipoBolsa);
    }

    public TipoBolsa editarTipoBolsa(Long id, TipoBolsa tipoBolsa) throws TipoBolsaNotFoundException {
        return tipoBolsaService.editar(id, tipoBolsa);
    }

    public void deletarTipoBolsa(Long id) throws TipoBolsaNotFoundException {
        tipoBolsaService.deletar(id);
    }

    // ------------------- TipoAuxilio ------------------- //
    public List<TipoAuxilio> listarTipoAuxilios() {
        return tipoAuxilioService.listar();
    }

    public TipoAuxilio buscarTipoAuxilio(Long id) throws TipoAuxilioNotFoundException {
        return tipoAuxilioService.buscar(id);
    }

    public TipoAuxilio salvarTipoAuxilio(TipoAuxilio tipoAuxilio) {
        return tipoAuxilioService.salvar(tipoAuxilio);
    }

    public TipoAuxilio editarTipoAuxilio(Long id, TipoAuxilio tipoAuxilio) throws TipoAuxilioNotFoundException {
        return tipoAuxilioService.editar(id, tipoAuxilio);
    }

    public void deletarTipoAuxilio(Long id) throws TipoAuxilioNotFoundException {
        tipoAuxilioService.deletar(id);
    }

    // ------------------- Auxilio ------------------- //
    public List<Auxilio> listarAuxilios() {
        return auxilioService.listar();
    }

    public Auxilio buscarAuxilio(Long id) throws AuxilioNotFoundException {
        return auxilioService.buscar(id);
    }

    public Auxilio salvarAuxilio(Auxilio auxilio) {
        return auxilioService.salvar(auxilio);
    }

    public Auxilio editarAuxilio(Long id, Auxilio auxilio) throws AuxilioNotFoundException {
        return auxilioService.editar(id, auxilio);
    }

    public void deletarAuxilio(Long id) throws AuxilioNotFoundException {
        auxilioService.deletar(id);
    }


}
