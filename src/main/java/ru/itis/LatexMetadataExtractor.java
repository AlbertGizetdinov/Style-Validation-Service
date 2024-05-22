package ru.itis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LatexMetadataExtractor {

    private static final Pattern TITLE_PATTERN = Pattern.compile("\\\\title\\{([^}]*)\\}");
    private static final Pattern TIT_PATTERN = Pattern.compile("\\\\tit\\{([^}]*)\\}");
    private static final Pattern AUTHOR_PATTERN = Pattern.compile("\\\\author\\{([^}]*)\\}");

    public static void main(String[] args) {
        String folderPath = "src/main/resources/documents";
        String jsonFilePath = "src/main/resources/jsons/metadata.json";

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode documentsArray = mapper.createArrayNode();

        try {
            Files.walk(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".tex"))
                    .forEach(path -> processFile(path, documentsArray, mapper));

            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath), documentsArray);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processFile(Path filePath, ArrayNode documentsArray, ObjectMapper mapper) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            String title = null;
            String author = null;

            while ((line = br.readLine()) != null) {
                if (title == null) {
                    Matcher titleMatcher = TITLE_PATTERN.matcher(line);
                    if (titleMatcher.find()) {
                        title = titleMatcher.group(1);
                    }
                }
                if (title == null) {
                    Matcher titleMatcher = TIT_PATTERN.matcher(line);
                    if (titleMatcher.find()) {
                        title = titleMatcher.group(1);
                    }
                }
                if (author == null) {
                    Matcher authorMatcher = AUTHOR_PATTERN.matcher(line);
                    if (authorMatcher.find()) {
                        author = authorMatcher.group(1);
                    }
                }
                if (title != null && author != null) {
                    break;
                }
            }

            if (title != null || author != null) {
                ObjectNode documentObject = mapper.createObjectNode();
                documentObject.put("path", filePath.toString());

                if (title != null) {
                    documentObject.put("doco:Title", title);
                }

                if (author != null) {
                    documentObject.put("doco:Author", author.replaceAll("\\\\,", ""));
                }

                documentsArray.add(documentObject);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

