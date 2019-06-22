package com.repositories;

import com.models.UserVO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserVO, String> {
	UserVO findByuserId(String userId);
	void deleteByuserId(String userId);
}
