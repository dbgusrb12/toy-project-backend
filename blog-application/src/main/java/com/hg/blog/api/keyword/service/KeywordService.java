package com.hg.blog.api.keyword.service;

import com.hg.blog.api.keyword.dto.KeywordDto.GetKeywordList;
import com.hg.blog.domain.keyword.service.KeywordQueryService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordQueryService keywordQueryService;

    @Transactional
    public List<GetKeywordList> getKeywords() {
        return keywordQueryService.getKeywordTop10().stream()
            .map(keywordGrouping -> new GetKeywordList(
                keywordGrouping.getCount(),
                keywordGrouping.getContent()
            ))
            .collect(Collectors.toList());
    }
}
