package ro.per.online.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import ro.per.online.constantes.Constantes;
import ro.per.online.services.LoginService;

/**
 * Controlador de las operaciones relacionadas con el login del usuario y la seguridad de la aplicación.
 * @author ATOS
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * Número máximo de usuarios que pueden iniciar sessión con el mismo nombre de usuario.
	 */
	private static final int MAXCONCURRENTUSERSESSIONS = 1;

	/**
	 * Implementación de UserDetailsService que necesita Spring Security.
	 */
	@Autowired
	private transient LoginService loginService;

	/**
	 * Implementación de lo que tiene que hacer Spring Security cuando un usuario se loguee con éxito. Usado para la
	 * auditoría de acceso a la aplicación.
	 */
	@Autowired
	private transient AuthenticationSuccessHandlerPersonalizado authenticationSuccessHandlerPersonalizado;

	/**
	 * Manejador de login incorrectos en el sistema.
	 */
	@Autowired
	private transient AuthenticationFailureHandler authenticationFailureHandlerPersonalizado;

	/**
	 * Configuramos el UserDetailsService y el PasswordEncoder que vamos a usar.
	 * @param auth constructor de autenticación
	 * @throws Exception excepción genérica
	 */
	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(loginService).passwordEncoder(passwordEncoder());
	}

	/**
	 * Configuración de la codificación de la contraseña usando BCrypt.
	 * @return PasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Configuración de la seguridad para no permitir el acceso a páginas por usuarios que no tengan los permisos
	 * adecuados. Defininicón de la redirección de la apliación al iniciar y cerrar sesión en la aplicación.
	 */
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		// Desactivada protección Cross-Site, incompatible con la implementación actual del sistema
		http.csrf().disable();
		http.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN));
		// Gestión de peticiones HTTP a recursos del sistema en base a la sesión del usuario
		http.authorizeRequests()
				// Recursos comunes
				.antMatchers("/css/**", "/images/**", "/js/**", "/javax.faces.resource/**").permitAll()

				// Acceso al sistema
				.antMatchers(Constantes.RUTALOGIN + "/**").anonymous().antMatchers(Constantes.ACCESO).anonymous()
				.antMatchers(Constantes.INDEX).authenticated().antMatchers(Constantes.ACCESO).anonymous()
				// Acceso a la administración sólo para el role ADMIN
				.antMatchers("/usuarioLost/**").hasAnyRole("ADMIN", "ADMIN").and().formLogin()
				.loginPage(Constantes.RUTALOGIN).loginProcessingUrl(Constantes.RUTALOGIN)
				.defaultSuccessUrl(Constantes.INDEX).failureUrl(Constantes.RUTALOGIN).and().logout()
				.logoutUrl("/login/logout").logoutSuccessUrl(Constantes.RUTALOGIN);
		http.logout().logoutUrl(Constantes.RUTALOGOUT).logoutSuccessUrl(Constantes.RUTALOGIN);

		// Inicio de sesión
		http.formLogin().loginPage(Constantes.RUTALOGIN).permitAll()
				.successHandler(authenticationSuccessHandlerPersonalizado)
				.failureHandler(authenticationFailureHandlerPersonalizado);

		// Cierre de sesión
		http.logout().invalidateHttpSession(true).logoutUrl(Constantes.RUTALOGOUT)
				.logoutSuccessUrl(Constantes.RUTALOGIN).deleteCookies("JSESSIONID");

		// configuración para el manejo de las sessiones de los usuarios
		http.sessionManagement().invalidSessionUrl(Constantes.RUTALOGIN).maximumSessions(MAXCONCURRENTUSERSESSIONS)
				.maxSessionsPreventsLogin(false).sessionRegistry(sessionRegistry()).expiredUrl(Constantes.RUTALOGIN);

	}

	/**
	 * Usado por spring security para saber los usuarios (Principal) que han iniciado sesión.
	 * @return SessionRegistry
	 */
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	/**
	 * Controlar la sesión de un usuario. Trigger usado cuando un usuario cancela su sesión. Es necesario tenerlo
	 * definido para poder validar las sesiones que tiene abiertas un usuario.
	 * @return HttpSessionEventPublisher
	 */
	@Bean
	public static HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}
}
