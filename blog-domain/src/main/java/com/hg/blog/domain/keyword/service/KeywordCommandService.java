package com.hg.blog.domain.keyword.service;

import com.hg.blog.domain.keyword.entity.Keyword;
import com.hg.blog.domain.keyword.entity.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeywordCommandService {

    private final KeywordRepository keywordRepository;

    public Keyword addKeyword(String content) {
        Keyword keyword = Keyword.of(content);
        return keywordRepository.save(keyword);
    }
}
