package blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Integer> {

}
