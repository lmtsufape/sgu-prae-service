package br.edu.ufape.sguPraeService.fachada;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


import br.edu.ufape.sguPraeService.comunicacao.dto.agendamento.AgendamentoResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.endereco.EnderecoRequest;
import br.edu.ufape.sguPraeService.comunicacao.dto.estudante.*;
import br.edu.ufape.sguPraeService.comunicacao.dto.tipoatendimento.TipoAtendimentoUpdateRequest;
import br.edu.ufape.sguPraeService.models.*;
import br.edu.ufape.sguPraeService.comunicacao.dto.pagamento.PagamentoPatchRequest;
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
import br.edu.ufape.sguPraeService.comunicacao.dto.beneficio.BeneficioRelatorioResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.beneficio.EstudanteRelatorioResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.beneficio.PagamentoRelatorioResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.beneficio.RelatorioFinanceiroResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.documento.DocumentoResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.estudante.CredorResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.estudante.EstudanteResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.estudante.RelatorioBeneficioResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.estudante.RelatorioEstudanteAssistidoResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.profissional.ProfissionalResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.AlunoResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.FuncionarioResponse;
import br.edu.ufape.sguPraeService.exceptions.BeneficioNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.EstudanteSemAuxilioAtivoException;
import br.edu.ufape.sguPraeService.exceptions.GlobalAccessDeniedException;
import br.edu.ufape.sguPraeService.exceptions.TipoBeneficioNotFoundException;
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
import br.edu.ufape.sguPraeService.servicos.interfaces.BeneficioService;
import br.edu.ufape.sguPraeService.servicos.interfaces.CancelamentoService;
import br.edu.ufape.sguPraeService.servicos.interfaces.CronogramaService;
import br.edu.ufape.sguPraeService.servicos.interfaces.DadosBancariosService;
import br.edu.ufape.sguPraeService.servicos.interfaces.EnderecoService;
import br.edu.ufape.sguPraeService.servicos.interfaces.EstudanteService;
import br.edu.ufape.sguPraeService.servicos.interfaces.PagamentoService;
import br.edu.ufape.sguPraeService.servicos.interfaces.ProfissionalService;
import br.edu.ufape.sguPraeService.servicos.interfaces.TipoAtendimentoService;
import br.edu.ufape.sguPraeService.servicos.interfaces.TipoBeneficioService;
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
    private final TipoBeneficioService tipoBeneficioService;
    private final BeneficioService beneficioService;
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

    public Page<EstudanteResponse> listarEstudantes(Pageable pageable) throws EstudanteNotFoundException {
        Page<Estudante> estudantes = estudanteService.listarEstudantes(pageable);

        if (estudantes.isEmpty()) {
            return Page.empty();
        }

        List<UUID> userIds = estudantes.stream()
                .map(Estudante::getUserId)
                .toList();

        List<AlunoResponse> usuarios = authServiceHandler.buscarAlunos(userIds);
        if (usuarios.isEmpty()) {
            return Page.empty();
        }

        Map<UUID, AlunoResponse> mapaAlunos = usuarios.stream()
                .collect(Collectors.toMap(AlunoResponse::getId, Function.identity()));

        List<EstudanteResponse> listaEstudantes = estudantes.getContent().stream()
                .map(estudante -> {
                    EstudanteResponse resp = new EstudanteResponse(estudante, modelMapper);
                    // "lookup" no mapa para anexar o AlunoResponse correto
                    AlunoResponse ar = mapaAlunos.get(estudante.getUserId());
                    resp.setAluno(ar);
                    return resp;
                })
                .toList();
        return new PageImpl<>(
                listaEstudantes,
                pageable,
                estudantes.getTotalElements()
        );
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
        Page<Estudante> estudantes = beneficioService.listarEstudantesComBeneficioAtivo(pageable);
        if (estudantes.isEmpty()) {
            return Page.empty(pageable);
        }
        List<AlunoResponse> alunosNoCurso = authServiceHandler.buscarAlunosPorCurso(id);

        return getCredorResponses(pageable, estudantes, alunosNoCurso);
    }

    public Page<AlunoResponse> listarCredoresParaPublicacao(Pageable pageable) {
        Page<Estudante> estudantes = beneficioService.listarEstudantesComBeneficioAtivo(pageable);
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

    public Page<CredorResponse> listarCredoresComBeneficiosAtivos(Pageable pageable) {
        return gerarCredores(pageable, beneficioService::listarEstudantesComBeneficioAtivo);
    }

    public Page<CredorResponse> listarCredoresPorBeneficio(Long beneficioId, Pageable pageable) {
        return gerarCredores(pageable, pg ->
                beneficioService.listarEstudantesPorAuxilio(beneficioId, pg)
        );
    }
    public RelatorioEstudanteAssistidoResponse gerarRelatorioEstudanteAssistido(Long estudanteId)
            throws EstudanteNotFoundException {
        Estudante estudante = estudanteService.buscarEstudante(estudanteId);
        List<Beneficio> beneficiosAtivos = beneficioService.listarPorEstudante(estudanteId);

        if (beneficiosAtivos.isEmpty()) {
            throw new EstudanteSemAuxilioAtivoException();
        }

        List<RelatorioBeneficioResponse> beneficios = beneficiosAtivos.stream()
                .map(beneficio -> new RelatorioBeneficioResponse(
                        beneficio.getTipoBeneficio().getTipo(),
                        beneficio.getValorPagamento(),
                        beneficio.getInicioBeneficio(),
                        beneficio.getFimBeneficio()))
                .collect(Collectors.toList());

        var aluno = authServiceHandler.buscarAlunoPorId(estudante.getUserId());

        return new RelatorioEstudanteAssistidoResponse(
                aluno.getNome(),
                estudante.getRendaPercapta(),
                estudante.getContatoFamilia(),
                estudante.isDeficiente(),
                estudante.getTipoDeficiencia(),
                estudante.getTipoEtnia() != null ? estudante.getTipoEtnia().getTipo() : null,
                beneficios);
    }

    private Page<CredorResponse> getCredorResponses(Pageable pageable, Page<Estudante> estudantes, List<AlunoResponse> alunos) {
        Map<UUID, AlunoResponse> mapaAlunos = alunos.stream()
                .collect(Collectors.toMap(AlunoResponse::getId, Function.identity()));


        List<CredorResponse> listaCredores = estudantes.getContent().stream()
                .map(estudante -> {
                    AlunoResponse aluno = mapaAlunos.get(estudante.getUserId());

                    EstudanteResponse er = new EstudanteResponse(estudante, modelMapper);
                    er.setAluno(aluno);

                    List<Beneficio> beneficiosAtivos = beneficioService.listarPorEstudante(estudante.getId()).stream()
                            .filter(Beneficio::isAtivo).toList();


                    return new CredorResponse(
                            er,
                            estudante.getDadosBancarios(),
                            beneficiosAtivos
                    );
                })
                .toList();


        return new PageImpl<>(
                listaCredores,
                pageable,
                estudantes.getTotalElements()
        );
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



    public EstudanteResponse buscarEstudanteAtual() throws EstudanteNotFoundException {
        UUID userId = authenticatedUserProvider.getUserId();
        Estudante estudante = estudanteService.buscarPorUserId(userId);
        AlunoResponse userInfo = authServiceHandler.getAlunoInfo();
        EstudanteResponse response = new EstudanteResponse(estudante, modelMapper);
        response.setAluno(userInfo);
        return response;
    }

    public EstudanteResponse buscarEstudantePorUserId(UUID userId) throws EstudanteNotFoundException {
        Estudante estudante = estudanteService.buscarPorUserId(userId);
        AlunoResponse userInfo = authServiceHandler.buscarAlunoPorId(userId);
        EstudanteResponse response = new EstudanteResponse(estudante, modelMapper);
        response.setAluno(userInfo);
        return response;
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
    public AgendamentoResponse agendarVaga(Long id) throws VagaNotFoundException, UnavailableVagaException {
        try {
            Vaga vaga = vagaService.buscar(id);
            Estudante estudante = estudanteService.buscarPorUserId(authenticatedUserProvider.getUserId());

            if (vaga.isDisponivel()) {
                vaga.setDisponivel(false);
                vagaService.salvar(vaga);
                Agendamento agendamento = agendamentoService.agendar(vaga, estudante);
                return mapToAgendamentoResponse(agendamento);
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

    public AgendamentoResponse buscarAgendamento(Long id) throws AgendamentoNotFoundException {
        Agendamento agendamento = agendamentoService.buscar(id);
        return mapToAgendamentoResponse(agendamento);
    }

    public Page<AgendamentoResponse> listarAgendamentosPorEstudante(Long estudanteId, Pageable pageable) {
        Estudante estudante = estudanteService.buscarEstudante(estudanteId);
        return agendamentoService.listarAgendamentosPorEstudante(estudante, pageable)
                .map(this::mapToAgendamentoResponse);
    }

    public Page<AgendamentoResponse> listarAgendamentosPorProfissional(Long userId, Pageable pageable) {
        Profissional profissional = profissionalService.buscar(userId);
        return agendamentoService.listarPorProfissional(profissional, pageable)
                .map(this::mapToAgendamentoResponse);
    }

    public Page<AgendamentoResponse> listarAgendamentoPorEstudanteAtual(Pageable pageable) {
        return agendamentoService.listarAgendamentosEstudanteAtual(pageable)
                .map(this::mapToAgendamentoResponse);
    }

    public Page<AgendamentoResponse> listarAgendamentoPorProfissionalAtual(Pageable pageable) {
        return agendamentoService.listarPorProfissionalAtual(pageable)
                .map(this::mapToAgendamentoResponse);
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

    private AgendamentoResponse mapToAgendamentoResponse(Agendamento agendamento) {
        AgendamentoResponse response = new AgendamentoResponse(agendamento, modelMapper);
        if (agendamento.getEstudante() != null && agendamento.getEstudante().getUserId() != null) {
            AlunoResponse aluno = authServiceHandler.buscarAlunoPorId(agendamento.getEstudante().getUserId());
            if (response.getEstudante() != null) {
                response.getEstudante().setAluno(aluno);
            }
        }
        return response;
    }


    // ------------------- TipoBeneficio ------------------- //
    public Page<TipoBeneficio> listarTipoBeneficios(Pageable pageable) {
        return tipoBeneficioService.listar(pageable);
    }

    public TipoBeneficio buscarTipoBeneficio(Long id) throws TipoBeneficioNotFoundException {
        return tipoBeneficioService.buscar(id);
    }

    public TipoBeneficio salvarTipoBeneficio(TipoBeneficio tipoBeneficio) {
        return tipoBeneficioService.salvar(tipoBeneficio);
    }

    public TipoBeneficio editarTipoBeneficio(Long id, TipoBeneficio tipoBeneficio) throws TipoBeneficioNotFoundException {
        return tipoBeneficioService.editar(id, tipoBeneficio);
    }

    public void deletarTipoBeneficio(Long id) throws TipoBeneficioNotFoundException {
        tipoBeneficioService.deletar(id);
    }

    public void desativarTipoBeneficio(Long id) throws TipoBeneficioNotFoundException {
        tipoBeneficioService.desativar(id);
    }

    // ------------------- Beneficio ------------------- //
    public Page<Beneficio> listarBeneficios(Pageable pageable) {
        return beneficioService.listar(pageable);
    }

    public Page<Beneficio> listarBeneficiosPorTipo(Long tipoId, Pageable pageable) throws BeneficioNotFoundException {
        return beneficioService.listarPorTipo(tipoId, pageable);
    }

    public Page<Beneficio> listarBeneficiosPorEstudanteId(Long estudanteId, Pageable pageable) throws EstudanteNotFoundException {
        return beneficioService.listarPorEstudante(estudanteId, pageable);
    }

    public Beneficio buscarBeneficios(Long id) throws BeneficioNotFoundException {
        return beneficioService.buscar(id);
    }

    public Beneficio salvarBeneficios(Long estudanteId, Beneficio beneficio, MultipartFile termo, Long tipoBeneficioId)
            throws TipoBeneficioNotFoundException {
        Estudante estudante = estudanteService.buscarEstudante(estudanteId);
        TipoBeneficio tipoBeneficio = tipoBeneficioService.buscar(tipoBeneficioId);
        beneficio.setEstudantes(estudante);
        beneficio.setTipoBeneficio(tipoBeneficio);

        MultipartFile[] arquivos = { termo };
        List<Documento> documentos = armazenamentoService.salvarArquivo(arquivos);
        beneficio.setTermo(documentos.getFirst());


        return beneficioService.salvar(beneficio);

    }

    public Beneficio editarBeneficios(Long id, Long estudanteId, Beneficio beneficio, MultipartFile termo, Long tipoBeneficioId)
            throws BeneficioNotFoundException, TipoBeneficioNotFoundException {
        Beneficio aux = buscarBeneficios(id);
        Estudante estudante = estudanteService.buscarEstudante(estudanteId);
        TipoBeneficio tipoBeneficio = tipoBeneficioService.buscar(tipoBeneficioId);
        beneficio.setEstudantes(estudante);
        beneficio.setTipoBeneficio(tipoBeneficio);

        if (termo != null) {
            MultipartFile[] arquivos = { termo };
            List<Documento> documentos = armazenamentoService.salvarArquivo(arquivos);
            beneficio.setTermo(documentos.getFirst());
            beneficio.getTermo().setId(aux.getTermo().getId());
        }

        return beneficioService.editar(aux, beneficio);
    }

    public void deletarBeneficio(Long id) throws BeneficioNotFoundException {
        beneficioService.deletar(id);
    }

    public List<Beneficio> listarBeneficiosPendentesMesAtual()  {
        return beneficioService.listarBeneficiosPendentesMesAtual();
    }

    public List<Beneficio> buscarBeneficioPorPagamentoId(Long pagamentoId) {
        return beneficioService.buscarPorPagamento(pagamentoId);
    }

    public RelatorioFinanceiroResponse gerarRelatorioFinanceiro(LocalDate inicio, LocalDate fim) {
        List<Beneficio> beneficios = beneficioService
                .listar().stream()
                .filter(aux -> aux.isAtivo() && aux.isStatus()
                        && !aux.getInicioBeneficio().isAfter(fim)
                        && !aux.getFimBeneficio().isBefore(inicio))
                .toList();

        List<BeneficioRelatorioResponse> detalhes = new ArrayList<>();
        BigDecimal totalGeral = BigDecimal.ZERO;

        for (Beneficio beneficio : beneficios) {
            List<Estudante> estudantes = beneficioService.listarEstudantesPorAuxilio(beneficio.getId());
            List<EstudanteRelatorioResponse> estudantesDto = new ArrayList<>();

            List<Pagamento> pagamentosFiltrados = beneficio.getPagamentos().stream()
                    .filter(p -> !p.getData().isBefore(inicio) && !p.getData().isAfter(fim) && p.isAtivo())
                    .toList();

            BigDecimal totalAuxilio = beneficio.getPagamentos().stream()
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
            detalhes.add(new BeneficioRelatorioResponse(
                    beneficio.getId(),
                    beneficio.getTipoBeneficio().getDescricao(),
                    beneficio.getValorPagamento(),
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

    public List<Pagamento> listarPagamentosPorBeneficioId(Long beneficioId) throws BeneficioNotFoundException {
        return beneficioService.buscar(beneficioId).getPagamentos();
    }

    public List<Beneficio> listarPagosPorMes() throws BeneficioNotFoundException {
        return beneficioService.listarPagosPorMes();
    }

    public Pagamento buscarPagamento(Long id) throws PagamentoNotFoundException {
        return pagamentoService.buscar(id);
    }

    public List<Pagamento> salvarPagamentos(List<Pagamento> pagamentos) throws BeneficioNotFoundException {
        for (Pagamento p : pagamentos) {
            Long id = p.getBeneficio().getId();
            Beneficio b = buscarBeneficios(id);
            p.setBeneficio(b);
        }
        return pagamentoService.salvar(pagamentos);
    }

    public Pagamento editarPagamento(Long id, PagamentoPatchRequest dto) throws PagamentoNotFoundException {
        Pagamento pagamento = pagamentoService.buscar(id);

        if (dto.getValor() != null) {
            pagamento.setValor(dto.getValor());
        }
        if (dto.getData() != null) {
            pagamento.setData(dto.getData());
        }
        return pagamentoService.salvar(List.of(pagamento)).getFirst();
    }

    public void deletarPagamento(Long id) throws PagamentoNotFoundException {
        pagamentoService.deletar(id);
    }

    public void desativarPagamento(Long id) throws PagamentoNotFoundException {
        pagamentoService.desativar(id);
    }

    public Page<Pagamento> listarPagamentosPorValor(BigDecimal min, BigDecimal max, Pageable pageable) {
        return pagamentoService.listarPorValor(min, max, pageable);
    }

    public List<Pagamento> listarPagamentosPorEstudante(Long estudanteId) {
        return pagamentoService.listarPorEstudanteId(estudanteId);
    }

    // ------------------- Armazenamento ------------------- //

    public List<DocumentoResponse> converterDocumentosParaBase64(List<Documento> documentos) throws IOException {
        return armazenamentoService.converterDocumentosParaBase64(documentos);
    }
}