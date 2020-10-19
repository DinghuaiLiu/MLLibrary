import java.util.ArrayList;

public class DummyModel extends Model {

    private double avgAppleX = 0.0;
    private double avgAppleY = 0.0;

    @Override
    void train(ArrayList<DataPoint> data) {
        double sumAppleX = 0;
        double sumAppleY = 0;
        for (int i = 0; i < data.size(); i++) {
            double f1 = data.get(i).getF1();
            double f2 = data.get(i).getF2();
            String label = data.get(i).getLabel();
            if (label.equals("apple")) {
                sumAppleX += f1;
                sumAppleY += f2;
            }
        }
        this.avgAppleX = sumAppleX / data.size();
        this.avgAppleY = sumAppleY / data.size();
    }

    @Override
    String test(ArrayList<DataPoint> data) {
        for (int i = 0; i < data.size(); i++) {
            double f1 = data.get(i).getF1();
            double f2 = data.get(i).getF2();
            if (f1 - this.avgAppleX + f2 - this.avgAppleY < 1) {
                System.out.println("apple");
            } else {
                System.out.println("other");
            }
        }
        return "Test over";
    }

    @Override
    Double getAccuracy(ArrayList<DataPoint> data) {
        return this.avgAppleX + this.avgAppleX;
    }

    @Override
    Double getPrecision(ArrayList<DataPoint> data) {
        return this.avgAppleX + this.avgAppleX;
    }
}
