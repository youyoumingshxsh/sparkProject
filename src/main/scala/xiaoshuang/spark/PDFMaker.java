package xiaoshuang.spark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;

public class PDFMaker {

    private static int fontSize = 10;
    private static PDSimpleFont font = PDType1Font.COURIER_BOLD;

    public static void addFile(PDDocument document, PDPage page, File file)
            throws Exception {

        final int margin = 40;
        float height = font.getFontDescriptor().getFontBoundingBox()
                .getHeight() / 1000;

        // calculate font height and increase by 5 percent.
        height = height * fontSize * 1.05f;
        BufferedReader data = new BufferedReader(new FileReader(file));
        String nextLine = null;
        PDPageContentStream contentStream = null;
        float y = -1;
        float maxStringLength = page.getMediaBox().getWidth() - 2 * margin;

        // There is a special case of creating a PDF document from an empty
        // string.
        boolean textIsEmpty = true;

        document.addPage(page);
        contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(font, fontSize);
        contentStream.beginText();
        y = page.getMediaBox().getHeight() - margin + height;
        contentStream.moveTextPositionByAmount(margin, y);

        while ((nextLine = data.readLine()) != null) {

            // The input text is nonEmpty. New pages will be created and
            // added
            // to the PDF document as they are needed, depending on the
            // length of
            // the text.
            textIsEmpty = false;

            String[] lineWords = nextLine.trim().split(" ");
            int lineIndex = 0;
            while (lineIndex < lineWords.length) {
                StringBuffer nextLineToDraw = new StringBuffer();
                float lengthIfUsingNextWord = 0;
                do {
                    nextLineToDraw.append(lineWords[lineIndex]);
                    nextLineToDraw.append(" ");
                    lineIndex++;
                    if (lineIndex < lineWords.length) {
                        String lineWithNextWord = nextLineToDraw.toString()
                                + lineWords[lineIndex];
                        lengthIfUsingNextWord = (font
                                .getStringWidth(lineWithNextWord) / 1000)
                                * fontSize;
                    }
                } while (lineIndex < lineWords.length
                        && lengthIfUsingNextWord < maxStringLength);
                if (y < margin) {
                    // We have crossed the end-of-page boundary and need to
                    // extend the
                    // document by another page.
                    page = new PDPage();
                    document.addPage(page);
                    if (contentStream != null) {
                        contentStream.endText();
                        contentStream.close();
                    }
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(font, fontSize);
                    contentStream.beginText();
                    y = page.getMediaBox().getHeight() - margin + height;
                    contentStream.moveTextPositionByAmount(margin, y);
                }
                // System.out.println( "Drawing string at " + x + "," + y );

                if (contentStream == null) {
                    throw new IOException(
                            "Error:Expected non-null content stream.");
                }
                contentStream.moveTextPositionByAmount(0, -height);
                y -= height;
                contentStream.drawString(nextLineToDraw.toString());
            }

        }

        // If the input text was the empty string, then the above while loop
        // will have short-circuited
        // and we will not have added any PDPages to the document.
        // So in order to make the resultant PDF document readable by Adobe
        // Reader etc, we'll add an empty page.
        if (textIsEmpty) {
            document.addPage(page);
        }

        if (contentStream != null) {
            contentStream.endText();
            contentStream.close();
        }
    }

    public static PDPage addDir(PDDocument document, PDOutlineItem root,
                                PDPage page, File dir) throws Exception {
        if (!dir.isDirectory()) {
            throw new Exception("not directory");
        }

        for (File file : dir.listFiles()) {
            if (page == null) {
                page = new PDPage();
            }
            String name = file.getName();
            PDOutlineItem item = new PDOutlineItem();
            item.setTitle(name);
            root.addFirst(item);
            //root.appendChild(item);
            if (file.isDirectory()) {
                addDir(document, item, page, file);
            } else {
                addFile(document, page, file);
            }
            item.setDestination(page);
            page = null;
        }

        return page;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            PDDocument document = new PDDocument();
            PDDocumentOutline outline = new PDDocumentOutline();
            document.getDocumentCatalog().setDocumentOutline(outline);

            // Create a root element to show in the tree
            PDOutlineItem root = new PDOutlineItem();
            root.setTitle("Outline");
            outline.addFirst(root);
            //outline.appendChild(root);

            addDir(document, root, null, new File("/opt/software"));

            document.save("test.pdf");
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

