package com.example.demo.src.content;

import com.example.demo.config.BaseException;
import com.example.demo.src.content.model.*;
import com.example.demo.utils.JwtService;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ContentProvider {

    private final ContentDao contentDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ContentProvider(ContentDao contentDao, JwtService jwtService) {
        this.contentDao = contentDao;
        this.jwtService = jwtService;
    }

    public GetContentDetailRes getContentDetail(int contentId, int profileId, int seasonIdx) throws BaseException {
        try {
            GetContentDetailRes getContentDetailRes = contentDao.getContentDetail(contentId, profileId, seasonIdx);
            return getContentDetailRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetContentSimpleRes> getCollectionContents(int contentId) throws BaseException {
        try {
            List<GetContentSimpleRes> getCollectionContentsRes = contentDao.getCollectionContents(contentId);
            return getCollectionContentsRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetContentSimpleRes> getMoreLikeThisContents(int contentId) throws BaseException {
        try {
            List<GetContentSimpleRes> getMoreLikeThisContentsRes = contentDao.getMoreLikeThisContents(contentId);
            return getMoreLikeThisContentsRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetVideoSimpleRes> getTrailerAndMoreContents(int contentId) throws BaseException {
        try {
            List<GetVideoSimpleRes> getTrailerAndMoreContentsRes = contentDao.getTrailerAndMoreContents(contentId);
            return getTrailerAndMoreContentsRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPackageContentRes> getPackageContents() throws BaseException {
        try {
            System.out.println("1");
            List<GetPackageContentRes> getPackageSimplesRes = contentDao.getPackageContents();
            return getPackageSimplesRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
