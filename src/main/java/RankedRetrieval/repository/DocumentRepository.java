package RankedRetrieval.repository;

import RankedRetrieval.model.Resources;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends CrudRepository<Resources, Integer> {
//    Object findAllById(int id);
}
