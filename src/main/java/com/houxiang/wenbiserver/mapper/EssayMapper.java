package com.houxiang.wenbiserver.mapper;

import com.houxiang.wenbiserver.model.Essay;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EssayMapper {

    //增加一篇文章
    public int insertEssay(Essay essay);

    //保证同一个作者文章标题的唯一性
    public int selectEssayByAuthorIdAndEssayTitle(@Param("authorId") int authorId,@Param("essayTitle") String essayTitle);

    //通过文章id查询文章信息
    public Essay selectEssayByEssayId(@Param("essayId") int essayId,@Param("userId") int userId);

    //收藏文章
    public int collectEssay(@Param("essayId") int essayId,@Param("userId") int userId);

    //通过UserId获取用户收藏的文章
    public List<Essay> getCollectEssayByUserId(@Param("userId")int userId);

    //通过UserId获得用户创造的文章
    public List<Essay> getCreateEssayByUserId(@Param("userId")int userId);

    //获取最新10篇文章
    public List<Essay> getLastTenEssay();

    //递增访问次数
    public int addVisitCount(@Param("essayId") int essayId);

    //查询最近10000篇文章内的访问数和收藏数
    public List<Essay> getHotEssayData();
}
