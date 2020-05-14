package Hotelmanage;



import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
@Table(name="ProductManagement_table")
public class ProductManagement {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer productId;
    private String productName;
    private Integer productCnt;
    private boolean orderStatus;

    public Integer getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(Integer reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    private Integer reservationNumber;

//    @Autowired
//    ProductManagementRepository productManagementRepository;

    @PostUpdate
    public void onPostUpdate(){
        if(this.orderStatus){
            OrderAdded orderAdded = new OrderAdded();
            orderAdded.setProductId(this.productId);
            orderAdded.setProductName(this.productName);
            orderAdded.setReservationNumber(this.reservationNumber);
            orderAdded.publishAfterCommit();
            System.out.println("###### 주문 추가 완료 ######");
            ProductAdded productAdded = new ProductAdded();
            productAdded.setProductCnt(this.productCnt);
            productAdded.setProductId(this.productId);
            productAdded.setProductName(this.productName);

            productAdded.publishAfterCommit();
        }
    }

    @PreUpdate
    public void onPreUpdate(){
        if(!this.orderStatus){
                System.out.println("###### 상품 추가 ######");

                Integer nProductCnt = 10;
                ProductAdded productAdded = new ProductAdded();

                setProductCnt(100+this.productCnt);

                productAdded.setProductCnt(this.productCnt);
                productAdded.setProductId(this.productId);
                productAdded.setProductName(this.productName);

                productAdded.publishAfterCommit();
        }
    }

    @PrePersist
    public void onPrePersist(){
        if(!this.orderStatus){
            ProductAdded productAdded = new ProductAdded();

            productAdded.setProductCnt(this.productCnt);
            productAdded.setProductId(this.productId);
            productAdded.setProductName(this.productName);

            productAdded.publishAfterCommit();
        }
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductCnt() {
        return productCnt;
    }

    public void setProductCnt(Integer productCnt) {
        this.productCnt = productCnt;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public boolean isOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(boolean orderStatus) {
        this.orderStatus = orderStatus;
    }
}
