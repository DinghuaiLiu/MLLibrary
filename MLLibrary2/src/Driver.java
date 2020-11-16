import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Driver {

    public static void main(String[] args) throws FileNotFoundException {
        Random rand = new Random();
        ArrayList<DataPoint> trainList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            trainList.add(new DataPoint(rand.nextDouble(), rand.nextDouble(), "apple", "red"));
        }
        ArrayList<DataPoint> testList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            testList.add(new DataPoint(rand.nextDouble(), rand.nextDouble(), "apple", "red"));
            testList.add(new DataPoint(rand.nextDouble(), rand.nextDouble(), "banana", "yellow"));
        }

        DummyModel dummyCli = new DummyModel();
        dummyCli.train(trainList);  // start train data
        String result = dummyCli.test(testList);  // start test data

        System.out.println("model accuracy is: " + dummyCli.getAccuracy(testList));

        // GUI
        JFrame myFrame = new JFrame();
        myFrame.setTitle("MLLibrary");
        Container myPane = myFrame.getContentPane();
        myPane.setLayout(new GridLayout(4, 2));

        JButton startTrainBtn = new JButton("start train");
        addActionListener(startTrainBtn, "success.", false);
        myPane.add(startTrainBtn);

        JButton startTestBtn = new JButton("start test");
        addActionListener(startTestBtn, result, false);
        myPane.add(startTestBtn);

        JButton accyBtn = new JButton("get accuracy");
        addActionListener(accyBtn, String.valueOf(dummyCli.getAccuracy(testList)), true);
        myPane.add(accyBtn);

        JButton precBtn = new JButton("get getPrecision");
        myPane.add(precBtn);
        addActionListener(precBtn, String.valueOf(dummyCli.getPrecision(testList)), true);

        ArrayList<DataPoint> data=new ArrayList<DataPoint>();
        try (Scanner scanner = new Scanner(new File("titanic.csv"));) {
            int cnt=0;
            while (scanner.hasNextLine()) {
                List<String> records = getRecordFromLine(scanner.nextLine());
                if(cnt==0) {
                    cnt++;
                    continue;
                }
                String age=records.get(records.size()-1);
                String fare=records.get(records.size()-2);
                if(age.length()!=0&&fare.length()!=0&&(isInteger(age)||isDouble(age))&&(isInteger(fare)||isDouble(fare))){
                    double f1=Double.parseDouble(age);
                    double f2=Double.parseDouble(fare);
                    DataPoint p=new DataPoint(f1,f2,records.get(1));
                    data.add(p);
                }
            } }

        ArrayList<DataPoint> train = new ArrayList<DataPoint>();
        ArrayList<DataPoint> test = new ArrayList<DataPoint>();
        for(int i=0;i<data.size();i++){
            double randNum = rand.nextDouble();
            if (randNum < 0.9) {
                train.add(data.get(i));
            } else {
                test.add(data.get(i));
            }
        }

        KNNModel knnModel=new KNNModel(3,train);
        knnModel.train(train);

        JButton Knn_startTrainBtn = new JButton("KNN start train");
        addActionListener(Knn_startTrainBtn, "success.", false);
        myPane.add(Knn_startTrainBtn);

        knnModel.test(test);

        JButton Knn_startTestBtn = new JButton("KNN start test");
        addActionListener(Knn_startTestBtn, result, false);
        myPane.add(Knn_startTestBtn);

        JButton Knn_accyBtn = new JButton("knn get accuracy");
        addActionListener(Knn_accyBtn, String.valueOf(knnModel.getAccuracy(testList)), true);
        myPane.add(Knn_accyBtn);

        JButton Knn_precBtn = new JButton("knn get getPrecision");
        myPane.add(Knn_precBtn);
        addActionListener(Knn_precBtn, String.valueOf(knnModel.getPrecision(testList)), true);

        myFrame.pack();
        myFrame.setVisible(true);

    }

    private static java.util.List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    private static void addActionListener(JButton button, String msg, Boolean enabled) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.setEnabled(enabled);
                JOptionPane.showMessageDialog(null, msg);
            }
        });
    }
}
