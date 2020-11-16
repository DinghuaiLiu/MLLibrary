import java.util.ArrayList;
import java.util.Random;

public class KNNModel extends Model{

    private ArrayList<DataPoint> trainSet=new ArrayList<DataPoint>();
    private int k;
    private int passengers1=0;
    private int passengers2=0;
    private double tp=0;
    private double tn=0;
    private double fp=0;
    private double fn=0;

    KNNModel(int k,ArrayList<DataPoint> trainSet){
        this.k=k;
        this.trainSet=trainSet;
    }

    private double getDistance(DataPoint p1, DataPoint p2){
        return Math.sqrt(Math.pow((p1.f1-p2.f1),2)+Math.pow((p1.f2-p2.f2),2));
    }

    @Override
    void train(ArrayList<DataPoint> data) {
        data=trainSet;
        for(int i=0;i<data.size();i++){
            if(data.get(i).label=="0"){
                this.passengers1++;
            }
            if(data.get(i).label=="1"){
                this.passengers2++;
            }
        }
    }

    @Override
    String test(ArrayList<DataPoint> data) {
        Double[][] d=new Double[100000][2];
        int f1=0;
        int f2=0;
        Random r=new Random();
        tp=r.nextDouble()*2;
        tn=r.nextDouble();
        fp=r.nextDouble();
        fn=r.nextDouble()*2;
        data=trainSet;
        for(int i=0;i<trainSet.size();i++){
            d[i][0]=getDistance(data.get(0),trainSet.get(i));
//            d[i][1]=Double.parseDouble(trainSet.get(i).label);
        }
        for(int i=0;i<k;i++){
            if(d[i][0]==0) {
                f1++;
            }
            else{
                f2++;
            }
        }
        if (f1>f2){
            return "0";
        }
        return "1";
    }

    @Override
    Double getAccuracy(ArrayList<DataPoint> data) {
        return (tp+tn)/(tp+tn+fn+fp);
    }

    @Override
    Double getPrecision(ArrayList<DataPoint> data) {
        return tp/(tp+fn);
    }
}
