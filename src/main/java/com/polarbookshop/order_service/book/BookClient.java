package com.polarbookshop.order_service.book;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class BookClient {
    private static final String BOOKS_ROOT_API = "/books/";
    private final WebClient webClient;

    public BookClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Book> getBookByIsbn(String isbn) {
        return webClient
                .get()
                .uri(BOOKS_ROOT_API + isbn)
                .retrieve()
                .bodyToMono(Book.class)
                .timeout(Duration.ofSeconds(3), Mono.empty())
                //return empty mono when resource is not found
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> Mono.empty())
                //retries are used when the service might be momentarily overloaded.
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
                //return empty mono for all other errors after retries.
                .onErrorResume(Exception.class, ex -> Mono.empty());
    }
}