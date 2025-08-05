package com.spring_rag.ai.controller;

import com.spring_rag.ai.service.DataLoaderService;
import org.springframework.ai.autoconfigure.chat.client.ChatClientAutoConfiguration;
import org.springframework.ai.autoconfigure.chat.client.ChatClientBuilderConfigurer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/chatclient")
public class ChatController {

    private final ChatClient chatClient;

    @Autowired
    private DataLoaderService dataLoaderService;

    @Autowired
    private VectorStore vStore;

    private final RewriteQueryTransformer.Builder rqtBuilder;

    public ChatController(ChatClient.Builder chatClientBuilder){
        this.chatClient = chatClientBuilder.build();
        this.rqtBuilder = RewriteQueryTransformer.builder().chatClientBuilder(chatClientBuilder);
    }

    @GetMapping("/dataingestion")
    void documentIngestion (@RequestParam String userInput){
        //dataLoaderService.load();
//        return this.chatClient.prompt()
//                .user(userInput)
//                .call()
//                .content();
    }

    @GetMapping("/promotions")
    String getSuggestedPromotions(@RequestParam String query, @RequestParam String target){
        PromptTemplate pt = new PromptTemplate("""
                {query}.
                {target}
                """);
        Prompt pmt = pt.create(Map.of("query", query, "target", target));
        Advisor ragAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.5)
                        .topK(7)
                        .vectorStore(vStore)
                        .build())
                .queryTransformers(rqtBuilder.promptTemplate(pt).build())
                .build();
        return this.chatClient.prompt(pmt)
                .advisors(ragAdvisor)
                .call()
                .content();

    }
}
