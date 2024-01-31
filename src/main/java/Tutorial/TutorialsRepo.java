package Tutorial;

import Brush.Brushes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TutorialsRepo extends JpaRepository<Tutorials, Integer> {
    @Query("SELECT p FROM Tutorials p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Tutorials> findByTitleIgnoreCase(String name);

    @Query("SELECT p FROM Tutorials p WHERE LOWER(p.difficulty) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Tutorials> findByDifficultyIgnoreCase(String name);
}
