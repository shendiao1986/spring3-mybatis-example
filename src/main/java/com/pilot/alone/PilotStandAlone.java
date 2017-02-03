package com.pilot.alone;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.github.pagehelper.PageRowBounds;
import com.pilot.mapper.CommentMapper;
import com.pilot.mapper.PersonMapper;
import com.pilot.mapper.PostMapper;
import com.pilot.model.Comment;
import com.pilot.model.Person;
import com.pilot.model.Post;

public class PilotStandAlone {

	public static void main(String[] args) throws Exception {
		String resource = "mybatis/mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

		deleteAll(sqlSessionFactory);

		savePersons(sqlSessionFactory, constructPersons());

		System.out.println("save successfully ");

		pilotPagination(sqlSessionFactory);

		/*pilotExecuteViaXmlConfig(sqlSessionFactory);
		pilotExecuteViaMapper(sqlSessionFactory);
		pilotExecuteViaHybrid(sqlSessionFactory);
		
		pilotComplexObjectsViaMapperReleNestQuery(sqlSessionFactory);
		pilotComplexObjectsViaXmlReleNestResult(sqlSessionFactory);
		pilotComplexObjectsViaMapperReleNestResult(sqlSessionFactory);*/

	}

	public static List<Person> constructPersons() {
		Person p1 = new Person();
		p1.setFirstName("a");
		p1.setSecondName("a1");

		Post pt1 = new Post();
		pt1.setTitle("aaaa");

		Comment c11 = new Comment();
		c11.setContent("a-comment-1");
		pt1.getComments().add(c11);

		Comment c12 = new Comment();
		c12.setContent("a-comment-2");
		pt1.getComments().add(c12);

		Comment c13 = new Comment();
		c13.setContent("a-comment-3");
		pt1.getComments().add(c13);

		p1.setPost(pt1);

		Person p2 = new Person();
		p2.setFirstName("b");
		p2.setSecondName("b1");

		Post pt2 = new Post();
		pt2.setTitle("bbbb");

		Comment c21 = new Comment();
		c21.setContent("b-comment-1");
		pt2.getComments().add(c21);

		Comment c22 = new Comment();
		c22.setContent("b-comment-2");
		pt2.getComments().add(c22);

		Comment c23 = new Comment();
		c23.setContent("b-comment-3");
		pt2.getComments().add(c23);

		p2.setPost(pt2);

		Person p3 = new Person();
		p3.setFirstName("c");
		p3.setSecondName("c1");

		Post pt3 = new Post();
		pt3.setTitle("cccc");

		Comment c31 = new Comment();
		c31.setContent("c-comment-1");
		pt3.getComments().add(c31);

		Comment c32 = new Comment();
		c32.setContent("c-comment-2");
		pt3.getComments().add(c32);

		Comment c33 = new Comment();
		c33.setContent("c-comment-3");
		pt3.getComments().add(c33);

		p3.setPost(pt3);

		List<Person> persons = new ArrayList<>();
		persons.add(p1);
		persons.add(p2);
		persons.add(p3);

		return persons;
	}

	public static void savePersons(SqlSessionFactory sqlSessionFactory, List<Person> persons) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			PersonMapper ps = session.getMapper(PersonMapper.class);
			for (Person p : persons) {
				ps.savePerson(p);
				Post post = p.getPost();
				post.setPersonId(p.getId());

				PostMapper postMapper = session.getMapper(PostMapper.class);
				postMapper.savePost(post);

				CommentMapper cmtMapper = session.getMapper(CommentMapper.class);

				for (Comment cmt : post.getComments()) {
					cmt.setPostId(post.getId());
					cmtMapper.saveComment(cmt);
				}

			}

			session.commit();
		}
	}

	public static void deleteAll(SqlSessionFactory sqlSessionFactory) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			CommentMapper ct = session.getMapper(CommentMapper.class);
			ct.deleteAll();
			PostMapper pt = session.getMapper(PostMapper.class);
			pt.deleteAll();
			PersonMapper ps = session.getMapper(PersonMapper.class);
			ps.deleteAll();
			session.commit();
		}
	}

	public static void pilotPagination(SqlSessionFactory sqlSessionFactory) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			PersonMapper ps = session.getMapper(PersonMapper.class);
			PageRowBounds prb = new PageRowBounds(7, 10);
			List<Comment> cmts = ps.selectAllComments(prb);
			System.out.println(prb.getTotal());
			for (Comment cmt : cmts) {
				System.out.println(cmt.getContent());
			}
		}
	}

	public static void pilotExecuteViaXmlConfig(SqlSessionFactory sqlSessionFactory) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			List<Person> ps = session.selectList("selectAllAsList");
			for (Person p : ps) {
				System.out.println(p);
				p.getPost();
			}
		}
	}

	public static void pilotExecuteViaMapper(SqlSessionFactory sqlSessionFactory) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			PersonMapper ps = session.getMapper(PersonMapper.class);
			for (Person p : ps.selectAllAsMapCursor()) {
				System.out.println(p);
			}
		}
	}

	public static void pilotExecuteViaHybrid(SqlSessionFactory sqlSessionFactory) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			PersonMapper ps = session.getMapper(PersonMapper.class);
			for (Person p : ps.selectAllAsCursor()) {
				System.out.println(p);
			}
		}
	}

	public static void pilotComplexObjectsViaMapperReleNestQuery(SqlSessionFactory sqlSessionFactory) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			List<Person> ps = session.getMapper(PersonMapper.class).selectPersonViaReleNestQuery();
			for (Person p : ps) {
				System.out.println(p);
			}
		}
	}

	//通过xml resultmap的配置来控制是否是 关联嵌套结果 或者 关联嵌套查询
	public static void pilotComplexObjectsViaXmlReleNestResult(SqlSessionFactory sqlSessionFactory) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			List<Person> ps = session.selectList("selectAllComplexObjects");
			for (Person p : ps) {
				System.out.println(p);
			}
		}
	}

	//通过xml resultmap的配置来控制是否是 关联嵌套结果 或者 关联嵌套查询
	public static void pilotComplexObjectsViaMapperReleNestResult(SqlSessionFactory sqlSessionFactory) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			List<Person> ps = session.getMapper(PersonMapper.class).selectAllComplexes();
			for (Person p : ps) {
				System.out.println(p);
			}
		}
	}

}
