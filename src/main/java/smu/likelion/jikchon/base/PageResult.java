package smu.likelion.jikchon.base;


import lombok.Getter;

import java.util.List;

import org.springframework.data.domain.Page;

@Getter
public class PageResult<T> {
    int currentPage;
    int pageSize;
    int totalPage;
    Long totalElements;
    List<T> content;

    public PageResult(Page<T> data) {
        currentPage = data.getPageable().getPageNumber();
        pageSize = data.getPageable().getPageSize();
        totalPage = data.getTotalPages();
        totalElements = data.getTotalElements();
        content = data.getContent();
    }

    public static <T> PageResult<T> ok(Page<T> data) {
        return new PageResult<>(data);
    }
}
