package com.example.demo.domain;



import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.example.demo.util.Constants;

@Entity
@Table(name="gps")
public class GPS {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	@Column(name = "userId", length = 1024)
	private String userId;
	@Column(name = "fileName", length = 1024)
	private String fileName;
	@Column(name = "name", length = 1024)
	private String name;
	@Column(name="desc",columnDefinition="TEXT")
    private String desc;
	@Column(name = "author", length = 1024)
    private String author;
	@Column(name=Constants.GPX_DATE)
    @Temporal(TemporalType.TIMESTAMP)
    private Date gpxDate;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name="gpxContent",columnDefinition="TEXT")
    private String gpxContent;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getGpxDate() {
		return gpxDate;
	}

	public void setGpxDate(Date gpxDate) {
		this.gpxDate = gpxDate;
	}

	public String getGpxContent() {
		return gpxContent;
	}

	public void setGpxContent(String gpxContent) {
		this.gpxContent = gpxContent;
	}

	
}
