package com.example.demo.src.content.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentSimpleRes {
    private String contentTitle;
    private String contentPosterURL;
    private String contentURL;
}
