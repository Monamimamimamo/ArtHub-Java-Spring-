package Program;

import Program.Programs;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProgramsRepo extends CrudRepository<Programs, Integer> {
    @Query("SELECT p FROM Programs p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Programs> findByNameIgnoreCase(String name);


    @Query("SELECT p FROM Programs p WHERE LOWER(p.systems) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Programs> findBySystemsIgnoreCase(String name);

}
