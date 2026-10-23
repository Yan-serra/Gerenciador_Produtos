
package com.produtos.gerenciador.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.produtos.gerenciador.model.Categoria;
import com.produtos.gerenciador.service.CategoriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // Cadastra uma categoria.
    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Categoria categoria) {

        try {
            Categoria novaCategoria = categoriaService.salvar(categoria);

            return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);

        } catch (IllegalArgumentException erro) {
            return criarRespostaErro(HttpStatus.BAD_REQUEST, erro.getMessage());
        }
    }

    // Lista todas as categorias.
    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    // Busca uma categoria pelo ID.
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {

        try {
            return ResponseEntity.ok(categoriaService.buscarPorId(id));

        } catch (NoSuchElementException erro) {
            return criarRespostaErro(HttpStatus.NOT_FOUND, erro.getMessage());
        }
    }

    // Atualiza uma categoria.
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,@Valid @RequestBody Categoria categoria) {

        try {
            Categoria atualizada = categoriaService.atualizar(id, categoria);

            return ResponseEntity.ok(atualizada);

        } catch (NoSuchElementException erro) {
            return criarRespostaErro(HttpStatus.NOT_FOUND, erro.getMessage());

        } catch (IllegalArgumentException erro) {
            return criarRespostaErro(HttpStatus.BAD_REQUEST, erro.getMessage());
        }
    }

    // Exclui uma categoria sem produtos vinculados.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {

        try {
            categoriaService.excluir(id);

            return ResponseEntity.ok("Categoria excluída com sucesso!");

        } catch (NoSuchElementException erro) {
            return criarRespostaErro(HttpStatus.NOT_FOUND, erro.getMessage());

        } catch (IllegalArgumentException erro) {
            return criarRespostaErro(HttpStatus.BAD_REQUEST, erro.getMessage());
        }
    }

    // Trata campos inválidos.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> tratarValidacao(MethodArgumentNotValidException erro) {

        String mensagem = erro.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return criarRespostaErro(HttpStatus.BAD_REQUEST, mensagem);
    }

    // Trata JSON incompleto ou malformado.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> tratarJsonInvalido(
            HttpMessageNotReadableException erro) {

        return criarRespostaErro(
                HttpStatus.BAD_REQUEST,
                "JSON inválido. Verifique os dados enviados.");
    }

    // Cria uma resposta de erro em JSON.
    private ResponseEntity<?> criarRespostaErro(HttpStatus status, String mensagem) {

        Map<String, String> resposta = new HashMap<>();
        resposta.put("erro", mensagem);

        return ResponseEntity.status(status).body(resposta);
    }
}