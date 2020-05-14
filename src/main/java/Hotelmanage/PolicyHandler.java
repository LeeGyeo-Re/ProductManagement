package Hotelmanage;

import Hotelmanage.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    @Autowired
    ProductManagementRepository productManagementRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrdered_상품주문(@Payload Ordered ordered){

        if(ordered.isMe()){
            if(productManagementRepository.findById(ordered.getProductId()) != null){
                System.out.println("##### listener 상품 수량 변경 : " + ordered.toJson());
                ProductManagement productManagement = productManagementRepository.findById(ordered.getProductId()).get();
                Integer preProductCnt = productManagement.getProductCnt();
                productManagement.setOrderStatus(true);
                if(preProductCnt-ordered.getOrderCnt()>=0) {
                    productManagement.setProductCnt(preProductCnt - ordered.getOrderCnt());
                    productManagement.setReservationNumber(ordered.getReservationNumber());
                    productManagementRepository.save(productManagement);
                }
                else
                    System.out.println("##### 재고 없음 #####");

            }

        }
    }

}
