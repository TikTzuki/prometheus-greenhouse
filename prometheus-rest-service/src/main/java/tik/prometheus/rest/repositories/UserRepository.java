package tik.prometheus.rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import tik.prometheus.rest.models.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	User findByUserName(String username);
	User findByEmail(String email);
}
