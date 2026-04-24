package com.shoestock.desktop.util;

// Classe estática que guarda os dados da sessão atual
// Como o JavaFX não tem contexto como o Spring,
// usamos variáveis estáticas para compartilhar entre telas
public class SessionManager {

    private static String token;   // JWT do usuário logado
    private static String role;    // perfil: GESTOR ou ESTOQUISTA
    private static String name;    // nome do usuário

    public static void setSession(String token, String role, String name) {
        SessionManager.token = token;
        SessionManager.role = role;
        SessionManager.name = name;
    }

    public static String getToken() {
        return token;
    }

    public static String getRole() {
        return role;
    }

    public static String getName() {
        return name;
    }

    // Limpa a sessão ao fazer logout
    public static void clear() {
        token = null;
        role = null;
        name = null;
    }

    // Verifica se há alguém logado
    public static boolean isLoggedIn() {
        return token != null;
    }
}