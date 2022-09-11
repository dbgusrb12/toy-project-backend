package com.hg.blog.domain.keyword.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeywordEventPublisher {

    private final ApplicationEventPublisher appEventPublisher;

    public void keywordEvent(String content) {
        KeywordEvent event = new KeywordEvent(content);
        appEventPublisher.publishEvent(event);
    }
}
