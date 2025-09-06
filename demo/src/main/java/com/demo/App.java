package com.demo;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.pdfcleanup.PdfCleanUpLocation;
import com.itextpdf.pdfcleanup.PdfCleanUpTool;
import com.itextpdf.pdfcleanup.CleanUpProperties;
import java.util.*;

public class App {
    public static void redactRectByCoordinates() {
        String input = System.getProperty("user.dir") + "/src/main/resources/input.pdf";
        String outputWithinCropbox = System.getProperty("user.dir") + "/src/main/resources/output/positiveRedactionItextWithinCropbox.pdf";
        String outputOutsideCropbox = System.getProperty("user.dir") + "/src/main/resources/output/positiveRedactionItextOutsideCropbox.pdf";

        try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(input), new PdfWriter(outputWithinCropbox))) {
            List<PdfCleanUpLocation> locations = new ArrayList<>();
            PdfPage page2 = pdfDoc.getPage(2);
            Rectangle cropbox = page2.getCropBox();
            float heightCropbox = cropbox.getHeight();
            float widthCropbox = cropbox.getWidth();
            float xCropboxStarts = cropbox.getX();
            float yCropboxStarts = cropbox.getY();
            float xCropboxEnds = cropbox.getX() + widthCropbox;
            float yCropboxEnds = cropbox.getY() + heightCropbox;
            float widthRedactions = 1000;
            locations.add(new PdfCleanUpLocation(2, new Rectangle(0, 200, 1000, heightCropbox), null));
            // // (w, h, x, y)
            // locations.add(new PdfCleanUpLocation(2, new Rectangle(xCropboxStarts-widthRedactions, yCropboxStarts, widthRedactions, heightCropbox), null));
            // locations.add(new PdfCleanUpLocation(2, new Rectangle(xCropboxEnds+widthRedactions, yCropboxStarts, widthRedactions, heightCropbox), null));
            PdfCleanUpTool cleaner = new PdfCleanUpTool(pdfDoc, locations, new CleanUpProperties());
            cleaner.cleanUp();
            pdfDoc.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        
        try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(input), new PdfWriter(outputOutsideCropbox))) {
            List<PdfCleanUpLocation> locations = new ArrayList<>();
            PdfPage page2 = pdfDoc.getPage(2);
            Rectangle cropbox = page2.getCropBox();
            float heightCropbox = cropbox.getHeight();
            float widthCropbox = cropbox.getWidth();
            float xCropboxStarts = cropbox.getX();
            float yCropboxStarts = cropbox.getY();
            float xCropboxEnds = cropbox.getX() + widthCropbox;
            float yCropboxEnds = cropbox.getY() + heightCropbox;
            float heightRedactions = 1000;
            // locations.add(new PdfCleanUpLocation(2, new Rectangle(200, 0, 1000, heightCropbox), null));
            // // (w, h, x, y)
            // // x-axis 0 is left-hand side, y-axis 0 is bottom
            locations.add(new PdfCleanUpLocation(2, new Rectangle(xCropboxStarts, yCropboxStarts, widthCropbox, (heightRedactions * -1)), null));
            System.err.println("start in the bottom, left-hand corner and extend across the entire x axis, - 1000 below cropbox left-hand corner of 0");
            System.err.println(xCropboxStarts, yCropboxStarts, widthCropbox, (heightRedactions * -1));
            locations.add(new PdfCleanUpLocation(2, new Rectangle(xCropboxStarts, yCropboxEnds, widthCropbox, heightRedactions, null)));
            System.err.println("start in the top, left-hand corner and extend across the entire x axis, + 1000 above cropbox left-hand corner of 0");
            
            PdfCleanUpTool cleaner = new PdfCleanUpTool(pdfDoc, locations, new CleanUpProperties());
            cleaner.cleanUp();
            pdfDoc.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        redactRectByCoordinates();
    }
}
