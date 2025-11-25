package com.tyonex.top_up.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                .info(new Info()
                        .title("Top-Up System API")
                        .version("1.0.0")
                        .description("E-Belge Gönderim Sistemi API Dokümantasyonu\n\n" +
                                "Bu API, e-Fatura ve e-Arşiv belgelerini göndermek için kullanılır.\n\n" +
                                "**Kullanım:**\n" +
                                "1. Önce `/api/auth/register` veya `/api/auth/login` ile giriş yapın\n" +
                                "2. Dönen `token` değerini kopyalayın\n" +
                                "3. Sağ üstteki \"Authorize\" butonuna tıklayın\n" +
                                "4. Token'ı `Bearer {token}` formatında girin (Bearer kelimesi otomatik eklenir)\n" +
                                "5. Artık tüm endpoint'leri test edebilirsiniz")
                        .contact(new Contact()
                                .name("Top-Up System")
                                .email("support@topup.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token ile authentication. Login endpoint'inden aldığınız token'ı buraya girin.")));
    }
}

