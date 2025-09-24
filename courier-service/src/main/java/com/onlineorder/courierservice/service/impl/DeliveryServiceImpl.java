package com.onlineorder.courierservice.service.impl;

import com.onlineorder.courierservice.dto.CreateDeliveryRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper mapper;
    private final CourierRepository courierRepository;

    private Courier getCourier(Long id){
        return courierRepository.findById(id).orElseThrow(()->new NotFoundException("Courier not found: "+id));
    }

    @Override
    public DeliveryResponse assign(CreateDeliveryRequest request) {
        Courier courier=getCourier(request.courierId());
        Delivery delivery=Delivery.builder()
                .orderId(request.orderId())
                .courier(courier)
                .status(request.status())
                .pickupAddress(request.pickupAddress())
                .dropoffAddress(request.dropoffAddress())
                .estimatedMinutes(request.estimatedMinutes())
                .build();
        return mapper.toResponse(deliveryRepository.save(delivery));
    }

    @Override
    public DeliveryResponse getById(Long id) {
        Delivery delivery=deliveryRepository.findById(id).orElseThrow(()->new NotFoundException("Delivery not found with id: "+id));
        Long courierId=delivery.getCourier() != null ? delivery.getCourier().getId() : null;
       return new DeliveryResponse(delivery.getId(),delivery.getOrderId(),courierId,delivery.getStatus(),delivery.getPickupAddress(),delivery.getDropoffAddress(),delivery.getEstimatedMinutes());
    }

    @Override
    public Page<DeliveryResponse> list(Pageable pageable) {
        return deliveryRepository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    public Page<DeliveryResponse> listByCourier(Long courierId, Pageable pageable) {
        Courier courier=getCourier(courierId);
        return deliveryRepository.findAllByCourier(courier,pageable).map(mapper::toResponse);
    }

    @Override
    public Page<DeliveryResponse> listByStatus(DeliveryStatus status, Pageable pageable) {
        return deliveryRepository.findAllByStatus(status,pageable).map(mapper::toResponse);
    }

    @Override
    public DeliveryResponse update(Long id, UpdateDeliveryRequest request) {
        Delivery delivery=deliveryRepository.findById(id).orElseThrow(()->new NotFoundException("Delivery not found with id: "+id));
        if(request.courierId()!=null && !request.courierId().equals(delivery.getCourier().getId())){
            delivery.setCourier(getCourier(request.courierId()));
        }
        mapper.updateFromRequest(request,delivery);
        return toResponse(deliveryRepository.save(delivery));
    }

    @Override
    public void delete(Long id) {
        if(!deliveryRepository.existsById(id)){
            throw new NotFoundException("Delivery not found with id: "+id);
        }
        deliveryRepository.deleteById(id);
    }

    private DeliveryResponse toResponse(Delivery delivery){
        Long courierId=delivery.getCourier() != null ? delivery.getCourier().getId() : null;
        return  new DeliveryResponse(delivery.getId(),delivery.getOrderId(),courierId,delivery.getStatus(),delivery.getPickupAddress(),delivery.getDropoffAddress(),delivery.getEstimatedMinutes());

    }
}
