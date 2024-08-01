package blog.controller;

import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import blog.entity.BlogEntity;
import blog.entity.FileEntity;
import blog.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class BlogController {
	
	@Autowired
	private BlogService blogService;
	
	@Operation(summary = "블로그 목록 조회", description = "모든 블로그 글 목록을 조회합니다.")
	@GetMapping("/blog")
	public List<BlogEntity> openBlogList(HttpServletRequest request) throws Exception {
		log.trace("trace log...");
		log.debug("debug log...");
		log.info("info log...");
		log.warn("warn log...");
		log.error("error log...");
		
		return blogService.selectBlogList();
	}
	
	@Operation(summary = "블로그 글 작성 페이지로 이동", description = "블로그 글 작성 페이지로 이동합니다.")
	@GetMapping("/blog/write")
	public String openBlogWrite() throws Exception {
		return "/blog/blogWrite";
		
	}
	
	@Operation(summary = "블로그 글 등록", description = "게시물 제목과 내용을 저장합니다.")
	@Parameter(name = "blogEntity", description = "게시물 정보를 담고 있는 객체", required = true)
	@PostMapping("/blog/write")
	// public void insertBlog(@RequestBody BlogEntity blogEntity, MultipartHttpServletRequest request) throws Exception {
	public void insertBlog(@RequestPart(value="data", required=true) BlogEntity blogEntity, @RequestPart(value="files", required=false) MultipartFile[] files) throws Exception {
		blogService.insertBlog(blogEntity, files);
		
	}
	
	@Operation(summary = "블로그 글 상세 조회", description = "특정 ID의 블로그 글을 조회합니다.")
	@GetMapping("/blog/{blogId}")
	public BlogEntity openBlogDetail(@PathVariable("blogId") int blogId) throws Exception {
		return blogService.selectBlogDetail(blogId);
	}
	
	@Operation(summary = "블로그 글 수정", description = "특정 ID의 블로그 글을 수정합니다.")
	@PutMapping("/blog/{blogId}")
	public void updateBlog(@PathVariable("blogId") int blogId, @RequestBody BlogEntity blogEntity) throws Exception {
		blogEntity.setBlogId(blogId);
		blogService.updateBlog(blogEntity);
		
	}
	
	@Operation(summary = "블로그 글 삭제", description = "특정 ID의 블로그 글을 삭제합니다.")
	@DeleteMapping("/blog/{blogId}")
	public void deleteBlog(@PathVariable("blogId") int blogId) throws Exception {
		blogService.deleteBlog(blogId);
	}
	
	
	@Operation(summary = "파일 다운로드", description = "특정 ID의 파일을 다운로드합니다.")
	@GetMapping("/blog/file/{fileId}/{blogId}")
	public void downloadBlogFile(@PathVariable("fileId") int fileId, @PathVariable("blogId") int blogId, HttpServletResponse response) throws Exception {
		FileEntity fileEntity = blogService.selectBlogFileInfo(fileId, blogId);
		if (ObjectUtils.isEmpty(fileEntity)) {
			return;
		}
		
		Path path = Paths.get(fileEntity.getFilePath());
		byte[] file = Files.readAllBytes(path); 
		
		response.setContentType("application/octet-stream");
		response.setContentLength(file.length);
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fileEntity.getOriginalFileName(), "UTF-8") + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		response.getOutputStream().write(file);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}


}


