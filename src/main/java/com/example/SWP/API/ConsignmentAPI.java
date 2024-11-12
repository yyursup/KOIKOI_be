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
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("showConsign")
    public ResponseEntity getOrderByAccountById(){
        return ResponseEntity.ok(consignmentService.getCareList());
    }

}
