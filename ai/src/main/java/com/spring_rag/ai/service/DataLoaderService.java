package com.spring_rag.ai.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class DataLoaderService {
    private static final Logger logger = LoggerFactory.getLogger(DataLoaderService.class);

    @Value("classpath:/data/Promotion_Selection_Best_Practices.pdf")
    private Resource pdfResource;

    private VectorStore vectorStore;

    public DataLoaderService( VectorStore vectorStore1){
        this.vectorStore = vectorStore1;
    }

    public void load() {
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(this.pdfResource,
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder().build())
                        .withPagesPerDocument(1).build());
        var tokenTextSplitter = new TokenTextSplitter();
        this.vectorStore.accept(tokenTextSplitter.apply(pdfReader.get()));

    }
}
