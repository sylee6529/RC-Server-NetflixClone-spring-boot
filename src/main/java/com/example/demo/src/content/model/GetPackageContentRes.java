package com.example.demo.src.content.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetPackageContentRes {
    private int packageId;
    private String packageTitle;
    private List<GetContentSimpleRes> contentList;
}