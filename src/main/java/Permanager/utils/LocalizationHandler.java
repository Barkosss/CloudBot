package Permanager.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalizationHandler {

    public enum Language {
        RUSSIAN("ru"),
        ENGLISH("en");

        private final String language;

        Language(String language) {
            this.language = language;
        }

        public String getLang() {
            return language;
        }
    }

    public String get(String path, Language language) {
        JSONHandler jsonHandler = new JSONHandler();

        String languagePath = String.format("language/content_%s.json", language.getLang());
        if (jsonHandler.check(languagePath, path)) {
            return (String) jsonHandler.read(languagePath, path);
        }
        return path;
    }

    public String get(String path, List<String> replaces, Language language) {
        ValidateService validate = new ValidateService();

        String content = get(path, language);
        List<String> findReplaces = parseReplace(content);

        int indexReplace = 0;
        for (String word : findReplaces) {
            if (word.charAt(1) == 'i') { // Если число
                Optional<Integer> isInteger = validate.isValidInteger(replaces.get(indexReplace));

                if (isInteger.isPresent()) {
                    content = content.replaceFirst(word, replaces.get(indexReplace));
                    indexReplace++;
                } else {
                    //logger.error("Replace message expected number");
                }
            } else if (word.charAt(1) == 'd') { // Если дата
                Optional<LocalDate> isLocalDate = validate.isValidDate(replaces.get(indexReplace));

                if (isLocalDate.isPresent()) {
                    content = content.replaceFirst(word, replaces.get(indexReplace));
                    indexReplace++;
                } else {
                    //logger.error("Replace message expected LocalDate");
                }
            } else { // Другие типы
                content = content.replaceFirst(word, replaces.get(indexReplace));
                indexReplace++;
            }
        }

        return content;
    }

    private List<String> parseReplace(String message) {
        List<String> array = new ArrayList<>();

        for (String word : message.split(" ")) {
            // Проверяем, что слово начинается и заканчивается на % (Спец символ)
            Pattern pattern = Pattern.compile("%[a-z]?[A-Z_]+%");
            Matcher matcher = pattern.matcher(word);
            if (matcher.find()) {
                array.add(matcher.group());
            }
        }

        return array;
    }
}
