package ru.itis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import ru.itis.model.Article;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ArticleService {
    private List<Article> articles;

    public ArticleService(String filePath) {
        this.articles = loadArticlesFromJson(filePath);
    }

    private List<Article> loadArticlesFromJson(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Article.class);
            return mapper.readValue(new File(filePath), listType);
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Article> getArticles() {
        return articles;
    }
}
