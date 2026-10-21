
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
import org.springframework.web.bind.annotation.RestController;

import com.produtos.gerenciador.model.Categoria;
import com.produtos.gerenciador.service.CategoriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // POST - Cadastra uma nova categoria.
    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Categoria categoria) {

        try {
            Categoria novaCategoria = categoriaService.salvar(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);

        } catch (IllegalArgumentException erro) {
            return criarRespostaErro(HttpStatus.BAD_REQUEST, erro.getMessage());
        }
    }

    // GET - Lista todas as categorias cadastradas.
    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    // GET - Busca uma categoria pelo ID.
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {

        try {
            return ResponseEntity.ok(categoriaService.buscarPorId(id));

        } catch (NoSuchElementException erro) {
            return criarRespostaErro(HttpStatus.NOT_FOUND, erro.getMessage());
        }
    }

    // PUT - Atualiza uma categoria existente.
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

    // DELETE - Exclui uma categoria pelo ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {

        try {
            categoriaService.excluir(id);

            return ResponseEntity.ok("Categoria excluída com sucesso!");

        } catch (NoSuchElementException erro) {
            return criarRespostaErro(HttpStatus.NOT_FOUND, erro.getMessage());
        }
    }

    // Trata os erros gerados pela anotação @Valid.
    // Exemplo: nome vazio, nulo ou contendo apenas espaços.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> tratarValidacao(MethodArgumentNotValidException erro) {

        String mensagem = erro.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        return criarRespostaErro(HttpStatus.BAD_REQUEST, mensagem);
    }

    // Monta uma resposta de erro em formato JSON.
    // Este método evita repetir a criação do JSON em vários lugares.
    private ResponseEntity<?> criarRespostaErro(HttpStatus status, String mensagem) {

        Map<String, String> resposta = new HashMap<>();

        resposta.put("erro", mensagem);

        return ResponseEntity.status(status).body(resposta);
    }
}
