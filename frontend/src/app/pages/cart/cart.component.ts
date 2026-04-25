import { Component } from '@angular/core';
import { NavbarComponent } from '../../components/navbar/navbar.component';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [NavbarComponent],
  template: `
    <app-navbar></app-navbar>
    <div style="text-align:center; padding: 60px; color: #a8a8b3;">
      <h2>🛒 Carrinho — Em construção</h2>
    </div>
  `
})
export class CartComponent {}