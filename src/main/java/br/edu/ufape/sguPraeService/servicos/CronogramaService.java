 package br.edu.ufape.sguPraeService.servicos;
 
 import br.edu.ufape.sguPraeService.models.Cronograma;
 import br.edu.ufape.sguPraeService.dados.CronogramaRepository;
 import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.CronogramaNotFoundException;
 import org.modelmapper.ModelMapper;
 import lombok.RequiredArgsConstructor;
 import org.springframework.stereotype.Service;

 import java.util.List;
 
 @Service
 @RequiredArgsConstructor
 public class CronogramaService implements br.edu.ufape.sguPraeService.servicos.interfaces.CronogramaService {
     private final CronogramaRepository repository;
     private final ModelMapper modelMapper;
 
     @Override
     public List<Cronograma> listar() {
         return repository.findAll();
     }

     @Override
     public List<Cronograma> listarPorTipoAtendimento(Long id) {
         return repository.findByTipoAtendimentoId(id);
     }

     @Override
     public List<Cronograma> listarPorProfissional(String userId) {
         return repository.findByProfissional_UserId(userId);
     }
 
     @Override
     public Cronograma buscar(Long id) throws CronogramaNotFoundException {
         return repository.findById(id).orElseThrow(CronogramaNotFoundException::new);
     }
 
     @Override
     public Cronograma salvar(Cronograma entity) {
         return repository.save(entity);
     }
 
     @Override
     public Cronograma editar(Long id, Cronograma entity) throws CronogramaNotFoundException {
         Cronograma cronograma = buscar(id);
         modelMapper.map(entity, cronograma);
         return repository.save(cronograma);
     }
 
     @Override
     public void deletar(Long id){
         repository.deleteById(id);
     }
 }
 