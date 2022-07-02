package com.example.demo.src.content;

import com.example.demo.src.content.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContentDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetContentInfoRes getContentInfo(int contentId, int profileId) {
        String getContentByContentIdQuery = "select previewImgURL,previewVideoURL,contentTitle,releasedYear,maturityRating," +
                " (select count(*) from Season where contentId =?) as seasonCount," +
                " contentIntroduction,contentPosterURL,contentCatchPhrase,isReleased," +
                " isAvailToDownload, (IF((select contentId from MyList where contentId =?) is not null, 'Y', 'N')) as isInMyList," +
                " matchScore as myMatchScore, myRate from Content" +
                " left outer join MyRate MR on Content.contentId = MR.contentId" +
                " left outer join MyMatchScore MMS on Content.contentId = MMS.contentId" +
                " where Content.contentId =? and MR.profileId = ?";
        Object[] getContentInfoByContentIdParams = new Object[] {contentId, contentId, contentId, profileId};
        return this.jdbcTemplate.queryForObject(getContentByContentIdQuery,
                (rs, rowNum) -> new GetContentInfoRes(
                        rs.getString("previewImgURL"),
                        rs.getString("previewVideoURL"),
                        rs.getString("contentTitle"),
                        rs.getInt("releasedYear"),
                        rs.getString("maturityRating"),
                        rs.getInt("seasonCount"),
                        rs.getString("contentIntroduction"),
                        rs.getString("contentPosterURL"),
                        rs.getString("contentCatchPhrase"),
                        rs.getString("isReleased"),
                        rs.getString("isAvailToDownload"),
                        rs.getString("isInMyList"),
                        rs.getInt("myMatchScore"),
                        rs.getInt("myRate")),
                getContentInfoByContentIdParams);
    }

    public List<String> getContentCreators(int contentId) {
        String getContentCreatorByContentIdQuery = "select creatorName from ContentCreator where contentId =?";
        int getContentCreatorByContentIdParams = contentId;

        return this.jdbcTemplate.query(getContentCreatorByContentIdQuery,
                (rs, rowNum) -> new String(
                        rs.getString("creatorName")),
                getContentCreatorByContentIdParams);
    }

    public List<String> getContentGenres(int contentId) {
        String getContentGenreByContentIdQuery = "select genreName from ContentGenre where contentId=?";
        int getContentGenreByContentIdParam = contentId;

        return this.jdbcTemplate.query(getContentGenreByContentIdQuery,
                (rs, rowNum) -> new String(
                        rs.getString("genreName")),
                getContentGenreByContentIdParam);
    }

    public List<String> getContentCasts(int contentId) {
        String getContentCastByContentIdQuery = "select castName from ContentCast where contentId =?";
        int getContentCastByContentIdParam = contentId;

        return this.jdbcTemplate.query(getContentCastByContentIdQuery,
                (rs, rowNum) -> new String(
                        rs.getString("castName")),
                getContentCastByContentIdParam);
    }

    public List<String> getContentKeywords(int contentId) {
        String getContentKeywordByContentIdQuery = "select keywordName from ContentKeyword where contentId =?";
        int getContentKeywordByContentIdParam = contentId;

        return this.jdbcTemplate.query(getContentKeywordByContentIdQuery,
                (rs, rowNum) -> new String(
                        rs.getString("keywordName")),
                getContentKeywordByContentIdParam);
    }

    public List<GetEpisodeInfoRes> getEpisodeInfos(int contentId, int seasonIdx) {
        String getEpisodeInfoByContentIdQuery = "select Episode.episodePartNo as partNo,episodeTitle,(IF(MD.status = 'SUCCESS', episodeLocalURL, videoURL)) as videoURL," +
                " (time_format(videoLength, '%l시간 %i분')) as videoLength,episodeIntroduction," +
                " cast((time_to_sec(watchingTime) / time_to_sec(videoLength) * 100) AS signed integer) as watchingRate," +
                " MD.status as downloadStatus" +
                " from Episode" +
                " left outer join MyHistory MH on Episode.contentId = MH.contentId and MH.seasonIdx = Episode.seasonIdx and MH.episodePartNo = Episode.episodePartNo" +
                " left outer join MyDownload MD on Episode.episodeId = MD.episodeId" +
                " where Episode.contentId = ? and Episode.seasonIdx = ?";
        Object[] getEpisodeInfoByContentIdParam = new Object[]{contentId, seasonIdx};

        return this.jdbcTemplate.query(getEpisodeInfoByContentIdQuery,
                (rs, rowNum) -> new GetEpisodeInfoRes(
                        rs.getInt("partNo"),
                        rs.getString("episodeTitle"),
                        rs.getString("videoURL"),
                        rs.getString("videoLength"),
                        rs.getString("episodeIntroduction"),
                        rs.getString("watchingRate"),
                        rs.getString("downloadStatus")),
                getEpisodeInfoByContentIdParam);
    }

    public GetCurrentWatchRes getCurrentWatch(int contentId, int profileId) {
        String getCurrentWatchResByContentIdQuery = "select MyHistory.episodePartNo as episodeIdx, MyHistory.seasonIdx," +
                " time_format(SEC_TO_TIME(time_to_sec(videoLength) - time_to_sec(watchingTime)), '%l시간 %i분') as remainTime," +
                " cast((time_to_sec(watchingTime) / time_to_sec(videoLength) * 100) AS signed integer) as watchingRate," +
                " episodeTitle from MyHistory" +
                " inner join Episode E on MyHistory.contentId = E.contentId and E.seasonIdx = MyHistory.seasonIdx and E.episodePartNo = MyHistory.episodePartNo" +
                " where (MyHistory.contentId, MyHistory.updatedAt) in ( select contentId, max(updatedAt) as updatedAt from MyHistory group by contentId)" +
                " and E.contentId = ? and profileId = ?" +
                " order by MyHistory.updatedAt desc";
        Object[] getCurrentWatchResByContentIdParam = new Object[]{contentId, profileId};
        return this.jdbcTemplate.queryForObject(getCurrentWatchResByContentIdQuery,
                (rs, rowNum) -> new GetCurrentWatchRes(
                        rs.getInt("seasonIdx"),
                        rs.getInt("episodeIdx"),
                        rs.getString("episodeTitle"),
                        rs.getString("remainTime"),
                        rs.getInt("watchingRate")
                ), getCurrentWatchResByContentIdParam);
    }

    public GetContentDetailRes getContentDetail(int contentId, int profileId, int seasonIdx) {
        return new GetContentDetailRes(
                getContentInfo(contentId, profileId),
                getContentCreators(contentId),
                getContentCasts(contentId),
                getContentGenres(contentId),
                getContentKeywords(contentId),
                getEpisodeInfos(contentId, seasonIdx),
                getCurrentWatch(contentId, profileId)
        );
    }

    public List<GetContentSimpleRes> getCollectionContents(int contentId) {
        String getCollectionContentsByContentIdQuery = "select contentTitle, contentPosterURL, contentURL" +
                " from Collection" +
                " inner join Content C on Collection.collectionContentId = C.contentId" +
                " where Collection.contentId = ?";
        int getContentCollectionByContentIdParam = contentId;

        return this.jdbcTemplate.query(getCollectionContentsByContentIdQuery,
                (rs, rowNum) -> new GetContentSimpleRes(
                        rs.getString("contentTitle"),
                        rs.getString("contentPosterURL"),
                        rs.getString("contentURL")
                ), getContentCollectionByContentIdParam);
    }

    public List<GetContentSimpleRes> getMoreLikeThisContents(int contentId) {
        String getMoreLikeThisContentsByContentIdQuery = "select contentTitle, contentPosterURL, contentURL" +
                " from MoreLikeThis" +
                " inner join Content C on MoreLikeThis.moreLikeThisContentId = C.contentId" +
                " where MoreLikeThis.contentId = ?";
        int getMoreLikeThisContentByContentIdParam = contentId;

        return this.jdbcTemplate.query(getMoreLikeThisContentsByContentIdQuery,
                (rs, rowNum) -> new GetContentSimpleRes(
                        rs.getString("contentTitle"),
                        rs.getString("contentPosterURL"),
                        rs.getString("contentURL")
                ), getMoreLikeThisContentByContentIdParam);
    }

    public List<GetVideoSimpleRes> getTrailerAndMoreContents(int contentId) {
        String getTrailerAndMoreContentsByContentIdQuery = "select videoTitle, videoURL" +
                " from TrailerAndMore" +
                " where TrailerAndMore.contentId = ?";
        int getTrailerAndMoreContentsByContentIdParam = contentId;

        return this.jdbcTemplate.query(getTrailerAndMoreContentsByContentIdQuery,
                (rs, rowNum) -> new GetVideoSimpleRes(
                        rs.getString("videoTitle"),
                        rs.getString("videoURL")
                ), getTrailerAndMoreContentsByContentIdParam);
    }

    public List<GetPackageContentRes> getPackageContents() {
        String getPackageIdsQuery = "select P.packageId, packageTitle" +
                " from Content_Package_MAP" +
                " inner join Package P on Content_Package_MAP.packageId = P.packageId" +
                " group by Content_Package_MAP.packageId" +
                " having count(contentId) >= ?";
        int getPackageIdsParam = 3;      // 보여 줄 최소 Package 의 컨텐츠 개수
        String getPackageContentSimplesQuery = "select contentTitle, contentPosterURL, contentURL" +
                " from Package" +
                " inner join Content_Package_MAP CPM on Package.packageId = CPM.packageId" +
                " inner join Content C on CPM.contentId = C.contentId" +
                " where Package.packageId = ?" +
                " order by Package.packageId asc";
        int getPackageContentSimpleParam;
        List<GetPackageSimpleRes> packageSimpleList = this.jdbcTemplate.query(getPackageIdsQuery,
                (rs, rowNum) -> new GetPackageSimpleRes(
                        rs.getInt("packageId"),
                        rs.getString("packageTitle")
                ), getPackageIdsParam);

        System.out.println(packageSimpleList.size()); //test: 3

        List<GetPackageContentRes> getPackageContents = new ArrayList<GetPackageContentRes>();

        for(int i = 0; i < packageSimpleList.size(); i++) {
            getPackageContentSimpleParam = packageSimpleList.get(i).getPackageId();
            List<GetContentSimpleRes> contentSimpleList = this.jdbcTemplate.query(getPackageContentSimplesQuery,
                    (rs, rowNum) -> new GetContentSimpleRes(
                            rs.getString("contentTitle"),
                            rs.getString("contentPosterURL"),
                            rs.getString("contentURL")
                    ), getPackageContentSimpleParam);

            getPackageContents.add(new GetPackageContentRes(
                    packageSimpleList.get(i).getPackageId(),
                    packageSimpleList.get(i).getPackageTitle(),
                    contentSimpleList
                    ));
        }

        return getPackageContents;
    }
}
