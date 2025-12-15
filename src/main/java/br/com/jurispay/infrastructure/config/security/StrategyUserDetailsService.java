package br.com.jurispay.infrastructure.config.security;

import br.com.jurispay.infrastructure.config.security.model.DevUserDefinition;
import br.com.jurispay.infrastructure.config.security.strategy.UserSource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementação de UserDetailsService que usa a Strategy UserSource.
 * Permite trocar a fonte de usuários (in-memory, database, etc.) sem alterar este código.
 */
@Service
public class StrategyUserDetailsService implements UserDetailsService {

    private final UserSource userSource;
    private final PasswordEncoder passwordEncoder;

    public StrategyUserDetailsService(UserSource userSource, PasswordEncoder passwordEncoder) {
        this.userSource = userSource;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userSource.findByUsername(username)
                .map(this::buildUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    /**
     * Constrói UserDetails a partir de DevUserDefinition.
     *
     * @param definition definição do usuário
     * @return UserDetails para Spring Security
     */
    private UserDetails buildUserDetails(DevUserDefinition definition) {
        return User.withUsername(definition.getUsername())
                .password(passwordEncoder.encode(definition.getRawPassword()))
                .roles(definition.getRoles().toArray(new String[0]))
                .build();
    }
}

