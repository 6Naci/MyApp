import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

interface DataConnection {
    String INPUT_PATH = "src/1.txt";
    String OUTPUT_PATH = "src/statistika.txt";

    int loadData(MyApp myApp) throws IOException;

    void saveData(int year, int qq) throws IOException;
}

public class MyApp implements DataConnection {

    /**
     * @param total общая сумма значений по году.
     * @param number количество строк в заданной таблице (документе)
     */
    private String year;
    private int total;
    private static int number = 0;
    private static int index = 0;
    protected static int startYear = 1990;
    protected static int endYear = 2020;

    public MyApp(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static MyApp create(String year) {
        return new MyApp(year);
    }

    public static void main(String[] args) {
        try {
            System.out.println("app v.1.13");
            for (int i = startYear; i < endYear; i++) {
                MyApp myApp = create(i + "");
                myApp.setTotal(myApp.loadData(myApp));
                double qq = myApp.getTotal() > 0 ? (double) myApp.getTotal() / (double) number : 0;
                if (qq > 0) System.out.println(i + " " + qq);
                myApp.saveData(i, (int) qq);
            }
            System.out.println("Success");
        } catch (Exception e) {
            System.out.println("Error!");
        }
    }

    /**
     * Получение данных из документа в виде списка строк и дальнейший поиск совпадений по текущему объекту
     *
     * @param myApp текущий объект приложения.
     * @return общая сумма значений из выбранного столбца по текущему объекту
     */
    public int loadData(MyApp myApp) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH), StandardCharsets.UTF_8);
        number = lines.size();
        for (String line : lines) {
            String[] columns = line.split(" ");
            if (columns[2].contains(myApp.getYear())) {
                myApp.setTotal(myApp.getTotal() + Integer.parseInt(columns[3]));
                ;
            }
        }
        return myApp.getTotal();
    }

    /**
     * Запись полученных данных по текущему объекту
     *
     * @param year текущий год
     * @param qq   видоизмененная общая сумма
     */
    public void saveData(int year, int qq) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(new File(OUTPUT_PATH), true)) {
            String s = index + " " + year + " " + qq + "\n";
            outputStream.write(s.getBytes());
            index++;
        }
    }
}