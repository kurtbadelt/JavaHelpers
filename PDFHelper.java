/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 *
 * @author kurtyanez
 */
public class PDFHelper {
    Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);

    private void createTable(Paragraph paragraph, ArrayList encabezados, final ArrayList[] datos)
            throws BadElementException {
        PdfPTable table = new PdfPTable(encabezados.size());

        for (int i = 0; i < encabezados.size(); i++) {
            PdfPCell celdaEncabezado = new PdfPCell(new Phrase(encabezados.get(i).toString()));
            celdaEncabezado.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(celdaEncabezado);
        }

        table.setHeaderRows(1);

        for (int i = 0; i < datos.length; i++) {
            for (int j = 0; j < datos[i].size(); j++) {
                PdfPCell celdaContenido = new PdfPCell(new Phrase(datos[i].get(j).toString()));
                celdaContenido.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(celdaContenido);
                
            }

        }

        paragraph.add(table);

    }

    public void Exportar(ArrayList encabezados, final ArrayList[] datos, String nombre, final String nombreReporte) {
        String File = nombre + ".pdf";
        
        try {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(File));
            document.open();
            addMetaData(document);
          //  addTitlePage(document);
            addContent(document,encabezados,datos);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addMetaData(Document document) {
        document.addTitle("Reporte Procomin");
        document.addSubject("Reporte");
        document.addKeywords("");
        document.addAuthor("Elemento 32");
        document.addCreator("Elemento 32");
    }

    private  void addTitlePage(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Reporte Procomin -", catFont));

        document.add(preface);
  
    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
    
        private  void addContent(Document document, ArrayList encabezados, final ArrayList[] datos) throws DocumentException {
        
       Paragraph paragraph = new Paragraph();

        // add a table
        createTable(paragraph,encabezados,datos);
        document.add(paragraph);



    }

}
