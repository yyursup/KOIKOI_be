package com.example.SWP.Repository;

import com.example.SWP.entity.Account;
import com.example.SWP.entity.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Long> {



    @Query("select fp from ForgotPassword fp where fp.otp = ?1 and fp.account = ?2 ")
    ForgotPassword findByOtpAndAccount(Integer otp, Account account);

    @Query("select fp from ForgotPassword fp where fp.otp = ?1")
    ForgotPassword findByOtp(Integer otp);
}
