package com.alipay.shop.designer.state.deprecated;

import com.alipay.shop.util.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hyy
 * @Description
 * @create 2023-12-19 19:20
 */

@Component
public class DeprecatedCreateOrder extends DeprecatedAbstractOrderState {

    @Autowired
    private RedisCommonProcessor redisClient;

    @Override
    protected DeprecatedOrder createOrder(String orderId, String productId, DeprecatedOrderContext context) {
        //利用redis超时时间来设置未支付订单
        //TODO: 也可以使用死信队列来实现
        DeprecatedOrder order = DeprecatedOrder.builder()
                .orderId(orderId)
                .productId(productId)
                .state(ORDER_WAIT_PAY)
                .build();

        //将新订单存入Redis缓存里面,15分钟后失效
        redisClient.set(orderId, order, 900);

        //TODO: 观察者模式,发送订单创建Event
        super.notifyObserver(orderId, ORDER_WAIT_PAY);
        return order;
    }
}