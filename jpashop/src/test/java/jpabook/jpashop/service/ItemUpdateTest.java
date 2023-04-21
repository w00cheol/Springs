package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class ItemUpdateTest {

    @Autowired ItemService itemService;
    @Autowired EntityManager em;

    @Test
    public void name() {
    }

    @Test
    public void updateTest() throws Exception {
        Book book = em.find(Book.class, 1L);

        book.setName("edited name");
    }
}
