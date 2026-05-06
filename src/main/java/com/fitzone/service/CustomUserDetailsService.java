package com.fitzone.service;

import com.fitzone.model.Member;
import com.fitzone.repository.FileMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private FileMemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check for in-memory Admin user
        if ("admin".equalsIgnoreCase(username) || "admin@fitzone.com".equalsIgnoreCase(username)) {
            return User.withUsername(username)
                    .password("{noop}admin") // {noop} means no password encoder is used for this specific user
                    .roles("ADMIN")
                    .build();
        }

        // Look up member by email
        Optional<Member> memberOpt = memberRepository.getAllMembers().stream()
                .filter(m -> m.getEmail().equalsIgnoreCase(username))
                .findFirst();

        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            // Using phone number as default password for existing members
            String defaultPassword = "{noop}" + member.getPhone();
            return User.withUsername(member.getEmail())
                    .password(defaultPassword)
                    .roles("MEMBER")
                    .build();
        }

        throw new UsernameNotFoundException("User not found with email: " + username);
    }
}
