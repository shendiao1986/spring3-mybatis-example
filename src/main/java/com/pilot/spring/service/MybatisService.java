package com.pilot.spring.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pilot.alone.PilotStandAlone;
import com.pilot.mapper.CommentMapper;
import com.pilot.mapper.PersonMapper;
import com.pilot.mapper.PostMapper;
import com.pilot.model.Comment;
import com.pilot.model.Person;
import com.pilot.model.Post;

@Service
public class MybatisService {

	@Autowired
	private PostMapper postMapper;

	@Autowired
	private CommentMapper cmtMapper;

	@Autowired
	private PersonMapper personMapper;

	public void pilot() {
		deletetAll();
		savePersons();
		System.out.println("save successfully ");
		RowBounds prb = new RowBounds(7, 10);
		List<Comment> cmts = personMapper.selectAllComments(prb);
		//System.out.println(prb.getTotal());
		for (Comment cmt : cmts) {
			System.out.println(cmt.getContent());
		}

		System.out.println(cmtMapper.getCommentTotal());

	}

	@Transactional
	public void deletetAll() {
		cmtMapper.deleteAll();
		postMapper.deleteAll();
		personMapper.deleteAll();
	}

	@Transactional
	public void savePersons() {
		List<Person> persons = PilotStandAlone.constructPersons();
		for (Person p : persons) {
			personMapper.savePerson(p);
			Post post = p.getPost();
			post.setPersonId(p.getId());

			postMapper.savePost(post);
			for (Comment cmt : post.getComments()) {
				cmt.setPostId(post.getId());
				cmtMapper.saveComment(cmt);
			}

		}
	}

}
