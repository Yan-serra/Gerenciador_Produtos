
package com.produtos.gerenciador.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.produtos.gerenciador.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Verifica se já existe uma categoria com o nome informado,
    // ignorando diferenças entre maiúsculas e minúsculas.
    boolean existsByNomeIgnoreCase(String nome);

    // Verifica duplicidade durante a atualização,
    // desconsiderando o próprio registro que está sendo editado.
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}
