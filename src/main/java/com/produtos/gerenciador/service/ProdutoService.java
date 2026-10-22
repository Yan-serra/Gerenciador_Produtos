
package com.produtos.gerenciador.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.produtos.gerenciador.model.Categoria;
import com.produtos.gerenciador.model.Produto;
import com.produtos.gerenciador.repository.CategoriaRepository;
import com.produtos.gerenciador.repository.ProdutoRepository;

@Service
public class ProdutoService {

    // Repositório responsável pelos produtos.
    @Autowired
    private ProdutoRepository produtoRepository;

    // Repositório utilizado para consultar as categorias.
    @Autowired
    private CategoriaRepository categoriaRepository;

    // POST - Cadastra um novo produto.
    public Produto salvar(Produto produto) {

        // Verifica os dados antes de salvar.
        validarProduto(produto);
        String nome = produto.getNome().trim();

        // Impede o cadastro de nomes duplicados.
        if (produtoRepository.existsByNomeIgnoreCase(nome)) {throw new IllegalArgumentException
        	("Já existe um produto com esse nome.");
        }

        // Verifica se a categoria existe no banco.
        Categoria categoria = buscarCategoria(produto.getCategoria().getId());

        // Organiza os dados antes do cadastro.
        produto.setId(null);
        produto.setNome(nome);
        produto.setCategoria(categoria);

        return produtoRepository.save(produto);
    }

    // GET - Lista todos os produtos.
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    // GET - Busca um produto pelo ID.
    public Produto buscarPorId(Long id) {

        return produtoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Produto não encontrado."));
    }

    // GET - Lista produtos de determinada categoria.
    public List<Produto> listarPorCategoria(Long categoriaId) {

        // Confirma que a categoria informada existe.
        buscarCategoria(categoriaId);

        return produtoRepository.findByCategoriaId(categoriaId);
    }

    // PUT - Atualiza um produto existente.
    public Produto atualizar(Long id, Produto dadosAtualizados) {

        // Verifica se o produto existe.
        Produto produto = buscarPorId(id);

        // Valida os novos dados.
        validarProduto(dadosAtualizados);
        String novoNome = dadosAtualizados.getNome().trim();

        // Impede utilizar o nome de outro produto.
        if (produtoRepository.existsByNomeIgnoreCaseAndIdNot(novoNome, id)) {
        	throw new IllegalArgumentException("Já existe outro produto com esse nome.");
        }

        // Confirma que a nova categoria existe.
        Categoria categoria = buscarCategoria(dadosAtualizados.getCategoria().getId());

        // Atualiza somente os campos permitidos.
        produto.setNome(novoNome);
        produto.setDescricao(dadosAtualizados.getDescricao());
        produto.setPreco(dadosAtualizados.getPreco());
        produto.setCategoria(categoria);

        return produtoRepository.save(produto);
    }

    // DELETE - Exclui um produto pelo ID.
    public void excluir(Long id) {

        // Verifica a existência antes da exclusão.
        Produto produto = buscarPorId(id);
        produtoRepository.delete(produto);
    }

    // Valida os dados obrigatórios do produto.
    private void validarProduto(Produto produto) {

        if (produto == null) {throw new IllegalArgumentException("Os dados do produto são obrigatórios.");
        }

        // Nome não pode ser nulo ou conter apenas espaços.
        if (produto.getNome() == null ||produto.getNome().isBlank()) {
        		throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }

        // Preço precisa existir e ser maior que zero.
        if (produto.getPreco() == null ||produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            	throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }

        // Categoria deve ser informada com um ID.
        if (produto.getCategoria() == null || produto.getCategoria().getId() == null) {
            	throw new IllegalArgumentException("Informe uma categoria válida.");
        }
    }

    // Consulta a categoria cadastrada no banco de dados.
    private Categoria buscarCategoria(Long id) {

        return categoriaRepository.findById(id).orElseThrow(() -> new NoSuchElementException
        		("Categoria não encontrada."));
    }
}
