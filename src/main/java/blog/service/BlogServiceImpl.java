package blog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import blog.common.FileUtils;
import blog.entity.BlogEntity;
import blog.entity.FileEntity;
import blog.repository.BlogRepository;
import blog.repository.FileRepository;
@Service
public class BlogServiceImpl implements BlogService {
	
	@Autowired
	private BlogRepository blogRepository;
	
	@Autowired
	private FileRepository fileRepository;
	
	@Autowired
	private FileUtils fileUtils;
	
	@Override
	public List<BlogEntity> selectBlogList() {
		return blogRepository.findAllByOrderByIdDesc();
	}

	@Override
	public void insertBlog(BlogEntity blogEntity, MultipartHttpServletRequest request) throws Exception {
		List<FileEntity> list = fileUtils.parseFileInfo(request);
		if (list != null) {
			for (FileEntity file : list) {
				file.setBlog(blogEntity);
			}
			blogEntity.setFileInfoList(list);
		}
		blogEntity.setCreatorId("tester");
		
		blogRepository.save(blogEntity);
		
	}

	@Override
	public BlogEntity selectBlogDetail(int id) throws Exception {
		Optional<BlogEntity> optional = blogRepository.findById(id);
		if(optional.isPresent()) {
				BlogEntity blogEntity = optional.get();
				blogEntity.setHitCnt(blogEntity.getHitCnt() + 1);
				blogRepository.save(blogEntity);
				return blogEntity;
		} else {
				throw new Exception("일치하는 데이터가 없습니다.");
		}
	}

	@Override
	public void updateBlog(BlogEntity blogEntity) {
		blogEntity.setCreatorId("tester");
		
		blogRepository.save(blogEntity);
	}

	@Override
	public void deleteBlog(int id) {
		blogRepository.deleteById(id);
	}

	@Override
	public FileEntity selectBlogFileInfo(int fileId, int id) {
		return blogRepository.findBlogFile(fileId, id);
	}

}
