package com.ramcharans.chipotle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import lombok.Getter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.text.MessageFormat;


@Configuration
public class RabbitConfig {
    @Value("${PAYMENT_SUCCESS_QUEUE}")
    public String paymentSuccessQueue;
    
    @Value("${PAYMENT_SUCCESS_ROUTING_KEY}")
    public String paymentSuccessRoutingKey;
    
    @Value("${MEAL_FULFILLED_QUEUE}")
    public String mealFulfilledQueue;
    
    @Value("${MEAL_FULFILLED_ROUTING_KEY}")
    public String mealFulfilledRoutingKey;
    
    @Value("${PREPPED_INGREDIENT_STOCK_AT_THRESHOLD_QUEUE}")
    public String preppedIngredientStockAtThresholdQueue;
    
    @Value("${PREPPED_INGREDIENT_STOCK_AT_THRESHOLD_ROUTING_KEY}")
    public String preppedIngredientStockAtThresholdRoutingKey;
    
    @Value("${PREPPED_INGREDIENT_REPLENISHED_QUEUE}")
    public String preppedIngredientReplenishedQueue;
    
    @Value("${PREPPED_INGREDIENT_REPLENISHED_ROUTING_KEY}")
    public String preppedIngredientReplenishedRoutingKey;
    
    @Value("${CHIPOTLE_EXCHANGE}")
    public String exchange;
    
    @Bean
    Queue paymentSuccessQueue() {
        return new Queue(paymentSuccessQueue);
    }
    
    @Bean
    Queue mealFulfilledQueue() {
        return new Queue(mealFulfilledQueue);
    }
    
    @Bean
    Queue preppedIngredientStockAtThresholdQueue() {
        return new Queue(preppedIngredientStockAtThresholdQueue);
    }
    
    @Bean
    Queue preppedIngredientReplenishedQueue() {
        return new Queue(preppedIngredientReplenishedQueue);
    }
    
    @Bean
    DirectExchange chipotleExchange() {
        return new DirectExchange(exchange);
    }
    
    @Bean
    Binding paymentSuccessBinding(Queue paymentSuccessQueue, DirectExchange chipotleExchange) {
        return BindingBuilder.bind(paymentSuccessQueue).to(chipotleExchange).with(paymentSuccessRoutingKey);
    }
    
    @Bean
    Binding mealFulfilledBinding(Queue mealFulfilledQueue, DirectExchange chipotleExchange) {
        return BindingBuilder.bind(mealFulfilledQueue).to(chipotleExchange).with(mealFulfilledRoutingKey);
    }
    
    @Bean
    Binding preppedIngredientStockAtThresholdBinding(Queue preppedIngredientStockAtThresholdQueue,
                                                     DirectExchange chipotleExchange) {
        return BindingBuilder.bind(preppedIngredientStockAtThresholdQueue).to(chipotleExchange).with(preppedIngredientStockAtThresholdRoutingKey);
    }
    
    @Bean
    Binding preppedIngredientReplenishedBinding(Queue preppedIngredientReplenishedQueue,
                                                DirectExchange chipotleExchange) {
        return BindingBuilder.bind(preppedIngredientReplenishedQueue).to(chipotleExchange).with(preppedIngredientReplenishedRoutingKey);
    }
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        // NOTE: need to add new ObjectMapper().registerModule(new JavaTimeModule()) to add deserialization for
        //  LocalDateTime in Event objects; without this requirement, we can just do new Jackson2JsonMessageConverter()
        return new Jackson2JsonMessageConverter(new ObjectMapper().registerModule(new JavaTimeModule()));
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
    
    
}
