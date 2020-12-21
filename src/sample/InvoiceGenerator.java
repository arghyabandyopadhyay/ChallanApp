package sample;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class InvoiceGenerator {

    private BaseFont bfBold;
    private BaseFont bf;
    private int pageNumber = 0;
    private String pdfFilename="",path;
    private int tSrno=0,tQty=0;
    private double tAmount=0;


    public static void main(String[] args) {
        String pdfFilename = "one";
        InvoiceGenerator generateInvoice = new InvoiceGenerator();
        if (args.length < 1)
        {
            System.err.println("Usage: java "+ generateInvoice.getClass().getName()+
                    " E:/exportpdf.pdf");
            System.exit(1);
        }

        pdfFilename = args[0].trim();
        String name=args[1].trim();
        String now=args[2].trim();
        generateInvoice.createPDF(pdfFilename,name,now);

    }

    private void createPDF (String pdfFilename, String name,String now){

        Document doc = new Document();
        PdfWriter docWriter = null;
        initializeFonts();

        try {
            path = "BillingApplication/printout/"+pdfFilename+".pdf";
            docWriter = PdfWriter.getInstance(doc , new FileOutputStream(path));
            doc.addAuthor("Arghya Banyopadhyay");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("ArghyaBandyopadhyay.com");
            doc.addTitle("Invoice");
            doc.setPageSize(PageSize.A5);
            doc.open();
            PdfContentByte cb = docWriter.getDirectContent();

            boolean beginPage = true;
            int y = 0;
            File myReader=new File("BillingApplication/invoice_detail/"+pdfFilename+".txt");
            Scanner sc = null;
            try {
                sc = new Scanner(myReader);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null,pdfFilename+".txt file is missing","Error", JOptionPane.ERROR_MESSAGE);
            }

            while (sc.hasNextLine()){
                if(beginPage){
                    beginPage = false;
                    generateLayout(doc, cb);
                    generateHeader(doc, cb,pdfFilename,name,now);
                    y = 415;
                }
                String s=sc.nextLine();
                int firstIndex=s.indexOf(':');
                int secondIndex=s.indexOf(':',firstIndex+1);
                int thirdIndex=s.indexOf(':',secondIndex+1);
                int fourthIndex=s.indexOf(':',thirdIndex+1);
                int fifthIndex=s.indexOf(':',fourthIndex+1);
                int srNo=Integer.parseInt(s.substring(firstIndex+1,secondIndex));
                String s1=s.substring(secondIndex+1,thirdIndex);
                int a=Integer.parseInt(s.substring(thirdIndex+1,fourthIndex));
                Double price=Double.parseDouble(s.substring(fourthIndex+1,fifthIndex));
                Double tPrice=Double.parseDouble(s.substring(fifthIndex+1));
                generateDetail(doc, cb, srNo,s1,a,price,tPrice,y);
                tSrno++;
                tQty+=a;
                tAmount+=tPrice;
                y = y - 18;
                if(y < 50){
                    printPageNumber(cb);
                    doc.newPage();
                    beginPage = true;
                }
            }
            printPageNumber(cb);
            createContent(cb,60,40,"T Sr. no:",PdfContentByte.ALIGN_RIGHT);
            createContent(cb,75,40,String.valueOf(tSrno),PdfContentByte.ALIGN_RIGHT);
            createContent(cb,172,40, "Total Qty:",PdfContentByte.ALIGN_RIGHT);
            createContent(cb,218,40, String.valueOf(tQty),PdfContentByte.ALIGN_RIGHT);
            createContent(cb,298,40, "Total Amount:",PdfContentByte.ALIGN_RIGHT);
            DecimalFormat df = new DecimalFormat("0.00");
            createContent(cb,368,40, "Rs."+df.format(tAmount),PdfContentByte.ALIGN_RIGHT);
            tSrno=0;
            tQty=0;
            tAmount=0;
        }
        catch (DocumentException dex)
        {
            dex.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (doc != null)
            {
                doc.close();
            }
            if (docWriter != null)
            {
                docWriter.close();
            }
        }
    }

        private void generateLayout(Document doc, PdfContentByte cb)  {

        try {

            cb.setLineWidth(1f);
            // Invoice Header box layout
            cb.rectangle(20,490,350,60);
            cb.moveTo(20,520);
            cb.lineTo(370,520);
            cb.moveTo(210,490);
            cb.lineTo(210,550);
            cb.stroke();

            // Invoice Header box Text Headings
            createHeadings2(cb,22,533,"Name. :");
            createHeadings2(cb,22,503,"Invoice No. :");
            createHeadings2(cb,222,533,"Invoice Date. :");
            createHeadings2(cb,222,503,"Invoice Time. :");

            // Invoice Detail box layout
            cb.rectangle(20,50,350,400);
            cb.moveTo(20,430);
            cb.lineTo(370,430);
            cb.moveTo(50,50);
            cb.lineTo(50,450);
            cb.moveTo(170,50);
            cb.lineTo(170,450);
            cb.moveTo(230,50);
            cb.lineTo(230,450);
            cb.moveTo(300,50);
            cb.lineTo(300,450);
            cb.stroke();

            // Invoice Detail box Text Headings
            createHeadings(cb,22,433,"Sr no.");
            createHeadings(cb,52,433,"Description");
            createHeadings(cb,172,433,"Qty");
            createHeadings(cb,232,433,"Price");
            createHeadings(cb,302,433,"Amount");

            //add the images
            /*Image companyLogo = Image.getInstance("BillingApplication/images/download.png");
            companyLogo.setAbsolutePosition(25,700);
            companyLogo.scalePercent(25);
            doc.add(companyLogo);*/

        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void generateHeader(Document doc, PdfContentByte cb,String inv,String name,String now)  {

        try {

            createHeadings1(cb,125,455,"Order And Estimate");
            createHeadings(cb,145,570,"SHRI MAHAVIRAY NAMAH");
            createHeadings(cb,70,533,name);
            createHeadings(cb,90,503,inv);
            createHeadings(cb,300,533,now);
            LocalDateTime time = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            createHeadings(cb,300,503,dtf.format(time));

        }

        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void generateDetail(Document doc, PdfContentByte cb, int srno,String desc, int qty, double price, double amount,int y)  {
        DecimalFormat df = new DecimalFormat("0.00");

        try {

            createContent(cb,48,y,String.valueOf(srno),PdfContentByte.ALIGN_RIGHT);
            createContent(cb,52,y, desc,PdfContentByte.ALIGN_LEFT);
            createContent(cb,228,y, String.valueOf(qty),PdfContentByte.ALIGN_RIGHT);
            createContent(cb,298,y, df.format(price),PdfContentByte.ALIGN_RIGHT);
            createContent(cb,368,y, df.format(amount),PdfContentByte.ALIGN_RIGHT);

        }

        catch (Exception ex){
            ex.printStackTrace();
        }

    }
    private void createHeadings(PdfContentByte cb, float x, float y, String text){


        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }
    private void createHeadings2(PdfContentByte cb, float x, float y, String text){


        cb.beginText();
        cb.setFontAndSize(bfBold, 10);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }
    private void createHeadings1(PdfContentByte cb, float x, float y, String text){


        cb.beginText();
        cb.setFontAndSize(bfBold, 15);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }

    private void printPageNumber(PdfContentByte cb){


        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Page No. " + (pageNumber+1), 370 , 15, 0);
        cb.endText();

        pageNumber++;

    }

    private void createContent(PdfContentByte cb, float x, float y, String text, int align){


        cb.beginText();
        cb.setFontAndSize(bf, 10);
        cb.showTextAligned(align, text.trim(), x , y, 0);
        cb.endText();

    }

    private void initializeFonts(){


        try {
            bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
