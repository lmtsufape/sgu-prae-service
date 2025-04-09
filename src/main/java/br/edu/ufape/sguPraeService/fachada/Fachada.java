package br.edu.ufape.sguPraeService.fachada;


import br.edu.ufape.sguPraeService.auth.RabbitAuthServiceClient;
import br.edu.ufape.sguPraeService.comunicacao.dto.estudante.EstudanteResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.AlunoResponse;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.EnderecoNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.EstudanteNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.CronogramaNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.ProfissionalNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.TipoEtniaNotFoundException;
import br.edu.ufape.sguPraeService.models.*;
import br.edu.ufape.sguPraeService.auth.AuthServiceClient;
import br.edu.ufape.sguPraeService.servicos.interfaces.*;
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
    private final EnderecoService enderecoService;
    private final DadosBancariosService dadosBancariosService;
    private final TipoEtniaService tipoEtniaService;
    private final EstudanteService estudanteService;
    private final AuthServiceClient authServiceClient;
    private final ModelMapper modelMapper;
    private final RabbitAuthServiceClient rabbitAuthServiceClient;

    @Value("${authClient.client-id}")
    private String clientId;

    Logger logger = LoggerFactory.getLogger(Fachada.class);


    // ------------------- Profissional ------------------- //
    public List<Profissional> listarProfissionais() {
        return profissionalService.listar();
    }

    public Profissional buscarProfissional(Long id) throws ProfissionalNotFoundException {
        return profissionalService.buscar(id);
    }

    public Profissional salvarProfissional(Profissional profissional) {
        return profissionalService.salvar(profissional);
    }

    public Profissional editarProfissional(Long id, Profissional profissional) throws ProfissionalNotFoundException {
        return profissionalService.editar(id, profissional);
    }

    public void deletarProfissional(Long id) throws ProfissionalNotFoundException {
        profissionalService.deletar(id);
    }

    // ================== Estudante  ================== //

    @CircuitBreaker(name = "authServiceClient", fallbackMethod = "fallbackGetAlunoInfo")
    public EstudanteResponse salvarEstudante(Estudante estudante, Jwt token, Long tipoEtniaId) throws TipoEtniaNotFoundException {
        estudante.setTipoEtnia(tipoEtniaService.buscarTipoEtnia(tipoEtniaId));
        estudante.setUserId(token.getSubject());

        Estudante novoEstudante = estudanteService.salvarEstudante(estudante);
        AlunoResponse userInfo = authServiceClient.getAlunoInfo();
        rabbitAuthServiceClient.assignRoleToUser(token.getSubject(), clientId, "estudante");
        EstudanteResponse response = new EstudanteResponse(novoEstudante, modelMapper);
        response.setAluno(userInfo);
        return response;
    }

    public EstudanteResponse buscarEstudante(Long id) throws EstudanteNotFoundException {
        Estudante estudante = estudanteService.buscarEstudante(id);
        AlunoResponse userInfo = authServiceClient.buscarAlunoPorId(estudante.getUserId());
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
        List<AlunoResponse> usuarios = authServiceClient.buscarAlunos(userIds);
        for (int i = 0; i < estudantes.size(); i++) {
            Estudante estudante = estudantes.get(i);
            AlunoResponse usuario = usuarios.get(i);
            EstudanteResponse response = new EstudanteResponse(estudante, modelMapper);
            response.setAluno(usuario);
            estudanteResponse.add(response);
        }
        return estudanteResponse;
    }

    public EstudanteResponse atualizarEstudante(Estudante estudante, Jwt token, Long tipoEtniaId) throws EstudanteNotFoundException, TipoEtniaNotFoundException {
        estudante.setTipoEtnia(tipoEtniaService.buscarTipoEtnia(tipoEtniaId));
        Estudante novoEstudante = estudanteService.atualizarEstudante(estudante, token.getSubject());
        AlunoResponse userInfo = authServiceClient.getAlunoInfo();
        EstudanteResponse response = new EstudanteResponse(novoEstudante, modelMapper);
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




    // ---------------------------- fallback ---------------------------- //
    public EstudanteResponse fallbackGetAlunoInfo(Estudante estudante, Jwt token, Long tipoEtniaId, Throwable t) {
        logger.warn("Falha ao buscar informações do aluno no Auth Service. Usando fallback.", t);

        estudante.setTipoEtnia(tipoEtniaService.buscarTipoEtnia(tipoEtniaId));
        estudante.setUserId(token.getSubject());

        Estudante novoEstudante = estudanteService.salvarEstudante(estudante);
        rabbitAuthServiceClient.assignRoleToUser(token.getSubject(), clientId, "estudante");

        EstudanteResponse response = new EstudanteResponse(novoEstudante, modelMapper);

        response.setAluno(null);

        return response;
    }


}
