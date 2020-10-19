

public class DataPoint {
    Double f1;
    Double f2;
    String label;
    String type;

    public DataPoint(){
        this.f1 = 0.0;
        this.f2 = 0.0;
        this.label = null;
        this.type = null;
    }

    public DataPoint(double f1, double f2, String label, String type){
     this.f1 = f1;
     this.f2 = f2;
     this.label = label;
     this.type = type;
    }

    public DataPoint(double f1, double f2, String type){
        this.f1 = f1;
        this.f2 = f2;
        this.type = type;
    }

    public Double getF1() {
        return f1;
    }

    public void setF1(Double f1) {
        this.f1 = f1;
    }

    public Double getF2() {
        return f2;
    }

    public void setF2(Double f2) {
        this.f2 = f2;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
