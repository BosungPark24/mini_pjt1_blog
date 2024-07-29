package blog.dto;

import lombok.Data;

@Data
public class BlogDto {
	private int id;
	private String title;
	private String contents;
	private int hitCnt;
	private String createdDatetime;
	private String creatorId;
	private String updatedDatetime;
	private String updatorId;

}
