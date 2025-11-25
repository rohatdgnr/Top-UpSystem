package com.tyonex.top_up.controller;

import com.tyonex.top_up.dto.request.LoginRequest;
import com.tyonex.top_up.dto.request.RegisterRequest;
import com.tyonex.top_up.dto.response.ApiResponse;
import com.tyonex.top_up.dto.response.AuthResponse;
import com.tyonex.top_up.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.tyonex.top_up.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Kullanıcı kayıt ve giriş işlemleri")
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Kullanıcı kaydı",
            description = "Yeni bir kullanıcı hesabı oluşturur ve JWT token döner"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Kayıt başarılı",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Geçersiz istek veya email zaten kullanılıyor")
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("User registered successfully")
                .data(response)
                .build());
    }

    @Operation(
            summary = "Kullanıcı girişi",
            description = "Email ve şifre ile giriş yapar ve JWT token döner. " +
                    "Dönen token'ı Swagger'da 'Authorize' butonuna tıklayarak kullanabilirsiniz."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Giriş başarılı",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Geçersiz email veya şifre")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Login successful")
                .data(response)
                .build());
    }
}

