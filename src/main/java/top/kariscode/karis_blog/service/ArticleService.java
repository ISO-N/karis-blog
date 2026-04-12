package top.kariscode.karis_blog.service;

import top.kariscode.karis_blog.entity.Article;

import java.util.*;

public interface ArticleService {
    List<Article> findAll();
    Article findById(String id);
    List<Article> findPublishedArticles();
    Article findPublishedById(String id);
    Article create(String title,String summary,String content);
    Article update(String id,String title,String summary,String content);
    boolean deleteById(String id);
    Article publish(String id);
    Article unpublish(String id);
}
