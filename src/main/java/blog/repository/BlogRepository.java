package blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import blog.entity.BlogEntity;
import blog.entity.FileEntity;

public interface BlogRepository extends CrudRepository<BlogEntity, Integer> {
	List<BlogEntity> findAllByOrderByBlogIdDesc();
	
	@Query("SELECT file FROM FileEntity file WHERE file.fileId = :fileId AND file.blog.id = :blogId")
	public FileEntity findBlogFile(@Param("fileId") int fileId, @Param("blogId") int blogId);
	
}
