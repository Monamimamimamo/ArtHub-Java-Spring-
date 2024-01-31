package User;

import org.springframework.data.repository.CrudRepository;

public interface UsersRepo extends CrudRepository<Users, Long> {
    Users findByEmail(String email);
    Users findById(Integer userId);
}
