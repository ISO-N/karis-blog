package top.kariscode.karis_blog.service;

import top.kariscode.karis_blog.entity.Article;

import java.util.*;

public interface ArticleService {
    List<Article> findAll();
    Article findById(String id);
}
