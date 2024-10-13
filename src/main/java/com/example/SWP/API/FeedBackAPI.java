package com.example.SWP.API;

import com.example.SWP.Service.FeedbackService;
import com.example.SWP.entity.FeedBack;
import com.example.SWP.entity.Koi;
import com.example.SWP.model.FeedBackRequest;
import com.example.SWP.model.FeedBackResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class FeedBackAPI {

    @Autowired
    FeedbackService feedbackService;

    @PostMapping()
    public ResponseEntity newKoiFish(@Valid @RequestBody FeedBackRequest feedBackRequest ) {
        FeedBackResponse newFeedback = feedbackService.feedback(feedBackRequest);
        return ResponseEntity.ok(newFeedback);
    }

    @GetMapping()
    public ResponseEntity getAllKoi(){
        List<FeedBackResponse> koiList = feedbackService.getAllFeedback();
        return ResponseEntity.ok(koiList);
    }

    @PutMapping("{id}")
    public ResponseEntity updateFeeback(@Valid @RequestBody  FeedBackRequest feedBackRequest, @PathVariable Long id){
        try{
            FeedBackResponse newFeedback = feedbackService.updateFeedback(id,feedBackRequest);
            return ResponseEntity.ok(newFeedback);
        }catch (Exception e){
            throw new RuntimeException("Id of koi " + id + " not found ");
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteFeedback(@PathVariable Long id){
        try{
            FeedBack oldFeedback = feedbackService.deleteFeedback(id);
            return ResponseEntity.ok(oldFeedback);
        }catch (Exception e){
            throw new RuntimeException("Id of koi : " + id + " not found");
        }
    }
}
