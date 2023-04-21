package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Movie;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();

        Book book = createBook();
        int bookOrderCount = 2;

        Movie movie = createMovie();
        int movieOrderCount = 3;

        //when
        Long bookOrderId = orderService.order(member.getId(), book.getId(), bookOrderCount);
        Long movieOrderId = orderService.order(member.getId(), movie.getId(), movieOrderCount);

        //then
        Order getBookOrder = orderRepository.findOne(bookOrderId);
        Order getMovieOrder = orderRepository.findOne(movieOrderId);

        assertEquals("상품 주문 시 상태는 ORDER", OrderStatus.ORDER, getBookOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getMovieOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * bookOrderCount, getBookOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어든다.", 10 - bookOrderCount, book.getStockQuantity());

    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember();
        Book book = createBook();
        int orderCount = book.getStockQuantity() + 1;
        
        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        fail("재고 수량 부족 예외가 발생해야 합니다.");
    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember();
        Book book = createBook();
        int orderCount = 5;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);


        assertEquals("주문 취소 시 상태는 CANCEL이다", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문 취소 시 재고 원상복구", 10, book.getStockQuantity());
        System.out.println(getOrder.getStatus());
    }



    private Movie createMovie() {
        Movie movie = new Movie();
        movie.setActor("배우1");
        movie.setDirector("감독1");
        movie.setPrice(12000);
        movie.setStockQuantity(20);
        em.persist(movie);
        return movie;
    }

    private Book createBook() {
        Book book = new Book();
        book.setName("시골 JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);

        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "경기", "123-456"));
        em.persist(member);
        return member;
    }
}