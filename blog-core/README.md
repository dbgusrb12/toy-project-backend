blog-core 모듈
===

# 공통 모듈 계층

모든 모듈에 물려있는 공통 모듈,  
최대한 사용을 지양하고, POJO 형태의 자바 객체만 존재하게 설계

## 공통 모듈이 담당해야 될 책임

- 모든 모듈에 연동하기 때문에 의존성은 java, lombok 으로 제한한다.
- 최대한 사용하지 않는 것을 지향한다.
- 공통으로 사용 할 객체는 POJO 형태로 구현하여 제공한다.