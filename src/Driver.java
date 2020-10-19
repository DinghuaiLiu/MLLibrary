import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class Driver {

    public static void main(String[] args) {
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
        myPane.setLayout(new GridLayout(2, 2));

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

        myFrame.pack();
        myFrame.setVisible(true);
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
