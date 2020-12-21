package sample;


import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Item {
    private final SimpleIntegerProperty srNo;
    private final SimpleStringProperty desc;
    private final SimpleIntegerProperty qty;
    private final SimpleDoubleProperty po1;
    private final SimpleDoubleProperty tp;

    public Item(int a, String b, int c, double d, double e) {
        this.srNo = new SimpleIntegerProperty(a);
        this.desc = new SimpleStringProperty(b);
        this.qty = new SimpleIntegerProperty(c);
        this.po1 = new SimpleDoubleProperty(d);
        this.tp = new SimpleDoubleProperty(e);
    }

    public int getSrNo() {
        return srNo.get();
    }
    public void setSrNo(int a) {
        srNo.set(a);
    }

    public String getDesc() {
        return desc.get();
    }
    public void setDesc(String b) {
        desc.set(b);
    }

    public int getQty() {
        return qty.get();
    }
    public void setQty(int fName) {
        qty.set(fName);
    }

    public double getPo1() {
        return po1.get();
    }
    public void setPo1(double fName) {
        po1.set(fName);
    }
    public double getTp() {
        return tp.get();
    }
    public void setTp(double fName) {
        tp.set(fName);
    }

}