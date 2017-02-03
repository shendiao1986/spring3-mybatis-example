package com.pilot.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.SelectKey;

import com.pilot.model.Comment;

public interface CommentMapper {

	@Insert("INSERT INTO \"COMMENT\" (ID, POST_ID, CONTENT) VALUES(#{id}, #{postId}, #{content})")
	@SelectKey(before = true, keyProperty = "id", resultType = Long.class, statement = "SELECT S_COMMENT_INDEX.NEXTVAL FROM dual")
	public void saveComment(Comment comment);

	@Delete("DELETE FROM \"COMMENT\"")
	public void deleteAll();

}
