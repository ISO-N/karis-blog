package top.kariscode.karis_blog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import top.kariscode.karis_blog.entity.Article;
import top.kariscode.karis_blog.entity.ArticleStatus;
import top.kariscode.karis_blog.service.impl.MemoryArticleService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemoryArticleServiceTest {

    private MemoryArticleService articleService;

    @BeforeEach
    void setUp() {
        articleService = new MemoryArticleService();
    }

    @Test
    void createShouldAddDraftArticle() {
        int oldSize = articleService.findAll().size();

        Article created = articleService.create("新标题", "新摘要", "新正文");

        assertAll(
                () -> assertEquals(oldSize + 1, articleService.findAll().size()),
                () -> assertEquals("4", created.getId()),
                () -> assertEquals(ArticleStatus.DRAFT, created.getStatus()),
                () -> assertEquals("新标题", created.getTitle()),
                () -> assertEquals("新摘要", created.getSummary()),
                () -> assertEquals("新正文", created.getContent()),
                () -> assertNotNull(created.getCreatedAt()),
                () -> assertNotNull(created.getUpdatedAt()),
                () -> assertEquals(created.getCreatedAt(), created.getUpdatedAt())
        );
    }

    @Test
    void findPublishedArticlesShouldOnlyReturnPublishedArticles() {
        List<Article> publishedArticles = articleService.findPublishedArticles();

        assertAll(
                () -> assertEquals(2, publishedArticles.size()),
                () -> assertTrue(publishedArticles.stream()
                        .allMatch(article -> article.getStatus() == ArticleStatus.PUBLISHED)),
                () -> assertTrue(publishedArticles.stream().anyMatch(article -> "1".equals(article.getId()))),
                () -> assertTrue(publishedArticles.stream().anyMatch(article -> "2".equals(article.getId()))),
                () -> assertTrue(publishedArticles.stream().noneMatch(article -> "3".equals(article.getId())))
        );
    }

    @Test
    void findPublishedByIdShouldNotBypassStatusFilter() {
        assertAll(
                () -> assertNotNull(articleService.findPublishedById("1")),
                () -> assertNull(articleService.findPublishedById("3")),
                () -> assertNull(articleService.findPublishedById(null)),
                () -> assertNull(articleService.findPublishedById("999"))
        );
    }

    @Test
    void updateShouldReturnNullWhenIdIsInvalid() {
        assertAll(
                () -> assertNull(articleService.update(null, "标题", "摘要", "正文")),
                () -> assertNull(articleService.update("999", "标题", "摘要", "正文"))
        );
    }

    @Test
    void updateShouldModifyFieldsAndKeepCreatedAt() {
        Article beforeUpdate = articleService.findById("1");
        LocalDateTime createdAt = beforeUpdate.getCreatedAt();
        LocalDateTime oldUpdatedAt = beforeUpdate.getUpdatedAt();

        Article updated = articleService.update("1", "更新标题", "更新摘要", "更新正文");

        assertAll(
                () -> assertNotNull(updated),
                () -> assertEquals("更新标题", updated.getTitle()),
                () -> assertEquals("更新摘要", updated.getSummary()),
                () -> assertEquals("更新正文", updated.getContent()),
                () -> assertEquals(createdAt, updated.getCreatedAt()),
                () -> assertTrue(updated.getUpdatedAt().isAfter(oldUpdatedAt)),
                () -> assertSame(updated, articleService.findById("1"))
        );
    }

    @Test
    void deleteByIdShouldHandleExistingAndMissingArticle() {
        assertAll(
                () -> assertTrue(articleService.deleteById("1")),
                () -> assertNull(articleService.findById("1")),
                () -> assertFalse(articleService.deleteById("1")),
                () -> assertFalse(articleService.deleteById("999"))
        );
    }

    @Test
    void publishShouldChangeStatusAndMakeDraftVisible() {
        Article draftArticle = articleService.findById("3");
        LocalDateTime oldUpdatedAt = draftArticle.getUpdatedAt();

        Article published = articleService.publish("3");

        assertAll(
                () -> assertNotNull(published),
                () -> assertEquals(ArticleStatus.PUBLISHED, published.getStatus()),
                () -> assertTrue(published.getUpdatedAt().isAfter(oldUpdatedAt)),
                () -> assertSame(published, articleService.findPublishedById("3")),
                () -> assertNull(articleService.publish("999"))
        );
    }

    @Test
    void unpublishShouldChangeStatusAndHideArticleFromPublic() {
        Article publishedArticle = articleService.findById("1");
        LocalDateTime oldUpdatedAt = publishedArticle.getUpdatedAt();

        Article draft = articleService.unpublish("1");

        assertAll(
                () -> assertNotNull(draft),
                () -> assertEquals(ArticleStatus.DRAFT, draft.getStatus()),
                () -> assertTrue(draft.getUpdatedAt().isAfter(oldUpdatedAt)),
                () -> assertNull(articleService.findPublishedById("1")),
                () -> assertNull(articleService.unpublish("999"))
        );
    }
}
