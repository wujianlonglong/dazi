package com.wjl.dazi;

import com.wjl.dazi.model.LetterGame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DaziApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaziApplication.class, args);
		System.setProperty("java.awt.headless","false");
		LetterGame.startGame();
	}
}
