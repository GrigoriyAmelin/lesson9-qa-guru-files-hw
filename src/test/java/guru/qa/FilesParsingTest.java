package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Selenide;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selectors.byText;
import static org.assertj.core.api.Assertions.assertThat;

public class FilesParsingTest {

    ClassLoader classloader = getClass().getClassLoader();

    @Test
    void parsePdfTest() throws Exception {
        Selenide.open("https://junit.org/junit5/docs/current/user-guide/");
        File pdfDownload = Selenide.$(byText("PDF download")).download();
        PDF pdf = new PDF(pdfDownload);
        assertThat(pdf.author).contains("Marc Philipp");
    }

    @Test
    void parseXlsTest() throws Exception {
        Selenide.open("http://romashka2008.ru/price");
        File xlsDownload = Selenide.$(".site-main__inner a[href*='prajs_ot']").download();
        XLS xls = new XLS((xlsDownload));
        assertThat(xls.excel
                .getSheetAt(0)
                .getRow(11)
                .getCell(1)
                .getStringCellValue()).contains("Сахалинская обл, Южно-Сахалинск");
    }

    @Test
    void parseCsvTest() throws Exception {
        classloader.getResourceAsStream("files/Machine_readable_file_bdc_sf_2021_q4.csv");
        try (InputStream is = classloader.getResourceAsStream("files/Machine_readable_file_bdc_sf_2021_q4.csv");
             CSVReader reader = new CSVReader(new InputStreamReader(is))) {
            List<String[]> content = reader.readAll();
            assertThat(content.get(0)).contains(
                    "Series_reference",
                    "Period",
                    "Data_value",
                    "Suppressed",
                    "STATUS",
                    "UNITS",
                    "Magnitude",
                    "Subject",
                    "Group",
                    "Series_title_1",
                    "Series_title_2",
                    "Series_title_3",
                    "Series_title_4",
                    "Series_title_5");
        }
    }

    @Test
    void parseZipTest() throws Exception {
        try (InputStream is = classloader.getResourceAsStream("files/sample_zip_file.zip");
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                assertThat(entry.getName()).isEqualTo("7729314745_03214643000000017300_001_0521.txt");
            }
        }
    }
}
