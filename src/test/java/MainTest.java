import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainTest {

    /**
     * Проверка существования файлов
     */
    @BeforeAll
    public static void testFilesIsExist() {
        final String fileName = "data.csv";
        final String xmlFileName = "data.xml";

        File csvFile = new File(fileName);
        File xmlFile = new File(xmlFileName);

        Assertions.assertTrue(csvFile.exists());
        Assertions.assertTrue(xmlFile.exists());
    }

    /**
     * В задании указано, что:
     * В резyльтате работы программы в корне проекта должен появиться файл data2.json с содержимым,
     * аналогичным json-файлу из предыдущей задачи.
     * Данный тест проверяет что json из csv совпадает с json из xml
     */
    @Test
    public void testCSVEqualsXML() throws IOException, SAXException, ParserConfigurationException {
        final String fileName = "data.csv";
        final String xmlFileName = "data.xml";

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        List<Employee> listCSV = Main.parseCSV(columnMapping, fileName);
        String jsonCSV = Main.listToJson(listCSV);

        List<Employee> listXML = Main.parseXML(xmlFileName);
        String jsonXML = Main.listToJson(listXML);

        Assertions.assertEquals(jsonXML, jsonCSV);
    }

    /**
     * Проверка метода парсинга ХМЛ файла на невозможность парсинга CSV
     */
    @Test
    public void testParseXMLWithCSVFile() {
        final String fileName = "data.csv";
        Assertions.assertThrows(SAXParseException.class, () -> {
            Main.parseXML(fileName);
        });
    }
}
