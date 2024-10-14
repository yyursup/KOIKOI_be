package com.example.SWP.model.Request;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

 List<OrderDetailsRequest> orderDetailsRequests;
}
