package br.edu.ufape.sguPraeService.auth;

import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.PageResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.AlunoResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.FuncionarioResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@FeignClient(name = "authServiceClient", url = "${authClient.base-url}")
public interface AuthServiceClient {

    @PostMapping("/aluno/batch")
    List<AlunoResponse> buscarAlunos(@RequestBody List<UUID> userIds);

    @GetMapping("/curso/{id}/alunos")
    List<AlunoResponse> listarAlunosPorCurso(@PathVariable("id") Long id);

    @GetMapping("/aluno/current")
    AlunoResponse getAlunoInfo();

    @GetMapping("/aluno/{userId}")
    AlunoResponse buscarAlunoPorId( @PathVariable("userId") UUID userId);

    @PostMapping("/funcionario/batch")
    List<FuncionarioResponse> buscarFuncionarios(@RequestBody List<UUID> userIds);

    @GetMapping("/funcionario/current")
    FuncionarioResponse getFuncionarioInfo();

    @GetMapping("/funcionario/{userId}")
    FuncionarioResponse buscarFuncionarioPorId(@PathVariable("userId") UUID userId);

    @GetMapping("/aluno")
    PageResponse<AlunoResponse> buscarAlunoPorCpf(@RequestParam("cpf") String cpf);
}