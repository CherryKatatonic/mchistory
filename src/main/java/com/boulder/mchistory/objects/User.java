package com.boulder.mchistory.objects;

import java.awt.List;
import java.util.ArrayList;

public class User {

	// [START book]
		  private String email;
		  private String username;
		  private String hash;
		  private String actHash;
		  private Boolean emailVerified;
		  private Boolean isAdmin;
		  private List bookmarks;
		  private Long id;
		  private String imageUrl;
		  private String role;
		// [END book]
		// [START keys]
		  public static final String EMAIL = "email";
		  public static final String USERNAME = "username";
		  public static final String HASH = "hash";
		  public static final String ACTHASH = "actHash";
		  public static final String EMAILVERIFIED = "emailVerified";
		  public static final String ISADMIN = "isAdmin";
		  public static final String BOOKMARKS = "bookmarks";
		  public static final String ID = "id";
		  public static final String IMAGE_URL = "imageUrl";
		  public static final String ROLE = "role";
		// [END keys]
		// [START constructor]
		  // We use a Builder pattern here to simplify and standardize construction of Book objects.
		  private User(Builder builder) {
				this.email = builder.email;
		    this.username = builder.username;
		    this.hash = builder.hash;
		    this.actHash = builder.actHash;
		    this.emailVerified = builder.emailVerified;
		    this.isAdmin = builder.isAdmin;
		    this.bookmarks = builder.bookmarks;
		    this.id = builder.id;
		    this.imageUrl = builder.imageUrl;
		    this.role = builder.role;
		  }
		// [END constructor]
		// [START builder]
		  public static class Builder {
			private String email;
		    private String username;
		    private String hash;
		    private String actHash;
		    private Boolean emailVerified;
		    private Boolean isAdmin;
		    private List bookmarks;
		    private Long id;
		    private String imageUrl;
		    private String role;
		    
		    public Builder email(String email) {
		      this.email = email;
		      return this;
		    }

		    public Builder username(String username) {
		      this.username = username;
		      return this;
		    }
		    
		    public Builder hash(String hash) {
		    	this.hash = hash;
		    	return this;
		    }
		    
		    public Builder actHash(String actHash) {
		    	this.actHash = actHash;
		    	return this;
		    }
		    
		    public Builder emailVerified(Boolean verified) {
		      this.emailVerified = verified;
		      return this;
		    }
		    
		    public Builder isAdmin(Boolean admin) {
		    	this.isAdmin = admin;
		    	return this;
		    }
		    
		    public Builder bookmarks(List bookmarks) {
		    	this.bookmarks = bookmarks;
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
		    
		    public Builder role(String role) {
		    	this.role = role;
		    	return this;
		    }

		    public User build() {
		      return new User(this);
		    }
		  }
		  
		  public String getEmail() {
			  return email;
		  }
		  
		  public void setEmail(String email) {
			  this.email = email;
		  }

		  public String getUsername() {
		    return username;
		  }

		  public void setUsername(String username) {
		    this.username = username;
		  }
		  
		  public String getHash() {
			  return hash;
		  }
		  
		  public void setHash(String hash) {
			  this.hash = hash;
		  }
		  
		  public String getActHash() {
			  return actHash;
		  }
		  
		  public void setActHash(String actHash) {
			  this.actHash = actHash;
		  }

		  public Boolean getEmailVerified() {
		    return emailVerified;
		  }

		  public void setEmailVerified(Boolean verified) {
		    this.emailVerified = verified;
		  }
		  
		  public Boolean isAdmin() {
			  return isAdmin;
		  }
		  
		  public void setAdmin(Boolean admin) {
			  this.isAdmin = admin;
		  }
		  
		  public List getBookmarks() {
			  return bookmarks;
		  }
		  
		  public void setBookmarks(List bookmarks) {
			  this.bookmarks = bookmarks;
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
		  
		  public String getRole() {
			  return role;
		  }
		  
		  public void setRole(String role) {
			  this.role = role;
		  }
		// [END builder]
}
