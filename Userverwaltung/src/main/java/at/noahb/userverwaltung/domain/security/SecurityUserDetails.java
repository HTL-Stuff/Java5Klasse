package at.noahb.userverwaltung.domain.security;

import at.noahb.userverwaltung.domain.persistent.Role;
import at.noahb.userverwaltung.domain.persistent.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class SecurityUserDetails implements UserDetails {

    private String email;

    private String password;

    private List<Role> authorities;

    public static SecurityUserDetails of(User user) {
        return new SecurityUserDetails(user.getEmail(), user.getPassword(), List.of(user.getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream().map(Role::getRoleAuthority).toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
