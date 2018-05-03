//package xiaoshuang.spark;
//
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.font.PDFont;
//import org.apache.pdfbox.pdmodel.font.PDType1Font;
//import scala.tools.nsc.doc.base.comment.Paragraph;
//
//import java.io.IOException;
//public class PdfBoxTest {
//    public static void main(String[] args) throws IOException {
//        createBlank("test.pdf");
//    }
//
//    /***
//     * 创建1到多个空白页面
//     */
//    public static void createBlank(String outputFile) throws IOException {
//        //首先创建pdf文档类
//        PDDocument document = null;
//        try {
//            document = new PDDocument();
//            //实例化pdf页对象
//            PDPage blankPage = new PDPage();
//            PDFont font = PDType1Font.HELVETICA_BOLD;
//            PDPageContentStream content = new PDPageContentStream(document, blankPage);
//            content.beginText();
//            content.setFont(font, 15);
//            content.moveTextPositionByAmount(100, 700);
//            content.drawString("1. hello,page 1");
//            content.drawString("1.1 xitongzhibiao");
//
//            content.endText();
//            content.close();
//            PDPage blankPage1 = new PDPage();
//            PDPageContentStream content1 = new PDPageContentStream(document, blankPage1);
//            content1.beginText();
//            content1.setFont(font, 15);
//            content1.moveTextPositionByAmount(100, 700);
//            content1.drawString("hello,page 2s");
//
//            content1.endText();
//            content1.close();
//            PDPage blankPage2 = new PDPage();
//            //插入文档类
//            document.addPage(blankPage);
//            document.addPage(blankPage1);
//            document.addPage(blankPage2);
//            //记得一定要写保存路径,如"H:\\text.pdf"
//            document.save(outputFile);
//            System.out.println("over");
//        } finally {
//            if (document != null) {
//                document.close();
//            }
//        }
//    }
//}
