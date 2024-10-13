package com.example.SWP.Repository;

import com.example.SWP.entity.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<FeedBack, Long> {

    FeedBack findFeedBackById(long id);

    List<FeedBack> findFeedBackByIsDeletedFalse();
}