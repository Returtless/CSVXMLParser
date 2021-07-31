import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        final String fileName = "data.csv";
        final String xmlFileName = "data.xml";
        final String jsonFileName = "data.json";
        final String jsonXmlFileName = "data1.json";
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(jsonXmlFileName, json);

        list = parseXML(xmlFileName);
        json = listToJson(list);
        writeString(jsonFileName, json);
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> staff = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return staff;
    }

    public static List<Employee> parseXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> staff = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));
            Node root = doc.getDocumentElement();
            read(root, staff);

        return staff;
    }

    private static void read(Node node, List<Employee> staff) {
        NodeList nodeList = node.getChildNodes();
        boolean isEmployee = "employee".equals(node.getNodeName());
        Employee employee = isEmployee ? new Employee() : null;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);
            if (Node.ELEMENT_NODE == childNode.getNodeType()) {
                Element element = (Element) childNode;
                if (isEmployee) {
                    employee.setAttribute(element.getNodeName(), element.getTextContent());
                }
                read(childNode, staff);
            }
        }
        if (employee != null) {
            staff.add(employee);
        }
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        return list.isEmpty() ? "" : gson.toJson(list, new TypeToken<List<Employee>>() {
        }.getType());
    }

    public static void writeString(String fileName, String string) {
        File tempFile = new File(fileName);
        try {
            tempFile.createNewFile();
        } catch (IOException ex) {
            System.out.println("Ошибка создания файла");
        }
        if (tempFile.canWrite()) {
            try (FileWriter fileWriter = new FileWriter(tempFile)) {
                fileWriter.write(string);
            } catch (IOException e) {
                System.out.println("Ошибка записи в файл");
            }
        }
    }
}
