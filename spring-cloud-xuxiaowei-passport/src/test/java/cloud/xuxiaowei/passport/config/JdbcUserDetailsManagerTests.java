package cloud.xuxiaowei.passport.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * JDBC 用户管理服务 测试类
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@SpringBootTest
class JdbcUserDetailsManagerTests {

	@Autowired
	private UserDetailsService userDetailsService;

	private JdbcUserDetailsManager jdbcUserDetailsManager;

	@Autowired
	public void setJdbcUserDetailsManager(DataSource dataSource) {
		this.jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
	}

	/**
	 * 创建用户
	 */
	@Test
	void createUser() throws JsonProcessingException {
		String username = "xuxiaowei-" + RandomStringUtils.randomNumeric(3);
		String password = UUID.randomUUID().toString();
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("programmer"));

		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		String encode = passwordEncoder.encode(password);

		log.info("用户名: {}", username);
		log.info("密码: {}", password);
		log.info("密码（加密）: {}", encode);
		log.info("权限: {}", authorities);

		UserDetails user = new User(username, encode, authorities);
		jdbcUserDetailsManager.createUser(user);

		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		assertNotNull(userDetails);

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

		log.info("\n{}", objectWriter.writeValueAsString(userDetails));

		assertNotNull(userDetails.getUsername());
		assertNotNull(userDetails.getPassword());
		assertNotNull(userDetails.getAuthorities());
	}

}
