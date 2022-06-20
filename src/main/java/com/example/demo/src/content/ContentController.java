package com.example.demo.src.content;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.content.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/contents")
public class ContentController {

    @Autowired
    private final ContentProvider contentProvider;
    @Autowired
    private final ContentService contentService;
    @Autowired
    private final JwtService jwtService;

    public ContentController(ContentProvider contentProvider, ContentService contentService, JwtService jwtService) {
        this.contentProvider = contentProvider;
        this.contentService = contentService;
        this.jwtService = jwtService;
    }

    /**
     * 컨텐츠 상세 정보 조회
     * [GET] /contents/:contentId
     * @return BaseResponse<GetContentRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{contentId}")  // (GET) 127.0.0.1:9000/app/contents/:contentId?profileId=&seasonIdx=
    public BaseResponse<GetContentDetailRes> getContent(@PathVariable("contentId") int contentId, @RequestParam int profileId, @RequestParam(defaultValue = "1") int seasonIdx) {
        // Get Content
        try {
            GetContentDetailRes getContentDetailRes = contentProvider.getContentDetail(contentId, profileId, seasonIdx);
            return new BaseResponse<>(getContentDetailRes);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 컨텐츠 컬렉션 목록 조회
     * [GET] /contents/:contentId/collection
     * @return BaseResponse<List<GetCollectionRes>>
     */
    @ResponseBody
    @GetMapping("/{contentId}/collection")
    public BaseResponse<List<GetContentSimpleRes>> getCollections(@PathVariable("contentId") int contentId) {
        // Get Collection List
        try {
            List<GetContentSimpleRes> getCollectionsRes = contentProvider.getContentCollection(contentId);
            return new BaseResponse<>(getCollectionsRes);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
