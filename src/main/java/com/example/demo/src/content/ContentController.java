package com.example.demo.src.content;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.content.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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
     * 컬렉션 컨텐츠 목록 조회
     * [GET] /contents/:contentId/collection
     * @return BaseResponse<List<GetCollectionRes>>
     */
    @ResponseBody
    @GetMapping("/{contentId}/collection")
    public BaseResponse<List<GetContentSimpleRes>> getCollectionContents(@PathVariable("contentId") int contentId) {
        // Get Collection List
        try {
            List<GetContentSimpleRes> getCollectionContentsRes = contentProvider.getCollectionContents(contentId);
            return new BaseResponse<>(getCollectionContentsRes);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 함께 시청된 컨텐츠 목록 조회
     * [GET] /contents/:contentId/more-like-this
     * @return BaseResponse<List<GetContentSimpleRes>>
     */
    @ResponseBody
    @GetMapping("/{contentId}/more-like-this")
    public BaseResponse<List<GetContentSimpleRes>> getMoreLikeThisContents(@PathVariable("contentId") int contentId) {
        // Get MoreLikeThis List
        try {
            List<GetContentSimpleRes> getMoreLikeThisContentsRes = contentProvider.getMoreLikeThisContents(contentId);
            return new BaseResponse<>(getMoreLikeThisContentsRes);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 예고편 및 다른 영상 목록 조회
     * [GET] /contents/:contentId/trailer-and-more
     * @return BaseResponse<List<GetVideoSimpleRes>>
     */
    @ResponseBody
    @GetMapping("/{contentId}/trailer-and-more")
    public BaseResponse<List<GetVideoSimpleRes>> getTrailerAndMoreContents(@PathVariable("contentId") int contentId) {
        // Get TrailerAndMore List
        try {
            List<GetVideoSimpleRes> getTrailerAndMoreContentsRes = contentProvider.getTrailerAndMoreContents(contentId);
            return new BaseResponse<>(getTrailerAndMoreContentsRes);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 컨텐츠 전체 목록 조회
     * [GET] /contents
     * @return BaseResponse<List<GetPackageContentRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetPackageContentRes>> getPackageContents() {
        // Get Contents List
        try {
            List<GetPackageContentRes> getPackageContentsRes = contentProvider.getPackageContents();
            return new BaseResponse<>(getPackageContentsRes);
        } catch(BaseException exception) {
            System.out.println(exception);
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
