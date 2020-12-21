package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller2 implements Initializable {
    public Button deleteSelected;
    @FXML
    private TableView <Invoice_amount> tbv = new TableView<Invoice_amount>();
    @FXML
    TableView<Item> tbv1;
    @FXML
    TableColumn<Invoice_amount, String> tbc6;
    @FXML
    TableColumn<Invoice_amount, String> tbc7;
    @FXML
    TableColumn<Invoice_amount, Double> tbc8;
    @FXML
    TableColumn<Item, String> tbc9;
    @FXML
    TableColumn<Item, String> tbc10;
    @FXML
    TableColumn<Item, Integer> tbc11;
    @FXML
    TableColumn<Item, Integer> tbc12;
    @FXML
    TableColumn<Item, Integer> tbc13;
    @FXML
    TextField tfTsrNo;
    @FXML
    TextField tfQty;
    @FXML
    TextField tfAmount;
    @FXML
    Button prntInvoice;
    @FXML
    Button fetch;
    @FXML
    Button deleteAll;
    @FXML
    Button all;
    @FXML
    Label lb2;
    @FXML
    DatePicker dp;
    DateTimeFormatter dtf;
    private final static ObservableList<Invoice_amount> data1 =
            FXCollections.observableArrayList();
    private final static ObservableList<Invoice_amount> data2 =
            FXCollections.observableArrayList();
    private final static ObservableList<Item> data =
            FXCollections.observableArrayList();
    final KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.P,
            KeyCombination.CONTROL_ANY);
    final KeyCombination keyComb2 = new KeyCodeCombination(KeyCode.A,
            KeyCombination.CONTROL_ANY);
    int tSrNo=0,Qty=0;
    double t=0.0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        data1.clear();
        File myReader=new File("BillingApplication/invoice_amount.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(myReader);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"invoice_amount.txt file is missing","Error", JOptionPane.ERROR_MESSAGE);
        }
        tbc6.setCellValueFactory(
                new PropertyValueFactory<>("desc"));
        tbc7.setCellValueFactory(
                new PropertyValueFactory<>("qty"));
        tbc8.setCellValueFactory(
                new PropertyValueFactory<>("po1"));
        while (sc.hasNextLine()) {
            String s=sc.nextLine();
            int firstIndex=s.indexOf(':');
            int secondIndex=(s.substring(firstIndex+1)).indexOf(':');
            String billNo=s.substring(0,firstIndex);
            String Name=s.substring(firstIndex+1,firstIndex+secondIndex+1);
            double Amount=Double.parseDouble(s.substring(firstIndex+secondIndex+2));
            data1.add(new Invoice_amount(billNo,Name,Amount));
        }
        deleteAll.setOnMouseClicked(event -> {
            try {
                deleteFromTable();
                
                tbv.setItems(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        deleteSelected.setOnMouseClicked(event -> {
            try {
                deleteValue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        dp.setOnKeyPressed(keyEvent ->
        {
            KeyCode keyCode=keyEvent.getCode();
            if(keyCode.equals(KeyCode.ENTER))
            {
                fetch();
            }
        });
        fetch.setOnMouseClicked(e->fetch());
        prntInvoice.setOnMouseClicked(mouseEvent -> askPrint());
        all.setOnAction(e->fetchDetails());
        tbv.setItems(data1);
        TableView.TableViewSelectionModel<Invoice_amount> selectionModel = tbv.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        tbv.setOnKeyReleased(keyEvent ->{
            KeyCode keyCode=keyEvent.getCode();
            if (keyCode.equals(KeyCode.UP)||keyCode.equals(KeyCode.DOWN)) {
                if(checkNoOfSelection())fetchDetail();
                else tbv1.setItems(null);
            }
            else if (keyComb2.match(keyEvent)) {
                selectionModel.selectAll();
                tbv1.setItems(null);
            }
        });
        tbv.setOnMouseClicked(mouseEvent -> {
            if(checkNoOfSelection())fetchDetail();
            else tbv1.setItems(null);
        });
        tbv.setPlaceholder(new Label("No rows to display"));
        tbv.setOnKeyPressed(keyEvent -> {
            KeyCode keyCode=keyEvent.getCode();
            if(keyCode.equals(KeyCode.ENTER))
            {
                fetchDetail();
            }
        });
        tbv.setOnKeyPressed(keyEvent -> {
            KeyCode keyCode = keyEvent.getCode();
            if (keyCode.equals(KeyCode.DELETE)) {
                try {
                    deleteValue();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void fetchDetails() {
        data1.clear();
        File myReader=new File("BillingApplication/invoice_amount.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(myReader);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"invoice_amount.txt file is missing","Error", JOptionPane.ERROR_MESSAGE);
        }
        tbc6.setCellValueFactory(
                new PropertyValueFactory<Invoice_amount, String>("desc"));
        tbc7.setCellValueFactory(
                new PropertyValueFactory<Invoice_amount, String>("qty"));
        tbc8.setCellValueFactory(
                new PropertyValueFactory<Invoice_amount, Double>("po1"));
        while (sc.hasNextLine()) {
            String s=sc.nextLine();
            int firstIndex=s.indexOf(':');
            int secondIndex=(s.substring(firstIndex+1)).indexOf(':');
            String billNo=s.substring(0,firstIndex);
            String Name=s.substring(firstIndex+1,firstIndex+secondIndex+1);
            double Amount=Double.parseDouble(s.substring(firstIndex+secondIndex+2));
            data1.add(new Invoice_amount(billNo,Name,Amount));
        }
        tbv.setItems(data1);
    }

    private void askPrint() {
        Invoice_amount selectedItem = tbv.getSelectionModel().getSelectedItem();
        if(checkNoOfSelection())
        {
            String path = "BillingApplication/printout/" + selectedItem.getDesc()+".pdf";
            try {
                P.main(new String[]{path});
            } catch (Exception e) {
            e.printStackTrace();
            }
        }
    }
    private void fetch()
    {
        try {
            LocalDate i = dp.getValue();
            dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            if(i.format(dtf).toString().isEmpty())tbv.setItems(data1);
            String s1 = getInvoice(i.format(dtf).toString());
            data2.clear();
            File myReader = new File("BillingApplication/invoice_amount.txt");
            Scanner sc = null;
            int i1=0;
            try {
                sc = new Scanner(myReader);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "invoice_amount.txt file is missing", "Error", JOptionPane.ERROR_MESSAGE);
            }
            tbc6.setCellValueFactory(
                    new PropertyValueFactory<Invoice_amount, String>("desc"));
            tbc7.setCellValueFactory(
                    new PropertyValueFactory<Invoice_amount, String>("qty"));
            tbc8.setCellValueFactory(
                    new PropertyValueFactory<Invoice_amount, Double>("po1"));
            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                int firstIndex = s.indexOf(':');
                int secondIndex = (s.substring(firstIndex + 1)).indexOf(':');
                String billNo = s.substring(0, firstIndex);
                String Name = s.substring(firstIndex + 1, firstIndex + secondIndex + 1);
                double Amount = Double.parseDouble(s.substring(firstIndex + secondIndex + 2));
                if (billNo.startsWith(s1)) {
                    i1++;
                    data2.add(new Invoice_amount(billNo, Name, Amount));
                }
            }
            if(i1!=0)
            {
                tbv.setItems(data2);
                lb2.setText("");
            }
            else
            {
                lb2.setText("No Records Found");
            }
        }
        catch (Exception e)
        {
            dp.setValue(null);
            tbv.setItems(data1);
        }

    }

    private String getInvoice(String toString) {
        String s=toString.replace('/','_');
        return s;
    }

    private void fetchDetail() {
        Invoice_amount selectedItem = tbv.getSelectionModel().getSelectedItem();
        data.clear();
        tSrNo=0;
        Qty=0;
        t=0.0;
        tfQty.setText(String.valueOf(Qty));
        tfAmount.setText(String.valueOf(t));
        tfTsrNo.setText(String.valueOf(tSrNo));
        File myReader=new File("BillingApplication/invoice_detail/"+selectedItem.getDesc()+".txt");
        Scanner sc = null;
        try {
            sc = new Scanner(myReader);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,selectedItem.getDesc()+".txt file is missing","Error", JOptionPane.ERROR_MESSAGE);
        }
        tbc9.setCellValueFactory(
                new PropertyValueFactory<>("srNo"));
        tbc10.setCellValueFactory(
                new PropertyValueFactory<>("desc"));
        tbc11.setCellValueFactory(
                new PropertyValueFactory<>("qty"));
        tbc12.setCellValueFactory(
                new PropertyValueFactory<>("po1"));
        tbc13.setCellValueFactory(
                new PropertyValueFactory<>("tp"));
        while (sc.hasNextLine()) {
            String s=sc.nextLine();
            int firstIndex=s.indexOf(':');
            int secondIndex=s.indexOf(':',firstIndex+1);
            int thirdIndex=s.indexOf(':',secondIndex+1);
            int fourthIndex=s.indexOf(':',thirdIndex+1);
            int fifthIndex=s.indexOf(':',fourthIndex+1);
            String invoiceNumber=s.substring(0,firstIndex);
            int srNo=Integer.parseInt(s.substring(firstIndex+1,secondIndex));
            String s1=s.substring(secondIndex+1,thirdIndex);
            int a=Integer.parseInt(s.substring(thirdIndex+1,fourthIndex));
            Double price=Double.parseDouble(s.substring(fourthIndex+1,fifthIndex));
            Double tPrice=Double.parseDouble(s.substring(fifthIndex+1));
            tSrNo++;
            Qty+=a;
            t+=tPrice;
            data.add(new Item(srNo,s1,a,price,tPrice));
        }
        tfQty.setText(String.valueOf(Qty));
        tfAmount.setText(String.valueOf(t));
        tfTsrNo.setText(String.valueOf(tSrNo));
        tbv1.setItems(data);
    }

    private boolean checkNoOfSelection() {
        ObservableList<Invoice_amount> selectedItem = tbv.getSelectionModel().getSelectedItems();
        return selectedItem.size() == 1;
    }

    private void deleteValue() throws IOException {
        List<Invoice_amount> selectedItems = tbv.getSelectionModel().getSelectedItems();
        for(Invoice_amount selectedItem :selectedItems)
        {
            String invoiceNumber=selectedItem.getDesc();
            File myReader=new File("BillingApplication/invoice_amount.txt");
            Scanner sc = null;
            try {
                sc = new Scanner(myReader);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null,"invoice_amount.txt file is missing","Error", JOptionPane.ERROR_MESSAGE);
            }
            String str=null;
            while(sc.hasNextLine())
            {
                str=sc.nextLine();
                if(str.substring(0,str.indexOf(':')).equals(selectedItem.getDesc()))
                {
                    break;
                }
            }
            PrintWriter pw = new PrintWriter("BillingApplication/output.txt");

            // BufferedReader object for input.txt
            BufferedReader br1 = new BufferedReader(new FileReader("BillingApplication/invoice_amount.txt"));

            String line1 = br1.readLine();

            // loop for each line of input.txt
            while(line1 != null)
            {
                // if line is not present in delete.txt
                // write it to output.txt
                if(!str.contains(line1))
                    pw.println(line1);

                line1 = br1.readLine();
            }
            pw.flush();
            // closing resources
            br1.close();
            pw.close();
            PrintWriter pw1 = new PrintWriter("BillingApplication/invoice_amount.txt");
            // BufferedReader object for input.txt
            BufferedReader br11 = new BufferedReader(new FileReader("BillingApplication/output.txt"));

            String line11 = br11.readLine();
            // loop for each line of input.txt
            while(line11 != null)
            {
                // if line is not present in delete.txt
                // write it to output.txt
                pw1.println(line11);

                line11 = br11.readLine();
            }

            pw1.flush();

            // closing resources
            br11.close();
            pw1.close();
            File file = new File("BillingApplication/invoice_amount.txt");
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write("");
            deleteDir(new File("BillingApplication/invoice_detail/"+selectedItem.getDesc()+".txt"));
        }
        tbv.getItems().removeAll(selectedItems);
    }

    private void deleteFromTable() throws IOException {
        PrintWriter writer = new PrintWriter("BillingApplication/invoice_amount.txt");
        writer.print("");
        writer.close();
        deleteDir(new File("BillingApplication/invoice_detail"));
        File newFile=new File("BillingApplication/invoice_detail");
        newFile.mkdir();
    }
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir (new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
