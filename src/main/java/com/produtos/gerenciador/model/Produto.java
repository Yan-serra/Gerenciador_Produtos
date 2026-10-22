
package com.produtos.gerenciador.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Produto {

    // Identificador único, gerado automaticamente.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome obrigatório do produto.
    @NotBlank(message = "O nome do produto é obrigatório.")
    private String nome;

    // Descrição do produto (opcional).
    private String descricao;

    // Preço obrigatório e maior que zero.
    @NotNull(message = "O preço do produto é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false,message = "O preço deve ser maior que zero.")
    private BigDecimal preco;

    // Muitos produtos podem pertencer à mesma categoria.
    // A categoria é obrigatória para cadastrar o produto.
    @NotNull(message = "A categoria do produto é obrigatória.")
    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // Construtor vazio necessário para o JPA.
    public Produto() {
    }

    // Getters e setters.
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
