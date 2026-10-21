
package com.produtos.gerenciador.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.produtos.gerenciador.model.Categoria;
import com.produtos.gerenciador.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Cadastra uma nova categoria.
    public Categoria salvar(Categoria categoria) {

        // Verifica se o nome está vazio.
        validarNome(categoria.getNome());

        // Impede o cadastro de categorias com nomes repetidos.
        if (categoriaRepository.existsByNomeIgnoreCase(categoria.getNome().trim())) {
        	throw new IllegalArgumentException("Já existe uma categoria com esse nome.");
        }

        // Remove espaços desnecessários no começo e no fim.
        categoria.setNome(categoria.getNome().trim());

        return categoriaRepository.save(categoria);
    }

    // Retorna todas as categorias cadastradas.
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    // Procura uma categoria pelo identificador.
    public Categoria buscarPorId(Long id) {

        return categoriaRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Categoria não encontrada."));
    }

    // Atualiza os dados de uma categoria existente.
    public Categoria atualizar(Long id, Categoria dadosAtualizados) {

        // Verifica se a categoria existe.
        Categoria categoria = buscarPorId(id);

        // Valida o novo nome.
        validarNome(dadosAtualizados.getNome());

        String novoNome = dadosAtualizados.getNome().trim();

        // Impede utilizar o nome de outra categoria.
        if (categoriaRepository.existsByNomeIgnoreCaseAndIdNot(
                novoNome, id)) { throw new IllegalArgumentException("Já existe outra categoria com esse nome.");
        }

        // Altera somente o nome da categoria encontrada.
        categoria.setNome(novoNome);

        return categoriaRepository.save(categoria);
    }

    // Exclui uma categoria pelo identificador.
    public void excluir(Long id) {

        // Verifica se a categoria existe antes da exclusão.
        Categoria categoria = buscarPorId(id);

        categoriaRepository.delete(categoria);
    }

    // Método auxiliar para validar o nome.
    private void validarNome(String nome) {

        if (nome == null || nome.isBlank()) {throw new IllegalArgumentException("O nome da categoria é obrigatório.");
        }
    }
}
