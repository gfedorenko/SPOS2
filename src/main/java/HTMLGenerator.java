//package main.java;
//import j2html.tags.ContainerTag;
//
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.util.ArrayList;
//
//import static j2html.TagCreator.*;
//
//public class HTMLGenerator {
//
//    public static void generate(ArrayList<Token> tokens) {
//
//        // render html string using j2html library
//        String html = html(
//                body(
//                        div(
//                                tokens.stream().map(token ->
//                                        p(token.getContent()).withClass(token.getTokenType())
//                                ).toArray(ContainerTag[]::new)
//                        )
//                )
//        ).render();
//
//        // write html string to output file
//        FileWriter fileWriter;
//        BufferedWriter bufferedWriter;
//        try {
//            fileWriter = new FileWriter("output/output.html");
//            bufferedWriter = new BufferedWriter(fileWriter);
//            bufferedWriter.write(html);
//            bufferedWriter.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}