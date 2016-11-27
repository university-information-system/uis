package at.ac.tuwien.inso.view.pdf;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.ac.tuwien.inso.entity.Grade;


public class GradePDFView extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> map,
                                    Document document,
                                    PdfWriter pdfWriter,
                                    HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse) throws Exception {

        Grade grade = (Grade) map.get("grade");
        String validationLink = (String) map.get("validationLink");

        Paragraph header = new Paragraph(new Chunk(generateHeaderString(grade), FontFactory.getFont(FontFactory.HELVETICA, 30)));
        Paragraph note = new Paragraph(new Chunk(generateNoteString(grade), FontFactory.getFont(FontFactory.HELVETICA, 30)));
        Paragraph validation = new Paragraph(new Chunk(generateValidationString(validationLink), FontFactory.getFont(FontFactory.HELVETICA, 20)));


        document.add(header);
        document.add(note);
        document.add(validation);

    }

    private String generateHeaderString(Grade grade) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(grade.getStudent().getName());
        stringBuilder.append(" actively participated in the course ");
        stringBuilder.append(grade.getCourse().getSubject().getName());
        stringBuilder.append(" in ");
        stringBuilder.append(grade.getCourse().getSemester().getLabel());
        stringBuilder.append("");

        return stringBuilder.toString();
    }

    private String generateNoteString(Grade grade) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" and was graded with ");
        stringBuilder.append(grade.getMark());
        stringBuilder.append(" by ");
        stringBuilder.append(grade.getLecturer().getName());
        return stringBuilder.toString();
    }

    private String generateValidationString(String validationLink) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("This is an automatically generated document. ");
        stringBuilder.append("The document is valid without a signature. ");
        stringBuilder.append("To validate the document, please go to: ");
        stringBuilder.append(validationLink);
        return stringBuilder.toString();
    }
}
