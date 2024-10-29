package com.example.SWP.API;

import com.example.SWP.Service.DashBoardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class DashBoardAPI {

    @Autowired
    DashBoardService dashBoardService;

    @GetMapping("/stats")
    public ResponseEntity getDashBoard(){
        Map<String, Object> stats = dashBoardService.getDashboard();
        return ResponseEntity.ok(stats);
    }
    @GetMapping("/monthly-revenue")
    public Map<String, Object> getMonthlyRevenue() {
        return dashBoardService.getMonthlyRevenue();
    }
}
