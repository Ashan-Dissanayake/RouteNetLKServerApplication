package lk.ashan.routenetlkserverapllication.security;

import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class CustomUserPrincipal implements UserDetails {

    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean accountLocked;

    public CustomUserPrincipal(User user,
                               Collection<? extends GrantedAuthority> authorities, boolean accountLocked) {
        this.user = user;
        this.authorities = authorities;
        this.accountLocked = accountLocked;
    }

    public User getUserEntity() {
        return this.user;
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return user.getPassword(); }
    @Override public String getUsername() { return user.getUsername(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return !accountLocked; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
