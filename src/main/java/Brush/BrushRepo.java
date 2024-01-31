package Brush;

import Program.Programs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BrushRepo extends JpaRepository<Brushes, Integer> {

    @Query("SELECT p FROM Brushes p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Brushes> findByTitleIgnoreCase(String name);

    @Query("SELECT p FROM Brushes p WHERE LOWER(p.program) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Brushes> findByProgramIgnoreCase(String name);
}
