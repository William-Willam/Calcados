//Os DTOs (Data Transfer Objects) definem o que a API recebe e o que ela retorna,
//sem expor dados sensíveis como a senha.

package com.shoestock.backend.dto;

import com.shoestock.backend.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDTO {

    // Objeto usado para CADASTRAR ou ATUALIZAR um usuário
    // Contém senha pois é o que o cliente envia para a API
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{

        @NotBlank(message = "Nome é obrigatório")
        private String name;

        @Email(message = "Email inválido")
        @NotBlank(message = "Email obrigatório")
        private String email;

        @NotBlank(message = "Senha é obrigatório")
        private String password;

        @NotNull(message = "Perfil é obrigatório")
        private Role role;
    }

    // Objeto usado para RETORNAR dados do usuário
    // Nunca expõe a senha!
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private String email;
        private Role role;
    }
}
