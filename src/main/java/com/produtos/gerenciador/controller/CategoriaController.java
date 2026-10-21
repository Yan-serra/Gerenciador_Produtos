
package com.produtos.gerenciador.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.produtos.gerenciador.model.Categoria;
import com.produtos.gerenciador.service.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // POST - Cadastra uma categoria.
    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Categoria categoria) {

        try {Categoria novaCategoria = categoriaService.salvar(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);

        } catch (IllegalArgumentException erro) {
            return ResponseEntity.badRequest().body(erro.getMessage());
        }
    }

    // GET - Lista todas as categorias.
    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    // GET - Busca uma categoria pelo ID.
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {

        try {
            return ResponseEntity.ok(categoriaService.buscarPorId(id));

        } catch (java.util.NoSuchElementException erro) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro.getMessage());
        }
    }

    // PUT - Atualiza uma categoria existente.
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,@Valid @RequestBody Categoria categoria) {

        try {
            return ResponseEntity.ok(categoriaService.atualizar(id, categoria));

        } catch (java.util.NoSuchElementException erro) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro.getMessage());

        } catch (IllegalArgumentException erro) {
            return ResponseEntity.badRequest().body(erro.getMessage());
        }
    }

    // DELETE - Exclui uma categoria.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {

        try {
            categoriaService.excluir(id);
            return ResponseEntity.ok("Categoria excluída com sucesso!");

        } catch (java.util.NoSuchElementException erro) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro.getMessage());
        }
    }
}
