package br.edu.ufape.sguPraeService.servicos;

import br.edu.ufape.sguPraeService.dados.DadosBancariosRepository;
import br.edu.ufape.sguPraeService.models.DadosBancarios;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DadosBancariosService implements br.edu.ufape.sguPraeService.servicos.interfaces.DadosBancariosService{
    private final DadosBancariosRepository dadosBancariosRepository;
    private final ModelMapper modelMapper;

    public DadosBancarios buscarDadosBancarios(Long id) {
        return dadosBancariosRepository.findById(id).orElse(null);
    }

    public List<DadosBancarios> listarDadosBancarios() {
        return dadosBancariosRepository.findAll();
    }

    public DadosBancarios salvarDadosBancarios(DadosBancarios dadosBancarios) {
        return dadosBancariosRepository.save(dadosBancarios);
    }

    public DadosBancarios atualizarDadosBancarios(Long id, DadosBancarios novosDadosBancarios) {
        DadosBancarios dadosBancariosAtuais = dadosBancariosRepository.findById(id)
                .orElse(null);

        if (dadosBancariosAtuais != null) {
            modelMapper.map(novosDadosBancarios, dadosBancariosAtuais);
            return dadosBancariosRepository.save(dadosBancariosAtuais);
        }

        return null;
    }

    public void deletarDadosBancarios(Long id) {
        dadosBancariosRepository.deleteById(id);
    }
}
