package br.edu.ufape.sguPraeService.servicos;

import br.edu.ufape.sguPraeService.auth.AuthServiceClient;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.AlunoPublicResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.AlunoResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.FuncionarioResponse;
import br.edu.ufape.sguPraeService.exceptions.AuthServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceHandler implements br.edu.ufape.sguPraeService.servicos.interfaces.AuthServiceHandler {
    private final AuthServiceClient authServiceClient;

    @CircuitBreaker(name = "authServiceClient", fallbackMethod = "fallbackAlunoInfo")
    @Override
    public AlunoResponse getAlunoInfo() {
        return authServiceClient.getAlunoInfo();
    }

    @CircuitBreaker(name = "authServiceClient", fallbackMethod = "fallbackBuscarAlunoPorId")
    @Override
    public AlunoResponse buscarAlunoPorId(UUID userId) {
        return authServiceClient.buscarAlunoPorId(userId);
    }
    @CircuitBreaker(name = "authServiceClient", fallbackMethod = "fallbackBuscarAlunos")
    @Override
    public List<AlunoResponse> buscarAlunos(List<UUID> userIds) {
        return authServiceClient.buscarAlunos(userIds);
    }

    @CircuitBreaker(name = "authServiceClient", fallbackMethod = "fallbackBuscarAlunosPorCurso")
    @Override
    public List<AlunoResponse> buscarAlunosPorCurso(Long idCurso) {
        return authServiceClient.listarAlunosPorCurso(idCurso);
    }


    @CircuitBreaker(name = "authServiceClient", fallbackMethod = "fallbackGetTecnicoInfo")
    @Override
    public FuncionarioResponse getTecnicoInfo() {
        return authServiceClient.getFuncionarioInfo();
    }

    @CircuitBreaker(name = "authServiceClient", fallbackMethod = "fallbackBuscarTecnicoPorId")
    @Override
    public FuncionarioResponse buscarTecnicoPorId(UUID userId) {
        return authServiceClient.buscarFuncionarioPorId(userId);
    }


    @CircuitBreaker(name = "authServiceClient", fallbackMethod = "fallbackBuscarTecnicos")
    @Override
    public List<FuncionarioResponse> buscarTecnicos(List<UUID> userIds) {
        return authServiceClient.buscarFuncionarios(userIds);
    }

    @Override
    @CircuitBreaker(name = "authServiceClient", fallbackMethod = "fallbackBuscarAlunosPublicos")
    public List<AlunoPublicResponse> buscarAlunosPublicos(List<UUID> userIds) {
        return authServiceClient.buscarAlunosPublicos(userIds);
    }

    @Override
    public AlunoResponse fallbackAlunoInfo(Throwable t) {
        log.error("Não foi possível obter info do aluno atual no AuthService", t);
        throw new AuthServiceUnavailableException("Serviço de Autenticação indisponível. Tente novamente mais tarde.", t);
    }

    @Override
    public AlunoResponse fallbackBuscarAlunoPorId(UUID userId, Throwable t) {
        log.error("Não foi possível buscar aluno com id {} no AuthService", userId, t);
        throw new AuthServiceUnavailableException("Não foi possível carregar os dados do aluno.", t);
    }

    @Override
    public List<AlunoResponse> fallbackBuscarAlunos(List<UUID> userIds, Throwable t) {
        log.error("Não foi possível buscar alunos em lote no AuthService", t);
        throw new AuthServiceUnavailableException("Falha na comunicação com o servidor central para obter a lista de alunos.", t);
    }

    @Override
    public List<AlunoResponse> fallbackBuscarAlunosPorCurso(Long idCurso, Throwable t) {
        log.error("Não foi possível buscar alunos do curso {} no AuthService", idCurso, t);
        throw new AuthServiceUnavailableException("Não foi possível carregar os alunos deste curso.", t);
    }

    @Override
    public FuncionarioResponse fallbackGetTecnicoInfo(Throwable t) {
        log.error("Não foi possível obter info do técnico atual", t);
        throw new AuthServiceUnavailableException("Serviço de Autenticação indisponível.", t);
    }

    @Override
    public FuncionarioResponse fallbackBuscarTecnicoPorId(UUID userId, Throwable t) {
        log.error("Não foi possível buscar técnico com id {}", userId, t);
        throw new AuthServiceUnavailableException("Não foi possível carregar os dados do profissional.", t);
    }

    @Override
    public List<FuncionarioResponse> fallbackBuscarTecnicos(UUID userId, Throwable t) {
        log.error("Não foi possível buscar técnicos em lote", t);
        throw new AuthServiceUnavailableException("Falha na comunicação com o servidor central.", t);
    }

    @Override
    public List<AlunoPublicResponse> fallbackBuscarAlunosPublicos(List<UUID> userIds, Throwable t) {
        log.error("Falha ao buscar alunos públicos em batch", t);
        throw new AuthServiceUnavailableException("Não foi possível carregar os dados públicos dos alunos.", t);
    }
}
