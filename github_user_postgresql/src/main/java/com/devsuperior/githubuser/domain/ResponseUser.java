package com.devsuperior.githubuser.domain;

import java.util.List;

import com.devsuperior.githubuser.dto.UserDTO;

public class ResponseUser {

	private List<UserDTO> content;

	public List<UserDTO> getContent() {
		return content;
	}

	public void setContent(List<UserDTO> content) {
		this.content = content;
	}	
}
