package com.example.SWP.Service;

import com.example.SWP.Enums.Role;
import com.example.SWP.Repository.AccountRepository;
import com.example.SWP.Repository.KoiRepository;
import com.example.SWP.Repository.PaymentRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DashBoardService {
    @Autowired
    KoiRepository koiRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PaymentRepository paymentRepository;
    public Map<String, Object> getDashboard(){
        Map<String, Object> dashboard = new HashMap<>();

        long totalProducts = koiRepository.count();
        dashboard.put("totalProducts",totalProducts);

        long customerCount = accountRepository.countByRole(Role.CUSTOMER);
        dashboard.put("customerCount",customerCount);

        long staffCount = accountRepository.countByRole(Role.STAFF);
        dashboard.put("staffCount",staffCount);

        long fishCount = koiRepository.getTotalFishCount();
        dashboard.put("fishCount",fishCount);

        return dashboard;
    }

    public Map<String, Object> getMonthlyRevenue() {
        Map<String, Object> monthlyRevenue = new HashMap<>();
        List<Object[]> results = paymentRepository.findMonthlyRevenue();
        List<Map<String, Object>> mapList = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("year", result[0]);
            monthData.put("month", result[1]);
            monthData.put("totalRevenue", result[2]);
            mapList.add(monthData);
        }

        monthlyRevenue.put("results", mapList);
        return monthlyRevenue;
    }

}
