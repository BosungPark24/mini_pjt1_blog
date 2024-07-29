package blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import blog.entity.BlogEntity;
import blog.entity.FileEntity;

public interface BlogRepository extends CrudRepository<BlogEntity, Integer> {
	List<BlogEntity> findAllByOrderByIdDesc();
	
	@Query("SELECT file FROM FileEntity file WHERE file.fileId = :fileId AND file.blog.id = :id")
	public FileEntity findBlogFile(@Param("fileId") int fileId, @Param("id") int id);
}
