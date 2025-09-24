package com.onlineorder.courierservice.service.impl;

import com.onlineorder.courierservice.dto.CourierResponse;
import com.onlineorder.courierservice.dto.CreateCourierRequest;
import com.onlineorder.courierservice.dto.UpdateCourierRequest;
import com.onlineorder.courierservice.exception.NotFoundException;
import com.onlineorder.courierservice.mapper.CourierMapper;
import com.onlineorder.courierservice.model.Courier;
import com.onlineorder.courierservice.repository.CourierRepository;
import com.onlineorder.courierservice.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourierServiceImpl implements CourierService {

    private final CourierRepository repository;
    private final CourierMapper mapper;


    @Override
    public CourierResponse create(CreateCourierRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public CourierResponse getById(Long id) {
        Courier courier=repository.findById(id).orElseThrow(()->new NotFoundException("Courier not found with id: "+id));
        return mapper.toResponse(courier);
    }

    @Override
    public Page<CourierResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    public CourierResponse update(Long id, UpdateCourierRequest request) {
        Courier courier=repository.findById(id).orElseThrow(()->new NotFoundException("Courier not found with id: "+id));
        mapper.updateFromRequest(request,courier);
        return mapper.toResponse(repository.save(courier));
    }

    @Override
    public void delete(Long id) {
        if(!repository.existsById(id)){
            throw new NotFoundException("Courier not found with id: "+id);
        }
        repository.deleteById(id);
    }

}
