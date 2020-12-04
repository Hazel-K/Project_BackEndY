package blog.hazelk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.hazelk.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByUsername(String username);
}
