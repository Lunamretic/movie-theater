package ua.epam.spring.hometask.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.discount.strategy.DiscountStrategy;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class DiscountAspect {

    public static class Register {
        private long totalCount;

        private Map<User, Long> countForUser;

        private Register(User user) {
            totalCount = 1;
            countForUser = new HashMap<>();
            countForUser.put(user, 1L);
        }

        public long getTotalCount() {
            return totalCount;
        }

        public Map<User, Long> getCountForUser() {
            return countForUser;
        }

        @Override
        public String toString() {
            return "Register{" +
                    "totalCount=" + totalCount +
                    ", countForUser=" + countForUser +
                    '}';
        }
    }

    private Map<DiscountStrategy, Register> discountMap = new HashMap<>();

    public Map<DiscountStrategy, Register> getDiscountMap() {
        return discountMap;
    }

    @Pointcut(value = "execution(* calculateDiscount(..)) && args(user, ..)", argNames = "user")
    private void executionCalculateDiscount(User user) {}

    @Pointcut("target(ua.epam.spring.hometask.service.discount.strategy.DiscountStrategy)")
    private void withinDiscountStrategy() {}

    @AfterReturning(
            pointcut = "executionCalculateDiscount(user) && withinDiscountStrategy()",
            returning = "discount", argNames = "joinPoint,user,discount")
    private void afterDiscountStrategyCalculateDiscount(JoinPoint joinPoint, User user, double discount) {
        if (discount != 0) {
            DiscountStrategy discountStrategy = (DiscountStrategy) joinPoint.getTarget();

            if (discountMap.containsKey(discountStrategy)) {
                discountMap.get(discountStrategy).totalCount++;
                discountMap.get(discountStrategy).countForUser.computeIfPresent(user, (k,v) -> v++);
                discountMap.get(discountStrategy).countForUser.putIfAbsent(user, 1L);
            } else {
                discountMap.put(discountStrategy, new Register(user));
            }

        }
    }
}
