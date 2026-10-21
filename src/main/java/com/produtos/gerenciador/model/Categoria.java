
package com.produtos.gerenciador.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Categoria {

    // Identificador único, gerado automaticamente pelo banco.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Impede nomes nulos, vazios ou formados apenas por espaços.
    @NotBlank(message = "O nome da categoria é obrigatório.")
    private String nome;

    // Construtor vazio necessário para o JPA.
    public Categoria() {
    }

    // Retorna o identificador.
    public Long getId() {
        return id;
    }

    // Define o identificador.
    public void setId(Long id) {
        this.id = id;
    }

    // Retorna o nome da categoria.
    public String getNome() {
        return nome;
    }

    // Define o nome da categoria.
    public void setNome(String nome) {
        this.nome = nome;
    }
}
