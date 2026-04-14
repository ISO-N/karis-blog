package top.kariscode.karis_blog.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import top.kariscode.karis_blog.entity.Article;
import top.kariscode.karis_blog.entity.ArticleStatus;
import top.kariscode.karis_blog.repository.ArticleRepository;
import top.kariscode.karis_blog.service.ArticleService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class DbArticleService implements ArticleService {

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "createdAt", "id");

    private final ArticleRepository articleRepository;

    public DbArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll(DEFAULT_SORT);
    }

    @Override
    public Article findById(String id) {
        if (id == null) {
            return null;
        }
        return articleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Article> findPublishedArticles() {
        return articleRepository.findByStatus(ArticleStatus.PUBLISHED)
                .stream()
                .sorted(Comparator.comparing(Article::getCreatedAt).thenComparing(Article::getId))
                .toList();
    }

    @Override
    public Article findPublishedById(String id) {
        if (id == null) {
            return null;
        }
        return articleRepository.findByIdAndStatus(id, ArticleStatus.PUBLISHED).orElse(null);
    }

    @Override
    public Article create(String title, String summary, String content) {
        LocalDateTime now = LocalDateTime.now();

        Article article = new Article();
        article.setId(nextId());
        article.setTitle(title);
        article.setSummary(summary);
        article.setContent(content);
        article.setStatus(ArticleStatus.DRAFT);
        article.setCreatedAt(now);
        article.setUpdatedAt(now);

        return articleRepository.save(article);
    }

    @Override
    public Article update(String id, String title, String summary, String content) {
        Article article = findById(id);
        if (article == null) {
            return null;
        }

        article.setTitle(title);
        article.setSummary(summary);
        article.setContent(content);
        article.setUpdatedAt(LocalDateTime.now());

        return articleRepository.save(article);
    }

    @Override
    public boolean deleteById(String id) {
        if (findById(id) == null) {
            return false;
        }

        articleRepository.deleteById(id);
        return true;
    }

    @Override
    public Article publish(String id) {
        Article article = findById(id);
        if (article == null) {
            return null;
        }

        article.setStatus(ArticleStatus.PUBLISHED);
        article.setUpdatedAt(LocalDateTime.now());

        return articleRepository.save(article);
    }

    @Override
    public Article unpublish(String id) {
        Article article = findById(id);
        if (article == null) {
            return null;
        }

        article.setStatus(ArticleStatus.DRAFT);
        article.setUpdatedAt(LocalDateTime.now());

        return articleRepository.save(article);
    }

    private String nextId() {
        return articleRepository.findAll().stream()
                .map(Article::getId)
                .map(this::toInt)
                .max(Integer::compareTo)
                .map(maxId -> String.valueOf(maxId + 1))
                .orElse("1");
    }

    private Integer toInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
