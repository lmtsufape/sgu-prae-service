 package br.edu.ufape.sguPraeService.servicos;
 
 import br.edu.ufape.sguPraeService.auth.AuthenticatedUserProvider;
 import br.edu.ufape.sguPraeService.models.Cronograma;
 import br.edu.ufape.sguPraeService.dados.CronogramaRepository;
 import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.CronogramaNotFoundException;
 import jakarta.ws.rs.NotAllowedException;
 import org.modelmapper.ModelMapper;
 import lombok.RequiredArgsConstructor;
 import org.springframework.stereotype.Service;

 import java.util.List;
 import java.util.UUID;

 @Service
 @RequiredArgsConstructor
 public class CronogramaService implements br.edu.ufape.sguPraeService.servicos.interfaces.CronogramaService {
     private final CronogramaRepository repository;
     private final ModelMapper modelMapper;
     private final AuthenticatedUserProvider authenticatedUserProvider;
 
     @Override
     public List<Cronograma> listar() {
         return repository.findAllByAtivoTrue();
     }

     @Override
     public List<Cronograma> listarPorTipoAtendimento(Long id) {
         return repository.findByAtivoTrueAndTipoAtendimento_Id(id);
     }

     @Override
     public List<Cronograma> listarPorProfissional(UUID userId) {
         return repository.findAllByAtivoTrueAndProfissional_UserId(userId);
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
         UUID userId = authenticatedUserProvider.getUserId();
         Cronograma cronograma = buscar(id);
         if (!cronograma.getProfissional().getUserId().equals(userId)) {
             throw new NotAllowedException("Você não tem permissão para editar este cronograma.");
         }
         modelMapper.map(entity, cronograma);
         return repository.save(cronograma);
     }
 
     @Override
     public void deletar(Long id){
         UUID userId = authenticatedUserProvider.getUserId();
         Cronograma cronograma = buscar(id);
         if (!cronograma.getProfissional().getUserId().equals(userId)) {
             throw new NotAllowedException("Você não tem permissão para deletar este cronograma.");
         }
         cronograma.setAtivo(false);
         repository.save(cronograma);
     }
 }
 