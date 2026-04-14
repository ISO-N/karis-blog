package top.kariscode.karis_blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.kariscode.karis_blog.entity.Article;
import top.kariscode.karis_blog.entity.ArticleStatus;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, String> {

    List<Article> findByStatus(ArticleStatus status);

    Optional<Article> findByIdAndStatus(String id, ArticleStatus status);
}
