package com.ly.aidemo.data;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Component
public class DocsReferenceReader {

    private static final Logger logger = LoggerFactory.getLogger(DocsReferenceReader.class.getName());
    private final JdbcClient jdbcClient;
    private final VectorStore vectorStore;

    @Value("classpath:/docs/faq_cafeteria.pdf")
    private Resource docResource;

    public DocsReferenceReader(JdbcClient jdbcClient, VectorStore vectorStore) {
        this.jdbcClient = jdbcClient;
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void init() {
        logger.info("Reading reference docs from {}", docResource.getFilename());
        var count = jdbcClient.sql("select count(*) from vector_store")
                .query(Integer.class).single();

        logger.info("Current count of the Vector Store:{}", count);

        if (count == 0) {
            logger.info("Loading Spring Boot Reference Document into Vector Store");

            var config = PdfDocumentReaderConfig.builder()
                    .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder()
                            .withNumberOfBottomTextLinesToDelete(0)
                            .withNumberOfTopPagesToSkipBeforeDelete(0)
                            .build())
                    .withPagesPerDocument(1)
                    .build();

            var pdfReader = new PagePdfDocumentReader(docResource, config);
            var textSplitter = new TokenTextSplitter();
            vectorStore.accept(textSplitter.apply(pdfReader.get()));

            logger.info("Application is ready");
        }
    }
}
