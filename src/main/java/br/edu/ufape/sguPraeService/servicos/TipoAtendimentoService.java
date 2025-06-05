 package br.edu.ufape.sguPraeService.servicos;
 
 import br.edu.ufape.sguPraeService.models.TipoAtendimento;
 import br.edu.ufape.sguPraeService.dados.TipoAtendimentoRepository;
 import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.TipoAtendimentoNotFoundException;
 import org.modelmapper.ModelMapper;
 import org.springframework.data.domain.Page;
 import org.springframework.data.domain.Pageable;
 import org.springframework.stereotype.Service;
 import lombok.RequiredArgsConstructor;

 @Service @RequiredArgsConstructor
 public class TipoAtendimentoService implements br.edu.ufape.sguPraeService.servicos.interfaces.TipoAtendimentoService {
     private final TipoAtendimentoRepository repository;
     private final ModelMapper modelMapper;
 
     @Override
     public Page<TipoAtendimento> listar(Pageable pageable) {
         return repository.findAll(pageable);
     }


     @Override
     public TipoAtendimento buscar(Long id) throws TipoAtendimentoNotFoundException {
         return repository.findById(id).orElseThrow(TipoAtendimentoNotFoundException::new);
     }
 
     @Override
     public TipoAtendimento salvar(TipoAtendimento entity) {
         return repository.save(entity);
     }
 
     @Override
     public TipoAtendimento editar(Long id, TipoAtendimento entity) throws TipoAtendimentoNotFoundException {
         TipoAtendimento tipoatendimento = buscar(id);
         modelMapper.map(entity, tipoatendimento);
         return repository.save(tipoatendimento);
     }

     @Override
     public TipoAtendimento deletarHorario(Long id, int index) throws TipoAtendimentoNotFoundException, IndexOutOfBoundsException {
         TipoAtendimento tipoatendimento = buscar(id);
         tipoatendimento.getHorarios().remove(index);
         return repository.save(tipoatendimento);
     }
 
     @Override
     public void deletar(Long id){
         repository.deleteById(id);
     }
 }
 