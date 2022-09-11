package com.hg.blog.domain.keyword.event;

import com.hg.blog.domain.keyword.service.KeywordCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeywordEventListener {

    private final KeywordCommandService keywordCommandService;

    @Async
    @EventListener
    public void keywordEvent(KeywordEvent event) {
        keywordCommandService.addKeyword(event.getContent());
    }
}
