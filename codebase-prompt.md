# Sports Player Analysis — Full Codebase Context Prompt

Paste everything below this line as your system prompt or first user message when starting a new Claude session for this project.

---

You are an expert Java/Spring Boot developer assisting with the **sports-player-analysis** application.

## Project Overview

A Spring Boot REST API for managing sports players, with stateless JWT-based authentication. Players can be registered, queried, updated, and deleted. Auth-protected routes require a valid Bearer token. The app uses an in-memory H2 database (configurable to PostgreSQL).

## Tech Stack

- **Java 21**
- **Spring Boot 4.0.6** (spring-boot-starter-webmvc, spring-boot-starter-data-jpa, spring-boot-starter-security)
- **Spring Security** with stateless JWT authentication
- **JJWT 0.11.5** (jjwt-api, jjwt-impl, jjwt-jackson) for JWT operations
- **BCrypt** for password hashing
- **H2** in-memory database (runtime), **PostgreSQL** driver also on classpath
- **Hibernate / Spring Data JPA** for ORM
- **Maven** build tool

## Package Structure

```
com.sports_analysis_app.sports_analysis_app
├── SportsAnalysisAppApplication.java          ← Entry point
├── annotation/
│   └── auth/
│       └── AuthRequired.java                  ← Custom meta-annotation wrapping @PreAuthorize
├── common/
│   ├── controller/
│   │   └── HelloWorldController.java          ← Test/health endpoints
│   └── dto/
│       └── ApiErrorResponse.java              ← Standardized error response DTO
├── config/
│   └── SecurityConfig.java                    ← Spring Security filter chain config
├── player/
│   ├── controller/PlayerController.java       ← /api/player/** endpoints
│   ├── dto/
│   │   ├── PlayerRequest.java                 ← Create player body
│   │   └── PlayerUpdateRequest.java           ← Update player body
│   ├── entity/Player.java                     ← JPA entity → "players" table
│   ├── repository/PlayerRepository.java       ← Spring Data JPA repository
│   └── service/PlayerService.java             ← Player business logic
├── security/
│   ├── JwtAccessDeniedHandler.java            ← 403 handler
│   ├── JwtAuthenticationEntryPoint.java       ← 401 handler
│   ├── JwtAuthenticationFilter.java           ← Bearer token extraction + validation filter
│   ├── JwtUtil.java                           ← JWT sign / parse / validate
│   ├── PasswordEncoder.java                   ← BCrypt wrapper
│   ├── SecurityErrorWriter.java               ← Writes JSON error to HttpServletResponse
│   └── SecurityUtil.java                      ← Helper to read current user from SecurityContext
└── user/
    ├── controller/UserController.java         ← /api/auth/** endpoints
    ├── dto/
    │   ├── AuthResponse.java
    │   ├── LoginRequest.java
    │   ├── RefreshRequest.java
    │   └── RegisterRequest.java
    ├── entity/User.java                       ← JPA entity → "users" table
    ├── repository/UserRepository.java
    └── service/UserService.java               ← Register / login / refresh logic
```

## API Routes

| Method | Path | Auth Required | Description |
|--------|------|---------------|-------------|
| GET | /hello | No | Hello world |
| GET | /api/hello | No | Hello world v2 |
| POST | /api/auth/register | No | Register user → returns tokens |
| POST | /api/auth/login | No | Login → returns tokens |
| POST | /api/auth/refresh | No | Exchange refresh token → new tokens |
| GET | /api/auth/me/{id} | No | Get user by ID |
| GET | /api/auth/email/{email} | No | Get user by email |
| POST | /api/player | **Yes** | Create player |
| DELETE | /api/player/{id} | **Yes** | Delete player |
| GET | /api/player/{id} | No | Get player by ID |
| GET | /api/player/email/{email} | No | Get player by email |
| GET | /api/player/name/{name} | No | Get player by name |
| GET | /api/player | No | Get all players |
| GET | /api/player/role/{role} | No | Get players by role |
| GET | /api/player/team/{team} | No | Get players by team |
| GET | /api/player/search | No | Search players by name or email |
| PUT | /api/player/{id} | No | Update player |

## Configuration (application.properties)

```properties
spring.application.name=sports_analysis_app
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
server.port=8080
jwt.access_token_secret=<min 32 char secret>
jwt.refresh_token_secret=<min 32 char secret>
jwt.access_token_expiration=<millis>
jwt.refresh_token_expiration=<millis>
```

---

## Full Source Files

### Entry Point

**`SportsAnalysisAppApplication.java`**
```java
package com.sports_analysis_app.sports_analysis_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SportsAnalysisAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(SportsAnalysisAppApplication.class, args);
    }
}
```

---

### Security Layer

**`config/SecurityConfig.java`**
```java
package com.sports_analysis_app.sports_analysis_app.config;

import com.sports_analysis_app.sports_analysis_app.security.JwtAuthenticationFilter;
import com.sports_analysis_app.sports_analysis_app.security.JwtAuthenticationEntryPoint;
import com.sports_analysis_app.sports_analysis_app.security.JwtAccessDeniedHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/player/**").authenticated()
                .requestMatchers("/api/users/**").authenticated()
                .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        return http.build();
    }
}
```

---

**`security/JwtUtil.java`**
```java
package com.sports_analysis_app.sports_analysis_app.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import javax.crypto.SecretKey;

@Component
public class JwtUtil {
    @Value("${jwt.access_token_secret}")
    private String accessTokenSecret;

    @Value("${jwt.refresh_token_secret}")
    private String refreshTokenSecret;

    @Value("${jwt.access_token_expiration}")
    private long expiration;

    @Value("${jwt.refresh_token_expiration}")
    private long refreshTokenExpiration;

    public String generateAccessToken(String email, Long userId) {
        SecretKey key = Keys.hmacShaKeyFor(accessTokenSecret.getBytes());
        return Jwts.builder()
            .setSubject(email)
            .claim("userId", userId)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public String generateRefreshToken(String email, Long userId) {
        SecretKey key = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
        return Jwts.builder()
            .setSubject(email)
            .claim("userId", userId)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public String extractEmailFromAccessToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(accessTokenSecret.getBytes());
        return Jwts.parserBuilder().setSigningKey(key).build()
            .parseClaimsJws(token).getBody().getSubject();
    }

    public String extractEmailFromRefreshToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
        return Jwts.parserBuilder().setSigningKey(key).build()
            .parseClaimsJws(token).getBody().getSubject();
    }

    public Long extractUserIdFromAccessToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(accessTokenSecret.getBytes());
        return ((Number) Jwts.parserBuilder().setSigningKey(key).build()
            .parseClaimsJws(token).getBody().get("userId")).longValue();
    }

    public Long extractUserIdFromRefreshToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
        return ((Number) Jwts.parserBuilder().setSigningKey(key).build()
            .parseClaimsJws(token).getBody().get("userId")).longValue();
    }

    public boolean validateAccessToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(accessTokenSecret.getBytes());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
```

---

**`security/JwtAuthenticationFilter.java`**
```java
package com.sports_analysis_app.sports_analysis_app.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sports_analysis_app.sports_analysis_app.security.SecurityErrorWriter.HttpServletResponseStatus;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final SecurityErrorWriter securityErrorWriter;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, SecurityErrorWriter securityErrorWriter) {
        this.jwtUtil = jwtUtil;
        this.securityErrorWriter = securityErrorWriter;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || authHeader.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!authHeader.startsWith("Bearer ")) {
                writeUnauthorized(request, response, "Authorization header must use the Bearer token format");
                return;
            }

            String token = authHeader.substring(7);

            if (token.isBlank()) {
                writeUnauthorized(request, response, "Bearer token is missing");
                return;
            }

            if (!jwtUtil.validateAccessToken(token)) {
                writeUnauthorized(request, response, "Access token is invalid or expired");
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                String email = jwtUtil.extractEmailFromAccessToken(token);

                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            writeUnauthorized(request, response, "Access token could not be processed");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeUnauthorized(
        HttpServletRequest request,
        HttpServletResponse response,
        String message
    ) throws IOException {
        securityErrorWriter.write(request, response, HttpServletResponseStatus.UNAUTHORIZED, message);
    }
}
```

---

**`security/JwtAuthenticationEntryPoint.java`**
```java
package com.sports_analysis_app.sports_analysis_app.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.sports_analysis_app.sports_analysis_app.security.SecurityErrorWriter.HttpServletResponseStatus;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final SecurityErrorWriter securityErrorWriter;

    public JwtAuthenticationEntryPoint(SecurityErrorWriter securityErrorWriter) {
        this.securityErrorWriter = securityErrorWriter;
    }

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException, ServletException {
        securityErrorWriter.write(
            request, response,
            HttpServletResponseStatus.UNAUTHORIZED,
            "Authentication is required to access this route"
        );
    }
}
```

---

**`security/JwtAccessDeniedHandler.java`**
```java
package com.sports_analysis_app.sports_analysis_app.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.sports_analysis_app.sports_analysis_app.security.SecurityErrorWriter.HttpServletResponseStatus;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final SecurityErrorWriter securityErrorWriter;

    public JwtAccessDeniedHandler(SecurityErrorWriter securityErrorWriter) {
        this.securityErrorWriter = securityErrorWriter;
    }

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        securityErrorWriter.write(
            request, response,
            HttpServletResponseStatus.FORBIDDEN,
            "You do not have permission to access this route"
        );
    }
}
```

---

**`security/SecurityErrorWriter.java`**
```java
package com.sports_analysis_app.sports_analysis_app.security;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.sports_analysis_app.sports_analysis_app.common.dto.ApiErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@Component
public class SecurityErrorWriter {
    private final ObjectMapper objectMapper;

    public SecurityErrorWriter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void write(
        HttpServletRequest request,
        HttpServletResponse response,
        HttpServletResponseStatus status,
        String message
    ) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiErrorResponse errorResponse = new ApiErrorResponse(
            status.value(), status.reasonPhrase(), message, request.getRequestURI()
        );

        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }

    public enum HttpServletResponseStatus {
        UNAUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"),
        FORBIDDEN(HttpServletResponse.SC_FORBIDDEN, "Forbidden");

        private final int value;
        private final String reasonPhrase;

        HttpServletResponseStatus(int value, String reasonPhrase) {
            this.value = value;
            this.reasonPhrase = reasonPhrase;
        }

        public int value() { return value; }
        public String reasonPhrase() { return reasonPhrase; }
    }
}
```

---

**`security/PasswordEncoder.java`**
```java
package com.sports_analysis_app.sports_analysis_app.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String hashPassword(String password) {
        return encoder.encode(password);
    }

    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}
```

---

**`security/SecurityUtil.java`**
```java
package com.sports_analysis_app.sports_analysis_app.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        return null;
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}
```

---

**`annotation/auth/AuthRequired.java`**
```java
package com.sports_analysis_app.sports_analysis_app.annotation.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("isAuthenticated()")
public @interface AuthRequired {}
```

---

### Common

**`common/dto/ApiErrorResponse.java`**
```java
package com.sports_analysis_app.sports_analysis_app.common.dto;

import java.time.Instant;

public class ApiErrorResponse {
    private final Instant timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    public ApiErrorResponse(int status, String error, String message, String path) {
        this.timestamp = Instant.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public Instant getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public String getPath() { return path; }
}
```

---

**`common/controller/HelloWorldController.java`**
```java
package com.sports_analysis_app.sports_analysis_app.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/api/hello")
    public String firstApi() {
        return "Hello this is my first spring boot api";
    }
}
```

---

### User Domain

**`user/entity/User.java`**
```java
package com.sports_analysis_app.sports_analysis_app.user.entity;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, name = "created_at")
    private Long createdAt;

    @Column(nullable = false, name = "updated_at")
    private Long updatedAt;

    public User() {}

    public User(String name, String email, String password, Long createdAt, Long updatedAt) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Long updatedAt) { this.updatedAt = updatedAt; }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Long getCreatedAt() { return createdAt; }
    public Long getUpdatedAt() { return updatedAt; }

    @Override
    public String toString() {
        return "User {id=" + id + ", name='" + name + "', email='" + email + "'}";
    }
}
```

---

**`user/repository/UserRepository.java`**
```java
package com.sports_analysis_app.sports_analysis_app.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sports_analysis_app.sports_analysis_app.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
```

---

**`user/dto/RegisterRequest.java`**
```java
package com.sports_analysis_app.sports_analysis_app.user.dto;

public class RegisterRequest {
    private String name;
    private String email;
    private String password;

    public RegisterRequest(String email, String name, String password) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
```

---

**`user/dto/LoginRequest.java`**
```java
package com.sports_analysis_app.sports_analysis_app.user.dto;

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
```

---

**`user/dto/RefreshRequest.java`**
```java
package com.sports_analysis_app.sports_analysis_app.user.dto;

public class RefreshRequest {
    private String refreshToken;

    public RefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
```

---

**`user/dto/AuthResponse.java`**
```java
package com.sports_analysis_app.sports_analysis_app.user.dto;

public class AuthResponse {
    private Long userId;
    private String accessToken;
    private String refreshToken;
    private String email;
    private String message;

    public AuthResponse(Long userId, String accessToken, String refreshToken, String email, String message) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
        this.message = message;
    }

    public Long getUserId() { return userId; }
    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getEmail() { return email; }
    public String getMessage() { return message; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public void setEmail(String email) { this.email = email; }
    public void setMessage(String message) { this.message = message; }
}
```

---

**`user/controller/UserController.java`**
```java
package com.sports_analysis_app.sports_analysis_app.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sports_analysis_app.sports_analysis_app.user.entity.User;
import com.sports_analysis_app.sports_analysis_app.user.dto.*;
import com.sports_analysis_app.sports_analysis_app.user.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return service.registerUser(request.getEmail(), request.getName(), request.getPassword());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return service.loginUser(request.getEmail(), request.getPassword());
    }

    @PostMapping("/refresh")
    public AuthResponse login(@RequestBody RefreshRequest request) {
        return service.refreshUserSession(request.getRefreshToken());
    }

    @GetMapping("/me/{id}")
    public User getUser(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @GetMapping("/email/{email}")
    public User getUser(@PathVariable String email) {
        return service.getUserByEmail(email);
    }
}
```

---

**`user/service/UserService.java`**
```java
package com.sports_analysis_app.sports_analysis_app.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sports_analysis_app.sports_analysis_app.security.JwtUtil;
import com.sports_analysis_app.sports_analysis_app.security.PasswordEncoder;
import com.sports_analysis_app.sports_analysis_app.user.dto.AuthResponse;
import com.sports_analysis_app.sports_analysis_app.user.entity.User;
import com.sports_analysis_app.sports_analysis_app.user.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse registerUser(String email, String name, String password) {
        if (email == null || email.trim().isEmpty())
            throw new IllegalArgumentException("Email is Required");

        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null)
            throw new IllegalArgumentException("Email already registered");

        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name is Required");

        if (password == null || password.trim().isEmpty())
            throw new IllegalArgumentException("Password is Required");

        long now = System.currentTimeMillis();
        String hashedPassword = passwordEncoder.hashPassword(password);
        User user = new User(name, email, hashedPassword, now, now);
        User saveUser = userRepository.save(user);

        String accessToken = jwtUtil.generateAccessToken(email, saveUser.getId());
        String refreshToken = jwtUtil.generateRefreshToken(email, saveUser.getId());

        return new AuthResponse(saveUser.getId(), accessToken, refreshToken, saveUser.getEmail(), "User registered successfully");
    }

    public AuthResponse loginUser(String email, String password) {
        if (email == null || email.trim().isEmpty())
            throw new IllegalArgumentException("Email is Required");

        if (password == null || password.trim().isEmpty())
            throw new IllegalArgumentException("Password is Required");

        User existingUser = userRepository.findByEmail(email);
        if (existingUser == null)
            throw new IllegalArgumentException("Email already registered");

        if (!passwordEncoder.verifyPassword(password, existingUser.getPassword()))
            throw new IllegalArgumentException("Invalid Email or Password");

        String accessToken = jwtUtil.generateAccessToken(email, existingUser.getId());
        String refreshToken = jwtUtil.generateRefreshToken(email, existingUser.getId());

        return new AuthResponse(existingUser.getId(), accessToken, refreshToken, existingUser.getEmail(), "Login Successful");
    }

    public AuthResponse refreshUserSession(String refreshToken) {
        if (refreshToken == null || refreshToken.trim().isEmpty())
            throw new IllegalArgumentException("Refresh Token is Required");

        if (!jwtUtil.validateRefreshToken(refreshToken))
            throw new IllegalArgumentException("Refresh Token not verified");

        String email = jwtUtil.extractEmailFromRefreshToken(refreshToken);
        Long userId = jwtUtil.extractUserIdFromRefreshToken(refreshToken);

        String accessToken = jwtUtil.generateAccessToken(email, userId);
        String newRefreshToken = jwtUtil.generateRefreshToken(email, userId);

        return new AuthResponse(userId, accessToken, newRefreshToken, email, "User Session Refresh Successful");
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new IllegalArgumentException("User not found");
        return user;
    }
}
```

---

### Player Domain

**`player/entity/Player.java`**
```java
package com.sports_analysis_app.sports_analysis_app.player.entity;

import com.sports_analysis_app.sports_analysis_app.player.dto.PlayerRequest;
import jakarta.persistence.*;

@Entity
@Table(name="players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String currentTeamName;

    private Integer jerseyNumber;
    private Integer runsScored = 0;
    private Integer wicketsTaken = 0;
    private Float battingAverage = 0f;
    private Float bowlingAverage = 0f;

    @Column(nullable = false, name = "created_at")
    private Long createdAt;

    @Column(nullable = false, name = "updated_at")
    private Long updatedAt;

    public Player() {}

    public Player(PlayerRequest playerPayload, Long createdAt, Long updatedAt) {
        this.email = playerPayload.getEmail();
        this.name = playerPayload.getName();
        this.role = playerPayload.getRole();
        this.currentTeamName = playerPayload.getCurrentTeamName();
        this.jerseyNumber = playerPayload.getJerseyNumber();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getCurrentTeamName() { return currentTeamName; }
    public void setCurrentTeamName(String currentTeamName) { this.currentTeamName = currentTeamName; }
    public Integer getJerseyNumber() { return jerseyNumber; }
    public void setJerseyNumber(Integer jerseyNumber) { this.jerseyNumber = jerseyNumber; }
    public Integer getRunsScored() { return runsScored; }
    public void setRunsScored(Integer runsScored) { this.runsScored = runsScored; }
    public Integer getWicketsTaken() { return wicketsTaken; }
    public void setWicketsTaken(Integer wicketsTaken) { this.wicketsTaken = wicketsTaken; }
    public Float getBattingAverage() { return battingAverage; }
    public void setBattingAverage(Float battingAverage) { this.battingAverage = battingAverage; }
    public Float getBowlingAverage() { return bowlingAverage; }
    public void setBowlingAverage(Float bowlingAverage) { this.bowlingAverage = bowlingAverage; }
    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
    public Long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Long updatedAt) { this.updatedAt = updatedAt; }
}
```

---

**`player/repository/PlayerRepository.java`**
```java
package com.sports_analysis_app.sports_analysis_app.player.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sports_analysis_app.sports_analysis_app.player.entity.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByEmail(String email);
    Player findByNameContainingIgnoreCase(String name);
    List<Player> findAllByRoleContainingIgnoreCase(String role);
    List<Player> findAllByCurrentTeamNameContainingIgnoreCase(String team);
    List<Player> findAllByEmailContainingIgnoreCaseOrNameContainingIgnoreCase(String email, String name);
}
```

---

**`player/dto/PlayerRequest.java`**
```java
package com.sports_analysis_app.sports_analysis_app.player.dto;

public class PlayerRequest {
    private String name;
    private String email;
    private String role;
    private String currentTeamName;
    private Integer jerseyNumber;

    public PlayerRequest(String name, String email, String role, String currentTeamName, Integer jerseyNumber) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.currentTeamName = currentTeamName;
        this.jerseyNumber = jerseyNumber;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getCurrentTeamName() { return currentTeamName; }
    public void setCurrentTeamName(String currentTeamName) { this.currentTeamName = currentTeamName; }
    public Integer getJerseyNumber() { return jerseyNumber; }
    public void setJerseyNumber(Integer jerseyNumber) { this.jerseyNumber = jerseyNumber; }
}
```

---

**`player/dto/PlayerUpdateRequest.java`**
```java
package com.sports_analysis_app.sports_analysis_app.player.dto;

public class PlayerUpdateRequest {
    private String name;
    private String email;
    private String role;
    private String currentTeamName;
    private Integer jerseyNumber;

    public PlayerUpdateRequest(String name, String email, String role, String currentTeamName, Integer jerseyNumber) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.currentTeamName = currentTeamName;
        this.jerseyNumber = jerseyNumber;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getCurrentTeamName() { return currentTeamName; }
    public void setCurrentTeamName(String currentTeamName) { this.currentTeamName = currentTeamName; }
    public Integer getJerseyNumber() { return jerseyNumber; }
    public void setJerseyNumber(Integer jerseyNumber) { this.jerseyNumber = jerseyNumber; }
}
```

---

**`player/service/PlayerService.java`**
```java
package com.sports_analysis_app.sports_analysis_app.player.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sports_analysis_app.sports_analysis_app.player.dto.PlayerRequest;
import com.sports_analysis_app.sports_analysis_app.player.dto.PlayerUpdateRequest;
import com.sports_analysis_app.sports_analysis_app.player.entity.Player;
import com.sports_analysis_app.sports_analysis_app.player.repository.PlayerRepository;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public Optional<Player> getPlayerById(Long playerId) {
        if (playerId == null)
            throw new IllegalArgumentException("Please provide appropriate player id");
        Optional<Player> player = playerRepository.findById(playerId);
        if (player == null)
            throw new IllegalArgumentException("Player with this id does not exist");
        return player;
    }

    public Player getPlayerByEmail(String email) {
        if (email == null || email.trim().isEmpty())
            throw new IllegalArgumentException("Email is Required");
        Player player = playerRepository.findByEmail(email);
        if (player == null)
            throw new IllegalArgumentException("Player with this email does not exist");
        return player;
    }

    public Player registerPlayer(PlayerRequest playerPayload) {
        if (playerPayload.getEmail() == null || playerPayload.getEmail().trim().isEmpty())
            throw new IllegalArgumentException("Email is Required");

        Player existingPlayer = playerRepository.findByEmail(playerPayload.getEmail());
        if (existingPlayer != null)
            throw new IllegalArgumentException("Email already registered");

        if (playerPayload.getName() == null || playerPayload.getName().trim().isEmpty())
            throw new IllegalArgumentException("Name is Required");

        long now = System.currentTimeMillis();
        return playerRepository.save(new Player(playerPayload, now, now));
    }

    public void deletePlayer(Long playerId) {
        if (playerId == null)
            throw new IllegalArgumentException("Please provide appropriate player id");
        playerRepository.deleteById(playerId);
    }

    public Player getPlayerByName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name is Required");
        Player player = playerRepository.findByNameContainingIgnoreCase(name);
        if (player == null)
            throw new IllegalArgumentException("Player with this name does not exist");
        return player;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public List<Player> getPlayersByRole(String role) {
        if (role == null || role.trim().isEmpty())
            throw new IllegalArgumentException("Role is Required");
        return playerRepository.findAllByRoleContainingIgnoreCase(role);
    }

    public List<Player> getPlayersByTeam(String team) {
        if (team == null || team.trim().isEmpty())
            throw new IllegalArgumentException("Team is Required");
        return playerRepository.findAllByCurrentTeamNameContainingIgnoreCase(team);
    }

    public List<Player> searchPlayers(String query) {
        if (query == null || query.trim().isEmpty())
            throw new IllegalArgumentException("Search term is Required");
        return playerRepository.findAllByEmailContainingIgnoreCaseOrNameContainingIgnoreCase(query, query);
    }

    public Player updatePlayer(Long id, PlayerUpdateRequest request) {
        Player existingPlayer = this.getPlayerById(id)
            .orElseThrow(() -> new RuntimeException("Player not found with id" + id));

        existingPlayer.setName(request.getName());
        existingPlayer.setEmail(request.getEmail());
        existingPlayer.setRole(request.getRole());
        existingPlayer.setCurrentTeamName(request.getCurrentTeamName());
        existingPlayer.setJerseyNumber(request.getJerseyNumber());

        return playerRepository.save(existingPlayer);
    }
}
```

---

**`player/controller/PlayerController.java`**
```java
package com.sports_analysis_app.sports_analysis_app.player.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sports_analysis_app.sports_analysis_app.annotation.auth.AuthRequired;
import com.sports_analysis_app.sports_analysis_app.player.dto.PlayerRequest;
import com.sports_analysis_app.sports_analysis_app.player.dto.PlayerUpdateRequest;
import com.sports_analysis_app.sports_analysis_app.player.entity.Player;
import com.sports_analysis_app.sports_analysis_app.player.service.PlayerService;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    private PlayerService service;

    @AuthRequired
    @PostMapping
    public Player create(@RequestBody PlayerRequest request) {
        return service.registerPlayer(request);
    }

    @AuthRequired
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deletePlayer(id);
    }

    @GetMapping("/{id}")
    public Optional<Player> getUser(@PathVariable Long id) {
        return service.getPlayerById(id);
    }

    @GetMapping("/email/{email}")
    public Player getUser(@PathVariable String email) {
        return service.getPlayerByEmail(email);
    }

    @GetMapping("/name/{name}")
    public Player getMethodName(@PathVariable String name) {
        return service.getPlayerByName(name);
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return service.getAllPlayers();
    }

    @GetMapping("/role/{role}")
    public List<Player> getPlayersByRole(@PathVariable String role) {
        return service.getPlayersByRole(role);
    }

    @GetMapping("/team/{team}")
    public List<Player> getPlayersByTeam(@PathVariable String team) {
        return service.getPlayersByTeam(team);
    }

    @GetMapping("/search")
    public List<Player> searchPlayers(@PathVariable String searchQuery) {
        return service.searchPlayers(searchQuery);
    }

    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody PlayerUpdateRequest request) {
        return service.updatePlayer(id, request);
    }
}
```

---

## Key Architecture Decisions

1. **Stateless JWT auth** — no server-side sessions; every request is self-contained with a Bearer token
2. **Dual secrets** — access token and refresh token are signed with different secrets; they cannot be swapped
3. **Token rotation** — `/api/auth/refresh` issues a brand new refresh token, not just a new access token
4. **Two-layer auth guard** — URL-level (`SecurityConfig.authenticated()`) + method-level (`@AuthRequired`) on create/delete
5. **Filter-level error writing** — `SecurityErrorWriter` writes directly to `HttpServletResponse` because at filter level MVC error handling is not available
6. **BCrypt passwords** — passwords are one-way hashed; the raw password never persists
7. **H2 in-memory DB** — `ddl-auto=create-drop` means schema is rebuilt on every startup; all data is lost on restart
8. **No `UserDetailsService`** — Spring Security's default user-lookup mechanism is bypassed; identity is proven purely by JWT signature validation
