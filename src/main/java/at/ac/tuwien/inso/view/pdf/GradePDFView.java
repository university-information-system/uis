package at.ac.tuwien.inso.view.pdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

import at.ac.tuwien.inso.entity.Grade;


public class GradePDFView extends AbstractPdfView {

    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA, 30);
    private static final Font SMALL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 8);
    private static final Paragraph EMPTY_LINE = new Paragraph(" ");


    @Override
    protected void buildPdfDocument(Map<String, Object> map,
                                    Document document,
                                    PdfWriter pdfWriter,
                                    HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse) throws Exception {
        Grade grade = (Grade) map.get("grade");
        String validationLink = (String) map.get("validationLink");

        document.add(generateTitle());
        document.add(EMPTY_LINE);
        document.add(generateContent(grade));
        document.add(EMPTY_LINE);
        document.add(generateValidation(validationLink));
    }

    private Paragraph generateTitle() {
        Paragraph title = new Paragraph(new Chunk(generateTitleString(), TITLE_FONT));
        title.setAlignment(Element.ALIGN_CENTER);
        return title;
    }

    private String generateTitleString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Grade certificate");

        return stringBuilder.toString();
    }

    private Paragraph generateContent(Grade grade) throws BadElementException {
        Paragraph content = new Paragraph();
        Table table = createTable(grade);

        content.add(table);
        return content;
    }


    private Table createTable(Grade grade) throws BadElementException {
        Table table = new Table(3);

        table = setTableOptions(table);

        Cell name = new Cell();
        name.setColspan(3);
        name.add(new Paragraph("Name: ", SMALL_FONT));
        name.add(new Paragraph(grade.getStudent().getName()));

        Cell course = new Cell();
        course.setColspan(3);
        course.add(new Paragraph("Course: ", SMALL_FONT));
        course.add(new Paragraph(grade.getCourse().getSubject().getName()));

        Cell semester = new Cell();
        semester.add(new Paragraph("Semester: ", SMALL_FONT));
        semester.add(new Paragraph(grade.getCourse().getSemester().getLabel()));

        Cell ects = new Cell();
        ects.add(new Paragraph("ECTS-Credits", SMALL_FONT));
        ects.add(new Paragraph(grade.getCourse().getSubject().getEcts().toPlainString()));

        Cell note = new Cell();
        note.add(new Paragraph("Note: ", SMALL_FONT));
        note.add(new Paragraph(grade.getMark().getMark() + ""));

        Cell lecturer = new Cell();
        lecturer.setColspan(3);
        lecturer.add(new Paragraph("Lecturer: ", SMALL_FONT));
        lecturer.add(new Paragraph(grade.getLecturer().getName()));

        table.addCell(name);
        table.addCell(course);
        table.addCell(semester);
        table.addCell(ects);
        table.addCell(note);
        table.addCell(lecturer);

        return table;
    }

    private Table setTableOptions(Table table) {
        table.setTableFitsPage(true);
        table.setCellsFitPage(true);
        table.setPadding(4);

        return table;
    }

    private Paragraph generateValidation(String validationLink) {
        Paragraph validation = new Paragraph(generateValidationString(validationLink), SMALL_FONT);
        return validation;
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
