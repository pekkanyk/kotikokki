package kotikokki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/*import kotikokki.service.*;
import kotikokki.controller.*;
import kotikokki.domain.*;
import kotikokki.repository.*;
*/
@SpringBootApplication
@EnableScheduling
public class KotikokkiApplication {

    public static void main(String[] args) {
	SpringApplication.run(KotikokkiApplication.class, args);
    }

}
