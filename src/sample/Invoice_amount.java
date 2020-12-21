package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Invoice_amount {
    private final SimpleStringProperty desc;
    private final SimpleStringProperty qty;
    private final SimpleDoubleProperty po1;

    public Invoice_amount(String b, String c, double d) {
        this.desc = new SimpleStringProperty(b);
        this.qty = new SimpleStringProperty(c);
        this.po1 = new SimpleDoubleProperty(d);
    }

    public String getDesc() {
        return desc.get();
    }
    public void setDesc(String b) {
        desc.set(b);
    }

    public String getQty() {
        return qty.get();
    }
    public void setQty(String fName) {
        qty.set(fName);
    }

    public double getPo1() {
        return po1.get();
    }
    public void setPo1(double fName) {
        po1.set(fName);
    }

}