package com.houxiang.wenbiserver.mapper;

import com.houxiang.wenbiserver.model.Essay;
import org.apache.ibatis.annotations.Param;

public interface EssayMapper {

    //增加一篇文章
    public int insertEssay(Essay essay);

    //保证同一个作者文章标题的唯一性
    public int selectEssayByAuthorIdAndEssayTitle(@Param("authorId") int authorId,@Param("essayTitle") String essayTitle);

    //通过文章id查询文章信息
    public Essay selectEssayByEssayId(@Param("essayId") int essayId);
}
