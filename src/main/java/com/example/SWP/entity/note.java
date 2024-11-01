//package com.example.SWP.entity;
//
//public class note {
//}
// sửa entity của Account,Cart,KoiOrder,Consignment,Transactions
//JsonManagedReference được sử dụng cho các mối quan hệ "cha"
// trong khi @JsonBackReference được sử dụng cho các mối quan hệ "con" để ngăn vòng lặp.
//@JsonIgnore vẫn được sử dụng ở một số quan hệ không cần thiết trong JSON response.
//@PathVariable:
//Mục đích: Dùng để lấy giá trị từ đường dẫn URL (path).

//Vị trí: Các biến được trích xuất từ URL động trong định nghĩa đường dẫn (URI).

//Ví dụ: Sử dụng khi bạn muốn lấy giá trị từ một phần cụ thể trong đường dẫn.

//@PathVariable:
//URL: /orders/123
//Truy xuất: {orderId} từ đường dẫn URL.

// @RequestParam:
//Mục đích: Dùng để lấy giá trị từ query parameters (tham số truy vấn) trong URL.
//
//Vị trí: Các biến được trích xuất từ các tham số trong phần truy vấn của URL (sau dấu ?).
//
//Ví dụ: Sử dụng khi bạn muốn lấy giá trị từ tham số truy vấn dạng key-value.

//@RequestParam:
//URL: /orders?orderId=123
//Truy xuất: ?orderId=123 từ tham số truy vấn.

//Nếu bi dính lỗi stackOverFlow hãy đảm bảo rằng không có vòng lặp trong các phương thức toString, equals, và hashCode
// thay vì dùng @Data hãy su dụng @Getter @Setter bởi vì trong @Data bao  gồm ca các phương thức toString()  equals()

