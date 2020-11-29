import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class Graph extends JPanel {

    private static final long serialVersionUID = 1L;
    private int labelPadding = 40;
    private Color lineColor = new Color(255, 255, 254);

    /**
     * set different color with different result
     */
    private Color blueColor = new Color(0, 0, 255);
    private Color cyanColor = new Color(0, 255, 255);
    private Color yellowColor = new Color(255, 255, 0);
    private Color redColor = new Color(255, 0, 0);

    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);

    private static int pointWidth = 5;

    /**
     *  Number of grids and the padding width
    */
    private int numXGridLines = 6;
    private int numYGridLines = 6;
    private int padding = 40;

    private ArrayList<DataPoint> data;

    /**
     * record the value of accuracy、precision
     */
    private static Double accuracy;
    private static Double precision;

    /**
     *  record real data, key is f1_f2, value is label
     */
    private static Map<String, String> map = new HashMap<>();

    private static KNNModel knnModel;

    /**
     * Constructor method
     */
    public Graph(ArrayList<DataPoint> testData, ArrayList<DataPoint> trainData) {
        this.data = testData;
//        knnModel = new KNNModel(31, trainData);
        knnModel = new KNNModel(15, trainData);
        knnModel.train(trainData);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double minF1 = getMinF1Data();
        double maxF1 = getMaxF1Data();
        double minF2 = getMinF2Data();
        double maxF2 = getMaxF2Data();

        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) -
                labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLUE);

        double yGridRatio = (maxF2 - minF2) / numYGridLines;
        for (int i = 0; i < numYGridLines + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 -
                    labelPadding)) / numYGridLines + padding + labelPadding);
            int y1 = y0;
            if (data.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = String.format("%.2f", (minF2 + (i * yGridRatio)));
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 6, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        double xGridRatio = (maxF1 - minF1) / numXGridLines;
        for (int i = 0; i < numXGridLines + 1; i++) {
            int y0 = getHeight() - padding - labelPadding;
            int y1 = y0 - pointWidth;
            int x0 = i * (getWidth() - padding * 2 - labelPadding) / (numXGridLines) + padding + labelPadding;
            int x1 = x0;
            if (data.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                g2.setColor(Color.BLACK);
                String xLabel = String.format("%.2f", (minF1 + (i * xGridRatio)));
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(xLabel);
                g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // Draw the main axis
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() -
                padding, getHeight() - padding - labelPadding);

        // Draw the points
        paintPoints(g2, minF1, maxF1, minF2, maxF2);
    }

    private void paintPoints(Graphics2D g2, double minF1, double maxF1, double minF2, double maxF2) {
        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        double xScale = ((double) getWidth() - (3 * padding) - labelPadding) / (maxF1 - minF1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (maxF2 - minF2);
        g2.setStroke(oldStroke);
        for (int i = 0; i < data.size(); i++) {
            int x1 = (int) ((data.get(i).getF1() - minF1) * xScale + padding + labelPadding);
            int y1 = (int) ((maxF2 - data.get(i).getF2()) * yScale + padding);
            int x = x1 - pointWidth / 2;
            int y = y1 - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;

            // TODO: Depending on the type of data and how it is tested, change color here.
            // You need to test your data here using the model to obtain the test value 
            // and compare against the true label.

            ArrayList<DataPoint> list = new ArrayList<>();
            list.add(data.get(i));
            String label = knnModel.test(list);
            String labelTrue = map.get(data.get(i).getF1() + "_" + data.get(i).getF2());
            if ("1".equals(label)) {
                if ("1".equals(labelTrue)) {
                    g2.setColor(blueColor);
                } else {
                    g2.setColor(cyanColor);
                }
            } else {
                if ("1".equals(labelTrue)) {
                    g2.setColor(yellowColor);
                } else {
                    g2.setColor(redColor);
                }
            }
            g2.fillOval(x, y, ovalW, ovalH);

            // according the test data, update the value of accuracy、precision
            precision = knnModel.getPrecision(data);
            accuracy = knnModel.getAccuracy(data);
        }

    }

    /**
     * @Return the min values
     */
    private double getMinF1Data() {
        double minData = Double.MAX_VALUE;
        for (DataPoint pt : this.data) {
            minData = Math.min(minData, pt.getF1());
        }
        return minData;
    }

    private double getMinF2Data() {
        double minData = Double.MAX_VALUE;
        for (DataPoint pt : this.data) {
            minData = Math.min(minData, pt.getF2());
        }
        return minData;
    }

    /**
     * @Return the max values;
     */
    private double getMaxF1Data() {
        double maxData = Double.MIN_VALUE;
        for (DataPoint pt : this.data) {
            maxData = Math.max(maxData, pt.getF1());
        }
        return maxData;
    }

    private double getMaxF2Data() {
        double maxData = Double.MIN_VALUE;
        for (DataPoint pt : this.data) {
            maxData = Math.max(maxData, pt.getF2());
        }
        return maxData;
    }

    /**
     *  Run createAndShowGui in the main method, where we create the frame too and pack it in the panel
     *  */
    private static void createAndShowGui(ArrayList<DataPoint> testData, ArrayList<DataPoint> trainData) {
        /* Main panel */
        Graph mainPanel = new Graph(testData, trainData);

        // Feel free to change the size of the panel
        mainPanel.setPreferredSize(new Dimension(700, 600));

        /* creating the frame */
        JFrame frame = new JFrame("CS 112 Lab Part 3");
        JPanel jPanel = new JPanel();
        jPanel.add(mainPanel);
        Container contentPane = frame.getContentPane();
        contentPane.add(jPanel);

        JPanel jPanel1 = new JPanel();
        jPanel1.setBackground(Color.GRAY);
        JButton accyBtn = new JButton("get accuracy");
        addActionListener(accyBtn, "accuracy");
        jPanel1.add(accyBtn);

        JButton precBtn = new JButton("get precision");
        addActionListener(precBtn, "precision");
        jPanel1.add(precBtn);
        contentPane.add(jPanel1, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void addActionListener(JButton button, String flag) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("accuracy".equals(flag)) {
                    JOptionPane.showMessageDialog(null, String.valueOf(accuracy));
                } else {
                    JOptionPane.showMessageDialog(null, String.valueOf(precision));
                }
            }
        });
    }

    /**
     *   The main method runs createAndShowGui
      */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // load titanic.csv data
                List<DataPoint> data = FileUtil.loadFile();

                ArrayList<DataPoint> train = new ArrayList<DataPoint>();
                ArrayList<DataPoint> test = new ArrayList<DataPoint>();

                // create train data and test data, and train : test = 9 : 1
                Random random = new Random();
                for (int i = 0; i < data.size(); i++) {
                    double randNum = random.nextDouble();

                    if (randNum < 0.9) {
                        train.add(data.get(i));
                    } else {
                        test.add(data.get(i));
                        map.put(data.get(i).getF1() + "_" + data.get(i).getF2(), data.get(i).getLabel());
                    }
                }
                createAndShowGui(test, train);
            }
        });
    }

}
