package com.hg.blog.domain.keyword.service;

import com.hg.blog.domain.keyword.entity.Keyword;
import com.hg.blog.domain.keyword.entity.KeywordRepository;
import com.hg.blog.domain.keyword.event.KeywordEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeywordCommandService {

    private final KeywordRepository keywordRepository;
    private final KeywordEventPublisher keywordEventPublisher;

    public void addKeywordEvent(String content) {
        try {
            keywordEventPublisher.keywordEvent(content);
        } catch (Exception e) {
            log.error("add keyword event error class ==> {}", e.getClass().getName());
            log.error("add keyword event error ==> {}", e.getMessage());
        }
    }

    public Keyword addKeyword(String content) {
        Keyword keyword = Keyword.of(content);
        return keywordRepository.save(keyword);
    }
}
