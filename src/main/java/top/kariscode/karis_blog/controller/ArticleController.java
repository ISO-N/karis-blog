package top.kariscode.karis_blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import top.kariscode.karis_blog.dto.CreateArticleRequest;
import top.kariscode.karis_blog.dto.UpdateArticleRequest;
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
    public ResponseEntity<Article> createArticle(@RequestBody CreateArticleRequest request){

        if(isInvalidArticleInput(request == null ? null : request.getTitle(),
                request == null ? null : request.getSummary(),
                request == null ? null : request.getContent())){
            return ResponseEntity.badRequest().build();
        }
        Article created = articleService.create(request.getTitle(),request.getSummary(),request.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // 编辑文章
    @PutMapping("/api/admin/articles/{id}")
    ResponseEntity<Article> updateArticle(@PathVariable String id,@RequestBody UpdateArticleRequest request){
        if(isInvalidArticleInput(request == null ? null : request.getTitle(),
                request == null ? null : request.getSummary(),
                request == null ? null : request.getContent())){
            return ResponseEntity.badRequest().build(); //400
        }
        Article updated = articleService.update(id,request.getTitle(),request.getSummary(), request.getContent());
        if(updated==null){
            return ResponseEntity.notFound().build(); //404
        }
        return ResponseEntity.ok(updated); //200
    }

    // 删除文章
    @DeleteMapping("/api/admin/articles/{id}")
    ResponseEntity<Boolean> deleteById(@PathVariable String id){
        if(articleService.deleteById(id)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/api/admin/articles/{id}/publish")
    ResponseEntity<Article> publish(@PathVariable String id){
        Article article = articleService.publish(id);
        if(article==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(article);
    }

    @PatchMapping("/api/admin/articles/{id}/unpublish")
    ResponseEntity<Article> unpublish(@PathVariable String id){
        Article article = articleService.unpublish(id);
        if(article==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(article);
    }

    private boolean isInvalidArticleInput(String title, String summary, String content) {
        return title == null || summary == null || content == null
                || title.isBlank() || summary.isBlank() || content.isBlank();
    }
}
