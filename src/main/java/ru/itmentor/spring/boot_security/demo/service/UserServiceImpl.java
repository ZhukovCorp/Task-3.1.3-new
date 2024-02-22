package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmentor.spring.boot_security.demo.Repository.UserRepository;
import ru.itmentor.spring.boot_security.demo.model.AddRequest;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import java.util.HashSet;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService  {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public User addUser(AddRequest addRequest) {
        addRequest.setPassword(passwordEncoder.encode(addRequest.getPassword()));
        User user = new User();
        user.setPassword(addRequest.getPassword());
        user.setName(addRequest.getName());
        user.setAge(addRequest.getAge());
        user.setLastName(addRequest.getLastName());
        User savedUser = userRepository.saveAndFlush(user);
        return savedUser;
    }

    @Transactional
    @Override
    public void delete(User user) {
        userRepository.deleteById(user.getId());
    }

    public void deleteUserById(int id) {
        Optional<User> userDelete = findById(id);

        if (userDelete.isPresent()) {
            User user = userDelete.get();
            delete(user);
        } else {
            throw new IllegalArgumentException("Неверный ID: " + id);
        }
    }

    public User findUserById(int id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID: " + id));
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public Set<Role> getRole(Set<String> rolesId) {
        Set<Role> roles = new HashSet<>();
        for (String id : rolesId) {
            roles.add(getRoleById(Integer.parseInt(id)));
        }
        return roles;
    }

    public Role getRoleById(int id) {
        return userRepository.getRoleById(id);
    }

}
