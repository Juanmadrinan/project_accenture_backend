package com.accenture.project_accenture_backend;

import com.accenture.project_accenture_backend.domain.entities.Branch;
import com.accenture.project_accenture_backend.domain.entities.Franchise;
import com.accenture.project_accenture_backend.domain.entities.Product;
import com.accenture.project_accenture_backend.domain.valueobjects.Stock;
import com.accenture.project_accenture_backend.domain.valueobjects.Name;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectAccentureBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectAccentureBackendApplication.class, args);
		// Crear franquicia
		Franchise franchise = Franchise.create(Name.of("Starbucks"));

		// Crear sucursal para esa franquicia
		Branch branch = Branch.create(
				franchise.getId(),
				Name.of("Sucursal Centro")
		);

		// Crear producto para esa sucursal
		Product product = Product.create(
				branch.getId(),
				Name.of("Café Americano"),
				Stock.of(50)
		);

		// Operaciones de negocio
		product.addStock(20);              // Stock ahora es 70
		product.updateName(Name.of("Café Espresso"));
		franchise.updateName(Name.of("Starbucks Coffee"));

		System.out.println(franchise.getName());
		System.out.println(branch.getName());
		System.out.println(product.getName());
		System.out.println(product.getStock());
	}
}

