package com.example.SWP.Service;

import com.example.SWP.Repository.FeedbackRepository;
import com.example.SWP.entity.Account;
import com.example.SWP.entity.FeedBack;
import com.example.SWP.model.FeedBackRequest;
import com.example.SWP.model.FeedBackResponse;
import com.example.SWP.model.RegisterResponse;
import com.example.SWP.utils.AccountUtils;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AccountUtils accountUtils;

    public List<FeedBackResponse> getAllFeedback(){
        List<FeedBack> feedBacks = feedbackRepository.findFeedBackByIsDeletedFalse();
        return feedBacks.stream().map(feedBack ->
                modelMapper.map(feedBack, FeedBackResponse.class)).collect(Collectors.toList());
    }

    public FeedBackResponse feedback(FeedBackRequest feedBackRequest){
        Account account = accountUtils.getCurrentAccount();
        FeedBack feedBack = modelMapper.map(feedBackRequest,FeedBack.class);
        feedBack.setAccount(account);
        try {
            FeedBack newFeedBack = feedbackRepository.save(feedBack);

            return modelMapper.map(newFeedBack, FeedBackResponse.class);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    public FeedBack deleteFeedback(Long feedback) {
        FeedBack oldFeedBack = feedbackRepository.findFeedBackById(feedback);
        if(oldFeedBack == null) {
            throw new RuntimeException("Koi not found");
        }
        oldFeedBack.setDeleted(true);
        return feedbackRepository.save(oldFeedBack);
    }
    public FeedBackResponse updateFeedback(Long feedbackId, FeedBackRequest feedBackRequest) {
        try {
            FeedBack oldFeedback = feedbackRepository.findFeedBackById(feedbackId);
            if (oldFeedback == null) {
                throw new EntityNotFoundException("Feedback with ID " + feedbackId + " not found");
            }
            oldFeedback.setFeedBackContent(feedBackRequest.getFeedBackContent());
            oldFeedback.setFeedBackDay(feedBackRequest.getFeedBackDay());
            feedbackRepository.save(oldFeedback);
            return modelMapper.map(oldFeedback, FeedBackResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while updating the feedback", e);
        }
    }


}
