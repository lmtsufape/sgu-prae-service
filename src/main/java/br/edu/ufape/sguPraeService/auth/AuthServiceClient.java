package br.edu.ufape.sguPraeService.auth;

import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.AlunoResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.FuncionarioResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(name = "authServiceClient", url = "${authClient.base-url}")
public interface AuthServiceClient {

    @PostMapping("/aluno/batch")
    List<AlunoResponse> buscarAlunos(@RequestBody List<String> userIds);

    @GetMapping("/aluno/current")
    AlunoResponse getAlunoInfo();

    @GetMapping("/aluno/buscar/{userId}")
    AlunoResponse buscarAlunoPorId( @PathVariable("userId") String userId);

    @PostMapping("/funcionario/batch")
    List<FuncionarioResponse> buscarFuncionarios(@RequestBody List<String> userIds);

    @GetMapping("/funcionario/current")
    FuncionarioResponse getFuncionarioInfo();

    @GetMapping("/funcionario/{userId}")
    FuncionarioResponse buscarFuncionarioPorId(@PathVariable("userId") String userId);

}