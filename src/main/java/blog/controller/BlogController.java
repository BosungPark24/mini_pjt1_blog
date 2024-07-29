package blog.controller;


import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import blog.entity.BlogEntity;
import blog.entity.FileEntity;
import blog.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
// @RequestMapping("/api")
public class BlogController {
	
	@Autowired
	private BlogService blogService;
	
	@GetMapping("/blog")
	public ModelAndView openBlogList(HttpServletRequest request) throws Exception {
		log.trace("trace log...");
		log.debug("debug log...");
		log.info("info log...");
		log.warn("warn log...");
		log.error("error log...");
		
		ModelAndView mv = new ModelAndView("/blog/blogList");
		
		List<BlogEntity> list = blogService.selectBlogList();
		mv.addObject("list", list);
		
		return mv;
	}
	
	@GetMapping("/blog/write")
	public ModelAndView openBlogWrite() throws Exception {
		//return "/blog/blogWrite";
		return new ModelAndView("blog/blogWrite");
	}
	
	@PostMapping("/blog/write")
	public String insertBlog(BlogEntity blogEntity, MultipartHttpServletRequest request) throws Exception {
		blogService.insertBlog(blogEntity, request);
		return "redirect:/blog";	
	}
	
	@GetMapping("/blog/{id}")
	public ModelAndView openBlogDetail(@PathVariable("id") int id) throws Exception {
		ModelAndView mv = new ModelAndView("/blog/blogDetail");
		
		BlogEntity blogEntity = blogService.selectBlogDetail(id);
		mv.addObject("blog", blogEntity);
		
		return mv;
	}
	
	@PutMapping("/blog/{id}")
	public String updateBlog(@PathVariable("id") int id, BlogEntity blogEntity) throws Exception {
		blogService.updateBlog(blogEntity);
		return "redirect:/blog";
	}
	
	@DeleteMapping("/blog/{id}")
	public String deleteBlog(@PathVariable("id") int id) throws Exception {
		blogService.deleteBlog(id);
		return "redirect:/blog";
	}
	
	@GetMapping("/blog/file/{fileId}/{id}")
	public void downloadBlogFile(@PathVariable("fileId") int fileId, @PathVariable("id") int id, HttpServletResponse response) throws Exception {
		FileEntity fileEntity = blogService.selectBlogFileInfo(fileId, id);
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


