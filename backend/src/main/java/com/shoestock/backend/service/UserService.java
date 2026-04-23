//Service — é a camada de regras de negócio.
//Exemplo: "antes de cadastrar um usuário, verificar se o email já existe"

package com.shoestock.backend.service;

import com.shoestock.backend.dto.UserDTO;
import com.shoestock.backend.entity.Role;
import com.shoestock.backend.entity.User;
import com.shoestock.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


// @Service marca essa classe como um componente de serviço do Spring
// @RequiredArgsConstructor (Lombok) gera o construtor com os campos final
// isso é a injeção de dependência — o Spring injeta os objetos automaticamente
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Criptografa a senha com BCrypt

    // Converte entidade → DTO de resposta (nunca expõe a senha)
    private UserDTO.Response toResponse(User user) {
        return UserDTO.Response.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    // cadastra um novo usuario
    public UserDTO.Response create(UserDTO.Request dto) {
        // Regra: email não pode ser duplicado
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + dto.getEmail());
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword())) // Criptografa a senha
                .role(dto.getRole())
                .build();

        return toResponse(userRepository.save(user));
    }

    // Lista todos os estoquistas
    public List<UserDTO.Response> listEstoquistas() {
        return userRepository.findByRole(Role.ESTOQUISTA)
                .stream()
                .map(this::toResponse) // Para cada User, aplica o método toResponse
                .toList();
    }

    // Atualiza dados de um usuário
    public UserDTO.Response update(Long id, UserDTO.Request dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());

        return toResponse(userRepository.save(user));
    }

    // Remove um usuário pelo ID
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado: " + id);
        }
        userRepository.deleteById(id);
    }
}
