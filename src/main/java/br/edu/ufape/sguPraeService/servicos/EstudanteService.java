package br.edu.ufape.sguPraeService.servicos;

import br.edu.ufape.sguPraeService.dados.EstudanteRepository;
import br.edu.ufape.sguPraeService.exceptions.ExceptionUtil;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.EstudanteNotFoundException;
import br.edu.ufape.sguPraeService.models.Estudante;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EstudanteService implements br.edu.ufape.sguPraeService.servicos.interfaces.EstudanteService {
    private final EstudanteRepository estudanteRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public Estudante salvarEstudante(Estudante estudante) {
        try {
            return estudanteRepository.save(estudante);
        }catch (DataIntegrityViolationException e){
            throw ExceptionUtil.handleDataIntegrityViolationException(e);
        }

    }

    @Override
    public Estudante buscarEstudante(Long id) throws EstudanteNotFoundException {
        return estudanteRepository.findById(id)
                .orElseThrow(EstudanteNotFoundException::new);
    }

    @Override
    public Estudante buscarPorUserId(UUID userId) throws EstudanteNotFoundException {
        return (Estudante) estudanteRepository.findByUserId(userId)
                .orElseThrow(EstudanteNotFoundException::new);
    }

    @Override
    public Page<Estudante> buscarPorUserIds(List<UUID> userIds, Pageable pageable) {
        return estudanteRepository.findByUserIdIn(userIds, pageable);
    }

    @Override
    public Page<Estudante> listarEstudantes(Pageable pageable) {
        return estudanteRepository.findAllByAtivoTrue(pageable);
    }

    @Override
    public List<Estudante> listarEstudantes() {
        return estudanteRepository.findAllByAtivoTrue();
    }

    @Override
    public Estudante atualizarEstudante(Estudante estudante, Estudante existente) throws EstudanteNotFoundException {
        try {
            modelMapper.map(estudante, existente);
            return estudanteRepository.save(existente);
        }catch (DataIntegrityViolationException e){
            throw ExceptionUtil.handleDataIntegrityViolationException(e);
        }

    }

    @Override
    public void deletarEstudante(Long id) throws EstudanteNotFoundException {
        Estudante estudante = estudanteRepository.findById(id).orElseThrow(EstudanteNotFoundException::new);
        estudante.setAtivo(false);
        estudanteRepository.save(estudante);
    }

    @Override
    public Page<Estudante> listarEstudantesComAuxilioAtivo(Pageable pageable) {
        return estudanteRepository.findAllWithAuxilioAtivo(pageable);
    }
    
    @Override
    public List<Estudante> listarEstudantesComAuxilioAtivo() {
        return estudanteRepository.findAllWithAuxilioAtivo();
    }

    @Override
    public Page<Estudante> listarEstudantesPorAuxilioId(Long auxilioId, Pageable pageable) {
        return estudanteRepository.findByAuxilioId(auxilioId, pageable);
    }

    @Override
    public List<Estudante> listarEstudantesPorAuxilioId(Long auxilioId) {
        return estudanteRepository.findByAuxilioId(auxilioId);
    }
}
