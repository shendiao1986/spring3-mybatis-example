package com.pilot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.RowBounds;

import com.pilot.model.Comment;
import com.pilot.model.Person;
import com.pilot.model.Post;

public interface PersonMapper {

	// @formatter:off

	public Cursor<Person> selectAllAsCursor();

	@Select("SELECT ID, FIRST_NAME,SECOND_NAME FROM PERSON")
	@Results({ @Result(property = "id", column = "ID", id = true),
			@Result(property = "firstName", column = "FIRST_NAME"),
			@Result(property = "secondName", column = "SECOND_NAME") })
	public Cursor<Person> selectAllAsMapCursor();

	@Delete("DELETE FROM PERSON")
	public void deleteAll();

	@Insert("INSERT INTO PERSON (ID, FIRST_NAME, SECOND_NAME) VALUES(#{id}, #{firstName}, #{secondName})")
	@SelectKey(before = true, keyProperty = "id", resultType = Long.class, statement = "SELECT S_PERSON_INDEX.NEXTVAL FROM dual")
	public void savePerson(Person person);

	/**
	 * 关联的嵌套 结果通过注解的话，必须要和XML中的resultmap来进行配合使用，仅仅通过注解是没办法做出和xml一样的配置的。注解不支持，仅支持关联的嵌套查询
	 * 
	 */
	@Select("SELECT p.ID as PID, p.FIRST_NAME, p.SECOND_NAME, pt.PERSON_ID, pt.ID as PTID, pt.TITLE, ct.ID as CTID, ct.POST_ID, ct.CONTENT FROM PERSON p left outer join Post pt on p.ID=pt.PERSON_ID left outer join \"COMMENT\" ct on ct.POST_ID=pt.ID")
	@ResultMap("PersonResultMap")
	public List<Person> selectAllComplexes();

	
	@Select("SELECT ID as PID, FIRST_NAME,SECOND_NAME FROM PERSON")
	@Results({
		@Result(id = true, property = "id", column = "PID"),
		@Result(property = "firstName", column = "FIRST_NAME"), 
		@Result(property = "secondName", column = "SECOND_NAME"),
		@Result(property = "post", column = "{personId=PID,ddd=FIRST_NAME}", one = @One(select="selectPostByPersonId")) 
	})
	public List<Person> selectPersonViaReleNestQuery();

	@Select("SELECT pt.PERSON_ID, pt.ID as PTID, pt.TITLE from Post pt where pt.PERSON_ID=#{personId}")
	@Results({ 
		@Result(id = true, property = "id", column = "PTID"),
		@Result(property = "personId", column = "PERSON_ID"), 
		@Result(property = "title", column = "TITLE"),
		@Result(property = "comments", column = "PTID", many = @Many(select = "selectCommontByPostId")) 
	})
	public List<Post> selectPostByPersonId(@Param("personId") Long personId,@Param("ddd") String ddd);

	@Select("SELECT ct.ID as CTID, ct.POST_ID, ct.CONTENT FROM \"COMMENT\" ct WHERE ct.POST_ID=#{postId}")
	@Results({ 
		@Result(id = true, property = "id", column = "CTID"), 
		@Result(property = "postId", column = "POST_ID"),
		@Result(property = "content", column = "CONTENT") 
	})
	public List<Comment> selectCommontByPostId(@Param("postId") Long postId);
	
	@Select("SELECT ct.ID as CTID, ct.POST_ID, ct.CONTENT FROM \"COMMENT\" ct")
	@Results({ 
		@Result(id = true, property = "id", column = "CTID"), 
		@Result(property = "postId", column = "POST_ID"),
		@Result(property = "content", column = "CONTENT") 
	})
	public List<Comment> selectAllComments(RowBounds bounds);


}
