package com.shoestock.backend.repository;

//Repository — é a camada que conversa com o banco de dados.
//Você só declara os métodos e o Spring cria as consultas SQL automaticamente.

// JpaRepository<User, Long>:
// → User = a entidade que esse repository gerencia
// → Long = o tipo do ID dessa entidade
// O Spring já fornece automaticamente: save, findById, findAll, delete, count, etc.


import com.shoestock.backend.entity.Role;
import com.shoestock.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // O Spring lê o nome do método e gera o SQL automaticamente:
    // SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // SELECT * FROM users WHERE email = ? (retorna true/false)
    boolean existsByEmail(String email);

    // SELECT * FROM users WHERE role = ?
    // Útil para listar todos os estoquistas
    List<User> findByRole(Role role);

}
