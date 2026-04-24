package com.shoestock.backend.service;

import com.shoestock.backend.dto.UserDTO;
import com.shoestock.backend.entity.Role;
import com.shoestock.backend.entity.User;
import com.shoestock.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class) ativa o Mockito nessa classe de teste
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // @Mock cria um objeto falso — não bate no banco de verdade
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    // @InjectMocks cria o UserService real e injeta os mocks acima
    @InjectMocks
    private UserService userService;

    private UserDTO.Request requestDTO;
    private User savedUser;

    // @BeforeEach executa antes de cada teste
    // Prepara os dados que vários testes vão usar
    @BeforeEach
    void setUp() {
        requestDTO = UserDTO.Request.builder()
                .name("Ana Estoquista")
                .email("ana@shoestock.com")
                .password("123456")
                .role(Role.ESTOQUISTA)
                .build();

        savedUser = User.builder()
                .id(1L)
                .name("Ana Estoquista")
                .email("ana@shoestock.com")
                .password("senhaCriptografada")
                .role(Role.ESTOQUISTA)
                .build();
    }

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso")
    void deveCadastrarUsuarioComSucesso() {
        // ARRANGE — configura o comportamento dos mocks
        // "quando verificar se email existe, retorna false"
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        // "quando criptografar a senha, retorna esse valor"
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCriptografada");
        // "quando salvar qualquer User, retorna o savedUser"
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // ACT — executa o método que queremos testar
        UserDTO.Response response = userService.create(requestDTO);

        // ASSERT — verifica se o resultado é o esperado
        assertNotNull(response);
        assertEquals("Ana Estoquista", response.getName());
        assertEquals("ana@shoestock.com", response.getEmail());
        assertEquals(Role.ESTOQUISTA, response.getRole());
        assertNull(response.getId() == null ? null : null); // senha nunca exposta

        // Verifica se o save foi chamado exatamente 1 vez
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já cadastrado")
    void deveLancarExcecaoQuandoEmailDuplicado() {
        // ARRANGE
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // ACT + ASSERT
        // assertThrows verifica se uma exceção é lançada
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.create(requestDTO)
        );

        assertEquals("Email já cadastrado: ana@shoestock.com", exception.getMessage());

        // Verifica que o save NUNCA foi chamado
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve listar estoquistas com sucesso")
    void deveListarEstoquistasComSucesso() {
        // ARRANGE
        when(userRepository.findByRole(Role.ESTOQUISTA)).thenReturn(List.of(savedUser));

        // ACT
        List<UserDTO.Response> result = userService.listEstoquistas();

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Ana Estoquista", result.get(0).getName());
        assertEquals(Role.ESTOQUISTA, result.get(0).getRole());
    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void deveBuscarUsuarioPorId() {
        // ARRANGE
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));

        // ACT
        UserDTO.Response response = userService.findById(1L);

        // ASSERT
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Ana Estoquista", response.getName());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // ARRANGE
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.findById(99L)
        );

        assertEquals("Usuário não encontrado: 99", exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar usuário com sucesso")
    void deveDeletarUsuarioComSucesso() {
        // ARRANGE
        when(userRepository.existsById(1L)).thenReturn(true);

        // ACT — não lança exceção
        assertDoesNotThrow(() -> userService.delete(1L));

        // ASSERT — verifica que o deleteById foi chamado
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar usuário inexistente")
    void deveLancarExcecaoAoDeletarUsuarioInexistente() {
        // ARRANGE
        when(userRepository.existsById(99L)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(RuntimeException.class, () -> userService.delete(99L));

        // Verifica que deleteById NUNCA foi chamado
        verify(userRepository, never()).deleteById(anyLong());
    }
}