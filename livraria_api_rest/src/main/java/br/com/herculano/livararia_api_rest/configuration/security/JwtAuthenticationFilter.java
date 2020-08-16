package br.com.herculano.livararia_api_rest.configuration.security;

import static br.com.herculano.livararia_api_rest.auth.token.Constants.HEADER_STRING;
import static br.com.herculano.livararia_api_rest.auth.token.Constants.TOKEN_PREFIX;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.herculano.livararia_api_rest.auth.token.UsuarioDetailsService;
import br.com.herculano.livararia_api_rest.configuration.handlerErrors.ApiError;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UsuarioDetailsService userDetailsService;

	@Autowired
	private TokenProvider jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
		String header = req.getHeader(HEADER_STRING);
		String username = null;
		String authToken = null;

		if (header != null && header.startsWith(TOKEN_PREFIX)) {

			authToken = header.replace(TOKEN_PREFIX, "");

			try {
				username = jwtTokenUtil.getUsernameFromToken(authToken);

				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

					UserDetails userDetails = userDetailsService.loadUserByUsername(username);

					if (jwtTokenUtil.validateToken(authToken, userDetails)) {
						UsernamePasswordAuthenticationToken authentication = jwtTokenUtil.getAuthentication(authToken,
								SecurityContextHolder.getContext().getAuthentication(), userDetails);

						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

						logger.info("authenticated user " + username + ", setting security context");

						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				}
				chain.doFilter(req, res);
			} catch (Exception e) {
				handleJWTException(e, req, res, username);
			}
		} else {
			logger.warn("couldn't find bearer string, will ignore the header");
			chain.doFilter(req, res);
		}
	}

	private void handleJWTException(Exception ex, HttpServletRequest req, HttpServletResponse res, String username) {
		try {
			String error = "";

			if (ex instanceof IllegalArgumentException) {
				error = "an error occured during getting username from token";
				logger.error(error);
			} else if (ex instanceof ExpiredJwtException) {
				error = "the token is expired and not valid anymore";
				logger.warn(error);
			} else if (ex instanceof SignatureException) {
				error = "Authentication Failed. Username or Password not valid.";
				logger.error(error);
			} else if (ex instanceof MalformedJwtException) {
				error = "Authentication Failed. Token not valid.";
				logger.error(error);
			} else if (ex instanceof UsernameNotFoundException) {
				error = "authenticated user " + username + ", setting security context.";
				logger.info(error);
			}

			ObjectMapper om = new ObjectMapper();
			om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			om.registerModule(new JavaTimeModule());
			
			ObjectWriter ow = om.writer();
			
			String json = ow.writeValueAsString(new ApiError(HttpStatus.FORBIDDEN, error, ex));

			res.setStatus(HttpServletResponse.SC_FORBIDDEN);
			res.addHeader("Content-Type", "application/json");
			res.getWriter().write(json);
			res.getWriter().flush();
			res.getWriter().close();
		} catch (Exception e) {
		}
	}
}