CREATE TABLE IF NOT EXISTS couriers (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(160) NOT NULL,
    phone VARCHAR(40),
    vehicle_type VARCHAR(40), -- BIKE, SCOOTER, CAR
    active BOOLEAN NOT NULL DEFAULT TRUE,
    current_lat DOUBLE PRECISION,
    current_lng DOUBLE PRECISION,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS deliveries (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    courier_id BIGINT NOT NULL,
    status VARCHAR(40) NOT NULL,
    pickup_address VARCHAR(255),
    dropoff_address VARCHAR(255),
    estimated_minutes INTEGER,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_delivery_courier
    FOREIGN KEY (courier_id) REFERENCES couriers(id)
    ON DELETE SET NULL
    );

CREATE INDEX IF NOT EXISTS idx_deliveries_courier ON deliveries(courier_id);
CREATE INDEX IF NOT EXISTS idx_deliveries_order ON deliveries(order_id);
