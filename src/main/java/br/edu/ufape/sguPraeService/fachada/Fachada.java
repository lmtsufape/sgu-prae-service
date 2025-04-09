package br.edu.ufape.sguPraeService.fachada;


import br.edu.ufape.sguPraeService.auth.RabbitAuthServiceClient;
import br.edu.ufape.sguPraeService.comunicacao.dto.estudante.EstudanteResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.profissional.ProfissionalResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.AlunoResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.FuncionarioResponse;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.EnderecoNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.EstudanteNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.CronogramaNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.ProfissionalNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.TipoEtniaNotFoundException;
import br.edu.ufape.sguPraeService.models.*;
import br.edu.ufape.sguPraeService.servicos.interfaces.*;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.TipoAtendimentoNotFoundException;
import br.edu.ufape.sguPraeService.models.Cronograma;
import br.edu.ufape.sguPraeService.models.Profissional;
import br.edu.ufape.sguPraeService.models.TipoAtendimento;
import br.edu.ufape.sguPraeService.servicos.interfaces.CronogramaService;
import br.edu.ufape.sguPraeService.servicos.interfaces.ProfissionalService;
import br.edu.ufape.sguPraeService.servicos.interfaces.TipoAtendimentoService;
import br.edu.ufape.sguPraeService.servicos.interfaces.VagaService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


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

    @Value("${authClient.client-id}")
    private String clientId;

    Logger logger = LoggerFactory.getLogger(Fachada.class);


    // ------------------- Profissional ------------------- //
    public List<ProfissionalResponse> listarProfissionais() {
        List<ProfissionalResponse> profissionalResponse = new ArrayList<>();
        List<Profissional> profissionais = profissionalService.listar();
        if (profissionais.isEmpty()) { return profissionalResponse; }
        List<String> userIds = profissionais.stream().map(Profissional::getUserId).toList();
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

    public ProfissionalResponse salvarProfissional(Profissional profissional, String userId) {
        profissional.setUserId(userId);
        Profissional novoProfissional = profissionalService.salvar(profissional);
        FuncionarioResponse response = authServiceHandler.getTecnicoInfo();
        rabbitAuthServiceClient.assignRoleToUser(userId, clientId, "profissional");
        ProfissionalResponse profissionalResponse = new ProfissionalResponse(novoProfissional, modelMapper);
        profissionalResponse.setTecnico(response);
        return profissionalResponse;
    }

    public ProfissionalResponse editarProfissional(String userId, Profissional profissional) throws ProfissionalNotFoundException {
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
    public EstudanteResponse salvarEstudante(Estudante estudante, Jwt token, Long tipoEtniaId) throws TipoEtniaNotFoundException {
        estudante.setTipoEtnia(tipoEtniaService.buscarTipoEtnia(tipoEtniaId));
        estudante.setUserId(token.getSubject());

        Estudante novoEstudante = estudanteService.salvarEstudante(estudante);
        AlunoResponse userInfo = authServiceHandler.getAlunoInfo();
        rabbitAuthServiceClient.assignRoleToUser(token.getSubject(), clientId, "estudante");
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
        List<String> userIds = estudantes.stream()
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
    public EstudanteResponse atualizarEstudante(Estudante estudante, Jwt token, Long tipoEtniaId) throws EstudanteNotFoundException, TipoEtniaNotFoundException {
        estudante.setTipoEtnia(tipoEtniaService.buscarTipoEtnia(tipoEtniaId));
        Estudante velhoEstudante = estudanteService.buscarPorUserId(token.getSubject());
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
    public List<Cronograma> listarCronogramasPorProfissional(String userId){
        return cronogramaService.listarPorProfissional(userId);
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

    public Cronograma salvarCronograma(Cronograma cronograma, Long tipoAtendimentoId, String userId) throws TipoAtendimentoNotFoundException {
        TipoAtendimento tipoAtendimento = buscarTipoAtendimento(tipoAtendimentoId);
        Profissional profissional = profissionalService.buscarPorUserId(userId);
        cronograma.setProfissional(profissional);
        cronograma.setTipoAtendimento(tipoAtendimento);
        cronograma.setVagas(vagaService.gerarVagas(tipoAtendimento.getHorarios(), tipoAtendimento.getTempoAtendimento()));
        return cronogramaService.salvar(cronograma);
    }

}
