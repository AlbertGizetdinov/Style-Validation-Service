package ru.itis.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Article {
    @JsonProperty("path")
    private String path;

    @JsonProperty("doco:Title")
    private String title;

    @JsonProperty("doco:Author")
    private String author;
}
