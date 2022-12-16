package com.devsuperior.githubuser;

import java.time.Instant;

import jakarta.persistence.Column;

public class User {
	
	private String id;
	private String login;
	private String name;
	private String location;
	
	@Column(name = "avatar_url")
	private String avatarUrl;
	private Integer followers;
	private Integer following;
	
	@Column(name = "created_at")
	private Instant createdAt;	
	
	public User() {
	}

	public User(String id, String login, String name, String location, String avatarUrl, Integer followers,
			Integer following, Instant createdAt) {
		super();
		this.id = id;
		this.login = login;
		this.name = name;
		this.location = location;
		this.avatarUrl = avatarUrl;
		this.followers = followers;
		this.following = following;
		this.createdAt = createdAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public Integer getFollowers() {
		return followers;
	}

	public void setFollowers(Integer followers) {
		this.followers = followers;
	}

	public Integer getFollowing() {
		return following;
	}

	public void setFollowing(Integer following) {
		this.following = following;
	}
	

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", name=" + name + ", location=" + location + ", avatarUrl="
				+ avatarUrl + ", followers=" + followers + ", following=" + following + ", createdAt=" + createdAt
				+ "]";
	}	
}
