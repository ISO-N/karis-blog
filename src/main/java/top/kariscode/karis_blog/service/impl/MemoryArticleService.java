package top.kariscode.karis_blog.service.impl;

import org.springframework.stereotype.Service;
import top.kariscode.karis_blog.entity.Article;
import top.kariscode.karis_blog.entity.ArticleStatus;
import top.kariscode.karis_blog.service.ArticleService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MemoryArticleService implements ArticleService {
    @Override
    public List<Article> findAll() {

        Article article1 = new Article();
        Article article2 = new Article();
        Article article3 = new Article();

        // article1
        article1.setId("1");
        article1.setTitle("1");
        article1.setSummary("1");
        article1.setContent("1");
        article1.setStatus(ArticleStatus.valueOf("PUBLISHED"));
        article1.setCreatedAt(LocalDateTime.of(2026,4,30,10,10,30));
        article1.setUpdatedAt(LocalDateTime.of(2026,4,30,10,10,30));

        // article2
        article2.setId("2");
        article2.setTitle("2");
        article2.setSummary("2");
        article2.setContent("2");
        article2.setStatus(ArticleStatus.valueOf("PUBLISHED"));
        article2.setCreatedAt(LocalDateTime.of(2026,4,30,10,10,30));
        article2.setUpdatedAt(LocalDateTime.of(2026,4,30,10,10,30));

        //article3
        article3.setId("3");
        article3.setTitle("3");
        article3.setSummary("3");
        article3.setContent("3");
        article3.setStatus(ArticleStatus.valueOf("DRAFT"));
        article3.setCreatedAt(LocalDateTime.of(2026,4,30,10,10,30));
        article3.setUpdatedAt(LocalDateTime.of(2026,4,30,10,10,30));

        List<Article> articles = new ArrayList<>();

        articles.add(article1);
        articles.add(article2);
        articles.add(article3);

        return articles;
    }

    @Override
    public Article findById(String id) {
        if(id == null) {
            return null;
        }

        List<Article> articles = findAll();

        for(Article article:articles){
            if(article.getId().equals(id)){
                return article;
            }
        }

        return null;
    }

    @Override
    public List<Article> findPublishedArticles(){
        List<Article> articles = findAll();
        List<Article> publishedArticles = new ArrayList<>();
        for(Article article:articles){
            if(article.getStatus().equals(ArticleStatus.PUBLISHED)){
                publishedArticles.add(article);
            }
        }
        return publishedArticles;
    }
}
