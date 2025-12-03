package com.onlineorder.paymentservice.strategy;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("stripePaymentStrategy")
@Slf4j
@ConditionalOnProperty(name="payment.provider",havingValue = "stripe")
public class StripePaymentStrategy implements PaymentStrategy{

    @Value("${stripe.api-key}")
    private String stripeApiKey;

    @PostConstruct
    public void init(){
        Stripe.apiKey=stripeApiKey;
    }
    @Override
    public boolean processPayment(BigDecimal amount, String currency, String paymentToken) {
        log.info("Stripe payment process started for amount={} currency={}",amount,currency);

        try {
            long amountInCents=amount.multiply(BigDecimal.valueOf(100)).longValue();

            PaymentIntentCreateParams params= PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency(currency != null ? currency : "usd")
                    .setPaymentMethod(paymentToken)
                    .setConfirm(true)
                    .setReturnUrl("http://localhost:3000/payment-success")
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                            .build()
                    )
                    .build();
            PaymentIntent paymentIntent=PaymentIntent.create(params);

            if("succeeded".equals(paymentIntent.getStatus())){
                log.info("stripe payment process completed successfully ID: {}",paymentIntent.getId());
                return  true;
            }
            else {
                log.warn("stripe payment process failed with status: {}",paymentIntent.getStatus());
                return false;
            }
        }catch (StripeException e){
            log.error("Stripe Error: {}",e.getMessage());
            return false;
        }
    }

}
