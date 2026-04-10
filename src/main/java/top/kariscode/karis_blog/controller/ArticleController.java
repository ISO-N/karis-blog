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
    public List<Article> getPublishedArticle() {
       return articleService.findPublishedArticles();
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<Article> findPublishedById(@PathVariable String id){
        Article article = articleService.findPublishedById(id);
        if(article==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(article);
    }

    @GetMapping("/api/admin/articles")
    public List<Article> getAllArticle(){
        return articleService.findAll();
    }

    @GetMapping("/api/admin/articles/{id}")
    public ResponseEntity<Article> getArticle (@PathVariable String id){
        Article article = articleService.findById(id);
        if(article==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(article);
    }
}
