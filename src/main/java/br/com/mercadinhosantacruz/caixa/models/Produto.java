package br.com.mercadinhosantacruz.caixa.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.jdbc.core.JdbcTemplate;

public class Produto {
    
    private int id;
    private String nome;
    private int qtd;
    private float preco;

    public Produto(String nome, int qtd, float preco) {
        this.nome = nome;
        this.qtd = qtd;
        this.preco = preco;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getQtd() {
        return qtd;
    }

    public float getPreco() {
        return preco;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }
    
    public void salvar(JdbcTemplate jdbc) {
        if (id > 0) {
            jdbc.update(
            "UPDATE produtos SET nome = ?, "
            + "preco = ?, qtd = ? "
            + "WHERE id = ?;", (ps) -> {
                ps.setString(1, nome);
                ps.setFloat(2, preco);
                ps.setInt(3, qtd);
                ps.setInt(4, id);
            });
        } else {
            jdbc.update(
            "INSERT INTO produtos (nome, preco, qtd) " +
            "VALUES (?, ?, ?);", (ps) -> {
                ps.setString(1, nome);
                ps.setFloat(2, preco);
                ps.setInt(3, qtd);
            });
        }
    }
    
    public static List<Produto> listar(JdbcTemplate
            jdbc) {
        List<Produto> produtos =
                new ArrayList<>();
        jdbc.query("SELECT * FROM produtos;",
        (rs) -> {
            do {
                Produto p = new Produto(
                        rs.getString("nome"),
                        rs.getInt("qtd"),
                        rs.getFloat("preco"));
                p.id = rs.getInt("id");
                produtos.add(p);
            } while(rs.next());
        });
        return produtos;
    }
    
    public static void remover(int id,
            JdbcTemplate jdbc) {
        jdbc.update("DELETE FROM produtos "
        + "WHERE id = ?", (ps) -> {
            ps.setInt(1, id);
        });
    }
    
    public static Produto buscar(int id,
            JdbcTemplate jdbc) {
        AtomicReference<Produto> produto
                = new AtomicReference<>();
        jdbc.query("SELECT * FROM produtos "
        + "WHERE id = ?;", (ps) -> {
            ps.setInt(1, id);
        }, (rs) -> {
            Produto p = new Produto(
                    rs.getString("nome"), 
                    rs.getInt("qtd"),
                    rs.getFloat("preco"));
            p.id = rs.getInt("id");
            produto.set(p);
        });
        return produto.get();
    }
    
}
