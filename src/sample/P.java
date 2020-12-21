package sample;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import javax.print.PrintException;
import javax.swing.*;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

public class P {

    public static void main(String[] args) throws PrintException, IOException {
        String filename;
        filename = args[0].trim();

        try {
            PDDocument pdf = PDDocument.load(new File(filename));
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPageable(new PDFPageable(pdf));
            job.print();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }
}