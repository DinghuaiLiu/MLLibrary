
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author Ting.Wang
 * @date 2019-11-26
 */
public class FileUtil {
    private static Pattern INTEGER_PATTERN = Pattern.compile("^[-\\+]?[\\d]*$");
    private static Pattern DOUBLE_PATTERN = Pattern.compile("^[-\\+]?[.\\d]*$");


    public static ArrayList<DataPoint> loadFile() {
        ArrayList<DataPoint> data = new ArrayList<DataPoint>();
        try {
            Scanner scanner = new Scanner(new File("titanic.csv"));
            int cnt = 0;
            while (scanner.hasNextLine()) {
                List<String> records = getRecordFromLine(scanner.nextLine());
                if (cnt == 0) {
                    cnt++;
                    continue;
                }
                String age = records.get(records.size() - 2);
                String fare = records.get(records.size() - 1);
                if (age.length() != 0 && fare.length() != 0 && (isInteger(age) || isDouble(age)) && (isInteger(fare) || isDouble(fare))) {
                    double f1 = Double.parseDouble(age);
                    double f2 = Double.parseDouble(fare);
                    DataPoint p = new DataPoint(f1, f2, records.get(1), records.get(1));
                    data.add(p);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return data;
    }

    private static List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    private static boolean isInteger(String str) {
        return INTEGER_PATTERN.matcher(str).matches();
    }

    private static boolean isDouble(String str) {
        return DOUBLE_PATTERN.matcher(str).matches();
    }


}