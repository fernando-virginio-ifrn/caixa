package br.com.mercadinhosantacruz.caixa.controllers;

import br.com.mercadinhosantacruz.caixa.models.Produto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CaixaController {
    
    @GetMapping("/")
    public String index(Model model) {
        List<Produto> produtos =
                Produto.listar(jdbc);
        model.addAttribute("produtos",
                produtos);
        return "index";
    }
    
    @Autowired JdbcTemplate jdbc;
    
    @PostMapping("/adicionar")
    public String adicionar(String nome,
            int qtd, float preco) {
        Produto p = new Produto(nome, 
                qtd, preco);
        p.salvar(jdbc);
        return "redirect:/";
    }
    
    @PostMapping("/atualizar")
    public String atualizar(int id, 
            String nome, int qtd, float preco) {
        Produto p = Produto.buscar(id, jdbc);
        p.setNome(nome);
        p.setQtd(qtd);
        p.setPreco(preco);
        p.salvar(jdbc);
        return "redirect:/";
    }
    
    @GetMapping("/excluir")
    public String excluir(int id) {
        Produto.remover(id, jdbc);
        return "redirect:/";
    }
    
    @GetMapping("/editar")
    public String editar(int id, Model model) {
        List<Produto> produtos =
                Produto.listar(jdbc);
        Produto p = Produto.buscar(id, jdbc);
        model.addAttribute("produtos",
                produtos);
        model.addAttribute("p", p);
        return "index";
    }
    
}
