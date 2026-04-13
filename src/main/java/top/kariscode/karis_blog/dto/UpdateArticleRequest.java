package top.kariscode.karis_blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateArticleRequest {

    @NotBlank
    @Size(max = 120)
    private String title;

    @NotBlank
    private String summary;

    @NotBlank
    private String content;

    public UpdateArticleRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
