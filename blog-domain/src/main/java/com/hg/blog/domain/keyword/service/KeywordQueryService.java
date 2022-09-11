package com.hg.blog.domain.keyword.service;

import com.hg.blog.domain.keyword.entity.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeywordQueryService {

    private final KeywordRepository keywordRepository;

}
