
package com.produtos.gerenciador.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.produtos.gerenciador.model.Produto;
import com.produtos.gerenciador.service.ProdutoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    // Permite utilizar os métodos do ProdutoService.
    @Autowired
    private ProdutoService produtoService;

    // POST - Cadastra um novo produto.
    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Produto produto) {

        try {
            Produto novoProduto = produtoService.salvar(produto);

            return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);

        } catch (IllegalArgumentException erro) {
            return criarRespostaErro(HttpStatus.BAD_REQUEST, erro.getMessage());

        } catch (NoSuchElementException erro) {
            return criarRespostaErro(HttpStatus.NOT_FOUND, erro.getMessage());
        }
    }

    // GET - Lista todos os produtos.
    @GetMapping
    public ResponseEntity<List<Produto>> listar() {

        return ResponseEntity.ok(produtoService.listarTodos());
    }

    // GET - Busca um produto pelo identificador.
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {

        try {
            return ResponseEntity.ok(produtoService.buscarPorId(id));

        } catch (NoSuchElementException erro) {
            return criarRespostaErro(HttpStatus.NOT_FOUND, erro.getMessage());
        }
    }

    // GET - Filtra produtos pelo ID da categoria.
    @GetMapping("/categoria")
    public ResponseEntity<?> listarPorCategoria(@RequestParam Long categoriaId) {

        try {
            return ResponseEntity.ok(
                    produtoService.listarPorCategoria(categoriaId));

        } catch (NoSuchElementException erro) {
            return criarRespostaErro(HttpStatus.NOT_FOUND, erro.getMessage());
        }
    }

    // PUT - Atualiza um produto existente.
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,@Valid @RequestBody Produto produto) {

        try {
            Produto atualizado = produtoService.atualizar(id, produto);

            return ResponseEntity.ok(atualizado);

        } catch (IllegalArgumentException erro) {
            return criarRespostaErro(HttpStatus.BAD_REQUEST, erro.getMessage());

        } catch (NoSuchElementException erro) {
            return criarRespostaErro(HttpStatus.NOT_FOUND, erro.getMessage());
        }
    }

    // DELETE - Exclui um produto existente.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {

        try {
            produtoService.excluir(id);

            return ResponseEntity.ok("Produto excluído com sucesso!");

        } catch (NoSuchElementException erro) {
            return criarRespostaErro(HttpStatus.NOT_FOUND, erro.getMessage());
        }
    }

    // Trata erros por nome vazio ou preço nulo
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> tratarValidacao(MethodArgumentNotValidException erro) {

        // Recupera a mensagem da primeira validação inválida.
        String mensagem = erro.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        return criarRespostaErro(HttpStatus.BAD_REQUEST, mensagem);
    }

    // Cria uma resposta JSON padronizada para os erros.
    private ResponseEntity<?> criarRespostaErro(HttpStatus status, String mensagem) {
        Map<String, String> resposta = new HashMap<>();
        resposta.put("erro", mensagem);

        return ResponseEntity.status(status).body(resposta);
    }
}