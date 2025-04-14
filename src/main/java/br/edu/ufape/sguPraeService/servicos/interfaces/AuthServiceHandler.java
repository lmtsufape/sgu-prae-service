package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.AlunoResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.FuncionarioResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.util.List;
import java.util.UUID;

public interface AuthServiceHandler {
    @CircuitBreaker(name = "authServiceClient", fallbackMethod = "fallbackAlunoInfo")
    AlunoResponse getAlunoInfo();

    @CircuitBreaker(name = "authServiceClient", fallbackMethod = "fallbackBuscarAlunoPorId")
    AlunoResponse buscarAlunoPorId(UUID userId);

    @CircuitBreaker(name = "authServiceClient", fallbackMethod = "fallbackBuscarAlunos")
    List<AlunoResponse> buscarAlunos(List<UUID> userIds);

    @CircuitBreaker(name = "authServiceClient", fallbackMethod = "fallbackGetTecnicoInfo")
    FuncionarioResponse getTecnicoInfo();

    @CircuitBreaker(name = "authServiceClient", fallbackMethod = "fallbackBuscarTecnicoPorId")
    FuncionarioResponse buscarTecnicoPorId(UUID userId);

    @CircuitBreaker(name = "authServiceClient", fallbackMethod = "fallbackBuscarTecnicos")
    List<FuncionarioResponse> buscarTecnicos(List<UUID> userIds);

    @SuppressWarnings("unused")
    AlunoResponse fallbackAlunoInfo(Throwable t);
    @SuppressWarnings("unused")
    AlunoResponse fallbackBuscarAlunoPorId(UUID userId, Throwable t);
    @SuppressWarnings("unused")
    List<AlunoResponse> fallbackBuscarAlunos(List<UUID> userIds, Throwable t);
    @SuppressWarnings("unused")
    FuncionarioResponse fallbackGetTecnicoInfo(Throwable t);
    @SuppressWarnings("unused")
    FuncionarioResponse fallbackBuscarTecnicoPorId(UUID userId, Throwable t);
    @SuppressWarnings("unused")
    List<FuncionarioResponse> fallbackBuscarTecnicos(UUID userId, Throwable t);
}
