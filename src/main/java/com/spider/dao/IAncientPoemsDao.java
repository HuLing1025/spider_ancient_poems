package com.spider.dao;

import com.spider.pojo.AncientPoems;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IAncientPoemsDao {
    @Insert("INSERT INTO spider_ancient_poems (title, dynasty, author, content) VALUES(#{title}, #{dynasty}, #{author}, #{content})")
    int insertOne(AncientPoems ancientPoems);

    @Select("SELECT COUNT(*) FROM spider_ancient_poems WHERE title = #{title} AND author = #{author} ")
    int Exists(AncientPoems ancientPoems);
}
