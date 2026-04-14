package top.kariscode.karis_blog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import top.kariscode.karis_blog.entity.Article;
import top.kariscode.karis_blog.entity.ArticleStatus;
import top.kariscode.karis_blog.exception.GlobalExceptionHandler;
import top.kariscode.karis_blog.service.ArticleService;

import java.time.LocalDateTime;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ArticleService articleService;

    @InjectMocks
    private ArticleController articleController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(articleController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void publicDetailShouldReturn404WhenArticleIsDraft() throws Exception {
        when(articleService.findPublishedById("3")).thenReturn(null);

        mockMvc.perform(get("/api/articles/3"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(jsonPath("$.detail").value("文章不存在"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void adminDetailShouldReturn200WhenArticleIsDraft() throws Exception {
        when(articleService.findById("3")).thenReturn(buildArticle("3", ArticleStatus.DRAFT));

        mockMvc.perform(get("/api/admin/articles/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.id").value("3"))
                .andExpect(jsonPath("$.data.status").value("DRAFT"));
    }

    @Test
    void adminDetailShouldReturn404WhenArticleDoesNotExist() throws Exception {
        when(articleService.findById("999")).thenReturn(null);

        mockMvc.perform(get("/api/admin/articles/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("文章不存在"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void adminCreateShouldReturn201() throws Exception {
        when(articleService.create("新标题", "新摘要", "新正文"))
                .thenReturn(buildArticle("4", ArticleStatus.DRAFT));

        mockMvc.perform(post("/api/admin/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "新标题",
                                  "summary": "新摘要",
                                  "content": "新正文"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.message").value("created"))
                .andExpect(jsonPath("$.data.id").value("4"))
                .andExpect(jsonPath("$.data.status").value("DRAFT"));

        verify(articleService).create("新标题", "新摘要", "新正文");
    }

    @Test
    void adminUpdateShouldReturn200() throws Exception {
        when(articleService.update("1", "更新标题", "更新摘要", "更新正文"))
                .thenReturn(buildArticle("1", ArticleStatus.PUBLISHED));

        mockMvc.perform(put("/api/admin/articles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "更新标题",
                                  "summary": "更新摘要",
                                  "content": "更新正文"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.id").value("1"));

        verify(articleService).update("1", "更新标题", "更新摘要", "更新正文");
    }

    @Test
    void adminDeleteShouldReturn204() throws Exception {
        when(articleService.deleteById("1")).thenReturn(true);

        mockMvc.perform(delete("/api/admin/articles/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(articleService).deleteById("1");
    }

    @Test
    void adminPublishShouldReturn200() throws Exception {
        when(articleService.publish("3")).thenReturn(buildArticle("3", ArticleStatus.PUBLISHED));

        mockMvc.perform(patch("/api/admin/articles/3/publish"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.id").value("3"))
                .andExpect(jsonPath("$.data.status").value("PUBLISHED"));

        verify(articleService).publish("3");
    }

    @Test
    void adminUnpublishShouldReturn200() throws Exception {
        when(articleService.unpublish("1")).thenReturn(buildArticle("1", ArticleStatus.DRAFT));

        mockMvc.perform(patch("/api/admin/articles/1/unpublish"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.status").value("DRAFT"));

        verify(articleService).unpublish("1");
    }

    private Article buildArticle(String id, ArticleStatus status) {
        Article article = new Article();
        article.setId(id);
        article.setTitle("标题" + id);
        article.setSummary("摘要" + id);
        article.setContent("正文" + id);
        article.setStatus(status);
        article.setCreatedAt(LocalDateTime.of(2026, 4, 1, 10, 10, 30));
        article.setUpdatedAt(LocalDateTime.of(2026, 4, 1, 10, 10, 30));
        return article;
    }
}
