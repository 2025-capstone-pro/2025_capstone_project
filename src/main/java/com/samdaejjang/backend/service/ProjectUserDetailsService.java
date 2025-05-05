package com.samdaejjang.backend.service;

import com.samdaejjang.backend.entity.Users;
import com.samdaejjang.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Users> findUser = userRepository.findByUsername(username);
        if (findUser.isPresent()) {
            return new User(findUser.get().getUsername(), findUser.get().getPassword(),new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("사용자를 DB 에서 찾을 수 없음: " + username);
        }
    }
}
