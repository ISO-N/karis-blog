package top.kariscode.karis_blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kariscode.karis_blog.entity.Article;
import top.kariscode.karis_blog.service.ArticleService;

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
}
