
package com.produtos.gerenciador.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.produtos.gerenciador.model.Categoria;
import com.produtos.gerenciador.repository.CategoriaRepository;
import com.produtos.gerenciador.repository.ProdutoRepository;

@Service
public class CategoriaService {

    // Repository responsável pelas operações de Categoria.
    @Autowired
    private CategoriaRepository categoriaRepository;

    // Repository utilizado para verificar produtos vinculados.
    @Autowired
    private ProdutoRepository produtoRepository;

    // POST - Cadastra uma nova categoria.
    public Categoria salvar(Categoria categoria) {

        // Verifica se o nome foi preenchido.
        validarNome(categoria.getNome());

        // Remove espaços no início e no final.
        String nome = categoria.getNome().trim();

        // Impede o cadastro de nomes duplicados.
        if (categoriaRepository.existsByNomeIgnoreCase(nome)) {
            throw new IllegalArgumentException("Já existe uma categoria com esse nome.");
        }

        // Define o nome tratado antes de salvar.
        categoria.setNome(nome);

        // O banco gera automaticamente o ID da nova categoria.
        categoria.setId(null);

        return categoriaRepository.save(categoria);
    }

    // GET - Lista todas as categorias.
    public List<Categoria> listarTodas() {

        return categoriaRepository.findAll();
    }

    // GET - Busca uma categoria pelo ID.
    public Categoria buscarPorId(Long id) {

        return categoriaRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Categoria não encontrada."));
    }

    // PUT - Atualiza uma categoria existente.
    public Categoria atualizar(Long id, Categoria dadosAtualizados) {

        // Confirma que a categoria existe.
        Categoria categoria = buscarPorId(id);

        // Verifica se o novo nome é válido.
        validarNome(dadosAtualizados.getNome());
        String novoNome = dadosAtualizados.getNome().trim();

        // Impede utilizar o nome de outra categoria.
        // O próprio registro fica fora dessa verificação.
        if (categoriaRepository.existsByNomeIgnoreCaseAndIdNot(novoNome, id)) {throw new IllegalArgumentException(
                    "Já existe outra categoria com esse nome.");
        }

        // Atualiza apenas o nome.
        categoria.setNome(novoNome);

        return categoriaRepository.save(categoria);
    }

    // DELETE - Exclui uma categoria.
    public void excluir(Long id) {

        // Confirma que a categoria existe.
        Categoria categoria = buscarPorId(id);

        // Impede a exclusão de categorias com produtos associados.
        if (produtoRepository.existsByCategoriaId(id)) {

            throw new IllegalArgumentException(
                    "Não é possível excluir uma categoria que possui produtos cadastrados.");
        }

        // Exclui somente quando não existem produtos vinculados.
        categoriaRepository.delete(categoria);
    }

    // Método auxiliar para verificar se o nome é válido.
    private void validarNome(String nome) {

        if (nome == null || nome.isBlank()) {

            throw new IllegalArgumentException("O nome da categoria é obrigatório.");
        }
    }
}