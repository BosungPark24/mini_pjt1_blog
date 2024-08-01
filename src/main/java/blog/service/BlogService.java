package blog.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import blog.entity.BlogEntity;
import blog.entity.FileEntity;

@Service
public interface BlogService {

	List<BlogEntity> selectBlogList();

	void insertBlog(BlogEntity blogEntity, MultipartHttpServletRequest request) throws Exception;
	void insertBlog(BlogEntity blogEntity, MultipartFile[] files) throws Exception;

	BlogEntity selectBlogDetail(int id) throws Exception;

	void updateBlog(BlogEntity blogEntity);

	void deleteBlog(int id);
	
	FileEntity selectBlogFileInfo(int fileId, int id);

	

}
