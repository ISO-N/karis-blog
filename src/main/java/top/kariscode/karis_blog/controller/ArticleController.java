package top.kariscode.karis_blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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

    // 创建文章
    @PostMapping("/api/admin/articles")
    public ResponseEntity<Article> createArticle(@RequestBody Article article){

        if(article == null||article.getTitle() ==null||article.getSummary()==null||article.getContent()==null||article.getTitle().isBlank()||article.getSummary().isBlank()||article.getContent().isBlank()){
            return ResponseEntity.badRequest().build();
        }
        Article created = articleService.create(article.getTitle(),article.getSummary(),article.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
