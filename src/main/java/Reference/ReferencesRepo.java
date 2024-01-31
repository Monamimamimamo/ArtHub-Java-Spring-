package Reference;

import Tutorial.Tutorials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReferencesRepo  extends JpaRepository<References, Integer> {
    @Query("SELECT p FROM References p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<References> findByTitleIgnoreCase(String name);

    @Query("SELECT p FROM References p WHERE LOWER(p.hashtag) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<References> findByTagIgnoreCase(String name);
}
