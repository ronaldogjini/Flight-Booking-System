package com.ronaldo.tripsuite.service.Impl;

import com.ronaldo.tripsuite.dto.JwtRequestDto;
import com.ronaldo.tripsuite.dto.JwtResponseDto;
import com.ronaldo.tripsuite.util.JwtUtil;
import com.ronaldo.tripsuite.repository.UserRepository;
import com.ronaldo.tripsuite.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtResponseDto createJwtToken(JwtRequestDto jwtRequestDto) throws Exception {
        System.out.print(jwtRequestDto);
        String userName = jwtRequestDto.getUsername();
        String userPassword = jwtRequestDto.getPassword();

        authenticate(userName, userPassword);

        UserDetails userDetails = loadUserByUsername(userName);
        System.out.println("bbbb" + userDetails.toString());
        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByUsername(userName).get();
        return new JwtResponseDto(user, newGeneratedToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).get();

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    getAuthority(user)
            );
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    private Set getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        return authorities;
    }

    private void authenticate(String userName, String userPassword) throws Exception {

        try {
            System.out.println("aaaaaa before " + " " + userName + " " + userPassword);

           Authentication a = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
            System.out.println("aaaaaa after " + " " + userName + " " + userPassword);

        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
