package com.bezina.utils;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PDFSplitter {
    public static void splitPdf(File file, List<Integer> pages, String outputDir, String nameTemplate) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            Splitter splitter = new Splitter();
            List<PDDocument> splitPages = splitter.split(document);
            int pageIndex = 1;
            for (PDDocument pageDoc : splitPages) {
                if (pages.contains(pageIndex)) {
                    pageDoc.save(new File(outputDir, nameTemplate + "-" + pageIndex + ".pdf"));
                }
                pageDoc.close();
                pageIndex++;
            }
        }
    }
    public static List<Integer> parsePages(String pagesText) {
        try {
            return Arrays.stream(pagesText.split(","))
                    .flatMap(range -> {
                        if (range.contains("-")) {
                            String[] bounds = range.split("-");
                            int start = Integer.parseInt(bounds[0]);
                            int end = Integer.parseInt(bounds[1]);
                            if ( (start > 0) && (end >0 )){
                                if ((start > end)){
                                    return IntStream.rangeClosed( end,start).boxed();
                                }
                                else return IntStream.rangeClosed( start,end).boxed();
                            }
                            else {
                                throw new NumberFormatException();
                            }
                        } else {
                            return IntStream.of(Integer.parseInt(range)).boxed();
                        }
                    })
                    .collect(Collectors.toList());
        }
        catch (NumberFormatException e) {
            return Collections.emptyList();
        }
    }
}
