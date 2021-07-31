import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        final String fileName = "data.csv";
        final String jsonFileName = "data.json";
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
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

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(list, new TypeToken<List<Employee>>() {
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
