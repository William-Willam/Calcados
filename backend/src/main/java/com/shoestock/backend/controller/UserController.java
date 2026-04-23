//O Controller é a camada que recebe as requisições HTTP e devolve as respostas.
// É aqui que definimos as rotas da API.

package com.shoestock.backend.controller;


import com.shoestock.backend.dto.UserDTO;
import com.shoestock.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController = @Controller + @ResponseBody
// Indica que essa classe é um controller REST e que os métodos retornam JSON
// @RequestMapping define o prefixo de todas as rotas dessa classe
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // POST /api/users
    // Cadastra um novo usuário
    // @RequestBody = o Spring lê o JSON do corpo da requisição e converte para UserDTO.Request
    // @Valid = ativa as validações do DTO (ex: @NotBlank, @Email)
    @PostMapping
    public ResponseEntity<UserDTO.Response> create(@Valid @RequestBody UserDTO.Request dto) {
        UserDTO.Response response = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // Retorna 201 Created
    }

    // GET /api/users/estoquistas
    // Lista todos os estoquistas (apenas o gestor vai chamar esse endpoint)
    @GetMapping("/estoquistas")
    public ResponseEntity<List<UserDTO.Response>> listEstoquistas() {
        return ResponseEntity.ok(userService.listEstoquistas()); // retorna 200 ok
    }

    // GET /api/users/{id}
    // Busca um usuário pelo ID
    // @PathVariable = captura o {id} da URL
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO.Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    // PUT /api/users/{id}
    // Atualiza dados de um usuário
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO.Response> update(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO.Request dto
    ){
        return ResponseEntity.ok(userService.update(id, dto));
    }

    // DELETE /api/users/{id}
    // Remove um usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO.Response> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();// Retorna 204 No Content
    }

}
