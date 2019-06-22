package com.repositories;

import java.util.List;
import com.models.SwinStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SwinStatusRepository extends MongoRepository<SwinStatus, String> {
	SwinStatus findByswinIp(String swinIp);
	List<SwinStatus> findByuserId(String userId);
	Long countByuserId(String userId);
	void deleteByswinIp(String swinIp);
	void deleteByuserId(String userId);
}
