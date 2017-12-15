package com.boulder.mchistory.objects;

import java.util.Date;

import com.google.cloud.Timestamp;

// [START example]
public class Post {
// [START book]
  private String type;
  private String title;
  private String createdBy;
  private String createdById;
  private Timestamp dateCreated;
  private String info;
  private Long id;
  private String imageUrl;
  private String videoUrl;
  private String album;
  private Timestamp date;
  private String dateString;
  private String time; 
  private String location;
// [END book]
// [START keys]
  public static final String TYPE = "type";
  public static final String CREATED_BY = "createdBy";
  public static final String CREATED_BY_ID = "createdById";
  public static final String DATECREATED = "dateCreated";
  public static final String INFO = "info";
  public static final String ID = "id";
  public static final String TITLE = "title";
  public static final String IMAGE_URL = "imageUrl";
  public static final String VIDEO_URL = "videoUrl";
  public static final String ALBUM = "album";
  public static final String DATE = "date";
  public static final String DATESTRING = "dateString";
  public static final String TIME = "time";
  public static final String LOCATION = "location";
// [END keys]
// [START constructor]
  // We use a Builder pattern here to simplify and standardize construction of Book objects.
  private Post(Builder builder) {
	this.type = builder.type;
    this.title = builder.title;
    this.createdBy = builder.createdBy;
    this.createdById = builder.createdById;
    this.dateCreated = builder.dateCreated;
    this.info = builder.info;
    this.id = builder.id;
    this.imageUrl = builder.imageUrl;
    this.videoUrl = builder.videoUrl;
    this.album = builder.album;
    this.date = builder.date;
    this.dateString = builder.dateString;
    this.time = builder.time;
    this.location = builder.location;
  }
// [END constructor]
// [START builder]
  public static class Builder {
	private String type;
    private String title;
    private String createdBy;
    private String createdById;
    private Timestamp dateCreated;
    private String info;
    private Long id;
    private String imageUrl;
    private String videoUrl;
    private String album;
    private Timestamp date;
    private String dateString;
    private String time;
    private String location;
    
    public Builder type(String type) {
    	this.type = type;
    	return this;
    }

    public Builder title(String title) {
      this.title = title;
      return this;
    }

    public Builder createdBy(String createdBy) {
      this.createdBy = createdBy;
      return this;
    }

    public Builder createdById(String createdById) {
      this.createdById = createdById;
      return this;
    }
    
    public Builder dateCreated(Timestamp dateCreated) {
    	this.dateCreated = dateCreated;
    	return this;
    }

    public Builder info(String info) {
      this.info = info;
      return this;
    }

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder imageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
      return this;
    }
    
    public Builder videoUrl(String videoUrl) {
      this.videoUrl = videoUrl;
      return this;
    }
    
    public Builder album(String album) {
    	this.album = album;
    	return this;
    }
    
    public Builder date(Timestamp date) {
    	this.date = date;
    	return this;
    }
    
    public Builder dateString(String dateString) {
    	this.dateString = dateString;
    	return this;
    }
    
    public Builder time(String time) {
    	this.time = time;
    	return this;
    }
    
    public Builder location(String location) {
    	this.location = location;
    	return this;
    }

    public Post build() {
      return new Post(this);
    }
  }
  
  public String getType() {
	  return type;
  }
  
  public void setType(String type) {
	  this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getCreatedById() {
    return createdById;
  }

  public void setCreatedById(String createdById) {
    this.createdById = createdById;
  }
  
  public Timestamp getDateCreated() {
	  return dateCreated;
  }
  
  public void setDateCreated(Timestamp dateCreated) {
	  this.dateCreated = dateCreated;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
  
  public String getVideoUrl() {
	  return videoUrl;
  }
  
  public void setVideoUrl(String videoUrl) {
	  this.videoUrl = videoUrl;
  }
  
  public String getAlbum() {
	  return album;
  }
  
  public void setAlbum(String album) {
	  this.album = album;
  }
  
  public Timestamp getDate() {
	  return date;
  }
  
  public void setDate(Timestamp date) {
	  this.date = date;
  }
  
  public String getDateString() {
	  return dateString;
  }
  
  public void setDateString(String dateString) {
	  this.dateString = dateString;
  }
  
  public String getTime() {
	  return time;
  }
  
  public void setTime(String time) {
	  this.time = time;
  }
  
  public String getLocation() {
	  return location;
  }
  
  public void setLocation(String location) {
	  this.location = location;
  }
  
// [END builder]
  @Override
  public String toString() {
    return
        "Title: " + title + ", Added by: " + createdBy;
  }
}
// [END example]
