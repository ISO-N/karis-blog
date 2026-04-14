package top.kariscode.karis_blog.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import top.kariscode.karis_blog.dto.ApiResponse;
import top.kariscode.karis_blog.dto.CreateArticleRequest;
import top.kariscode.karis_blog.dto.UpdateArticleRequest;
import top.kariscode.karis_blog.entity.Article;
import top.kariscode.karis_blog.exception.NotFoundException;
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
        return ResponseEntity.ok(requireArticle(articleService.findPublishedById(id)));
    }

    @GetMapping("/api/admin/articles")
    public ResponseEntity<ApiResponse<List<Article>>> getAllArticle(){
        return ResponseEntity.ok(ApiResponse.success(articleService.findAll()));
    }

    @GetMapping("/api/admin/articles/{id}")
    public ResponseEntity<ApiResponse<Article>> getArticle (@PathVariable String id){
        return ResponseEntity.ok(ApiResponse.success(requireArticle(articleService.findById(id))));
    }

    // 创建文章
    @PostMapping("/api/admin/articles")
    public ResponseEntity<ApiResponse<Article>> createArticle(@Valid @RequestBody CreateArticleRequest request){
        Article created = articleService.create(request.getTitle(),request.getSummary(),request.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(created));
    }

    // 编辑文章
    @PutMapping("/api/admin/articles/{id}")
    ResponseEntity<ApiResponse<Article>> updateArticle(@PathVariable String id,@Valid @RequestBody UpdateArticleRequest request){
        Article updated = articleService.update(id,request.getTitle(),request.getSummary(), request.getContent());
        return ResponseEntity.ok(ApiResponse.success(requireArticle(updated))); //200
    }

    // 删除文章
    @DeleteMapping("/api/admin/articles/{id}")
    ResponseEntity<Boolean> deleteById(@PathVariable String id){
        if(articleService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        throw new NotFoundException("文章不存在");
    }

    @PatchMapping("/api/admin/articles/{id}/publish")
    ResponseEntity<ApiResponse<Article>> publish(@PathVariable String id){
        return ResponseEntity.ok(ApiResponse.success(requireArticle(articleService.publish(id))));
    }

    @PatchMapping("/api/admin/articles/{id}/unpublish")
    ResponseEntity<ApiResponse<Article>> unpublish(@PathVariable String id){
        return ResponseEntity.ok(ApiResponse.success(requireArticle(articleService.unpublish(id))));
    }

    private Article requireArticle(Article article) {
        if (article == null) {
            throw new NotFoundException("文章不存在");
        }
        return article;
    }
}
