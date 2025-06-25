package br.edu.ufape.sguPraeService.fachada;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import br.edu.ufape.sguPraeService.comunicacao.dto.endereco.EnderecoRequest;
import br.edu.ufape.sguPraeService.comunicacao.dto.estudante.*;
import br.edu.ufape.sguPraeService.comunicacao.dto.tipoatendimento.TipoAtendimentoUpdateRequest;
import br.edu.ufape.sguPraeService.models.*;
import jakarta.ws.rs.NotAllowedException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.edu.ufape.sguPraeService.auth.AuthenticatedUserProvider;
import br.edu.ufape.sguPraeService.auth.RabbitAuthServiceClient;
import br.edu.ufape.sguPraeService.comunicacao.dto.auxilio.AuxilioRelatorioResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.auxilio.EstudanteRelatorioResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.auxilio.PagamentoRelatorioResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.auxilio.RelatorioFinanceiroResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.documento.DocumentoResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.profissional.ProfissionalResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.AlunoResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.FuncionarioResponse;
import br.edu.ufape.sguPraeService.exceptions.AuxilioNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.EstudanteSemAuxilioAtivoException;
import br.edu.ufape.sguPraeService.exceptions.GlobalAccessDeniedException;
import br.edu.ufape.sguPraeService.exceptions.TipoAuxilioNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.TipoBolsaNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.UnavailableVagaException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.AgendamentoNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.CancelamentoNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.CronogramaNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.EstudanteNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.PagamentoNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.ProfissionalNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.TipoAtendimentoNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.TipoEtniaNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.VagaNotFoundException;
import br.edu.ufape.sguPraeService.servicos.interfaces.AgendamentoService;
import br.edu.ufape.sguPraeService.servicos.interfaces.ArmazenamentoService;
import br.edu.ufape.sguPraeService.servicos.interfaces.AuthServiceHandler;
import br.edu.ufape.sguPraeService.servicos.interfaces.AuxilioService;
import br.edu.ufape.sguPraeService.servicos.interfaces.CancelamentoService;
import br.edu.ufape.sguPraeService.servicos.interfaces.CronogramaService;
import br.edu.ufape.sguPraeService.servicos.interfaces.DadosBancariosService;
import br.edu.ufape.sguPraeService.servicos.interfaces.EnderecoService;
import br.edu.ufape.sguPraeService.servicos.interfaces.EstudanteService;
import br.edu.ufape.sguPraeService.servicos.interfaces.PagamentoService;
import br.edu.ufape.sguPraeService.servicos.interfaces.ProfissionalService;
import br.edu.ufape.sguPraeService.servicos.interfaces.TipoAtendimentoService;
import br.edu.ufape.sguPraeService.servicos.interfaces.TipoAuxilioService;
import br.edu.ufape.sguPraeService.servicos.interfaces.TipoBolsaService;
import br.edu.ufape.sguPraeService.servicos.interfaces.TipoEtniaService;
import br.edu.ufape.sguPraeService.servicos.interfaces.VagaService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
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
    private final PagamentoService pagamentoService;
    private final ArmazenamentoService armazenamentoService;

    @Value("${authClient.client-id}")
    private String clientId;

    // ------------------- Profissional ------------------- //
    public List<ProfissionalResponse> listarProfissionais() {
        List<ProfissionalResponse> profissionalResponse = new ArrayList<>();
        List<Profissional> profissionais = profissionalService.listar();
        if (profissionais.isEmpty()) {
            return profissionalResponse;
        }
        List<UUID> userIds = profissionais.stream().map(Profissional::getUserId).toList();
        log.info("Profissionals encontrados: {}", userIds);
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

    // ================== Estudante ================== //

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

    private List<EstudanteResponse> buscarAlunosParaEstudantes(List<Estudante> estudantes) {
        if(estudantes.isEmpty())
            Collections.emptyList();

        List<UUID> userIds = estudantes.stream()
                .map(Estudante::getUserId)
                .toList();

        List<AlunoResponse> usuarios = authServiceHandler.buscarAlunos(userIds);
        if (usuarios.isEmpty()) {
            Collections.emptyList();
        }

        Map<UUID, AlunoResponse> mapaAlunos = usuarios.stream()
                .collect(Collectors.toMap(AlunoResponse::getId, Function.identity()));

        List<EstudanteResponse> listaEstudantes = estudantes.stream()
                .map(estudante -> {
                    EstudanteResponse resp = new EstudanteResponse(estudante, modelMapper);
                    AlunoResponse ar = mapaAlunos.get(estudante.getUserId());
                    if (ar != null) 
                        resp.setAluno(ar);
                    return resp;
                })
                .toList();
        return listaEstudantes;
    }

    public Page<Estudante> listarEstudantesPorAuxilio(Long auxilioId, Pageable pageable) {
        return estudanteService.listarEstudantesPorAuxilioId(auxilioId, pageable);
    }

    public Page<EstudanteResponse> listarEstudantes(Pageable pageable) throws EstudanteNotFoundException {
        Page<Estudante> estudantes = estudanteService.listarEstudantes(pageable);

        List<EstudanteResponse> listaEstudantes = buscarAlunosParaEstudantes(estudantes.getContent());
        return new PageImpl<>(
                listaEstudantes,
                pageable,
                estudantes.getTotalElements()
        );
    }

    public List<EstudanteResponse> listarEstudantes() throws EstudanteNotFoundException {
        List<Estudante> estudantes = estudanteService.listarEstudantes();
        return buscarAlunosParaEstudantes(estudantes);
    }

    @CircuitBreaker(name = "authServiceClient", fallbackMethod = "fallbackAtualizarEstudante")
    public EstudanteResponse atualizarEstudante(EstudanteUpdateRequest estudanteUpdateRequest)
            throws EstudanteNotFoundException, TipoEtniaNotFoundException {
        UUID userId = authenticatedUserProvider.getUserId();
        Estudante estudanteParcial = new Estudante();
        Estudante estudante = estudanteService.buscarPorUserId(userId);

        if(estudanteUpdateRequest.getRendaPercapta() != null){
            estudanteParcial.setRendaPercapta(estudanteUpdateRequest.getRendaPercapta());
        }

        if(estudanteUpdateRequest.getContatoFamilia() != null ){
            estudanteParcial.setContatoFamilia(estudanteUpdateRequest.getContatoFamilia());
        }

        if(estudanteUpdateRequest.getDeficiente() != null){
             estudanteParcial.setDeficiente(estudanteUpdateRequest.getDeficiente());
        }

        if(estudanteUpdateRequest.getTipoDeficiencia() != null){
             estudanteParcial.setTipoDeficiencia(estudanteUpdateRequest.getTipoDeficiencia());
        }

        if(estudanteUpdateRequest.getTipoEtniaId() != null){
             estudanteParcial.setTipoEtnia(tipoEtniaService.buscarTipoEtnia(estudanteUpdateRequest.getTipoEtniaId()));
        }

        if(estudanteUpdateRequest.getEndereco() != null){
            EnderecoRequest enderecoDTO = estudanteUpdateRequest.getEndereco();
            Endereco enderecoAtualizado = enderecoDTO.convertToEntity(enderecoDTO, this.modelMapper);
             estudanteParcial.setEndereco(enderecoService.editarEndereco( estudante.getEndereco().getId(), enderecoAtualizado));
        }

         Estudante estudanteAtualizado = estudanteService.atualizarEstudante(estudanteParcial, estudante);
        return new EstudanteResponse(estudanteAtualizado, modelMapper);
    }

    public void deletarEstudante(Long id) throws EstudanteNotFoundException {
        estudanteService.deletarEstudante(id);
    }

    public Page<EstudanteResponse> listarEstudantesPorCurso(Long idCurso, Pageable pageable) {
        List<AlunoResponse> alunosNoCurso = authServiceHandler.buscarAlunosPorCurso(idCurso);

        List<UUID> userIds = alunosNoCurso.stream()
                .map(AlunoResponse::getId)
                .collect(Collectors.toList());

        Map<UUID, AlunoResponse> mapaAlunos = alunosNoCurso.stream()
                .collect(Collectors.toMap(AlunoResponse::getId, Function.identity()));

        Page<Estudante> pageEntidades = estudanteService.buscarPorUserIds(userIds, pageable);

        return pageEntidades.map(estudante -> {
            EstudanteResponse resp = new EstudanteResponse(estudante, modelMapper);
            resp.setAluno(mapaAlunos.get(estudante.getUserId()));
            return resp;
        });
    }

    public Page<CredorResponse> listarCredoresPorCurso(Long id, Pageable pageable) {
        Page<Estudante> estudantes = estudanteService.listarEstudantesComAuxilioAtivo(pageable);
        if (estudantes.isEmpty()) {
            return Page.empty(pageable);
        }
        List<AlunoResponse> alunosNoCurso = authServiceHandler.buscarAlunosPorCurso(id);

        return getCredorResponses(pageable, estudantes, alunosNoCurso);
    }

    public Page<AlunoResponse> listarCredoresParaPublicacao(Pageable pageable) {
        Page<Estudante> estudantes = estudanteService.listarEstudantesComAuxilioAtivo(pageable);
        if (estudantes.isEmpty()) {
            return Page.empty(pageable);
        }
        List<UUID> userIds = estudantes.stream().map(Estudante::getUserId).toList();

        List<AlunoResponse> alunos  = authServiceHandler.buscarAlunos(userIds);
        Map<UUID, AlunoResponse> mapaAlunos = alunos.stream()
                .collect(Collectors.toMap(AlunoResponse::getId, Function.identity()));

        List<AlunoResponse> conteudo = estudantes.getContent().stream()
                .map(est -> mapaAlunos.get(est.getUserId()))
                .filter(Objects::nonNull)
                .toList();
        return new PageImpl<>(conteudo, pageable, conteudo.size());
    }

    public Page<CredorResponse> listarCredoresComAuxiliosAtivos(Pageable pageable) {
        return gerarCredores(pageable, estudanteService::listarEstudantesComAuxilioAtivo);
    }

    public Page<CredorResponse> listarCredoresPorAuxilio(Long auxilioId, Pageable pageable) {
        return gerarCredores(pageable, pg ->
                estudanteService.listarEstudantesPorAuxilioId(auxilioId, pg)
        );
    }

    public List<CredorResponse> listarCredoresComAuxiliosAtivos() {
        return gerarCredores(estudanteService.listarEstudantesComAuxilioAtivo());
    }

    public List<CredorResponse> listarCredoresPorAuxilio(Long auxilioId) {
        return gerarCredores(estudanteService.listarEstudantesPorAuxilioId(auxilioId));
    }

    public RelatorioEstudanteAssistidoResponse gerarRelatorioEstudanteAssistido(Long estudanteId)
            throws EstudanteNotFoundException {
        Estudante estudante = estudanteService.buscarEstudante(estudanteId);

        if (estudante == null) {
            throw new EstudanteNotFoundException();
        }

        if (estudante.getAuxilios() == null || estudante.getAuxilios().stream().noneMatch(Auxilio::isAtivo)) {
            throw new EstudanteSemAuxilioAtivoException();
        }

        List<RelatorioAuxilioResponse> auxilios = estudante.getAuxilios().stream()
                .filter(Auxilio::isAtivo)
                .map(auxilio -> new RelatorioAuxilioResponse(
                        auxilio.getTipoAuxilio().getTipo(),
                        auxilio.getValorBolsa(),
                        auxilio.getInicioBolsa(),
                        auxilio.getFimBolsa()))
                .collect(Collectors.toList());

        var aluno = authServiceHandler.buscarAlunoPorId(estudante.getUserId());

        return new RelatorioEstudanteAssistidoResponse(
                aluno.getNome(),
                estudante.getRendaPercapta(),
                estudante.getContatoFamilia(),
                estudante.isDeficiente(),
                estudante.getTipoDeficiencia(),
                estudante.getTipoEtnia() != null ? estudante.getTipoEtnia().getTipo() : null,
                auxilios);
    }

    private Page<CredorResponse> getCredorResponses(Pageable pageable, Page<Estudante> estudantes, List<AlunoResponse> alunos) {
        return new PageImpl<>(
                getCredorResponses(estudantes.getContent(), alunos),
                pageable,
                estudantes.getTotalElements()
        );
    }

    private List<CredorResponse> getCredorResponses(List<Estudante> estudantes, List<AlunoResponse> alunos) {
        Map<UUID, AlunoResponse> mapaAlunos = alunos.stream()
                .collect(Collectors.toMap(AlunoResponse::getId, Function.identity()));


        List<CredorResponse> listaCredores = estudantes.stream()
                .map(estudante -> {
                    AlunoResponse aluno = mapaAlunos.get(estudante.getUserId());

                    EstudanteResponse er = new EstudanteResponse(estudante, modelMapper);
                    er.setAluno(aluno);

                    List<Auxilio> auxAtivos = estudante.getAuxilios().stream()
                            .filter(Auxilio::isAtivo)
                            .toList();

                    return new CredorResponse(
                            er,
                            estudante.getDadosBancarios(),
                            auxAtivos
                    );
                })
                .toList();


        return listaCredores;
    }

    private Page<CredorResponse> gerarCredores(
            Pageable pageable,
            Function<Pageable, Page<Estudante>> fetchEstudantes
    ) {
        Page<Estudante> pageEstudantes = fetchEstudantes.apply(pageable);
        if (pageEstudantes.isEmpty()) {
            return Page.empty(pageable);
        }

        List<UUID> userIds = pageEstudantes.getContent().stream()
                .map(Estudante::getUserId)
                .toList();
        List<AlunoResponse> alunos = authServiceHandler.buscarAlunos(userIds);
        return getCredorResponses(pageable, pageEstudantes, alunos);
    }

    private List<CredorResponse> gerarCredores(List<Estudante> estudantes) {
        if (estudantes.isEmpty()) {
            return Collections.emptyList();
        }

        List<UUID> userIds = estudantes.stream()
                .map(Estudante::getUserId)
                .toList();
        List<AlunoResponse> alunos = authServiceHandler.buscarAlunos(userIds);
        return getCredorResponses(estudantes, alunos);
    }

    // ================== TipoEtnia ================== //

    public TipoEtnia salvarTipoEtnia(TipoEtnia tipoEtnia) {
        return tipoEtniaService.salvarTipoEtnia(tipoEtnia);
    }

    public TipoEtnia buscarTipoEtnia(Long id) throws TipoEtniaNotFoundException {
        return tipoEtniaService.buscarTipoEtnia(id);
    }

    public Page<TipoEtnia> listarTiposEtnia(Pageable pageable) {
        return tipoEtniaService.listarTiposEtnia(pageable);
    }

    public TipoEtnia atualizarTipoEtnia(Long id, TipoEtnia tipoEtnia) throws TipoEtniaNotFoundException {
        return tipoEtniaService.atualizarTipoEtnia(id, tipoEtnia);
    }

    public void deletarTipoEtnia(Long id) throws TipoEtniaNotFoundException {
        tipoEtniaService.deletarTipoEtnia(id);
    }


    // ================== Dados Bancarios ================== //

    public DadosBancarios salvarDadosBancarios(Long idEstudante, DadosBancarios dadosBancarios) {
        Estudante estudante = estudanteService.buscarEstudante(idEstudante);
        DadosBancarios salvo = dadosBancariosService.salvarDadosBancarios(dadosBancarios);
        estudante.setDadosBancarios(salvo);
        estudanteService.salvarEstudante(estudante);
        return salvo;
    }

    public Page<DadosBancarios> listarDadosBancarios(Pageable pageable) {
        return dadosBancariosService.listarDadosBancarios(pageable);
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
    public Page<TipoAtendimento> listarTipoAtendimentos(Pageable pageable) {
        return tipoAtendimentoService.listar(pageable);
    }

    public TipoAtendimento buscarTipoAtendimento(Long id) throws TipoAtendimentoNotFoundException {
        return tipoAtendimentoService.buscar(id);
    }

    public TipoAtendimento salvarTipoAtendimento(TipoAtendimento tipoAtendimento) {
        return tipoAtendimentoService.salvar(tipoAtendimento);
    }

    public TipoAtendimento editarTipoAtendimento(Long id, TipoAtendimentoUpdateRequest dto)
            throws TipoAtendimentoNotFoundException {
        TipoAtendimento tipoAtendimento = tipoAtendimentoService.buscar(id);
        if (dto.getNome() != null && !dto.getNome().isBlank()) {
            tipoAtendimento.setNome(dto.getNome());
        }

         if (dto.getTempoAtendimento() != null) {
            tipoAtendimento.setTempoAtendimento(dto.getTempoAtendimento());
        }

        if(dto.getHorarios() != null) {
            tipoAtendimento.setHorarios(dto.getHorarios());
        }

        return tipoAtendimentoService.editar(id, tipoAtendimento);
    }

    public void deletarTipoAtendimento(Long id) throws TipoAtendimentoNotFoundException {
        tipoAtendimentoService.deletar(id);
    }

    public TipoAtendimento deletarHorarioTipoAtendimento(Long id, int index) throws TipoAtendimentoNotFoundException {
        return tipoAtendimentoService.deletarHorario(id, index);
    }

    // ------------------- Cronograma ------------------- //
    public Page<Cronograma> listarCronogramasPorProfissional(Pageable pageable) {

        return cronogramaService.listarPorProfissional(authenticatedUserProvider.getUserId(), pageable);
    }

    public Page<Cronograma> listarCronogramas(Pageable pageable) {
        return cronogramaService.listar(pageable);
    }

    public Page<Cronograma> listarCronogramasPorTipoAtendimento(Long tipoAtendimentoId, Pageable pageable) {
        return cronogramaService.listarPorTipoAtendimento(tipoAtendimentoId, pageable);
    }

    public Cronograma buscarCronograma(Long id) throws CronogramaNotFoundException {
        return cronogramaService.buscar(id);
    }

    @Transactional
    public Cronograma salvarCronograma(Cronograma cronograma, Long tipoAtendimentoId)
            throws TipoAtendimentoNotFoundException {
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

    @Transactional
    public Cronograma editarCronograma(Long cronogramaId, Cronograma cronograma, Long tipoAtendimentoId)
            throws TipoAtendimentoNotFoundException, CronogramaNotFoundException {
        UUID userId = authenticatedUserProvider.getUserId();
        TipoAtendimento tipoAtendimento = buscarTipoAtendimento(tipoAtendimentoId);
        Cronograma cronogramaExistente = cronogramaService.buscar(cronogramaId);
        if (!cronogramaExistente.getProfissional().getUserId().equals(userId)) {
            throw new NotAllowedException("Você não tem permissão para editar este cronograma.");
        }
        cronogramaExistente.setData(cronograma.getData());
        cronogramaExistente.setTipoAtendimento(tipoAtendimento);
        List<Vaga> novas = vagaService
                .gerarVagas(tipoAtendimento.getHorarios(), tipoAtendimento.getTempoAtendimento());
        cronogramaExistente.trocarVagas(novas);
        return cronogramaService.salvar(cronogramaExistente);
    }

    public void deletarCronograma(Long id) throws CronogramaNotFoundException {
        cronogramaService.deletar(id);
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
    public CancelamentoAgendamento cancelarAgendamento(Long id, CancelamentoAgendamento cancelamento)
            throws AgendamentoNotFoundException {
        Agendamento agendamento = agendamentoService.buscar(id);
        if (!Objects.equals(agendamento.getEstudante().getUserId(), authenticatedUserProvider.getUserId())
                && !Objects.equals(agendamento.getVaga().getCronograma().getProfissional().getUserId(),
                        authenticatedUserProvider.getUserId())) {
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

    public Page<Agendamento> listarAgendamentosPorEstudante(Long estudanteId, Pageable pageable) {
        Estudante estudante = estudanteService.buscarEstudante(estudanteId);
        return agendamentoService.listarAgendamentosPorEstudante(estudante, pageable);
    }

    public Page<Agendamento> listarAgendamentosPorProfissional(Long userId, Pageable pageable) {
        Profissional profissional = profissionalService.buscar(userId);
        return agendamentoService.listarPorProfissional(profissional, pageable);
    }

    public Page<Agendamento> listarAgendamentoPorEstudanteAtual(Pageable pageable) {
        return agendamentoService.listarAgendamentosEstudanteAtual(pageable);
    }

    public Page<Agendamento> listarAgendamentoPorProfissionalAtual(Pageable pageable) {
        return agendamentoService.listarPorProfissionalAtual(pageable);
    }

    public Page<CancelamentoAgendamento> listarCancelamentosPorEstudanteAtual(Pageable pageable) {
        return cancelamentoService.ListarPorEstudanteAtual(pageable);
    }

    public Page<CancelamentoAgendamento> listarCancelamentosPorProfissionalAtual(Pageable pageable) {
        return cancelamentoService.ListarPorProfissionalAtual(pageable);
    }

    public CancelamentoAgendamento buscarCancelamento(Long id) throws CancelamentoNotFoundException {
        return cancelamentoService.buscar(id);
    }

    // ------------------- TipoBolsa ------------------- //
    public Page<TipoBolsa> listarTipoBolsas(Pageable pageable) {
        return tipoBolsaService.listar(pageable);
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

    public void desativarTipoBolsa(Long id) throws TipoBolsaNotFoundException {
        tipoBolsaService.desativar(id);
    }

    // ------------------- TipoAuxilio ------------------- //
    public Page<TipoAuxilio> listarTipoAuxilios(Pageable pageable) {
        return tipoAuxilioService.listar(pageable);
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

    public void desativarTipoAuxilio(Long id) throws TipoAuxilioNotFoundException {
        tipoAuxilioService.desativar(id);
    }

    // ------------------- Auxilio ------------------- //
    public Page<Auxilio> listarAuxilios(Pageable pageable) {
        return auxilioService.listar(pageable);
    }

    public List<Auxilio> listarAuxilios() {
        return auxilioService.listar();
    }

    public Page<Auxilio> listarAuxiliosPorTipo(Long tipoId, Pageable pageable) throws AuxilioNotFoundException {
        return auxilioService.listarPorTipo(tipoId, pageable);
    }

    public Page<Auxilio> listarAuxiliosPorEstudanteId(Long estudanteId, Pageable pageable) throws EstudanteNotFoundException {
        List<Auxilio> auxilios = estudanteService.buscarEstudante(estudanteId).getAuxilios();
        return new PageImpl<>(auxilios, pageable, auxilios.size());
    }

    public Auxilio buscarAuxilio(Long id) throws AuxilioNotFoundException {
        return auxilioService.buscar(id);
    }

    public Auxilio buscarAuxilioPorPagamentoId(Long pagamentoId) throws AuxilioNotFoundException {
        return auxilioService.buscarPorPagamentoId(pagamentoId);
    }

    public Auxilio salvarAuxilio(Long estudanteId, Auxilio auxilio, MultipartFile termo)
            throws TipoAuxilioNotFoundException, TipoBolsaNotFoundException {
        Estudante estudante = estudanteService.buscarEstudante(estudanteId);
        auxilio.setId(null);

        if (auxilio.getTipoAuxilio() != null) {
            TipoAuxilio tipoAuxilio = buscarTipoAuxilio(auxilio.getTipoAuxilio().getId());
            auxilio.setTipoAuxilio(tipoAuxilio);
            auxilio.setValorPagamento(auxilio.getValorBolsa());
        }

        if (auxilio.getTipoBolsa() != null) {
            TipoBolsa tipoBolsa = buscarTipoBolsa(auxilio.getTipoBolsa().getId());
            auxilio.setTipoBolsa(tipoBolsa);
        }

        MultipartFile[] arquivos = { termo };
        List<Documento> documentos = armazenamentoService.salvarArquivo(arquivos);
        auxilio.setTermo(documentos.getFirst());

        auxilio.addEstudante(estudante);

        auxilio = auxilioService.salvar(auxilio);
        return auxilio;
    }

    public Auxilio editarAuxilio(Long id, Auxilio auxilio, MultipartFile termo)
            throws AuxilioNotFoundException, TipoAuxilioNotFoundException, TipoBolsaNotFoundException {
        Auxilio aux = buscarAuxilio(id);
        auxilio.setId(id);

        if (auxilio.getTipoAuxilio() != null) {
            TipoAuxilio tipoAuxilio = buscarTipoAuxilio(auxilio.getTipoAuxilio().getId());
            auxilio.setTipoAuxilio(tipoAuxilio);
            auxilio.setTipoBolsa(null);
        }

        if (auxilio.getTipoBolsa() != null) {
            TipoBolsa tipoBolsa = buscarTipoBolsa(auxilio.getTipoBolsa().getId());
            auxilio.setTipoBolsa(tipoBolsa);
            auxilio.setTipoAuxilio(null);
        }

        if (termo != null) {
            MultipartFile[] arquivos = { termo };
            List<Documento> documentos = armazenamentoService.salvarArquivo(arquivos);
            auxilio.setTermo(documentos.getFirst());
            auxilio.getTermo().setId(aux.getTermo().getId());
        }

        return auxilioService.editar(id, auxilio);
    }

    public void deletarAuxilio(Long id) throws AuxilioNotFoundException {
        auxilioService.deletar(id);
    }

    public Page<Auxilio> listarAuxiliosPendentesMesAtual(Pageable pageable) {
        return auxilioService.listarAuxiliosPendentesMesAtual(pageable);
    }

    public RelatorioFinanceiroResponse gerarRelatorioFinanceiro(LocalDate inicio, LocalDate fim) {
        List<Auxilio> auxilios = auxilioService
                .listar().stream()
                .filter(aux -> aux.isAtivo() && aux.isStatus()
                        && !aux.getInicioBolsa().isAfter(fim)
                        && !aux.getFimBolsa().isBefore(inicio))
                .toList();

        List<AuxilioRelatorioResponse> detalhes = new ArrayList<>();
        BigDecimal totalGeral = BigDecimal.ZERO;

        for (Auxilio auxilio : auxilios) {
            List<Estudante> estudantes = estudanteService.listarEstudantesPorAuxilioId(auxilio.getId());
            List<EstudanteRelatorioResponse> estudantesDto = new ArrayList<>();

            List<Pagamento> pagamentosFiltrados = auxilio.getPagamentos().stream()
                    .filter(p -> !p.getData().isBefore(inicio) && !p.getData().isAfter(fim) && p.isAtivo())
                    .toList();

            BigDecimal totalAuxilio = auxilio.getPagamentos().stream()
                    .filter(p -> !p.getData().isBefore(inicio) && !p.getData().isAfter(fim) && p.isAtivo())
                    .map(Pagamento::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            List<PagamentoRelatorioResponse> pagamentosDto = pagamentosFiltrados.stream()
                    .map(p -> new PagamentoRelatorioResponse(p.getValor(), p.getData()))
                    .toList();

            for (Estudante estudante : estudantes) {
                var aluno = authServiceHandler.buscarAlunoPorId(estudante.getUserId());

                estudantesDto.add(new EstudanteRelatorioResponse(
                        aluno.getNome(),
                        aluno.getCpf(),
                        aluno.getMatricula(),
                        aluno.getEmail(),
                        aluno.getTelefone(),
                        aluno.getCurso()));
            }
            totalGeral = totalGeral.add(totalAuxilio);
            detalhes.add(new AuxilioRelatorioResponse(
                    auxilio.getId(),
                    auxilio.getTipoBolsa().getDescricao(),
                    auxilio.getValorBolsa(),
                    pagamentosDto,
                    totalAuxilio,
                    estudantesDto));
        }
        return new RelatorioFinanceiroResponse(detalhes, totalGeral);
    }

    // ------------------- Pagamento ------------------- //

    public List<Pagamento> listarPagamentos() {
        return pagamentoService.listar();
    }

    public Page<Pagamento> listarPagamentos(Pageable pageable) {
        return pagamentoService.listar(pageable);
    }

    public Page<Pagamento> listarPagamentosPorAuxilioId(Long auxilioId, Pageable pageable) throws AuxilioNotFoundException {
        List<Pagamento> pagamentos = auxilioService.buscar(auxilioId).getPagamentos();
        return new PageImpl<>(pagamentos, pageable, pagamentos.size());
    }

    public Page<Auxilio> listarPagosPorMes(int ano, int mes, Pageable pageable) throws AuxilioNotFoundException {
        return auxilioService.listarPagosPorMes(ano, mes, pageable);
    }

    public Pagamento buscarPagamento(Long id) throws PagamentoNotFoundException {
        return pagamentoService.buscar(id);
    }

    public List<Pagamento> salvarPagamentos(List<Pagamento> pagamentos, Long auxilioId) throws AuxilioNotFoundException {
        Auxilio auxilio = buscarAuxilio(auxilioId);
        List<Pagamento> rPagamentos = new ArrayList<>();
        for (Pagamento pagamento : pagamentos) {
            auxilio.addPagamento(pagamento);
            pagamento.setId(null);
            auxilio = auxilioService.editar(auxilioId, auxilio);
            rPagamentos.add(auxilio.getPagamentos().getLast());
        }
        return rPagamentos;
    }

    public Pagamento editarPagamento(Long id, Pagamento pagamento) throws PagamentoNotFoundException {
        return pagamentoService.editar(id, pagamento);
    }

    public void deletarPagamento(Long id) throws PagamentoNotFoundException, AuxilioNotFoundException {
        Pagamento pagamento = buscarPagamento(id);
        Auxilio auxilio = buscarAuxilioPorPagamentoId(id);
        auxilio.getPagamentos().remove(pagamento);
        auxilio = auxilioService.editar(auxilio.getId(), auxilio);
        pagamentoService.deletar(id);
    }

    public void desativarPagamento(Long id) throws PagamentoNotFoundException {
        pagamentoService.desativar(id);
    }

    public List<Pagamento> listarPagamentosPorValor(BigDecimal min, BigDecimal max) {
        return pagamentoService.listarPorValor(min, max);
    }

    public List<Pagamento> listarPagamentosPorEstudante(Long estudanteId) {
        buscarEstudante(estudanteId);
        return auxilioService.buscarPorEstudanteId(estudanteId).stream()
                .flatMap(aux -> aux.getPagamentos().stream())
                .filter(Pagamento::isAtivo)
                .sorted(Comparator.comparing(Pagamento::getData).reversed())
                .toList();
    }

    // ------------------- Armazenamento ------------------- //

    public List<DocumentoResponse> converterDocumentosParaBase64(List<Documento> documentos) throws IOException {
        return armazenamentoService.converterDocumentosParaBase64(documentos);
    }
}
