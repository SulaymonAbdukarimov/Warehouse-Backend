package org.example.appwarehouse.service;

import org.example.appwarehouse.entity.Product;
import org.example.appwarehouse.entity.User;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.payload.UserDto;
import org.example.appwarehouse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Result addUser(UserDto user) {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(user.getPhoneNumber());
        if (optionalUser.isPresent()) {
            return new Result("Bu nomerdagi user bor",false);
        };
        User user1 = new User();
        user1.setPhoneNumber(user.getPhoneNumber());
        user1.setFirstName(user.getFirstName());
        user1.setLastName(user.getLastName());
        user1.setPassword(user.getPassword());
        user1.setCode(generateCode());
        userRepository.save(user1);
        return new Result("User qo'shildi",true);
    };

    public Result updateUser(User user) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (!existingUser.isPresent()) {
            return new Result("Bunday user yo'q", false);
        }

        Optional<User> userWithSamePhone = userRepository.findByPhoneNumber(user.getPhoneNumber());

        if (userWithSamePhone.isPresent() && !userWithSamePhone.get().getId().equals(user.getId())) {
            return new Result("Bu nomerdagi user bor", false);
        }

        userRepository.save(user);
        return new Result("User o'zgartirildi", true);
    }

    public Result deleteUser(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return new Result("Bunday user yo'q", false);
        }
        userRepository.deleteById(id);
        return new Result("User o'chirildi",true);
    };

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return null;
        }
        return optionalUser.get();
    }

    private String generateCode() {
        Optional<User> optionalUser = userRepository.findTopByCodeAsNumberDesc();
        if (optionalUser.isPresent()) {
            String maxCodeStr = optionalUser.get().getCode();
            try {
                int maxCode = Integer.parseInt(maxCodeStr);
                return String.valueOf(maxCode + 1);
            } catch (NumberFormatException e) {
                return "1";
            }
        }
        return "1";
    };

}
