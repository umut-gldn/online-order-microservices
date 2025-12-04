package com.onlineorder.courierservice.service.impl;

import com.onlineorder.courierservice.dto.DeliveryResponse;
import com.onlineorder.courierservice.dto.UpdateDeliveryRequest;
import com.onlineorder.courierservice.exception.NotFoundException;
import com.onlineorder.courierservice.mapper.DeliveryMapper;
import com.onlineorder.courierservice.model.Courier;
import com.onlineorder.courierservice.model.Delivery;
import com.onlineorder.courierservice.model.DeliveryStatus;
import com.onlineorder.courierservice.repository.CourierRepository;
import com.onlineorder.courierservice.repository.DeliveryRepository;
import com.onlineorder.courierservice.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper mapper;
    private final CourierRepository courierRepository;

    private Delivery getDeliveryOrThrow(Long id){
        return deliveryRepository.findById(id).orElseThrow(()->new NotFoundException("Delivery not found with id: "+id));
    }
    private Courier getCourierOrThrow(Long id){
        return courierRepository.findById(id).orElseThrow(()->new NotFoundException("Courier not found with id: "+id));
    }

    @Override
    @Transactional
    public void assignCourierToOrder(Long orderId) {
        List<Courier> availableCouriers=courierRepository.findAllByActiveTrue();
        if(availableCouriers.isEmpty()){
            log.error("No active couriers found for this {} ",orderId);
            return;
        }
        Courier selectedCourier=availableCouriers.get(new Random().nextInt(availableCouriers.size()));

        Delivery delivery=Delivery.builder()
                .orderId(orderId)
                .courier(selectedCourier)
                .status(DeliveryStatus.ASSIGNED)
                .build();

        deliveryRepository.save(delivery);
        log.info("Courier {} assigned to order {}",selectedCourier.getId(),orderId);
    }

    @Override
    public DeliveryResponse getById(Long id) {
        return mapper.toResponse(getDeliveryOrThrow(id));
    }

    @Override
    public Page<DeliveryResponse> list(Pageable pageable) {
        return deliveryRepository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    public Page<DeliveryResponse> listByCourier(Long courierId, Pageable pageable) {
        Courier courier= getCourierOrThrow(courierId);
        return deliveryRepository.findAllByCourier(courier,pageable).map(mapper::toResponse);
    }

    @Override
    public Page<DeliveryResponse> listByStatus(DeliveryStatus status, Pageable pageable) {
        return deliveryRepository.findAllByStatus(status,pageable).map(mapper::toResponse);
    }

    @Override
    public DeliveryResponse update(Long id, UpdateDeliveryRequest request) {
        Delivery delivery=getDeliveryOrThrow(id);
        if(request.courierId()!=null && !request.courierId().equals(delivery.getCourier().getId())){
            Courier courier=getCourierOrThrow(request.courierId());
            delivery.setCourier(courier);
        }
        mapper.updateFromRequest(request,delivery);
        return mapper.toResponse(deliveryRepository.save(delivery));
    }

    @Override
    public void delete(Long id) {
       Delivery delivery=getDeliveryOrThrow(id);
        deliveryRepository.deleteById(id);
    }
}
