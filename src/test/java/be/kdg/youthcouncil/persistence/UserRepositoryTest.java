package be.kdg.youthcouncil.persistence;

import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.persistence.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	private PlatformUser newUser1;
	private PlatformUser newUser2;

	@BeforeEach
	public void beforeEach() {
		userRepository.findAll().forEach(x -> System.out.println(x.getUsername()));
		newUser1 = new PlatformUser("newUser1", "newUser1", "newUser1@localhost", "6857", "newUser1", "$2a$10$Us28KZ6IEZobDd3ZIkQ9kudH0BssiIpjhGlHWN/YuE761XKw8krIS");
		newUser2 = new PlatformUser("newUser2", "newUser2", "newUser2@localhost", "89876", "member1", "$2a$10$Us28KZ6IEZobDd3ZIkQ9kudH0BssiIpjhGlHWN/YuE761XKw8krIS");
		userRepository.findAll().forEach(x -> System.out.println(x.getUsername()));
	}

	@Test
	@Transactional
	public void removeUserRemovesUser() {
		userRepository.save(newUser1);
		assertTrue(userRepository.exists(Example.of(newUser1)));
		userRepository.delete(newUser1);
		assertFalse(userRepository.exists(Example.of(newUser1)));
	}

	@Test
	@Transactional
	public void saveUserSavesUser() {
		userRepository.save(newUser1);
		long id = newUser1.getId();
		PlatformUser newUser = userRepository.findById(id).get();
		assertEquals(newUser, newUser1);
	}

	@Test
	@Transactional
	public void saveUserWithExistingUsernameThrowsException() {
		assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(newUser2));
	}

	@Test
	@Transactional
	public void findAllPlatformUsersReturnsAtLeastFiveUsers() {
		List<PlatformUser> users = userRepository.findAll();
		assertTrue(users.size() >= 1);
	}

	@Test
	@Transactional
	public void findUserByUsernameFindsUser() {
		userRepository.save(newUser1);
		PlatformUser user = userRepository.findByUsername(newUser1.getUsername()).get();
		assertEquals(newUser1.getUsername(), user.getUsername());
	}
}
