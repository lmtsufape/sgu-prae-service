package br.edu.ufape.sguPraeService.servicos;

import br.edu.ufape.sguPraeService.dados.EstudanteRepository;
import br.edu.ufape.sguPraeService.exceptions.EstudanteNotFoundException;
import br.edu.ufape.sguPraeService.models.Estudante;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstudanteService implements br.edu.ufape.sguPraeService.servicos.interfaces.EstudanteService {
    private final EstudanteRepository estudanteRepository;
    private final ModelMapper modelMapper;

    public Estudante salvarEstudante(Estudante estudante) {
        if (!estudante.isDeficiente()) {
            estudante.setTipoDeficiencia(null);
        }
        return estudanteRepository.save(estudante);
    }

    public Estudante buscarEstudante(Long id) throws EstudanteNotFoundException{
        return estudanteRepository.findById(id).orElseThrow(EstudanteNotFoundException::new);
    }

    public List<Estudante> listarEstudantes() {
        return estudanteRepository.findAll();
    }

    public Estudante atualizarEstudante(Long id, Estudante estudante) throws EstudanteNotFoundException{
        Estudante estudanteExistente = estudanteRepository.findById(id).orElseThrow(EstudanteNotFoundException::new);
        if (estudanteExistente != null) {
            modelMapper.map(estudante, estudanteExistente);
            if (!estudanteExistente.isDeficiente()) {
                estudanteExistente.setTipoDeficiencia(null);
            }
            return estudanteRepository.save(estudanteExistente);
        }
        return null;
    }

    public void deletarEstudante(Long id) {
        estudanteRepository.deleteById(id);
    }
}