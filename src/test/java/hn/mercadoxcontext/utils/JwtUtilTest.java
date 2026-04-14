package hn.mercadoxcontext.utils;


import hn.shadowcore.mercadox.context.utils.JwtUtil;
import hn.shadowcore.mercadox.library.entity.model.auth.Organization;
import hn.shadowcore.mercadox.library.entity.model.auth.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void init() {
        jwtUtil = new JwtUtil("ahsdfjkasdhflkajsdlkvjakdsfhgkhasfd=1234ui", 15_000);
    }

    @Test
    void shouldGenerateAndParseJwtSuccessfully () {
        Organization org = Organization.builder().id(UUID.randomUUID()).build();
        User user = User.builder()
                .firstName("Example")
                .email("i@b.com")
                .organization(org)
                .userRoles(new HashSet<>())
                .build();

        final String token = jwtUtil.generateToken(user);

        assertThat(jwtUtil.validateToken(token)).isTrue();
        assertThat(jwtUtil.getEmailFromToken(token)).isEqualTo(user.getEmail());
        assertThat(jwtUtil.getOrgIdFromToken(token)).isEqualTo(String.valueOf(org.getId()));
    }

    @Test
    void shouldFail_ValidationOnMalformedToken() {
        assertThat(jwtUtil.validateToken("not.a.jwt")).isFalse();
    }

}
