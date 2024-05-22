package ru.itis;

import ru.itis.model.Article;
import ru.itis.service.ArticleService;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        ArticleService articleService = new ArticleService("src/main/resources/jsons/metadata.json");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.err.println("Введите команду (authors, titles, all, exit): ");
            String command = scanner.nextLine();

            switch (command) {
                case "authors":
                    printAuthors(articleService.getArticles());
                    break;
                case "titles":
                    printTitles(articleService.getArticles());
                    break;
                case "all":
                    printAll(articleService.getArticles());
                    break;
                case "exit":
                    System.out.println("Выход из программы.");
                    return;
                default:
                    System.out.println("Неизвестная команда. Попробуйте снова.");
            }
        }
    }

    private static void printAuthors(List<Article> articles) {
        List<String> authors = articles.stream()
                .map(Article::getAuthor)
                .distinct()
                .collect(Collectors.toList());
        authors.forEach(System.out::println);
    }

    private static void printTitles(List<Article> articles) {
        List<String> titles = articles.stream()
                .map(Article::getTitle)
                .collect(Collectors.toList());
        titles.forEach(System.out::println);
    }

    private static void printAll(List<Article> articles) {
        articles.forEach(article -> {
            System.out.println("Author(s): " + article.getAuthor());
            System.out.println("Title: " + article.getTitle());
            System.out.println();
        });
    }
}

