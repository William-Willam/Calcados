import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Product } from '../../models/product.model';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { NavbarComponent } from '../../components/navbar/navbar.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, NavbarComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {

  products = signal<Product[]>([]);
  filtroAtivo = signal<string>('TODOS');
  loading = signal<boolean>(true);
  addedProductId = signal<number | null>(null);

  constructor(
    private productService: ProductService,
    public cartService: CartService
  ) {}

  ngOnInit(): void {
    this.carregarProdutos();
  }

  carregarProdutos(): void {
    this.loading.set(true);
    this.productService.getAll().subscribe({
      next: (data) => {
        this.products.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }

  filtrar(tipo: string): void {
    this.filtroAtivo.set(tipo);
    this.loading.set(true);

    if (tipo === 'TODOS') {
      this.productService.getAll().subscribe({
        next: (data) => {
          this.products.set(data);
          this.loading.set(false);
        }
      });
    } else {
      this.productService.getByType(tipo).subscribe({
        next: (data) => {
          this.products.set(data);
          this.loading.set(false);
        }
      });
    }
  }

  adicionarAoCarrinho(product: Product): void {
    this.cartService.addToCart(product);
    this.addedProductId.set(product.id);
    setTimeout(() => this.addedProductId.set(null), 1000);
  }
}