package RankedRetrieval.repository;

import RankedRetrieval.model.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Images, Integer> {
    List<Images> findByLink(String link);
}
