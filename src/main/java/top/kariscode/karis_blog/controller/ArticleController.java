package top.kariscode.karis_blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.kariscode.karis_blog.entity.Article;
import top.kariscode.karis_blog.service.ArticleService;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/api/articles")
    public List<Article> getArticle() {
       return articleService.findAll();
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<Article> findById(@PathVariable String id){
        Article article = articleService.findById(id);
        if(article==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(article);
    }
}
