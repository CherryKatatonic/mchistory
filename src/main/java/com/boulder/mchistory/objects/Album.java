package com.boulder.mchistory.objects;

import java.util.ArrayList;
import java.util.List;

public class Album  {
	
	// [START book]
	  private String title;
	  private String createdBy;
	  private String createdById;
	  private Long id;
	  private String imageUrl;
	// [END book]
	// [START keys]
	  public static final String CREATED_BY = "createdBy";
	  public static final String CREATED_BY_ID = "createdById";
	  public static final String ID = "id";
	  public static final String TITLE = "title";
	  public static final String IMAGE_URL = "imageUrl";
	// [END keys]

	// [START constructor]
		  // We use a Builder pattern here to simplify and standardize construction of Book objects.
		  private Album(Builder builder) {
		    this.title = builder.title;
		    this.createdBy = builder.createdBy;
		    this.createdById = builder.createdById;
		    this.id = builder.id;
		    this.imageUrl = builder.imageUrl;
		  }
		// [END constructor]
		// [START builder]
		  public static class Builder {
		    private String title;
		    private String createdBy;
		    private String createdById;
		    private Long id;
		    private String imageUrl;
		    

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

		    public Builder id(Long id) {
		      this.id = id;
		      return this;
		    }

		    public Builder imageUrl(String imageUrl) {
		      this.imageUrl = imageUrl;
		      return this;
		    }

		    public Album build() {
		      return new Album(this);
		    }
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
		// [END builder]
		  @Override
		  public String toString() {
		    return
		        "Title: " + title + ", Added by: " + createdBy;
		  }

}
