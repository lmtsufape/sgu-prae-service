package br.edu.ufape.sguPraeService.auth;

import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.AlunoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(name = "auth-service", url = "${authClient.base-url}")
public interface AuthServiceClient {

    @PostMapping("/aluno/batch")
    List<AlunoResponse> buscarAlunos(@RequestBody List<String> userIds);

    @GetMapping("/aluno/current")
    AlunoResponse getAlunoInfo();

    @GetMapping("/aluno/buscar/{userId}")
    AlunoResponse buscarAlunoPorId( @PathVariable("userId") String userId);

}