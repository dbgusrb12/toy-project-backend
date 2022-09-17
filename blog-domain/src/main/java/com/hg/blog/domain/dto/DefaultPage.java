package com.hg.blog.domain.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

@Getter
public class DefaultPage<T> {

    private final List<T> content = new ArrayList<>();
    private final long totalElements;
    private final long totalPages;

    public DefaultPage(List<T> content, long totalElements, long totalPages) {
        this.content.addAll(content);
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public <U> DefaultPage<U> map(Function<? super T, ? extends U> converter) {
        return new DefaultPage<>(getConvertedContent(converter), this.totalElements, this.totalPages);
    }

    private <U> List<U> getConvertedContent(Function<? super T, ? extends U> converter) {
        Assert.notNull(converter, "Function must not be null");

        return this.content.stream().map(converter).collect(Collectors.toList());
    }

    public static <T> DefaultPage<T> of(Page<T> page) {
        return new DefaultPage<>(
            page.getContent(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
}
