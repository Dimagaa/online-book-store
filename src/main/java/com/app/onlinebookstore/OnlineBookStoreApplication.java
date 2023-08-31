package com.app.onlinebookstore;

import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineBookStoreApplication {

    private final BookService bookService;

    @Autowired
    public OnlineBookStoreApplication(BookService bookService) {
        this.bookService = bookService;
    }

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book theShining = new Book();
            theShining.setTitle("The Shining");
            theShining.setAuthor("Stephen King");
            theShining.setIsbn("130528082023");
            theShining.setPrice(BigDecimal.TEN);
            theShining.setDescription("""
                    Jack and his family move into an isolated hotel with a violent past.
                    Living in isolation, Jack begins to lose his sanity,
                    which affects his family members
                    """);
            theShining.setCoverImage("https://www.app.com/image?EgZAwgBEAAYgAQ");

            Book taleOfStars = new Book();
            taleOfStars.setTitle("A Tale of Stars");
            taleOfStars.setAuthor("Eleanor Rivers");
            taleOfStars.setIsbn("9781456321891");
            taleOfStars.setPrice(new BigDecimal("14.99"));
            taleOfStars.setDescription("""
                    In a world where stars are magical beings,
                    a young astronomer embarks on a journey to save the fading constellations
                    from a dark force threatening the night sky.
                    """
            );
            taleOfStars.setCoverImage("https://www.app.com/image?HgYBvhDAQABgAI");

            Book secretsOfTheForgottenCity = new Book();
            secretsOfTheForgottenCity.setTitle("Secrets of the Forgotten City");
            secretsOfTheForgottenCity.setAuthor("Maya Thompson");
            secretsOfTheForgottenCity.setIsbn("762401537821");
            secretsOfTheForgottenCity.setPrice(new BigDecimal("12.50"));
            secretsOfTheForgottenCity.setDescription("""
                    Archaeologist Emma uncovers an ancient city with advanced technology,
                    guarded by puzzles and traps.
                    She races against a rival group to uncover its secrets,
                    unaware of the dangers lurking within.
                    """
            );
            secretsOfTheForgottenCity.setCoverImage("https://www.app.com/image?LiMBwCAJEAwAFA");

            System.out.println(bookService.save(theShining));
            System.out.println(bookService.save(taleOfStars));
            System.out.println(bookService.save(secretsOfTheForgottenCity));

            bookService.findAll().forEach(System.out::println);

        };
    }
}
