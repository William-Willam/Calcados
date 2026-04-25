
import { Injectable, signal, computed } from '@angular/core';
import { Product } from '../models/product.model';
import { CartItem } from '../models/cart.model';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private items = signal<CartItem[]>([]);

  cartItems = this.items.asReadonly();

  totalItems = computed(() =>
    this.items().reduce((sum, item) => sum + item.quantity, 0)
  );

  totalPrice = computed(() =>
    this.items().reduce((sum, item) => sum + item.subtotal, 0)
  );

  addToCart(product: Product): void {
    const current = this.items();
    const existing = current.find(i => i.productId === product.id);

    if (existing) {
      this.items.set(current.map(item =>
        item.productId === product.id
          ? {
              ...item,
              quantity: item.quantity + 1,
              subtotal: (item.quantity + 1) * item.productPrice
            }
          : item
      ));
    } else {
      const newItem: CartItem = {
        id: Date.now(),
        productId: product.id,
        productName: product.name,
        productPrice: product.price,
        productImageUrl: product.imageUrl,
        quantity: 1,
        subtotal: product.price
      };
      this.items.set([...current, newItem]);
    }
  }

  removeItem(productId: number): void {
    this.items.set(this.items().filter(i => i.productId !== productId));
  }

  clearCart(): void {
    this.items.set([]);
  }
}