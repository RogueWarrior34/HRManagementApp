import java.io.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;

public class pdf1
{
public static void main(String gg[])
{
try
{
PdfWriter writer=new PdfWriter(new File("pdf1.pdf"));
PdfDocument pdfDoc=new PdfDocument(writer);
pdfDoc.addNewPage();
Document document=new Document(pdfDoc);
document.close();
System.out.println("PDF created."); 
}catch(Exception e)
{
System.out.println(e.getMessage());
}
}
}