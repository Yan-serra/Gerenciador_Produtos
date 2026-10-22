
package com.produtos.gerenciador.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.produtos.gerenciador.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Verifica se já existe um produto com determinado nome.
    // Ignora diferenças entre letras maiúsculas e minúsculas.
    boolean existsByNomeIgnoreCase(String nome);

    // Verifica nomes duplicados durante a atualização.
    // Desconsidera o próprio produto que está sendo editado.
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    // Lista os produtos que pertencem a determinada categoria.
    List<Produto> findByCategoriaId(Long categoriaId);

    // Verifica se existem produtos vinculados a uma categoria.
    // Será útil para impedir a exclusão de categorias em uso.
    boolean existsByCategoriaId(Long categoriaId);
}
