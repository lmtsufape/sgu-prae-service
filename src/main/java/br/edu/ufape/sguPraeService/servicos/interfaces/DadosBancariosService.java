package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.models.DadosBancarios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface DadosBancariosService {

    DadosBancarios buscarDadosBancarios(Long id);

    Page<DadosBancarios> listarDadosBancarios(Pageable pageable);

    DadosBancarios salvarDadosBancarios(DadosBancarios dadosBancarios);

    DadosBancarios atualizarDadosBancarios(Long id, DadosBancarios novosDadosBancarios);

    void deletarDadosBancarios(Long id);
}
