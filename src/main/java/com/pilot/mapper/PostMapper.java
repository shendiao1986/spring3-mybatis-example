package com.pilot.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.SelectKey;

import com.pilot.model.Post;

public interface PostMapper {

	@Insert("INSERT INTO POST (ID, PERSON_ID, TITLE) VALUES(#{id}, #{personId}, #{title})")
	@SelectKey(before = true, keyProperty = "id", resultType = Long.class, statement = "SELECT S_POST_INDEX.NEXTVAL FROM dual")
	public void savePost(Post post);

	@Delete("DELETE FROM POST")
	public void deleteAll();
}
