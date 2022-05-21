package guru.qa;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

public class SelenideFilesTest {

    @Test
    void selenideDownloadTest() throws Exception {
        Selenide.open("https://github.com/junit-team/junit5/blob/main/README.md");
        File downloadedFile = $("#raw-url").download();
        try (InputStream is = new FileInputStream(downloadedFile)) {
            assertThat(new String(is.readAllBytes(), StandardCharsets.UTF_8))
                    .contains("This repository is the home of the next generation of JUnit");
        }
    }

    @Test
    void uploadSelenideTest() {
        Selenide.open("https://the-internet.herokuapp.com/upload");
        $("input[type=file]")
//                .uploadFile(new File("C:\\Users\\amelin\\IdeaProjects\\lesson9-qa-guru-files\\src\\test\\resources\\files\\1.txt")) // Bad practice
                .uploadFromClasspath("files/1.txt");
        $("#file-submit").click();
        $("div.example").shouldHave(Condition.text("File uploaded!"));
        $("#uploaded-files").shouldHave(Condition.text("1.txt"));
    }
}
