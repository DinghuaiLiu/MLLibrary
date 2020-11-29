import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class KNNModel extends Model {

    private ArrayList<DataPoint> trainSet = new ArrayList<DataPoint>();
    private int k;
    private int passengers1 = 0;
    private int passengers2 = 0;
    /**
     * test return 1，really is 1
     */
    public double tp = 0;
    /**
     * test return 1，really is 0
     */
    private double fp = 0;
    /**
     * test return 0，really is 0
     */
    private double tn = 0;
    /**
     * test return 0，really is 1
     */
    private double fn = 0;
    private String one = "1";

    KNNModel(int k, ArrayList<DataPoint> trainSet) {
        this.k = k;
        this.trainSet = trainSet;
    }

    private double getDistance(DataPoint p1, DataPoint p2) {
        return Math.sqrt(Math.pow((p1.f1 - p2.f1), 2) + Math.pow((p1.f2 - p2.f2), 2));
    }

    @Override
    void train(ArrayList<DataPoint> data) {
        data = trainSet;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).label == "0") {
                this.passengers1++;
            }
            if (data.get(i).label == "1") {
                this.passengers2++;
            }
        }
    }

    @Override
    String test(ArrayList<DataPoint> data) {
        Double[][] d = new Double[trainSet.size()][2];
        int f1 = 0;
        int f2 = 0;

        for (int i = 0; i < trainSet.size(); i++) {
            d[i][0] = getDistance(data.get(0), trainSet.get(i));
            d[i][1] = Double.parseDouble(trainSet.get(i).label);
        }

        //sort d, according to the first column of d
        // this is need in Part2.
        sort(d, new int[] {0,1});

        String flag;
        for (int i = 0; i < k; i++) {
            if (d[i][1] == 0) {
                f1++;
            } else {
                f2++;
            }
        }
        if (f1 > f2) {
            flag = "0";
        } else {
            flag = "1";
        }

        // according to test result and true label, update the value of tp、fp、fn、tn
        String label = data.get(0).getLabel();
        if (one.equals(flag)) {
            if (one.equals(label)) {
                tp++;
            } else {
                fp++;
            }
        } else {
            if (one.equals(label)) {
                fn++;
            } else {
                tn++;
            }
        }
        return flag;
    }

    @Override
    Double getAccuracy(ArrayList<DataPoint> data) {
        return (tp + tn) / (tp + tn + fn + fp);
    }

    @Override
    Double getPrecision(ArrayList<DataPoint> data) {
        // Note: The formula in the document is incorrect.
        // It is the recall.
        // The accuracy rate is expressed in this way.
        // Please confirm with the teacher.
//        return tp / (tp + fn);
        return tp / (tp + fp);
    }

    public static void sort(Double[][] ob, final int[] order) {
        Arrays.sort(ob, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                Double[] one = (Double[]) o1;
                Double[] two = (Double[]) o2;
                for (int i = 0; i < order.length; i++) {
                    int k = order[i];
                    if (one[k] > two[k]) {
                        return 1;
                    } else if (one[k] < two[k]) {
                        return -1;
                    } else {
                        continue;
                    }
                }
                return 0;
            }
        });
    }
}
