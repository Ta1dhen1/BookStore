package com.example.BookStore.providers;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "cart")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	private Person person;

	@ManyToOne
	private Book book;

	private Integer quantity;
	
	@Transient
	private Double totalPrice;
	
	@Transient
	public Double totalOrderPrice;

}
