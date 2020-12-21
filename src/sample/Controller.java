package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable {
    private Controller Controller2;
    @FXML
    public Label  lbl1;
    @FXML
    TextField tf1;
    @FXML
    TextField tf11;
    @FXML
    TextField tf111;
    @FXML
    TextField tf2;
    @FXML
    TextField tf3;
    @FXML
    TextField tf4;
    @FXML
    TextField descTf;
    @FXML
    TextField qtyTf;
    @FXML
    TextField pTf;
    @FXML
    TextField dt;
    @FXML
    TextField tb4;
    @FXML
    TableColumn<Item, String> tbc1;
    @FXML
    TableColumn<Item, String> tbc2;
    @FXML
    TableColumn<Item, Integer> tbc3;
    @FXML
    TableColumn<Item, Integer> tbc4;
    @FXML
    TableColumn<Item, Integer> tbc5;
    @FXML
    BorderPane pane1;
    @FXML
    private TableView <Item> tbv = new TableView<Item>();
    @FXML
    Button add;
    @FXML
    Button inc;
    @FXML
    Button btn1;
    @FXML
    Button deleteButton;
    @FXML
    Button showPrev;
    @FXML
    Label lb1;

    static int ivnNum=0;
    int srNo=0;
    double t=0;
    protected final static ObservableList<Item> data =
            FXCollections.observableArrayList();
    private int qty;
    private double tPrice;
    private String invoiceNumber;
    private String array[]=getArray();
    final KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.P,
            KeyCombination.CONTROL_ANY);
    final KeyCombination keyComb2 = new KeyCodeCombination(KeyCode.A,
            KeyCombination.CONTROL_ANY);
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tbc1.setCellValueFactory(
                new PropertyValueFactory<>("srNo"));
        tbc2.setCellValueFactory(
                new PropertyValueFactory<>("desc"));
        tbc3.setCellValueFactory(
                new PropertyValueFactory<>("qty"));
        tbc4.setCellValueFactory(
                new PropertyValueFactory<>("po1"));
        tbc5.setCellValueFactory(
                new PropertyValueFactory<>("tp"));
        lb1.setBackground(new Background(new BackgroundFill(Color.web("#1565c0",0.8), CornerRadii.EMPTY, Insets.EMPTY)));
        generateInvoiceNumber(getDate());
        pane1.setOnKeyPressed(event ->
        {
            if (keyComb1.match(event)) {
                    printInvoice();
                }
        });
        dt.setText(getDate());
        showPrev.setOnMouseClicked(mouseEvent -> {
            try {
                nextPage();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,"Input Output Exception"+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        });
        TableView.TableViewSelectionModel<Item> selectionModel = tbv.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        tbv.setOnKeyPressed(keyEvent -> {
            KeyCode keyCode=keyEvent.getCode();
            if(keyCode.equals(KeyCode.DELETE))
            {
                deleteFromTable();
            }
            else if (keyComb2.match(keyEvent)) {
                    selectionModel.selectAll();
                }
        });
        tbv.setPlaceholder(new Label("No entries yet."));
        deleteButton.setOnAction(e -> deleteFromTable());
        tf3.setOnMouseClicked(event -> clear());
        tf3.setOnKeyPressed(keyEvent -> {
            KeyCode keyCode=keyEvent.getCode();
            if(keyCode.equals(KeyCode.ENTER))
            {
                descTf.requestFocus();
            }
        });
        add.setOnAction(event -> addToTable());
        inc.setOnAction(mouseEvent -> increase());
        btn1.setOnAction(mouseEvent -> {
                printInvoice();
        });
        descTf.setOnKeyPressed(keyEvent -> {
            KeyCode keyCode = keyEvent.getCode();
            if(keyCode.equals(KeyCode.ENTER))
            {
                descTf.setText(checkValue());
                qtyTf.requestFocus();
            }
        });
        qtyTf.setOnKeyPressed(keyEvent -> {
            KeyCode keyCode = keyEvent.getCode();
            if(keyCode.equals(KeyCode.ENTER))
            {
                pTf.requestFocus();
            }
        });
        pTf.setOnKeyPressed(keyEvent -> {
            KeyCode keyCode = keyEvent.getCode();
            if(keyCode.equals(KeyCode.ENTER))
            {
                addToTable();
            }
        });
    }

    private String checkValue() {
        String s=descTf.getText();
        try
        {
            int i=Integer.parseInt(s);
            return array[i-1];
        }
        catch (Exception e)
        {
            return s;
        }
    }

    private String getDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dtf.format(now);
    }

    private void nextPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("interface2.fxml"));
        Controller2 =loader.getController();
        Pane rootNode = loader.load();
        Scene scene = new Scene(rootNode);
        Stage primaryStage=new Stage();
        primaryStage.setTitle("Invoice Details");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void deleteFromTable() {
        ObservableList<Item> selectedItem = tbv.getSelectionModel().getSelectedItems();
        for(int i1=0;i1<selectedItem.size();i1++)
        {
            srNo--;
            t-=selectedItem.get(i1).getTp();
            qty-=selectedItem.get(i1).getQty();
        }
        tbv.getItems().removeAll(selectedItem);

        int i=1;
        for(Item i1:data)
        {
            i1.setSrNo(i++);
        }
        tf1.setText(String.valueOf(t));
        tf111.setText(String.valueOf(srNo));
        tf11.setText(String.valueOf(qty));
    }
    private void generateInvoiceNumber(String now) {
        File myReader = new File("BillingApplication/lastInvoiceNumber.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(myReader);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"invoice_amount.txt file is missing","Error", JOptionPane.ERROR_MESSAGE);
        }
        String lastInvoice=sc.nextLine();
        int index=lastInvoice.lastIndexOf("_");
        String lastInvoiceDate=lastInvoice.substring(0,index);

        lastInvoiceDate=lastInvoiceDate.replace('_','/');
        int lastNo=Integer.parseInt(lastInvoice.substring(index+1));
        if(lastInvoiceDate.equals(getDate())) ivnNum=lastNo+1;
        else ivnNum++;
        now=now.replace('/','_');
        invoiceNumber=now+"_"+ivnNum;
        tb4.setText(invoiceNumber);
    }
    private void clear() {
        tf3.setText("");
    }
    void increase()
    {
        String s=qtyTf.getText();
        if(s.equals(""))qtyTf.setText("1");
        else qtyTf.setText(String.valueOf(Integer.parseInt(s)+1));
    }
    void printInvoice(){
        tbc1.setComparator(tbc1.getComparator().reversed());
        this.tbv.getSortOrder().add(this.tbc1);
        try {
            String name=tf3.getText();
            if (!name.isEmpty());
            else throw new Exception("No Name entered");
            if(t>0)
            {
                FileWriter myWriter = new FileWriter("BillingApplication/invoice_detail/" + invoiceNumber + ".txt");
                for (Item i1 : data) {
                    myWriter.write(invoiceNumber + ":" + i1.getSrNo() + ":" + i1.getDesc() + ":" + i1.getQty() + ":" + i1.getPo1() + ":" + i1.getTp() + "\n");
                }
                myWriter.close();
                File file = new File("BillingApplication/invoice_amount.txt");
                FileWriter fr = new FileWriter(file, true);
                BufferedWriter br = new BufferedWriter(fr);
                br.write(invoiceNumber + ":" + name + ":" + t + "\n");
                InvoiceGenerator.main(new String[]{invoiceNumber,name,getDate()});
                br.close();
                fr.close();
                myWriter.close();
                data.clear();
                srNo=0;
                t=0;
                qty=0;
                tf3.setText("Walk In");
                tf11.setText("0");
                tf111.setText("0");
                tf1.setText("0.0");
                descTf.requestFocus();
                P.main(new String[]{"BillingApplication/printout/"+invoiceNumber+".pdf"});
                PrintWriter writer = new PrintWriter("BillingApplication/lastInvoiceNumber.txt");
                writer.print(invoiceNumber);
                writer.close();
                generateInvoiceNumber(getDate());
            }
            else
                JOptionPane.showMessageDialog(null,"Empty Invoice Not Allowed","Error",JOptionPane.ERROR_MESSAGE);
        }
            catch (Exception e) {
            JOptionPane.showMessageDialog(null,"An error occurred. "+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }

    }
    void addToTable() {
        String nm=tf3.getText();
        String s=descTf.getText();
        int a;
        double price;
        if(s.equals(""))
        {
            JOptionPane.showMessageDialog(null,"Item Description can't be Null","Error",JOptionPane.ERROR_MESSAGE);
            descTf.requestFocus();
            return;
        }
        try {
            a = Integer.parseInt(qtyTf.getText());
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null,"Quantity should be an Integer","Error",JOptionPane.ERROR_MESSAGE);
            qtyTf.setText("");
            qtyTf.requestFocus();
            return;
        }
        try {
            price = Double.parseDouble(pTf.getText());
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null,"Price should be numeric","Error",JOptionPane.ERROR_MESSAGE);
            pTf.setText("");
            pTf.requestFocus();
            return;
        }
        tPrice=a*price;
        srNo++;
        qty+=a;
        t+=tPrice;
        data.add(new Item(srNo,s,a,price,tPrice));
        if(srNo==1)tbc1.setComparator(tbc1.getComparator().reversed());
        tbc1.setSortNode(new Group());
        this.tbv.getSortOrder().add(this.tbc1);
        tbv.setItems(data);
        tbv.getSelectionModel().clearAndSelect(0);
        tf1.setText(String.valueOf(t));
        tf111.setText(String.valueOf(srNo));
        tf11.setText(String.valueOf(qty));
        descTf.setText("");
        qtyTf.setText("");
        pTf.setText("");
        descTf.requestFocus();
    }

    private String[] getArray() {
        File myReader=new File("BillingApplication/others/invoice_options.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(myReader);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"invoice_options.txt file is missing","Error", JOptionPane.ERROR_MESSAGE);
        }
        int l=0,i=0;
        while (sc.hasNextLine()){
            sc.nextLine();
            ++l;
        }
        String ar[]=new String[l];
        try {
            sc = new Scanner(myReader);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"invoice_options.txt file is missing","Error", JOptionPane.ERROR_MESSAGE);
        }
        while (sc.hasNextLine()) {
            String s=sc.nextLine();
            ar[i++]=s;
        }
        return ar;
    }
}
