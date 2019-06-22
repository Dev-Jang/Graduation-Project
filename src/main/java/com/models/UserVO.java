package com.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;

@Document(collection = "userList")
public class UserVO {
	@Id
	public String id;
	@Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
	public String userId;
	public String userPw;
	public String userName;

	public UserVO() {}

	public UserVO(String id, String userId, String userPw, String userName) {
		this.id = id;
		this.userId = userId;
		this.userPw = userPw;
		this.userName = userName;
	}

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }

	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }

	public String getUserPw() { return userPw; }
	public void setUserPw(String userPw) { this.userPw = userPw; }

	public String getUserName() { return userName; }
	public void setUserName(String userName) { this.userName = userName; }

}
