package com.example.SWP.API;

import com.example.SWP.Service.ConsignmentService;
import com.example.SWP.entity.Consignment;
import com.example.SWP.model.request.ConsignmentRequest;
import com.example.SWP.model.request.ConsignmentRequestCancel;
import com.example.SWP.model.request.KoiRequest;
import com.example.SWP.model.response.ConsignmentDetailsResponse;
import com.example.SWP.model.response.OrderDetailsResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/Consignment")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class ConsignmentAPI {

    @Autowired
    ConsignmentService consignmentService;

    @PostMapping("care/{id}")
    public ResponseEntity createConsignmentCare(@PathVariable long id, @RequestBody ConsignmentRequest consignmentRequest) {
        Consignment consignment   =  consignmentService.createConsignment
             (id,consignmentRequest.getStart_date(),consignmentRequest.getEnd_date());
        return ResponseEntity.ok(consignment);
    }

    @PostMapping("sell/{koiTypeId}")
    public ResponseEntity createConsignment(@RequestBody KoiRequest request, @PathVariable long koiTypeId) {
        Consignment consignment  = consignmentService.createConsignmentForSell(request,koiTypeId);
        return ResponseEntity.ok(consignment);
    }
    @DeleteMapping("{id}")
    public String deleteConsignmentDetailsById(@PathVariable long id) {
        consignmentService.delete(id);
        return "Delete successfully";
    }
    @PutMapping("increase/{id}")
    public ResponseEntity addConsign(@PathVariable long id) {
        return ResponseEntity.ok(consignmentService.add(id));
    }

    @PutMapping("decrease/{id}")
    public ResponseEntity updateQuantityConsign(@PathVariable long id) {
        return ResponseEntity.ok(consignmentService.removeOneProduct(id));
    }

    @PostMapping("cancel/{id}")
    public ResponseEntity cancelConsignment(@PathVariable long id, @RequestBody ConsignmentRequestCancel request) {
        Consignment consignment   =  consignmentService.cancelConsignment(id,request.getEnd_date());
        return ResponseEntity.ok(consignment);
    }

    @PostMapping("extend/{id}")
    public ResponseEntity extendConsign(@PathVariable long id, @RequestBody ConsignmentRequest consignmentRequest) {
        Consignment consignment   =  consignmentService.extendConsignment(id,consignmentRequest.getEnd_date());
        return ResponseEntity.ok(consignment);
    }

    @GetMapping("showConsignment/{id}")
    public ResponseEntity getConsignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(consignmentService.getConsignmentById(id));
    }

    @GetMapping("/details")
    public ResponseEntity  viewOrderDetails(@RequestParam long orderId) {
        Set<ConsignmentDetailsResponse> consignments = consignmentService.viewConsignmentDetails(orderId);
        return ResponseEntity.ok(consignments);
    }

    // Lấy tất cả các yêu cầu ký gửi - chỉ dành cho MANAGER
    @GetMapping("/allOfSell")
    public ResponseEntity<List<Consignment>> getAllConsignments() {
        List<Consignment> consignments = consignmentService.getAllConsignments();
        return ResponseEntity.ok(consignments);
    }

    @GetMapping("/allOfCare")
    public ResponseEntity<List<Consignment>> getAllConsignments2() {
        List<Consignment> consignments = consignmentService.getAllConsignments2();
        return ResponseEntity.ok(consignments);
    }

    // Lấy các yêu cầu ký gửi của user hiện tại
    @GetMapping("/user")
    public ResponseEntity<List<Consignment>> getUserConsignments() {
        List<Consignment> consignments = consignmentService.getUserConsignments();
        return ResponseEntity.ok(consignments);
    }

    // Phê duyệt yêu cầu ký gửi - chỉ dành cho MANAGER
    @PostMapping("/approve/{id}")
    public ResponseEntity<String> approveConsignment(@PathVariable Long id) {
        consignmentService.approveConsignment(id);
        return ResponseEntity.ok("Consignment approved successfully.");
    }

    // Từ chối yêu cầu ký gửi - chỉ dành cho MANAGER
    @PostMapping("/reject/{id}")
    public ResponseEntity<String> rejectConsignment(@PathVariable Long id) {
        consignmentService.rejectConsignment(id);
        return ResponseEntity.ok("Consignment rejected successfully.");
    }

    @GetMapping("/approved")
    public ResponseEntity<List<Consignment>> getApprovedConsignments() {
        List<Consignment> approvedConsignments = consignmentService.getApprovedConsignments();
        return ResponseEntity.ok(approvedConsignments);
    }

    @GetMapping("showConsign")
    public ResponseEntity getOrderByAccountById(){
        return ResponseEntity.ok(consignmentService.getCareList());
    }

}
