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

    @Override
    public Estudante salvarEstudante(Estudante estudante) {
        if (estudante.getContatoFamilia() == null) {
            throw new IllegalArgumentException("Contato da família deve conter no mínimo 10 caracteres.");
        }

        if (estudante.getTipoEtnia() == null) {
            throw new IllegalArgumentException("Tipo de etnia é obrigatório.");
        }

        if (estudante.isDeficiente()) {
            if (estudante.getTipoDeficiencia() == null || estudante.getTipoDeficiencia().isBlank()) {
                throw new IllegalArgumentException("Tipo de deficiência deve ser informado para estudantes com deficiência.");
            }
        } else {
            estudante.setTipoDeficiencia(null);
        }

        return estudanteRepository.save(estudante);
    }

    @Override
    public Estudante buscarEstudante(Long id) throws EstudanteNotFoundException {
        return estudanteRepository.findById(id)
                .orElseThrow(EstudanteNotFoundException::new);
    }

    @Override
    public List<Estudante> listarEstudantes() {
        return estudanteRepository.findAll();
    }

    @Override
    public Estudante atualizarEstudante(Long id, Estudante estudante) throws EstudanteNotFoundException {
        Estudante existente = estudanteRepository.findById(id)
                .orElseThrow(EstudanteNotFoundException::new);

        if (estudante.getContatoFamilia() == null) {
            throw new IllegalArgumentException("Contato da família deve conter no mínimo 10 caracteres.");
        }

        if (estudante.getTipoEtnia() == null) {
            throw new IllegalArgumentException("Tipo de etnia é obrigatório.");
        }

        if (estudante.isDeficiente()) {
            if (estudante.getTipoDeficiencia() == null || estudante.getTipoDeficiencia().isBlank()) {
                throw new IllegalArgumentException("Tipo de deficiência deve ser informado para estudantes com deficiência.");
            }
        } else {
            estudante.setTipoDeficiencia(null);
        }

        modelMapper.map(estudante, existente);

        return estudanteRepository.save(existente);
    }

    @Override
    public void deletarEstudante(Long id) throws EstudanteNotFoundException {
        if (!estudanteRepository.existsById(id)) {
            throw new EstudanteNotFoundException();
        }
        estudanteRepository.deleteById(id);
    }
}
