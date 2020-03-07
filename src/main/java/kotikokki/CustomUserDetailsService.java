/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki;

import java.util.Arrays;
import kotikokki.domain.Tili;
import kotikokki.repository.TiliRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pekka
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private TiliRepository tiliRepo;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Tili account = tiliRepo.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("No such user: " + username);
        }
        /*
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        account.getAuthorities().forEach((role) -> {
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        });
        */
        return new org.springframework.security.core.userdetails.User(
        account.getUsername(),
        account.getPassword(),
        true,
        true,
        true,
        true,
        //grantedAuthorities);
        Arrays.asList(new SimpleGrantedAuthority("USER")));
    }
}
