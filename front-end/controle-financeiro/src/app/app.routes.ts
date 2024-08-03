import { Routes } from '@angular/router';
import { CategoriesComponent } from './components/categories/categories.component';
import { TransactionsComponent } from './components/transactions/transactions.component';
import { UsersComponent } from './components/users/users.component';

export const routes: Routes = [
  { path: 'categories', component: CategoriesComponent },
  { path: 'transactions', component: TransactionsComponent },
  { path: 'users', component: UsersComponent },
  { path: '', redirectTo: '/categories', pathMatch: 'full' }
];
